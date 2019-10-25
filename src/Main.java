import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

import selfprogramming_exp24.FunctionTree.*;
import selfprogramming_exp24.Route.Association;

public class Main {
	public static Vector<Function> functions=
			new Vector<Function>();//һԪ����������һ����ֵ���ַ���
	public static Vector<Predicate> predicates1=new Vector<Predicate>();//һԪν��
	public static Vector<Predicate> predicates2=new Vector<Predicate>();//��Ԫν��
	//classes��routes��predicate1_routes��predicate2_routes�±�ƽ��
	public static Class[] classes=
		{Integer.class,String.class,Position.class,Human.class,Date.class,Student.class,Array.class,_Class.class};
	public static Vector<Route>[] routes=new Vector[classes.length]; //ÿ�����Ӧ��ȫ������·��
	public static PredicateAndRoutes[][] predicate1_routes=new PredicateAndRoutes[classes.length][0];
	public static PredicateAndRoutes[][] predicate2_routes=new PredicateAndRoutes[classes.length][0];
	//vars��trees�±�ƽ��
	public static Vector<Object> vars=new Vector<Object>();//���������ȫ�ֱ���
	public static Vector<Tree> trees=new Vector<Tree>();//ÿ��������Ӧ�����νṹ
    public static Vector<Route> getRoutes(Class cls) //�������� ��ȡ·��
	{
		for(int i=0;i<classes.length;i++)
		{
			if(classes[i]==cls)
			{
				if(routes[i]==null) return routes[i];
				else{
					if(routes[i].size()==0) return null;
					else return routes[i];
				}
			}
		}
		return null;
	}
	public static Vector<Route> getRoutes(Tree tree) //������ ��ȡ·��
	{
		Vector<Route> result=getRoutes(tree.root.data.getClass());
		if(result!=null) return result;
		else return getRoutes(tree.root.data);
	}
	public static Vector<Route> getRoutes(Object var) //���ݱ��� ��ȡ·��
	{
		Vector<Route> result=getRoutes(var.getClass());
		if(result!=null) 
			return result;
		else
		{
			try 
			{
				result=new Tree(var).getAllRoutes();
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
					| InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
			int indexOfCls=-1;
			for(int i=0;i<classes.length;i++)
			{
				if(classes[i]==var.getClass()){ indexOfCls=i;break; }
			}
			routes[indexOfCls]=new Vector<Route>();
			routes[indexOfCls].addAll(result);
			return result;
		}
	}
	public static Tree getTree(String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException //�������ֻ�ȡ��
	{
		for(Tree tree:trees)
		{
			if(Convenient.getName(tree.root.data).equals(name)) return tree;
		}
		return null;
	}
	public static Tree getTree(Object obj)//����ĳ��ȫ�ֱ�����Ӧ����
	{
		for(int i=0;i<vars.size();i++)
		{
			if(vars.get(i)==obj) return trees.get(i);
		}
		return null;
	}
	public static Object getVarByName(String str) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		if(Convenient.isNumeric(str))
			return new Integer(str);
		else if(Convenient.isPosition(str)) //������
		{
			Position pos=new Position();
			pos.x=new Integer(str.substring(1).split("[,]")[0]);
			String tmp_str=str.split("[,]")[1];
			pos.y=new Integer(tmp_str.substring(0, tmp_str.length()-1));
			return pos;
		}
		else if(Convenient.isDate(str)) //������
		{
			Date date=new Date(1000,1,1);
			String[] tmp=str.split("[.]");
			date.��=new Integer(tmp[0]);
			date.��=new Integer(tmp[1]);
			date.��=new Integer(tmp[2]);
			return date;
		}
		else
		{
			for(Object var:vars)
			{
				if(Convenient.getName(var).equals(str))
					return var;
			}
		}
		return null;
	}
	public static Object getVarOrStringByName(String str) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		Object temp=getVarByName(str);
		if(temp==null) return str;
		else return temp;
	}
	public static Object getVarOrValueByName(String str)
	{
		try {
			Object var=getVarByName(str);
			if(var!=null) return var;
			else
			{
				if(Convenient.isNumeric(str)==false) return str.toString();
				else return new Integer(str);
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
    public static Predicate getPredicateByMethod(Method m)
	{
		for(Predicate p:predicates1)
		{
			if(p.method==null) continue;
			if(p.method.equals(m)) return p;
		}
		for(Predicate p:predicates2)
		{
			if(p.method==null) continue;
			if(p.method.equals(m)) return p;
		}
		return null;
	}
    public static Function getFunction(String name)
    {
    	for(Function f:functions)
    	{
    		if(f.name.equals(name)) return f;
    	}
    	return null;
    }
    public static Vector<Predicate> status_property_findSuitableP(Tree tree) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	Vector<Predicate> results=new Vector<Predicate>();//�ȽϵĽ��������һϵ��ν��
		Vector<Route> routes=getRoutes(tree);
		Object obj=tree.root.data;
		
		Class argType=obj.getClass();
		int index=-1;
		for(int i=0;i<classes.length;i++)
			if(classes[i]==argType) {index=i;break;}
		
		PredicateAndRoutes[] predicatesOfCertainClass=Main.predicate1_routes[index];//����ض��������ν�ʺ�·��
		for(int i=0;i<predicatesOfCertainClass.length;i++)
		{
			PredicateAndRoutes outside=predicatesOfCertainClass[i];//�̶���һ��ν�ʣ����ֲ�ͬ��·��
			Predicate thisPredicate=outside.predicate;
			for(int j=0;j<outside.routes.size();j++)
			{
				Route inner_route=outside.routes.get(j);//һ��·��
				if(thisPredicate.judge(inner_route.get(obj))==true)
				{
					if(thisPredicate.isBasic()) //���������Ԫν��
					{
						Predicate predicateWithRoute=thisPredicate.copy();//��Ԫν�ʵĻ����ϼ�һ��·��
						predicateWithRoute.route=inner_route;//��·��
						predicateWithRoute.argType=argType;//�Ѳ������͸ı��������obj1��obj2������
						results.add(predicateWithRoute);//���뼯����
					}
					else //������Ԫν��
					{
						Vector<Predicate> arr=new Vector<Predicate>();//���������
						arr.add(thisPredicate);
						Predicate complexPredicate=new Predicate("",argType,1,arr,inner_route);
						results.add(complexPredicate);
					}
				}
			}
		}
		return results;
    }
    public static Vector<Predicate> compare_findSuitableP(Tree tree1,Tree tree2) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		//tree1��tree2�ֱ�������1������2�����νṹ
    	Vector<Predicate> results=new Vector<Predicate>();//�ȽϵĽ��������һϵ��ν�ʣ���ʾ����1�Ͳ���2֮��Ĺ�ϵ
		
		Object obj1=tree1.root.data;//���ĸ��ڵ���ǲ�����������
		Object obj2=tree2.root.data;
		Class argType=tree1.root.data.getClass();//�ڱ���ʾ�����У��������������ͱ�������ͬ��
		
		int index=-1; //��ȫ���������classes�е��±�
		for(int i=0;i<classes.length;i++)
		{
			if(classes[i]==argType){ index=i;break; } //�������ͼ���classes[i]
		}
		
		PredicateAndRoutes[] predicatesOfCertainClass=Main.predicate2_routes[index];//����ض��ࣨ�������ͣ�������ν�ʺ�·��
		for(int i=0;i<predicatesOfCertainClass.length;i++)
		{
			PredicateAndRoutes outside=predicatesOfCertainClass[i];//���ѭ���������̶���һ��ν�ʣ����ֲ�ͬ��·��
			Predicate thisPredicate=outside.predicate;//��ǰν��
			for(int j=0;j<outside.routes.size();j++)
			{
				Route inner_route=outside.routes.get(j); //ѡȡ���е�һ��·��
				if(thisPredicate.judge(inner_route.get(obj1), inner_route.get(obj2))==true) //�����ν�ʣ�����1������2������һ�������
				{
					if(thisPredicate.isBasic()) //���������Ԫν��
					{
						Predicate predicateWithRoute=thisPredicate.copy();//��Ԫν�ʵĻ����ϼ�һ��·��
						predicateWithRoute.route=inner_route;//��·��
						predicateWithRoute.argType=argType;//�Ѳ������͸ı��������obj1��obj2������
						results.add(predicateWithRoute);//���뼯����
					}
					else //������Ԫν��
					{
						Vector<Predicate> arr=new Vector<Predicate>();//���������
						arr.add(thisPredicate);//�ѵ�ǰν�ʷŽ���������
						Predicate complexPredicate=new Predicate("",argType,2,arr,inner_route); //�½�һ������ν�ʣ���ǰ��ν�ʳ�Ϊ����ν�ʵ�һ����
						results.add(complexPredicate);
					}
				}
			}
		}
		
		return results;
	}
	public static Vector<Predicate> findSuitablePredicates(Tree tree) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		Vector<Predicate> results=new Vector<Predicate>();
		Vector<Route> routes=getRoutes(tree);
		for(Predicate p:predicates1)
		{
			for(Route r:routes)
			{
				if(r.isSelf()) continue;
				Class nodeType=r.get(tree.root.data).getClass();
				if(nodeType.equals(p.argType)==false) continue;;
				if(p.judge(r.get(tree.root.data))==true)
				{
					if(p.route.isSelf())
					{
						Predicate predicateWithRoute=p.copy();
						predicateWithRoute.route=r;
						results.add(predicateWithRoute);
					}
					else
					{
						Vector<Predicate> arr=new Vector<Predicate>();
						arr.add(p);
						Predicate newPredicate=new Predicate("",nodeType,1,arr);
						results.add(newPredicate);
					}
				}
			}
		}
		return results;
	}
	public static Vector<Predicate> compare_findSuitableP(Object var1,Object var2) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		return compare_findSuitableP(getTree(var1),getTree(var2));
	}
	public static Vector<Predicate> findSuitablePredicates(Object var) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		return findSuitablePredicates( getTree(var) );
	}
	public static void refresh() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException//������·�߽���ˢ��
	{
		for(Object var:vars) //Ϊÿ����������һ����
		{
			Tree newTree=new Tree(var);
			trees.add(newTree);
			int index=-1;
			for(int i=0;i<classes.length;i++)
			{
				if(classes[i].equals(var.getClass()) )
				{
					index=i;
					break;
				}
			}
			if(index==-1) Convenient.showln(var.getClass().getSimpleName());
			else{
				if(Main.routes[index]==null) Main.routes[index]=newTree.getAllRoutes(); 
			}
		}
		//������·�����
		for(Vector<Route> routesOfClass:routes)
			if(routesOfClass!=null) routesOfClass.clear(); //����Ϊ��ͻᱻ�϶�Ϊnull
		for(Tree tree:trees)
		{
			Vector<Route>temp=Main.getRoutes(tree.root.data.getClass());
			if(temp==null) 
				continue;
			for(Route r:temp)
			{
				Convenient.showln("��ֵ:"+Convenient.getName(r.get(tree.root.data)));
				Convenient.showln("��·��:"+r.toString());
			}
			//System.out.println("���س�������......");
			//new Scanner(System.in).next();
		}
		getRoutes(new Integer(1234));
		getRoutes(new String("ǿ��ΪString��Ѱ��·��"));
		getRoutes(new Date(1000,1,1));
		getRoutes(new Position(21,37));
		getRoutes(getVarByName("������"));
		getRoutes(getVarByName("������"));
		getRoutes(getVarByName("����"));
		Array arr=new Array();
		arr.elements.add(78);
		arr.elements.add(94);
		arr.elements.add(87);
		getRoutes(arr);
	}
	public static void refresh_predicate1(boolean refreshed_function) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		for(int i=0;i<classes.length;i++)
		{
			int oldLenOfOutside=predicate1_routes[i].length;
			Convenient.showln("prediates1����ĳ���Ϊ��"+predicates1.size());
			predicate1_routes[i]=Arrays.copyOf(predicate1_routes[i], predicates1.size());
			//int newLenOfOutside=outside.length;
			for(int j=0;j<predicate1_routes[i].length;j++)
			{
				if(j<oldLenOfOutside&&refreshed_function==false) continue;
				if(j>=oldLenOfOutside) Main.predicate1_routes[i][j]=new PredicateAndRoutes();
				//PredicateAndRoutes inner=predicate1_routes[i][j];
				Main.predicate1_routes[i][j].predicate=predicates1.get(j);
				Main.predicate1_routes[i][j].routes.clear();
				Vector<Route> temp_routes=getRoutes(classes[i]);
				if(temp_routes==null){ //���·������Ϊnull
					//Convenient.showln(classes[i].getSimpleName()+"��·������Ϊ��");
					temp_routes=new Vector<Route>();//�ͽ���һ���յ�����
				}
				for(Route temp_r:temp_routes){
					if(temp_r.steps.size()>0)
					{
						if(temp_r.steps.lastElement().childName!=null) //�ų����е�Ԥ�����úõ���ѧ���㺯��
						{
							String functionName=temp_r.steps.lastElement().childName;
							if(functionName.equals("��һ")|| functionName.equals("�Ӷ�")|| functionName.equals("��һ")|| 
								functionName.equals("����")||functionName.equals("���෴��")||functionName.equals("����һ�߼�")
								||functionName.equals("ȡ���ַ�"))
								continue;
						}
					}
					
					if(getClass_resultOfRoute(temp_r, classes[i])==predicates1.get(j).argType)
						Main.predicate1_routes[i][j].routes.add(temp_r);
				}
			}
		}
	}
	public static void refresh_predicate2(boolean refreshed_function) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		for(int i=0;i<classes.length;i++)
		{
			//PredicateAndRoutes[] outside=predicate2_routes[i];
			int oldLenOfOutside=predicate2_routes[i].length;
			Convenient.showln("prediates2����ĳ���Ϊ��"+predicates2.size());
			predicate2_routes[i]=Arrays.copyOf(predicate2_routes[i], predicates2.size());
			//int newLenOfOutside=outside.length;
			for(int j=0;j<predicate2_routes[i].length;j++)
			{
				if(j<oldLenOfOutside&&refreshed_function==false) continue;
				if(j>=oldLenOfOutside) Main.predicate2_routes[i][j]=new PredicateAndRoutes();
				//PredicateAndRoutes inner=predicate2_routes[i][j];
				Main.predicate2_routes[i][j].predicate=predicates2.get(j);
				Main.predicate2_routes[i][j].routes.clear();
				Vector<Route> temp_routes=getRoutes(classes[i]);
				if(temp_routes==null){
					//Convenient.showln(classes[i].getSimpleName()+"��·������Ϊ��");
					temp_routes=new Vector<Route>();
				}
				for(Route temp_r:temp_routes)
				{
					if(temp_r.steps.size()>0)
					{
						if(temp_r.steps.lastElement().childName!=null) //�ų����е���ѧ���㺯��
						{
							String functionName=temp_r.steps.lastElement().childName;
							if(functionName.equals("��һ")|| functionName.equals("�Ӷ�")|| functionName.equals("��һ")|| 
								functionName.equals("����")||functionName.equals("���෴��")||functionName.equals("����һ�߼�")
								||functionName.equals("ȡ���ַ�"))
								continue;
							
							if(temp_r.steps.lastElement().childName.equals("����ת�ַ���"))
							{
								Convenient.showln("����������ת�ַ������ķ���ֵ����Ϊ"+getClass_resultOfRoute(temp_r, classes[i]).getSimpleName()+
										"����"+predicates2.get(j).name+"�Ĳ�������Ϊ"
										+predicates2.get(j).argType.getSimpleName());
							}
						}
					}
					
					if(getClass_resultOfRoute(temp_r, classes[i])==predicates2.get(j).argType)
					{
						Main.predicate2_routes[i][j].routes.add(temp_r);
					}
				}
			}
		}
	}
    public static void refresh_predicate2() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	refresh_predicate2(true);
    }
    public static Class getClass_resultOfRoute(Route route,Class argType) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		if(argType==Date.class)
			return route.get(new Date(2000,1,1)).getClass();
		else if(argType==Position.class)
			return route.get(new Position(4,5)).getClass();
		else if(argType==Integer.class)
		{
			return route.get(new Integer(1246)).getClass();
		}
		else if(argType==String.class)
		{
			return route.get("���д���ַ���").getClass();
		}
		else if(argType==Array.class)
		{
			Array arr=new Array(Integer.class);
			arr.elements.add(90);
			arr.elements.add(78);
			arr.elements.add(87);
			return route.get(arr).getClass();
		}
		else
		{
			for(Object var:vars)
				if(var.getClass()==argType)
					return route.get(var).getClass();
		}
		return null;
	}
	public static void init() throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException
	{
		Class[] types={Integer.class};
		predicates1.add(new Predicate("������",Basic.class.getMethod("������",types) , Integer.class, 1));
		predicates1.add(new Predicate("�Ǹ���",Basic.class.getMethod("�Ǹ���",types) , Integer.class, 1));
		predicates1.add(new Predicate("Ϊ��",Basic.class.getMethod("Ϊ��",types) , Integer.class, 1));
		predicates1.add(new Predicate("��ż��",Basic.class.getMethod("��ż��",types) , Integer.class, 1));
		predicates1.add(new Predicate("������",Basic.class.getMethod("������",types) , Integer.class, 1));
		predicates1.add(new Predicate("С��10",Basic.class.getMethod("С��10",types) , Integer.class, 1));
		
		types=new Class[2];
		types[0]=types[1]=Integer.class;
		
		predicates2.add(new Predicate("����",Basic.class.getMethod("����",types) , Integer.class, 2));
		predicates2.add(new Predicate("����",Basic.class.getMethod("����",types) , Integer.class, 2));
		predicates2.add(new Predicate("С��",Basic.class.getMethod("С��",types) , Integer.class, 2));
		types[0]=types[1]=String.class;
		predicates2.add(new Predicate("�ַ�������",Basic.class.getMethod("�ַ�������",types) , String.class, 2));
		predicates2.add(new Predicate("�ַ�������",Basic.class.getMethod("�ַ�������",types) , String.class, 2));
		predicates2.add(new Predicate("�ַ���С��",Basic.class.getMethod("�ַ���С��",types) , String.class, 2));
		
		types=new Class[1];
		types[0]=String.class;
		functions.add(new Function("ȡ���ַ�",String.class,String.class,Basic.class.getMethod("ȡ���ַ�", types)));
		types[0]=Integer.class;
		functions.add(new Function("��һ",Integer.class,Integer.class,Basic.class.getMethod("��һ",types)));
		functions.add(new Function("����",Integer.class,Integer.class,Basic.class.getMethod("����",types)));
		functions.add(new Function("��һ",Integer.class,Integer.class,Basic.class.getMethod("��һ",types)));
		functions.add(new Function("�Ӷ�",Integer.class,Integer.class,Basic.class.getMethod("�Ӷ�",types)));
		functions.add(new Function("���෴��",Integer.class,Integer.class,Basic.class.getMethod("���෴��",types)));
		functions.add(new Function("����һ�߼�",Integer.class,Integer.class,Basic.class.getMethod("����һ�߼�",types)));
		
		Human ������=new Human("������","��",new Date(1970,3,19));vars.add(������);
		Human ����=new Human("����","Ů",new Date(1985,10,11));vars.add(����);
		Human ������=new Human("������","Ů",new Date(1973,12,31));vars.add(������);
		Human �ز���=new Human("�ز���","��",new Date(1987,5,2));vars.add(�ز���);
		Human �δ���=new Human("�δ���","��",new Date(1986,8,15));vars.add(�δ���);
		Human ����=new Human("����","Ů",new Date(1977,9,12));vars.add(����);
		Human ���ǲ�=new Human("���ǲ�","��",new Date(1972,2,29));vars.add(���ǲ�);
		Human ¬��=new Human("¬��","Ů",new Date(1981,10,19));vars.add(¬��);
		Human ��ʫ=new Human("��ʫ","��",new Date(1988,3,29));vars.add(��ʫ);
		Human ��΢��=new Human("��΢��","Ů",new Date(1978,11,12));vars.add(��΢��);
		Human �Ź���=new Human("�Ź���","��",new Date(1974,4,10));vars.add(�Ź���);
		_Class һ��=new _Class("һ��",������,����,������,�ز���);vars.add(һ��);
		_Class ����=new _Class("����",���ǲ�,����,�δ���,����);vars.add(����);
		_Class ����=new _Class("����",¬��,��ʫ,��΢��,�Ź���);vars.add(����);
		Student ����=new Student("����","Ů",new Date(1999,12,17),һ��,81,72,83);vars.add(����);
		Student ����=new Student("����","Ů",new Date(2000,4,3),һ��,88,75,71);vars.add(����);
		Student ������=new Student("������","��",new Date(2000,3,22),һ��,69,76,63);vars.add(������);
		Student ֣����=new Student("֣����","��",new Date(1999,12,16),һ��,79,78,84);vars.add(֣����);
		Student ������=new Student("������","Ů",new Date(1999,9,26),һ��,90,95,93);vars.add(������);
		Student ʢ־��=new Student("ʢ־��","��",new Date(1999,9,10),һ��,85,100,99);vars.add(ʢ־��);
		Student ��ٻ=new Student("��ٻ","Ů",new Date(2000,1,25),һ��,83,92,81);vars.add(��ٻ);
		Student ��Զ��=new Student("��Զ��","��",new Date(1999,11,16),һ��,69,85,79);vars.add(��Զ��);
		Student ����=new Student("����","��",new Date(2000,1,17),һ��,66,72,63);vars.add(����);
		Student �����h=new Student("�����h","Ů",new Date(2000,2,21),����,92,90,86);vars.add(�����h);
		Student ʢ��=new Student("ʢ��","��",new Date(1999,10,6),����,79,75,79);vars.add(ʢ��);
		Student �����=new Student("�����","��",new Date(1999,12,9),����,89,98,89);vars.add(�����);
		Student �޼�=new Student("�޼�","��",new Date(1999,9,19),����,79,100,87);vars.add(�޼�);
		Student ֣����=new Student("֣����","Ů",new Date(1999,12,25),����,90,95,93);vars.add(֣����);
		Student ������=new Student("������","��",new Date(2000,2,7),����,89,90,77);vars.add(������);
		Student ���=new Student("���","Ů",new Date(1999,9,2),����,98,80,91);vars.add(���);
		Student ������=new Student("������","��",new Date(2000,1,30),����,81,93,69);vars.add(������);
		Student ����=new Student("����","Ů",new Date(1999,12,30),����,78,67,68);vars.add(����);
		Student ����=new Student("����","Ů",new Date(2000,8,21),����,81,69,70);vars.add(����);
		Student �˾�=new Student("�˾�","��",new Date(1998,11,18),����,76,71,81);vars.add(�˾�);
		Student ������=new Student("������","��",new Date(1999,9,30),����,69,78,61);vars.add(������);
		Student �����=new Student("�����","Ů",new Date(1999,9,5),����,78,66,80);vars.add(�����);
		Student ��С��=new Student("��С��","Ů",new Date(2000,2,29),����,89,80,72);vars.add(��С��);
		
		for(Object var:vars)
		{
			Tree newTree=new Tree(var);
			trees.add(newTree);
			int index=-1;
			for(int i=0;i<classes.length;i++)
			{
				if(classes[i].equals(var.getClass()) )
				{
					index=i;
					break;
				}
			}
			if(index==-1) Convenient.showln(var.getClass().getSimpleName());
			else{
				if(Main.routes[index]==null) Main.routes[index]=newTree.getAllRoutes(); 
			}
		}
		for(Tree tree:trees)
		{
			Vector<Route>temp=Main.getRoutes(tree.root.data.getClass());
			if(temp==null) 
			{
				Convenient.showln(tree.root.data.getClass().getSimpleName()+"�Ҳ���route");
				continue;
			}
		}
		getRoutes(new Integer(1234));
		getRoutes(new String("ǿ��ΪString��Ѱ��·��"));
		getRoutes(new Date(1000,1,1));
		getRoutes(new Position(21,37));
		
		Main.refresh_predicate1(true);
		Main.refresh_predicate2();
		
	}
	public static Vector<Function> findSuitableFunctions(Tree argTree,Object returnValue)  //argTree�ǲ������������νṹ��returnValue�Ƿ���ֵ�����û�����
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		Vector<Function> result=new Vector<Function>();//����һ�����ϣ������洢���з��������ĺ���
		Vector<Route> routes=Main.getRoutes(argTree);//��������ÿһ����㣬��ÿ�����ı������̣����������н�㣩��¼��һ��Route��������
		
		for(Route thisRoute:routes) //thisRoute������ǰ�������Ҫ������·��   ע����������Ϊ��������ȡ�
		{
			//��֪·�ߣ�thisRoute���ͳ����㣨���ڵ�argTree.root��������·��ĩβ�Ľ�㣬��ʽΪ��<·��>.get(������.data)
			if(thisRoute.get(argTree.root.data).equals(returnValue) ) //����������Ľ����ڷ���ֵ
				result.add(new Function("",returnValue.getClass(),argTree.root.data.getClass(),thisRoute));//�򱣴浱ǰ·�ߣ�ע���Ȱ�·�߰�װ��Function����
		}
		
		return result;//������һ�ֵõ������п��з������ȴ�ɸѡ
	}
    
    public static boolean createNewPredicate1() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	System.out.println("��ʾ�����Ҫ�˳���������XXX�����Ҫ������������ݣ�����XX");
		System.out.print("ν������=");
		Scanner sc=new Scanner(System.in);
		String pName=sc.next();
		Class argType;
		Integer count=1;
		Vector<Predicate> pSet=new Vector<Predicate>();
		while(true)
		{
			//boolean quitAndSave=false;
			String inp;//�û�������ı�
			//Array data=new Array(Object.class);//���飬��������һ������
			Object arg;
			System.out.print("����=");
			inp=sc.next();
			if(inp.equals("XX")) continue;//����XX����
			if(inp.equals("XXX"))  break;//����XXX�˳�
			arg=getVarByName(inp);
			argType=arg.getClass();
			
			Tree tree;
			if(argType.equals(Human.class)||argType.equals(Student.class)||argType.equals(_Class.class))
				tree=getTree(arg);
			else
				tree=new Tree(arg);
			Vector<Predicate> thisSet=status_property_findSuitableP(tree);//���ϵ�ǰ�������ݵ�����ν��
			if(1==count) pSet.addAll(thisSet);//ԭʼ�����ǿյģ��Ͱ�ȫ��ν�ʼӽ�ȥ
			else pSet=PFSet.P_retainAll(pSet, thisSet); //�����ȡ����
			
			if(pSet.size()==0)
			{
				System.out.println("û���κ�����������ν�ʣ�ѭ��������");
				return false;
			}
			else if(pSet.size()>1){ //�����̭���
				for(int i=0;i<pSet.size();i++)
				{
					System.out.println((i+1)+":");
					System.out.println("ν����ʽ��"+pSet.get(i));
					System.out.println("�����⣺��"+pSet.get(i).toString(arg));
				}
			}
			else if(pSet.size()==1)
			{
				System.out.println("ν����ʽ��"+pSet.firstElement());
				System.out.println("�����⡡��"+pSet.firstElement().toString(arg));
			}
			
			String choice="Y";
			if(pSet.size()>1)
			{
				System.out.print("�Ƿ񱣴浱ǰ�������Y/N��");
				choice=sc.next();
			}
			
			if(choice.equals("Y"))
			{
				if(pSet.size()==1)//����ǵ�����ν��
				{
					Predicate newPredicate=pSet.lastElement().copy();
					newPredicate.argType=argType;
					newPredicate.name=pName;
					predicates1.add(newPredicate);
				}
				else{
					Predicate complexPredicate=new Predicate(pName,argType,1,pSet);
					predicates1.add(complexPredicate);
				}
				System.out.println("���ѳɹ�����һԪν�ʣ���Ϊ��"+predicates1.lastElement().name);
				break;
			}
			
			count++;
		}
		refresh_predicate1(false);
		return true;
    }
    public static boolean createNewPredicate2() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		System.out.println("��ʾ�����Ҫ�˳���������XXX�����Ҫ������������ݣ�����XX");
		System.out.print("ν������=");
		Scanner sc=new Scanner(System.in);
		String pName=sc.next();
		Class argType;
		Integer count=1;
		//Vector<Array> datas=new Vector<Array>(); 
		Vector<Predicate> pSet=new Vector<Predicate>();
		while(true)
		{
			boolean quitAndSave=false;
			String inp;//�û�������ı�
			Array data=new Array(Object.class);//���飬��������һ������
			Object arg1,arg2;
			System.out.print("��"+count+"�����ݣ�����1=");
			inp=sc.next();
			if(inp.equals("XX")) continue;//����XX����
			if(inp.equals("XXX"))  break;//����XXX�˳�
			arg1=getVarByName(inp);
			argType=arg1.getClass();
			data.add(arg1);
			System.out.print("����������");System.out.print(Convenient.blanks(count.toString().length()));
			System.out.print("����2=");
			inp=sc.next();
			if(inp.equals("XX")) continue;
			else{
				arg2=getVarByName(inp);
				data.add(arg2);
			}
			
			Tree tree1,tree2;
			if(quitAndSave==false) //�û�û�������XXX
			{
				if(argType.equals(Human.class)||argType.equals(Student.class)||argType.equals(_Class.class)){
					tree1=getTree(data.get(0));
					tree2=getTree(data.get(1));
				}
				else{
					tree1=new Tree(data.get(0));
					tree2=new Tree(data.get(1));
				}
				Vector<Predicate> thisSet=compare_findSuitableP(tree1,tree2);//���ϵ�ǰ�������ݵ�����ν��
				if(1==count) pSet.addAll(thisSet);//ԭʼ�����ǿյģ��Ͱ�ȫ��ν�ʼӽ�ȥ
				else pSet=PFSet.P_retainAll(pSet, thisSet); //�����ȡ����
			}
			
			if(pSet.size()==0)
			{
				System.out.println("û���κ�����������ν�ʣ�ѭ��������");
				return false;
			}
			else if(pSet.size()>1){ //�����̭���
				for(int i=0;i<pSet.size();i++)
				{
					System.out.println((i+1)+":");
					System.out.println("ν����ʽ��"+pSet.get(i));
					System.out.println("�ȽϽ����"+pSet.get(i).toString(data.get(0), data.get(1)));
				}
			}
			else if(pSet.size()==1)
			{
				System.out.println("ν����ʽ��"+pSet.firstElement());
				System.out.println("�ȽϽ����"+pSet.firstElement().toString(data.get(0), data.get(1)));
			}
			
			String choice="Y";
			if(pSet.size()>1)
			{
				System.out.print("�Ƿ񱣴浱ǰ�������Y/N��");
				choice=sc.next();
			}
			
			if(choice.equals("Y"))
			{
				if(pSet.size()==1)//����ǵ�����ν��
				{
					Predicate newPredicate=pSet.lastElement().copy();
					newPredicate.argType=argType;
					newPredicate.name=pName;
					predicates2.add(newPredicate);
				}
				else{
					Predicate complexPredicate=new Predicate(pName,argType,2,pSet);
					predicates2.add(complexPredicate);
				}
				System.out.println("���ѳɹ�������Ԫν�ʣ���Ϊ��"+predicates2.lastElement().name);
				break;
			}
			
			count++;
		}
		refresh_predicate2(false);
		return true;
	}
	public static boolean createNewFunction() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		Scanner sc=new Scanner(System.in);
		System.out.print("�����뺯�������ƣ�");
		String fName=sc.next();
		Class argType,returnType;//�������ͣ�����ֵ����
		Integer count=1;//������������ʾ�����ǵڼ������ݡ�
		
		Vector<Function> fSet=new Vector<Function>();//�������ϣ���ǰʣ������п��з���
		while(true)
		{
			String inp;//��¼�û����������
			Object arg,returnValue;//�û�����Ĳ����ͷ���ֵ
			System.out.println("�������"+count+"�����ݣ�");
			System.out.print("����=");
			inp=sc.next();
			arg=getVarByName(inp);  //���ݱ��������ر������������������ꡢ���ڣ�����ʱ����һ����������ڣ�����������κ����ͣ������ַ���������
			argType=arg.getClass();
			Tree argTree=new Tree(arg);//Ϊ������������һ����
			System.out.print("����ֵ=");
			inp=sc.next();
			returnValue=getVarOrStringByName(inp);
			returnType=returnValue.getClass();
			Vector<Function> thisSet=findSuitableFunctions(argTree,returnValue);//�����û��ղ���������ݵõ��ķ���
			
			if(1==count) fSet.addAll(thisSet);//�����һ������ʱ������Ϊ�գ���ʱ�����еĿ��з���ȫ�����뼯����
			else fSet=PFSet.F_retainAll(fSet, thisSet); //���򣬾ͶԼ���������������
			
			if(fSet.size()==0) //�������Ϊ�գ������еķ���������̭��
			{
				System.out.println("û���κ����������ĺ�����ѭ������");
				return false;
			}
			else
			{
				System.out.println("�ҵ����º�����");
				for(int i=0;i<fSet.size();i++)
					System.out.println((i+1)+"��"+fSet.get(i).toString());
			}
			String choice;//�û���ѡ��Y����N
			if(fSet.size()>1) //������ڶ��ַ�����������п��ܴ��ڲ���ȷ�ķ�������ʱ��Ӧ��ѯ���û�
			{
				System.out.print("�Ƿ񱣴棿(Y/N)");
				choice=sc.next();
			}
			else choice="Y"; //���ֻ��һ�ַ�������ֱ�ӱ���
			
			if("Y".equals(choice))
			{
				if(fSet.size()==1)
					functions.add(fSet.firstElement());
				else //���ַ�������ѡһ��
				{
					System.out.println("��ֻ��ѡȡ���е�һ�ֽ���������뷽������ţ�");
					int number=sc.nextInt();
					functions.add(fSet.get(number-1)); //�ŵ�ȫ�������������
				}
				functions.lastElement().name=fName;
				functions.lastElement().argType=argType;
				functions.lastElement().returnType=returnValue.getClass();
				refresh();
				System.out.println("���ѳɹ�������������Ϊ��"+functions.lastElement().name+
						"���������ͣ�"+functions.lastElement().argType.getSimpleName()+
						"������ֵ���ͣ�"+functions.lastElement().returnType.getSimpleName());
				break;
			}
			count++;
		}
		
		refresh();//ˢ��·��
		refresh_predicate1(true);//ˢ��һԪν��·��
		refresh_predicate2();//ˢ�¶�Ԫν��·��
		return true;
	}
	public static Route getShortestRoute(Object arg,String returnValue) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		Class argType=arg.getClass();
		Vector<Route> temp=getRoutes(argType);
		Vector<Route> temp2=new Vector<Route>();
		for(Route r:temp)
			if(r.get(arg).toString().equals(returnValue)) temp2.add(r);
		Route shortestOne=temp2.get(0);
		for(Route r:temp2)
		{
			if(r.isSelf()) return r;
			else
			{
				if(r.steps.size()<shortestOne.steps.size())
					shortestOne=r;
			}
		}
		return shortestOne;
	}
	public static void createNewStrProcessor() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		Scanner sc=new Scanner(System.in);
		System.out.print("�����뺯������=");
		String fName=sc.next();
		System.out.print("����=");
		Object arg=getVarByName(sc.next());
		Class argType=arg.getClass();
		System.out.print("����ֵ=");
		String returnValue=sc.next();
		System.out.print("���ص��ַ�����������˶��ٸ��ɷ֣��Ӳ�������");
		int num=sc.nextInt();
		Vector<Route> routes=new Vector<Route>();
		for(int i=0;i<num;i++)
		{
			System.out.print("����"+(i+1)+"=");
			routes.add(getShortestRoute(arg,sc.next()));
		}
		StringProcessor sp=new StringProcessor(returnValue,arg,routes);
		System.out.println("\n"+sp);
		System.out.println("\n"+sp.toString(new Date(1997,7,1)));
	}
	public static void createNewConditionalStrProcessor() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		System.out.print("�����뺯��������=");
		Scanner sc=new Scanner(System.in);
		String fName=sc.next();
		Class argType = null;
		System.out.println(
				"��������һ�����������жϹ��ܵĺ������ڲ�ͬ����£����������ִ�в�ͬ�ķ�֧��������������п��ܵ������");
		System.out.println("һ�����������Ϻ������NEXT������END�����˳����������Ĺ��̡�");
		Vector<StringProcessor> sps=new Vector<StringProcessor>();
		Vector<Vector<Predicate>> statuses=new Vector<Vector<Predicate>>();
		for(int i=0;true;i++)
		{
			System.out.println("���"+(i+1)+"��");
			boolean exit=false;
			for(int j=0;true;j++)
			{
				int num;//�Ӳ����ĸ���
				Vector<Route> routes=new Vector<Route>();
				System.out.println("��������������ֵ������"+(j+1)+"��");
				System.out.print("����=");
				String inp=sc.next();
				if(inp.equals("NEXT")) 
				{
					String yesOrNo;//�Ƿ������ǰ����������
					System.out.println("���ղ�����ļ�������ȫ����������������");
					for(Predicate p:statuses.lastElement())
						System.out.println(p);
					System.out.print("�Ƿ������ǰ�����룿��ȷ��(Y/N)");
					yesOrNo=sc.next();
					if(yesOrNo.equals("Y")) break;
					else
					{
						j--;
						continue;//����ȥ
					}
				}
				if(inp.equals("END")) { System.out.println("\n");exit=true;break; }
				Object arg=getVarByName(inp);
				argType=arg.getClass();
				System.out.print("����ֵ=");
				String returnValue=sc.next();
				if(j==0) //һ���Ӻ�������ĵ�һ����������
				{
					String same; //�ַ�������ʾ���Ƿ�����һ�θ�ʽ��ͬ��
					if(i>0) //���ǵ�һ���Ӻ���
					{
						System.out.print("������ʽ�ǲ��Ǻ͸ղŵĺ���һ������Y/N��");
						same=sc.next();
					}
					else //i==0 ��һ���Ӻ���
						same="N";
					if(same.equals("N"))
					{
						System.out.print("���ص��ַ�����������˶��ٸ��ɷ֣��Ӳ�������");
						num=sc.nextInt();
						for(int k=0;k<num;k++)
						{
							System.out.print("����"+(k+1)+"=");
							routes.add(getShortestRoute(arg,sc.next()));
						}
					}
					else // �û������ˡ�Y��
						routes.addAll(sps.lastElement().routes);//���ϴ�һ��
				}
				
				if(j==0) sps.add(new StringProcessor(returnValue,arg,routes));//�½�StringProcessor
				if(j==0)
					statuses.add( status_property_findSuitableP(new Tree(arg)) );
				else
				{
					Vector<Predicate> temp=new Vector<Predicate>();
					temp.addAll(status_property_findSuitableP(new Tree(arg)));
					temp=PFSet.P_retainAll(temp, statuses.lastElement()); //ȡ����
					statuses.lastElement().clear();
					statuses.lastElement().addAll(temp);
				}
			}
			if(exit==true) break;
		}
		
		Vector<Branch> branches=new Vector<Branch>();//�Ѻ������������ϴ����Branch����֧��
		for(int i=0;i<sps.size();i++)
			branches.add(new Branch(new Function("",sps.get(i)), statuses.get(i),FunctionTree.MARK_1D));
		
		FunctionTree functionTree=new FunctionTree(String.class,argType,branches);
		functionTree.removeSamePart();
		Function newFunction=functionTree.toFunction();
		newFunction.generateSubsets();
		newFunction.name=fName;
		Function.sortByNumOfConditions(newFunction);
		
		System.out.println("���������ɹ�����Ϊ:"+newFunction.name);
		System.out.println("����"+newFunction.subFunctions.size()+"���Ӻ���");
		for(int i=0;i<newFunction.subFunctions.size();i++)
		{
			System.out.println("�Ӻ���"+(i+1)+"��\n"+newFunction.subFunctions.get(i).toString());
			System.out.println("\nִ���������£�");
			for(Predicate p: newFunction.subFunctions.get(i).conditions )
				System.out.println(p);
			System.out.println();
		}
		
		Main.functions.add(newFunction);//����ȫ��������
		refresh();
		refresh_predicate1(true);
		refresh_predicate2();
	}
    public static void createNewConditionalFunction() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	System.out.print("�����뺯��������=");
		Scanner sc=new Scanner(System.in);
		String fName=sc.next();
		Class argType = null;//��������
		Class returnType = null;//����ֵ���ͣ�ֻ����Integer��String
		System.out.println(
				"��������һ�����������жϹ��ܵĺ������ڲ�ͬ����£����������ִ�в�ͬ�ķ�֧��������������п��ܵ������");
		System.out.println("һ�����������Ϻ������NEXT������END�����˳����������Ĺ��̡�");
		Vector<Function> subFunctions=new Vector<Function>();
		Vector<Vector<Predicate>> statuses=new Vector<Vector<Predicate>>();

		for(int i=0;true;i++)
		{
			System.out.println("���"+(i+1)+"��");
			boolean exit=false;
			Vector<Function> suitableFunctions=new Vector<Function>();//��ѡ����
			Vector<Predicate> statusesOfArgOfThisSubFunction=new Vector<Predicate>();
			for(int j=0;true;j++)
			{
				int num;//�Ӳ����ĸ���
				Vector<Route> routes=new Vector<Route>();
				System.out.println("��������������ֵ������"+(j+1)+"��");
				System.out.print("����=");
				String inp=sc.next();
				if(inp.equals("NEXT")) 
				{
					System.out.println("\n");
					if(suitableFunctions.size()==0) 
					{
						System.out.println("û���κο��õķ������Ӻ�������ʧ�ܣ�������");
						i--; //�˻�ȥ
						exit=true;
						break;
					}
					else if(suitableFunctions.size()==1)
					{
						subFunctions.add(suitableFunctions.firstElement());//�����Ӻ���
						statuses.add(statusesOfArgOfThisSubFunction);//������
					}
					else// if(suitableFunctions.size()>1)
					{
						System.out.println("��ѡ�������£����������֣�");
						for(int k=0;k<suitableFunctions.size();k++)
							System.out.println( (k+1)+"��"+suitableFunctions.get(k) );
						int index_choice=sc.nextInt();
						subFunctions.add( suitableFunctions.get(index_choice-1) );
						statuses.add(statusesOfArgOfThisSubFunction);
					}
					break;
				}
				if(inp.equals("END")) { System.out.println("\n");exit=true;break; }
				Object arg=getVarByName(inp);
				if(arg==null)
				{
					if(Convenient.isNumeric(inp)) {
						arg=new Integer(inp);
						Convenient.showln("���ԣ����ղ�������һ��������");
					}
					else arg=inp.toString();
				}
				
				argType=arg.getClass();
				System.out.print("����ֵ=");
				String str_returnValue=sc.next();
				Object returnValue;
				if(Convenient.isNumeric(str_returnValue)) //�����жϲ�ת���������ͻ��ַ�����
				{   
					returnType=Integer.class;
					returnValue=new Integer(str_returnValue);
				}
				else
				{
					returnType=String.class;
					returnValue=new String(str_returnValue);
				}
				
				if(j==0) 
				{
					suitableFunctions.addAll(findSuitableFunctions(new Tree(arg), returnValue));
					statusesOfArgOfThisSubFunction.addAll( status_property_findSuitableP(new Tree(arg)) );
					statusesOfArgOfThisSubFunction=PFSet.P_deleteSuperset(statusesOfArgOfThisSubFunction);//ɾ������
				}
				else 
				{
					suitableFunctions=PFSet.F_retainAll(suitableFunctions, findSuitableFunctions(new Tree(arg), returnValue) );
					statusesOfArgOfThisSubFunction=PFSet.P_retainAll(   //���������㡱
							statusesOfArgOfThisSubFunction, status_property_findSuitableP(new Tree(arg)));
					statusesOfArgOfThisSubFunction=PFSet.P_deleteSuperset(statusesOfArgOfThisSubFunction);//ɾ������
				}
				
				System.out.println("����Ҫ��ĺ������£�");
				for(Function f:suitableFunctions)
					System.out.println(f);
				System.out.println("��������������ȫ������ʱ���Ӻ����Żᱻִ�У�");
				for(Predicate p:statusesOfArgOfThisSubFunction)
					System.out.println(p);
					
			}
			if(exit==true) break;
		}
		
		Vector<Branch> branches=new Vector<Branch>();//�Ѻ������������ϴ����Branch����֧��
		for(int i=0;i<subFunctions.size();i++)
			branches.add(new Branch(subFunctions.get(i), statuses.get(i),FunctionTree.MARK_1D));//һά�����ά��
		
		FunctionTree functionTree=new FunctionTree(returnType,argType,branches);
		functionTree.removeSamePart();
		Function newFunction=functionTree.toFunction();
		newFunction.generateSubsets();
		newFunction.name=fName;
		
		System.out.println("���������ɹ�����Ϊ:"+newFunction.name);
		Convenient.showln("��������Ϊ"+newFunction.argType.getSimpleName()+
				"������ֵ����Ϊ"+newFunction.returnType.getSimpleName());
		System.out.println("����"+newFunction.subFunctions.size()+"���Ӻ���");
		for(int i=0;i<newFunction.subFunctions.size();i++)
		{
			System.out.println("�Ӻ���"+(i+1)+"��\n"+newFunction.subFunctions.get(i).toString());
			System.out.println("\nִ���������£�");
			for(Predicate p: newFunction.subFunctions.get(i).conditions )
				System.out.println(p);
			System.out.println();
		}
		
		Main.functions.add(newFunction);//����ȫ��������
		refresh();
		refresh_predicate1(true);
		refresh_predicate2();
    }
    public static Predicate getPredicateByName(String name)
    {
    	for(Predicate p1:predicates1)
    		if(p1.name.equals(name)) return p1;
    	for(Predicate p2:predicates2)
    		if(p2.name.equals(name)) return p2;
    	return null;
    }
    public static Function getFunctionByName(String name)
    {
    	for(Function f:functions)
    		if(f.name.equals(name)) return f;
    	return null;
    }
    public static void showRoutes()
    {
    	for(int i=0;i<classes.length;i++)
    	{
    		if(classes[i]==Array.class) continue;
    		System.out.println(classes[i].getSimpleName()+"�ࣺ");
    		if(routes[i]==null)
    		{
    			System.out.println();
    			continue;
    		}
    		for(Route r:routes[i])
    			System.out.println(r);
    		System.out.println();
    	}
    }
    public static void showPredicate1Routes()
    {
    	for(int i=0;i<classes.length;i++)
    	{
    		if(classes[i]==Array.class) continue;
    		System.out.println(classes[i].getSimpleName()+"�ࣺ");
    		for(PredicateAndRoutes[] outside:predicate1_routes)
    		{
    			for(PredicateAndRoutes inner:outside)
    			{
    				for(int j=0;j<inner.routes.size();j++)
    					System.out.println(inner.predicate.name+"��"+inner.routes.get(j));
    			}
    		}
    		System.out.println();
    	}
    }
    public static void showPredicate2Routes()
    {
    	for(int i=0;i<classes.length;i++)
    	{
    		if(classes[i]==Array.class) continue;
    		System.out.println(classes[i].getSimpleName()+"�ࣺ");
    		for(PredicateAndRoutes[] outside:predicate2_routes)
    		{
    			for(PredicateAndRoutes inner:outside)
    			{
    				for(int j=0;j<inner.routes.size();j++)
    					System.out.println(inner.predicate.name+"��"+inner.routes.get(j));
    			}
    		}
    		System.out.println();
    	}
    }
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException 
	{
		init();
		
		while(true)
		{
			Scanner sc=new Scanner(System.in);
			String inp=sc.next();
			
			if(inp.equals("�½�����"))  createNewFunction();
			else if(inp.equals("�½�һԪν��"))  createNewPredicate1();
			else if(inp.equals("�½���Ԫν��")) createNewPredicate2();
			else if(inp.equals("�½��ַ�������"))  createNewStrProcessor();
			else if(inp.equals("�½����������ַ�������"))  createNewConditionalStrProcessor();
			else if(inp.equals("�½��������ĺ���")) createNewConditionalFunction();
			else if(inp.equals("�鿴·��")) showRoutes();
			else if(inp.equals("�鿴һԪν��·��"))  showPredicate1Routes();
			else if(inp.equals("�鿴��Ԫν��·��"))  showPredicate2Routes();
			else
			{
				Predicate p=getPredicateByName(inp);
				if(p!=null)
				{
					if(p.numOfArgs==1)
					{
						System.out.print("����=");
						Object arg=getVarOrValueByName(sc.next());
						if(arg==null)
						{
							System.out.println("��Ч�Ĳ�����");
							continue;
						}
						else
						{
							boolean judgement=p.judge(arg);
							if(judgement) System.out.println("��");
							else System.out.println("��");
						}
					}
					else if(p.numOfArgs==2)
					{
						System.out.print("����1=");
						Object arg1=getVarOrValueByName(sc.next());
						System.out.print("����2=");
						Object arg2=getVarOrValueByName(sc.next());
						if(arg1==null||arg2==null)
						{
							System.out.println("��Ч�Ĳ�����");
							continue;
						}
						else
						{
							boolean judgement=p.judge(arg1,arg2);
							if(judgement) System.out.println("��");
							else System.out.println("��");
						}
					}
				}
				else //����ν�ʣ������Ǻ���
				{
					Function f=getFunctionByName(inp);
					if(f==null)
					{
						System.out.println("��Ч������");
						continue;
					}
					else
					{
						System.out.print("����=");
						Object arg=getVarOrValueByName(sc.next());
						if(arg==null)
						{
							System.out.println("��Ч�Ĳ���");
							continue;
						}
						else
						{
							if(arg.getClass()!=f.argType)
							{
								System.out.println("�������Ͳ�ƥ��");
								continue;
							}
							else
								System.out.println(f.execute(arg)[1]);
						}
					}
				}
			}
		}
		
	}

}
