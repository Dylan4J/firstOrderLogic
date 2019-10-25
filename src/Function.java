import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;


public class Function {
	public String name="";
	public Class returnType;//����ֵ����
	public Class argType;//��������
	public Route route=new Route(true);
	public Method method;
	public Vector<Function> subFunctions=new Vector<Function>();//�Ӻ���
	public Vector<Predicate> conditions=new Vector<Predicate>();//ֻ����Щ����ȫ������ʱ�������Żᱻִ��
	public Vector<Pair> subsets=new Vector<Pair>();
	public StringProcessor sp;
	public boolean is_nop() //nop: No Operation��û�в��������������������ִ���κβ���������ֵ���ǲ�������
	{
		return route.isSelf()&&method==null&&subFunctions.size()==0;
	}
	public boolean is_onlyReturnComponent() //��������������κ�Ԫ����/��������������������Ƿ��ز�����ĳ���ɷ֣����������
	{
		return method==null&&subFunctions.size()==0;
	}
    public boolean equals(Function other)
	{
		//if(method!=null) return this.method.equals(other.method);
		//else 
			return this.route.equals(other.route);
    	/*
    	if(method==null)
		{
			if(other.method!=null) return false;
		}
		if(returnType.equals(other.returnType)==false|| method.equals(other.method)==false||
				argType.equals(other.argType)==false||route.equals(other.route)==false)
			return false;
		
		if(conditions.size()!=other.conditions.size()) return false;
		for(int i=0;i<conditions.size();i++)
		{
			if(conditions.get(i).equals(other.conditions.get(i))==false) return false;
		}
		
		if(subFunctions.size()!=other.subFunctions.size()) return false;
		for(int i=0;i<subFunctions.size();i++)
		{
			if(subFunctions.get(i).equals(other.subFunctions.get(i))==false) return false;
		}
		
		return true;
		*/
	}
	public Function(String name,StringProcessor sp)
	{
		this.name=name;
		this.sp=sp;
		this.returnType=String.class;
		this.argType=sp.argType;
	}
    public Function(String name,Class returnType,Class argType,Route route)
	{
		this.name=name;
		this.returnType=returnType;
		this.argType=argType;
		this.route=route;
	}
    public Function(String name,Class returnType,Class argType,Method method) //����������û��·�ߣ���������
	{
		this.name=name;
		this.returnType=returnType;
		this.argType=argType;
		this.method=method;
	}
	public Function(String name,Class returnType,Class argType,Method method,Vector<Predicate> conditions) //����������û��·�ߣ�������
	{
		this(name,returnType,argType,method);
		this.conditions=conditions;
	}
	public Function(String name,Class returnType,Class argType,Method method,Route route) //������������·�ߣ���������
	{
		this(name,returnType,argType,method);
		this.route=route;
	}
	public Function(String name,Class returnType,Class argType,Method method,Route route,Vector<Predicate> conditions) //������������·�ߣ�������
	{
		this(name,returnType,argType,method,route);
		this.conditions=conditions;
	}
	public Function(String name,Class returnType,Class argType,Vector<Function> subFuncs) //�ϳɺ�����û��·�ߣ���������
	{
		this.name=name;
		this.returnType=returnType;
		this.argType=argType;
		subFunctions=subFuncs;
	}
	public Function(String name,Class returnType,Class argType,Vector<Function> subFuncs,Vector<Predicate> conditions)  //�ϳɺ�����û��·�ߣ�������
	{
		this(name,returnType,argType,subFuncs);
		this.conditions=conditions;
	}
	public Function(String name,Class returnType,Class argType,Vector<Function> subFuncs,Route route) //�ϳɺ�������·�ߣ���������
	{
		this(name,returnType,argType,subFuncs);
		this.route=route;
	}
	public Function(String name,Class returnType,Class argType,Vector<Function> subFuncs,Route route,Vector<Predicate> conditions) //�ϳɺ�������·�ߣ�������
	{
		this(name,returnType,argType,subFuncs,route);
		this.conditions=conditions;
	}
	public Function(){}//������һ��nop�����κβ������ĺ���
	public Function(Route route) //������һ���������ز�����ĳ���ɷ֣���������� �ĺ���
	{
		this.route=route;
	}
	public Function copy()
	{
		Function f=new Function();
		f.name="";
		f.argType=this.argType;
		f.returnType=this.returnType;
		f.method=this.method;
		f.conditions=this.conditions;
		f.subFunctions=this.subFunctions;
		f.subsets=this.subsets;
		f.sp=this.sp;
		return f;
	}
	public void generateSubsets() //���ɡ����Ӽ� �泬������ϵ
	{
		if(subFunctions.size()==0) return;
		this.subsets.clear();
		for(int i=0;i<subFunctions.size();i++)
		{
			for(int j=0;j<subFunctions.size();j++)
			{
				if(i==j) continue;
				if(PFSet.PSet_contain(subFunctions.get(j).conditions, subFunctions.get(i).conditions) ==true) //����j���������� ����  ����i����������
					subsets.add(new Pair(i,j));
			}
		}
		
		if(subsets.size()>0)
		{
			Convenient.showln("������ϵ������ϣ�");
			for(Pair pair:subsets)
				Convenient.showln(pair.a+"����"+pair.b);
			Convenient.showln("");
		}
	}
	private boolean subset_judge(Object arg,int thisIndex) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		if(subsets.size()==0) return true;
		//for(int i=0;i<subFunctions.size();i++) //���������Ӻ���
		//{
		//	if( i==thisIndex ) continue; //�����Լ�
			for(Pair pair:this.subsets) //�������еġ����Ӽ� ������ �泬������ϵ
			{
				if(this.subFunctions.get(pair.a).equals(thisIndex)) //��thisIndex���Ӻ��� ��ִ��������������һ��������ִ�м��ϵ����Ӽ�
				{
					if((boolean) (this.subFunctions.get(pair.b).execute(arg)[0]) ==true) //��һ������ִ�гɹ���
					{
						Convenient.showln("����"+pair.a+"��"+pair.b+"���Ӽ���"+pair.a+"����ִֹ��");
						return false;
					}
				}
			}
	//	}
		return true;
	}
    public Object[] execute(Object arg) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		//Object _arg=route.get(arg);
		Object[] _args={arg};//����ɵ�Ԫ������ //�˴�_arg�޸�Ϊarg
		Object[] result={false,null};//��һ��Ԫ�ش��� �����Ƿ�ִ�У��Ƿ�������������ڶ���Ԫ���Ƿ���ֵ
		if(sp!=null)
		{
			if(conditions.size()==0)
			{
				result[0]=true;
				result[1]=sp.toString(arg);
				return result;
			}
			else
			{
				for(Predicate cond:conditions)
				{
					if(cond.judge(arg,null)==false)
						return result;//ʧ�ܣ�����{false,null}
				}
				result[0]=true;
				result[1]=sp.toString(arg);
				return result;
			}
		}
		if(method!=null)//����һ���򵥵ĺ���
		{
		    if(conditions.size()==0)
		    {
		    	result[0]=true;
		    	result[1]=method.invoke(new Basic(), _args);
		    	return result;
		    }
			else //if(conditions.size()>0) //ȫ�����������㣬�ſ�ִ��
			{
				for(Predicate cond:conditions)
				{
					if(cond.judge(arg,null)==false)
						return result;//ʧ�ܣ�����{false,null}
				}
				result[0]=true;
				result[1]=method.invoke(new Basic(), _args);
				return result;
			}
		}
		else //if(method==null) 
		{
			if(subFunctions.size()==0)//�򵥺���
			{
				for(Predicate cond:conditions)
				{
					if(cond.judge(arg,null)==false) //��������������
					{
						Object[] fail_result={false,null};
						return fail_result;//ʧ�ܣ�����{false,null}
					}
				}
				
				Object[] exe_result={true,route.get(arg)};
				return exe_result;//�ɹ�
			}
			else if(subFunctions.size()>0) //��һ�������ɸ��Ӻ������
			{
				int count=0;
				for(int i=0;i<subFunctions.size();i++)
				{
					count=i;
					if( subset_judge(arg, i)==false ) continue;//���� ����   �˴��޸�_arg��arg
					Object[] temp=subFunctions.get(i).execute(arg);     //�˴��޸ģ�_arg��arg���ܿ������ǵ��������ŷ�ת��Ԫ��
					if((boolean)(temp[0])==true) //�������㣬�õ�true 
						return temp;//ֻ�ܷ�������һ�ֽ�����������ֵ�жϺ���
					else
					{
						Convenient.show("������"+arg.toString()+"���������ͣ�"+arg.getClass().getSimpleName());
						Convenient.showln("�����������������㣬���µ�"+(i+1)+"���������ܱ�ִ�У�");
						for(Predicate cond:subFunctions.get(i).conditions)
							Convenient.showln(cond.toString()+"\n"+cond.toString(arg));
					}
				}
			}
		}
		return result;
	}
    public boolean isEmpty()
    {
    	return method==null&&subFunctions.size()==0;
    }
    public String toString()
    {
    	if(name.equals("")==false) return name;
    	else
    	{
    		if(sp==null) return route.toString();
    		else return sp.toStringSimple()+(route.isSelf()?"":route);
    	}
    }
    public static void sortByNumOfConditions(Function f)
    {
    	for(int i=0;i<f.subFunctions.size()-1;i++)
    	{
    		for(int j=i+1;j<f.subFunctions.size();j++)
    		{
    			if(f.subFunctions.get(i).conditions.size()<f.subFunctions.get(j).conditions.size())
    			{
    				Function temp=f.subFunctions.get(i).copy();//temp��subFunctions[i]
    				f.subFunctions.setElementAt(f.subFunctions.get(j), i); //subFunctions[i]��subFunctions[j]
    				f.subFunctions.setElementAt( temp , j); //subFunctions[j]��subFunctions[i]
    			}
    		}
    	}
    	f.subsets.clear();
    	f.generateSubsets();
    }
}
