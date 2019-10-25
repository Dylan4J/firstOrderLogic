import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public class Predicate {
	public String name="";
	public Method method;
	public Class argType;//��������
	public int numOfArgs;//��������
	public Route route=new Route(true);
	public Vector<Vector<Predicate>> conditions=
			new Vector<Vector<Predicate>>();
			//�������ֻҪһ�������ΪTRUE���ڲ����������ȫ������
	public boolean isBasic()
	{
		return method!=null&&route.isSelf()&&conditions.size()==0;
	}
    private String getMethodName()
	{
		if(method==null) return null;
		else return Main.getPredicateByMethod(method).name;
	}
	public Predicate(){}
	public Predicate(String name,Method method,Class argType,int num) //����ν�ʣ�û��·��
	{
		this.name=name;
		this.method=method;
		this.argType=argType;
		this.numOfArgs=num;
	}
	public Predicate(String name,Method method,Class argType,int num,Route route) //����ν�ʣ���·��
	{
		this(name,method,argType,num);
		this.route=route;
	}
	public Predicate(String name,Class argType,int num,Vector<Predicate> conditions) //�ϳ�ν�ʣ�OR���ӣ���û��·��
	{
		this.name=name;
		this.argType=argType;
		this.numOfArgs=num;
		this.conditions.add(new Vector<Predicate>());//�½�һ�飬����ʼ��
		Convenient.showln("��ʼ�������鳤��="+conditions.size());
		this.conditions.lastElement().addAll(conditions);//�Ѳ��������������ȫ����ӽ�ȥ
		Convenient.showln("��������Ժ����鳤��="+conditions.size());
	}
	public Predicate(String name,Class argType,int num,Vector<Vector<Predicate>> conditions,boolean mark_2D) //�ϳ�ν�ʣ�OR\AND���ӣ���û��·��
	{
		//���������mark_2Dû���κ����ã�ֻ��Ϊ�˱��������Ĺ��캯�������ظ� �����������һ������
		this.name=name;
		this.argType=argType;
		this.numOfArgs=num;
		for(Vector<Predicate> outside:conditions)//���
		{
			this.conditions.add(new Vector<Predicate>());//�½�һ�飬����ʼ��
			this.conditions.lastElement().addAll(outside);
		}
	}
	public Predicate(String name,Class argType,int num,Vector<Predicate> conditions,Route route) //�ϳ�ν�ʣ�OR���ӣ�����·��
	{
		this(name,argType,num,conditions);
		this.route=route;
	}
	public Predicate(String name,Class argType,int num,Vector<Vector<Predicate>> conditions,boolean mark_2D,Route route)//�ϳ�ν�ʣ�OR\AND���ӣ�����·��
	{
		this(name,argType,num,conditions,mark_2D);
		this.route=route;
	}
    public boolean judge(Object arg1,Object arg2) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	Object _arg1=route.get(arg1);
    	Object _arg2=( (arg2==null) ? null:route.get(arg2));
    	
    	if(method!=null) //��������Ǽ򵥵�ν�ʣ�����ڡ�С�ڣ�������Щν�ʺ���ȫ������Basic������
    	{
    		Object[] _args=new Object[2];
    		if(numOfArgs==2)
    		{
    			_args=new Object[2];
    			_args[0]=_arg1;_args[1]=_arg2;
    		}
    		else if(numOfArgs==1)
    		{
    			_args=new Object[1];
    			_args[0]=_arg1;
    		}
    		//Convenient.show("ʵ�ʲ�������"+_args[0].getClass().getSimpleName()+", "+_args[1].getClass().getSimpleName()+
    		//		"   methodҪ��"+method.getParameterCount()+"������"+method.getParameterTypes()[0].getSimpleName()+", "+
    		//		method.getParameterTypes()[1].getSimpleName());
    		Object temp_����=method.invoke(new Basic(), _args);
    		//Convenient.showln("������"+temp_����+"  "+temp_����.getClass().getSimpleName()+"   �������ͣ�"
    		//		+_args[1].getClass().getSimpleName()+"  ����2="+_args[1]);
    		return (boolean)temp_����;
    		//return (boolean) method.invoke(new Basic(), _args);
    	}
    	
    	for(Vector<Predicate> outside:conditions)//ֻҪ��һ�����㣬�ͷ���TRUE
    	{
    		boolean ok_inside=true;
    		for(Predicate inside:outside) //�ڲ����������ȫ������
    		{
    			if(inside.judge(_arg1, _arg2)==false)
    			{
    				ok_inside=false;
    				break;
    			}
    		}
    		if(ok_inside) return true;//һ�����㣬������������ֱ��return true
    	}
		return false;
    }
    public boolean judge(Object arg) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	if(conditions.size()==1)
    	{
    		if(conditions.get(0).size()==1)
    			return conditions.firstElement().firstElement().judge(route.get(arg));
    	}
    	return judge(arg,null);
    }
    public String toString()
    {
    	if(name.equals("")==false) //�Ѿ�ȡ������ν��
    		return this.name;
    	else if(method!=null&&conditions.size()==0) //Ԫν��+·��
    	{
    		return Main.getPredicateByMethod(method).name+(this.route.isSelf()?"":("  ����������"+this.route.toString()));
    	}
    	else if(conditions.size()>0)
    	{
    		String result="";
    		if(conditions.size()>1) //������鳤��>1
    		{
    			for(Vector<Predicate> or_outside:conditions)
        		{
        			result+="��  ";
        			if(or_outside.size()>1) //�ڲ����鳤��>1
        				for(Predicate and_inner:or_outside)
            				result+=and_inner.toString()+" �� ";
        			else //�ڲ����鳤��==1
        				result+=or_outside.firstElement().toString();
        			result+="\n";
        		}
        		result="��  "+result.substring(3, result.length()-1);
    		}
    		else //������鳤��==1
    		{
    			if(conditions.get(0).size()==1)//�ڲ����鳤��==1
    				result=conditions.get(0).get(0).toString();
    			else //�ڲ����鳤��>1
    			{
    				for(int i=0;i<conditions.get(0).size();i++)
    				{
    					result+=conditions.get(0).get(i).toString();
    					if(i!=conditions.get(0).size()-1) result+=" �� ";
    				}
    			}
    		}
    		return result+(this.route.isSelf()?"":("  ����������"+this.route.toString()));
    	}
		return "����ν��";
    }
    public String toString(Object _arg) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	Object arg=null;
		arg=route.get(_arg);
		//Convenient.showln(this.name+"("+Convenient.getName(arg)+")");
    	if(this.name.equals("")==false) //���ֲ�Ϊ��
    		return this.name+"("+Convenient.getName(arg)+")";
    	else if(this.name.equals("")&&conditions.size()==0)
    		return Main.getPredicateByMethod(this.method)+"("+Convenient.getName(arg)+")";
    	else
    	{
    		String result="";
    		if(conditions.size()==1) //���OR
    		{
    			if(conditions.get(0).size()==1)
    				return conditions.get(0).get(0).toString(arg);
    			else
    			{
    				for(int i=0;i<conditions.get(0).size();i++)
    				{
    					result+=conditions.get(0).get(i).toString(arg);
    					if(i+1!=conditions.get(0).size())
    						result+="�� ";
    				}
    				return result;
    			}
    		}
    		else //if(conditions.size()>1)
    		{
    			//��㡰�����ⲻֹһ��
    			result+="(";
    			for(Vector<Predicate> outside:conditions)
    			{
    				if(outside.size()==1) //�ڲ㡰�ҡ���AND������ֻ��һ��
    					result+=outside.get(0).toString(arg);
    				else
    				{
    					for(Predicate inner:outside)
        				{
        					result+=inner.toString(arg);
        				}
    				}
    			}
    			result+=")";
    		}
    		return result;
    	}
    	
    }
    public String toString(Object _arg1,Object _arg2) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
    {
    	Object arg1=null,arg2=null;
		try {
			arg1 = route.get(_arg1);
			arg2=route.get(_arg2);
		} catch (InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	if(name.equals("")==false&&method!=null)//Ԫν��
    	{
    		if(name.equals("����"))
    			return arg1+">"+arg2;
    		else if(name.equals("����"))
    			return arg1+"="+arg2;
    		else if(name.equals("С��"))
    			return arg1+"<"+arg2;
    		else return name+"("+Convenient.getName(arg1)+","+Convenient.getName(arg2)+")";
    	}
    	else if(name.equals("")==true&&method!=null)//Ԫν��+·��
    	{
    		if( "����".equals(getMethodName()))
    			return arg1+">"+arg2;
    		else if("����".equals(getMethodName()))
    			return arg1+"="+arg2;
    		else if("С��".equals(getMethodName()))
    			return arg1+"<"+arg2;
    		else return getMethodName()+"("+Convenient.getName(arg1)+","+Convenient.getName(arg2)+")";
    	}
    	else
    	{
    		String result="";
    		if(conditions.size()==1) //���OR
    		{
    			if(conditions.get(0).size()==1)
    				return conditions.get(0).get(0).toString(arg1,arg2);
    			else
    			{
    				for(Predicate inner:conditions.get(0))
    					result+=inner.toString(arg1,arg2)+"�� ";
    				return result.substring(0,result.length()-2);
    			}
    		}
    		else //if(conditions.size()>1)
    		{
    			//��㡰�����ⲻֹһ��
    			result+="(";
    			for(Vector<Predicate> outside:conditions)
    			{
    				if(outside.size()==1) //�ڲ㡰�ҡ���AND������ֻ��һ��
    					result+=outside.get(0).toString(arg1,arg2);
    				else
    				{
    					for(Predicate inner:outside)
        				{
        					result+=inner.toString(arg1,arg2);
        				}
    				}
    			}
    			result+=")";
    		}
    	}
		return null;
    }
    public Predicate copy()
    {
    	Predicate p=new Predicate();
    	p.name="";//�����ǲ��ᱻ�����ģ�����
    	p.argType=this.argType;
    	p.method=this.method;
    	p.numOfArgs=this.numOfArgs;
    	p.conditions=this.conditions;
    	p.route=this.route;
    	return p;
    }
    public boolean equals(Predicate other)
    {
    	if(this.name.equals("")==false)
    	{
    		//if(this.name.equals(other.name)==true) return true;
    	}
    	if(method==null)
    	{
    		if(other.method!=null) return false;
    	}
    	else if(method.equals(other.method)==false||argType.equals(other.argType)==false||numOfArgs!=other.numOfArgs)
    		return false;
    	else if(this.conditions.size()!=other.conditions.size()) return false;
    	else if(route.equals(other.route)==false) return false;
    	
    	if(conditions.size()>0)
    	{
    		for(int i=0;i<conditions.size();i++)
    		{
    			boolean innerEqual=true;
    			if(conditions.get(i).size()!=other.conditions.get(i).size()) return false;
    			if(conditions.get(i).size()>0)
    			{
    				for(int j=0;j<conditions.get(i).size();j++)
    				{
    					if(conditions.get(i).get(j).equals(other.conditions.get(i).get(j))==false)
    					{
    						innerEqual=false;
    						break;
    					}
    				}
    			}
    			if(innerEqual==false) return false;
    		}
    	}
    	
    	return true;
    }
}
