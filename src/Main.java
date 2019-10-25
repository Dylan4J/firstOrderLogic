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
			new Vector<Function>();//一元函数，返回一个数值或字符串
	public static Vector<Predicate> predicates1=new Vector<Predicate>();//一元谓词
	public static Vector<Predicate> predicates2=new Vector<Predicate>();//二元谓词
	//classes和routes、predicate1_routes、predicate2_routes下标平行
	public static Class[] classes=
		{Integer.class,String.class,Position.class,Human.class,Date.class,Student.class,Array.class,_Class.class};
	public static Vector<Route>[] routes=new Vector[classes.length]; //每个类对应的全部访问路径
	public static PredicateAndRoutes[][] predicate1_routes=new PredicateAndRoutes[classes.length][0];
	public static PredicateAndRoutes[][] predicate2_routes=new PredicateAndRoutes[classes.length][0];
	//vars和trees下标平行
	public static Vector<Object> vars=new Vector<Object>();//参与运算的全局变量
	public static Vector<Tree> trees=new Vector<Tree>();//每个变量对应的树形结构
    public static Vector<Route> getRoutes(Class cls) //根据类型 获取路线
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
	public static Vector<Route> getRoutes(Tree tree) //根据树 获取路线
	{
		Vector<Route> result=getRoutes(tree.root.data.getClass());
		if(result!=null) return result;
		else return getRoutes(tree.root.data);
	}
	public static Vector<Route> getRoutes(Object var) //根据变量 获取路线
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
	public static Tree getTree(String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException //根据名字获取树
	{
		for(Tree tree:trees)
		{
			if(Convenient.getName(tree.root.data).equals(name)) return tree;
		}
		return null;
	}
	public static Tree getTree(Object obj)//返回某个全局变量对应的树
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
		else if(Convenient.isPosition(str)) //坐标型
		{
			Position pos=new Position();
			pos.x=new Integer(str.substring(1).split("[,]")[0]);
			String tmp_str=str.split("[,]")[1];
			pos.y=new Integer(tmp_str.substring(0, tmp_str.length()-1));
			return pos;
		}
		else if(Convenient.isDate(str)) //日期型
		{
			Date date=new Date(1000,1,1);
			String[] tmp=str.split("[.]");
			date.年=new Integer(tmp[0]);
			date.月=new Integer(tmp[1]);
			date.日=new Integer(tmp[2]);
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
    	Vector<Predicate> results=new Vector<Predicate>();//比较的结果，返回一系列谓词
		Vector<Route> routes=getRoutes(tree);
		Object obj=tree.root.data;
		
		Class argType=obj.getClass();
		int index=-1;
		for(int i=0;i<classes.length;i++)
			if(classes[i]==argType) {index=i;break;}
		
		PredicateAndRoutes[] predicatesOfCertainClass=Main.predicate1_routes[index];//针对特定类的所有谓词和路线
		for(int i=0;i<predicatesOfCertainClass.length;i++)
		{
			PredicateAndRoutes outside=predicatesOfCertainClass[i];//固定的一个谓词，多种不同的路线
			Predicate thisPredicate=outside.predicate;
			for(int j=0;j<outside.routes.size();j++)
			{
				Route inner_route=outside.routes.get(j);//一条路线
				if(thisPredicate.judge(inner_route.get(obj))==true)
				{
					if(thisPredicate.isBasic()) //如果本身是元谓词
					{
						Predicate predicateWithRoute=thisPredicate.copy();//在元谓词的基础上加一条路线
						predicateWithRoute.route=inner_route;//加路线
						predicateWithRoute.argType=argType;//把参数类型改变参数对象obj1、obj2的类型
						results.add(predicateWithRoute);//加入集合中
					}
					else //本身不是元谓词
					{
						Vector<Predicate> arr=new Vector<Predicate>();//打包成数组
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
		//tree1、tree2分别代表参数1、参数2的树形结构
    	Vector<Predicate> results=new Vector<Predicate>();//比较的结果，返回一系列谓词，表示参数1和参数2之间的关系
		
		Object obj1=tree1.root.data;//树的根节点就是参数变量本身
		Object obj2=tree2.root.data;
		Class argType=tree1.root.data.getClass();//在本演示程序中，两个参数的类型必须是相同的
		
		int index=-1; //在全局数组变量classes中的下标
		for(int i=0;i<classes.length;i++)
		{
			if(classes[i]==argType){ index=i;break; } //参数类型即是classes[i]
		}
		
		PredicateAndRoutes[] predicatesOfCertainClass=Main.predicate2_routes[index];//针对特定类（参数类型）的所有谓词和路线
		for(int i=0;i<predicatesOfCertainClass.length;i++)
		{
			PredicateAndRoutes outside=predicatesOfCertainClass[i];//外层循环变量，固定的一个谓词，多种不同的路线
			Predicate thisPredicate=outside.predicate;//当前谓词
			for(int j=0;j<outside.routes.size();j++)
			{
				Route inner_route=outside.routes.get(j); //选取其中的一条路线
				if(thisPredicate.judge(inner_route.get(obj1), inner_route.get(obj2))==true) //如果“谓词（参数1，参数2）”这一命题成立
				{
					if(thisPredicate.isBasic()) //如果本身是元谓词
					{
						Predicate predicateWithRoute=thisPredicate.copy();//在元谓词的基础上加一条路线
						predicateWithRoute.route=inner_route;//加路线
						predicateWithRoute.argType=argType;//把参数类型改变参数对象obj1、obj2的类型
						results.add(predicateWithRoute);//加入集合中
					}
					else //本身不是元谓词
					{
						Vector<Predicate> arr=new Vector<Predicate>();//打包成数组
						arr.add(thisPredicate);//把当前谓词放进数组里面
						Predicate complexPredicate=new Predicate("",argType,2,arr,inner_route); //新建一个复杂谓词，当前的谓词成为复杂谓词的一部分
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
	public static void refresh() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException//对树和路线进行刷新
	{
		for(Object var:vars) //为每个变量建立一棵树
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
		//把所有路线清空
		for(Vector<Route> routesOfClass:routes)
			if(routesOfClass!=null) routesOfClass.clear(); //长度为零就会被认定为null
		for(Tree tree:trees)
		{
			Vector<Route>temp=Main.getRoutes(tree.root.data.getClass());
			if(temp==null) 
				continue;
			for(Route r:temp)
			{
				Convenient.showln("↓值:"+Convenient.getName(r.get(tree.root.data)));
				Convenient.showln("↑路线:"+r.toString());
			}
			//System.out.println("按回车键继续......");
			//new Scanner(System.in).next();
		}
		getRoutes(new Integer(1234));
		getRoutes(new String("强制为String类寻找路线"));
		getRoutes(new Date(1000,1,1));
		getRoutes(new Position(21,37));
		getRoutes(getVarByName("周雨婷"));
		getRoutes(getVarByName("何威慑"));
		getRoutes(getVarByName("二班"));
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
			Convenient.showln("prediates1数组的长度为："+predicates1.size());
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
				if(temp_routes==null){ //如果路径数组为null
					//Convenient.showln(classes[i].getSimpleName()+"的路径数组为空");
					temp_routes=new Vector<Route>();//就建立一个空的数组
				}
				for(Route temp_r:temp_routes){
					if(temp_r.steps.size()>0)
					{
						if(temp_r.steps.lastElement().childName!=null) //排除所有的预先设置好的数学运算函数
						{
							String functionName=temp_r.steps.lastElement().childName;
							if(functionName.equals("加一")|| functionName.equals("加二")|| functionName.equals("减一")|| 
								functionName.equals("减二")||functionName.equals("求相反数")||functionName.equals("二零一七减")
								||functionName.equals("取首字符"))
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
			Convenient.showln("prediates2数组的长度为："+predicates2.size());
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
					//Convenient.showln(classes[i].getSimpleName()+"的路径数组为空");
					temp_routes=new Vector<Route>();
				}
				for(Route temp_r:temp_routes)
				{
					if(temp_r.steps.size()>0)
					{
						if(temp_r.steps.lastElement().childName!=null) //排除所有的数学运算函数
						{
							String functionName=temp_r.steps.lastElement().childName;
							if(functionName.equals("加一")|| functionName.equals("加二")|| functionName.equals("减一")|| 
								functionName.equals("减二")||functionName.equals("求相反数")||functionName.equals("二零一七减")
								||functionName.equals("取首字符"))
								continue;
							
							if(temp_r.steps.lastElement().childName.equals("日期转字符串"))
							{
								Convenient.showln("函数【日期转字符串】的返回值类型为"+getClass_resultOfRoute(temp_r, classes[i]).getSimpleName()+
										"，而"+predicates2.get(j).name+"的参数类型为"
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
			return route.get("随便写个字符串").getClass();
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
		predicates1.add(new Predicate("是正数",Basic.class.getMethod("是正数",types) , Integer.class, 1));
		predicates1.add(new Predicate("是负数",Basic.class.getMethod("是负数",types) , Integer.class, 1));
		predicates1.add(new Predicate("为零",Basic.class.getMethod("为零",types) , Integer.class, 1));
		predicates1.add(new Predicate("是偶数",Basic.class.getMethod("是偶数",types) , Integer.class, 1));
		predicates1.add(new Predicate("是奇数",Basic.class.getMethod("是奇数",types) , Integer.class, 1));
		predicates1.add(new Predicate("小于10",Basic.class.getMethod("小于10",types) , Integer.class, 1));
		
		types=new Class[2];
		types[0]=types[1]=Integer.class;
		
		predicates2.add(new Predicate("大于",Basic.class.getMethod("大于",types) , Integer.class, 2));
		predicates2.add(new Predicate("等于",Basic.class.getMethod("等于",types) , Integer.class, 2));
		predicates2.add(new Predicate("小于",Basic.class.getMethod("小于",types) , Integer.class, 2));
		types[0]=types[1]=String.class;
		predicates2.add(new Predicate("字符串大于",Basic.class.getMethod("字符串大于",types) , String.class, 2));
		predicates2.add(new Predicate("字符串等于",Basic.class.getMethod("字符串等于",types) , String.class, 2));
		predicates2.add(new Predicate("字符串小于",Basic.class.getMethod("字符串小于",types) , String.class, 2));
		
		types=new Class[1];
		types[0]=String.class;
		functions.add(new Function("取首字符",String.class,String.class,Basic.class.getMethod("取首字符", types)));
		types[0]=Integer.class;
		functions.add(new Function("减一",Integer.class,Integer.class,Basic.class.getMethod("减一",types)));
		functions.add(new Function("减二",Integer.class,Integer.class,Basic.class.getMethod("减二",types)));
		functions.add(new Function("加一",Integer.class,Integer.class,Basic.class.getMethod("加一",types)));
		functions.add(new Function("加二",Integer.class,Integer.class,Basic.class.getMethod("加二",types)));
		functions.add(new Function("求相反数",Integer.class,Integer.class,Basic.class.getMethod("求相反数",types)));
		functions.add(new Function("二零一七减",Integer.class,Integer.class,Basic.class.getMethod("二零一七减",types)));
		
		Human 何威慑=new Human("何威慑","男",new Date(1970,3,19));vars.add(何威慑);
		Human 刘云=new Human("刘云","女",new Date(1985,10,11));vars.add(刘云);
		Human 方几何=new Human("方几何","女",new Date(1973,12,31));vars.add(方几何);
		Human 秦布朗=new Human("秦布朗","男",new Date(1987,5,2));vars.add(秦布朗);
		Human 任代数=new Human("任代数","男",new Date(1986,8,15));vars.add(任代数);
		Human 周琴=new Human("周琴","女",new Date(1977,9,12));vars.add(周琴);
		Human 严智才=new Human("严智才","男",new Date(1972,2,29));vars.add(严智才);
		Human 卢华=new Human("卢华","女",new Date(1981,10,19));vars.add(卢华);
		Human 文诗=new Human("文诗","男",new Date(1988,3,29));vars.add(文诗);
		Human 程微分=new Human("程微分","女",new Date(1978,11,12));vars.add(程微分);
		Human 张龚明=new Human("张龚明","男",new Date(1974,4,10));vars.add(张龚明);
		_Class 一班=new _Class("一班",何威慑,刘云,方几何,秦布朗);vars.add(一班);
		_Class 二班=new _Class("二班",严智才,刘云,任代数,周琴);vars.add(二班);
		_Class 三班=new _Class("三班",卢华,文诗,程微分,张龚明);vars.add(三班);
		Student 林云=new Student("林云","女",new Date(1999,12,17),一班,81,72,83);vars.add(林云);
		Student 罗莉=new Student("罗莉","女",new Date(2000,4,3),一班,88,75,71);vars.add(罗莉);
		Student 张玉明=new Student("张玉明","男",new Date(2000,3,22),一班,69,76,63);vars.add(张玉明);
		Student 郑驰名=new Student("郑驰名","男",new Date(1999,12,16),一班,79,78,84);vars.add(郑驰名);
		Student 周雨婷=new Student("周雨婷","女",new Date(1999,9,26),一班,90,95,93);vars.add(周雨婷);
		Student 盛志恒=new Student("盛志恒","男",new Date(1999,9,10),一班,85,100,99);vars.add(盛志恒);
		Student 刘倩=new Student("刘倩","女",new Date(2000,1,25),一班,83,92,81);vars.add(刘倩);
		Student 柴远航=new Student("柴远航","男",new Date(1999,11,16),一班,69,85,79);vars.add(柴远航);
		Student 林杨=new Student("林杨","男",new Date(2000,1,17),一班,66,72,63);vars.add(林杨);
		Student 李妍玥=new Student("李妍玥","女",new Date(2000,2,21),二班,92,90,86);vars.add(李妍玥);
		Student 盛振华=new Student("盛振华","男",new Date(1999,10,6),二班,79,75,79);vars.add(盛振华);
		Student 林清河=new Student("林清河","男",new Date(1999,12,9),二班,89,98,89);vars.add(林清河);
		Student 罗极=new Student("罗极","男",new Date(1999,9,19),二班,79,100,87);vars.add(罗极);
		Student 郑雨欣=new Student("郑雨欣","女",new Date(1999,12,25),二班,90,95,93);vars.add(郑雨欣);
		Student 李晓明=new Student("李晓明","男",new Date(2000,2,7),二班,89,90,77);vars.add(李晓明);
		Student 陈昕=new Student("陈昕","女",new Date(1999,9,2),二班,98,80,91);vars.add(陈昕);
		Student 汪豆豆=new Student("汪豆豆","男",new Date(2000,1,30),二班,81,93,69);vars.add(汪豆豆);
		Student 汪茜=new Student("汪茜","女",new Date(1999,12,30),二班,78,67,68);vars.add(汪茜);
		Student 陈珍=new Student("陈珍","女",new Date(2000,8,21),二班,81,69,70);vars.add(陈珍);
		Student 邓军=new Student("邓军","男",new Date(1998,11,18),三班,76,71,81);vars.add(邓军);
		Student 刘楚恒=new Student("刘楚恒","男",new Date(1999,9,30),三班,69,78,61);vars.add(刘楚恒);
		Student 李典雅=new Student("李典雅","女",new Date(1999,9,5),三班,78,66,80);vars.add(李典雅);
		Student 周小萌=new Student("周小萌","女",new Date(2000,2,29),三班,89,80,72);vars.add(周小萌);
		
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
				Convenient.showln(tree.root.data.getClass().getSimpleName()+"找不到route");
				continue;
			}
		}
		getRoutes(new Integer(1234));
		getRoutes(new String("强制为String类寻找路线"));
		getRoutes(new Date(1000,1,1));
		getRoutes(new Position(21,37));
		
		Main.refresh_predicate1(true);
		Main.refresh_predicate2();
		
	}
	public static Vector<Function> findSuitableFunctions(Tree argTree,Object returnValue)  //argTree是参数变量的树形结构，returnValue是返回值，由用户输入
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		Vector<Function> result=new Vector<Function>();//这是一个集合，用来存储所有符合条件的函数
		Vector<Route> routes=Main.getRoutes(argTree);//遍历树的每一个结点，把每个结点的遍历过程（经过的所有结点）记录到一个Route变量里面
		
		for(Route thisRoute:routes) //thisRoute遍历当前结点所需要经过的路径   注：遍历方法为”深度优先“
		{
			//已知路线（thisRoute）和出发点（根节点argTree.root），访问路线末尾的结点，格式为：<路线>.get(出发点.data)
			if(thisRoute.get(argTree.root.data).equals(returnValue) ) //如果被遍历的结点等于返回值
				result.add(new Function("",returnValue.getClass(),argTree.root.data.getClass(),thisRoute));//则保存当前路线，注意先把路线包装成Function函数
		}
		
		return result;//返回这一轮得到的所有可行方案，等待筛选
	}
    
    public static boolean createNewPredicate1() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	System.out.println("提示：如果要退出，请输入XXX；如果要撤销错误的数据，请输XX");
		System.out.print("谓词名称=");
		Scanner sc=new Scanner(System.in);
		String pName=sc.next();
		Class argType;
		Integer count=1;
		Vector<Predicate> pSet=new Vector<Predicate>();
		while(true)
		{
			//boolean quitAndSave=false;
			String inp;//用户输入的文本
			//Array data=new Array(Object.class);//数组，用来保存一组数据
			Object arg;
			System.out.print("参数=");
			inp=sc.next();
			if(inp.equals("XX")) continue;//输入XX撤销
			if(inp.equals("XXX"))  break;//输入XXX退出
			arg=getVarByName(inp);
			argType=arg.getClass();
			
			Tree tree;
			if(argType.equals(Human.class)||argType.equals(Student.class)||argType.equals(_Class.class))
				tree=getTree(arg);
			else
				tree=new Tree(arg);
			Vector<Predicate> thisSet=status_property_findSuitableP(tree);//符合当前这组数据的所有谓词
			if(1==count) pSet.addAll(thisSet);//原始集合是空的，就把全部谓词加进去
			else pSet=PFSet.P_retainAll(pSet, thisSet); //否则就取交集
			
			if(pSet.size()==0)
			{
				System.out.println("没有任何满足条件的谓词，循环结束。");
				return false;
			}
			else if(pSet.size()>1){ //输出淘汰结果
				for(int i=0;i<pSet.size();i++)
				{
					System.out.println((i+1)+":");
					System.out.println("谓词形式："+pSet.get(i));
					System.out.println("真命题：　"+pSet.get(i).toString(arg));
				}
			}
			else if(pSet.size()==1)
			{
				System.out.println("谓词形式："+pSet.firstElement());
				System.out.println("真命题　："+pSet.firstElement().toString(arg));
			}
			
			String choice="Y";
			if(pSet.size()>1)
			{
				System.out.print("是否保存当前结果？（Y/N）");
				choice=sc.next();
			}
			
			if(choice.equals("Y"))
			{
				if(pSet.size()==1)//如果是单个的谓词
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
				System.out.println("您已成功创建一元谓词，名为："+predicates1.lastElement().name);
				break;
			}
			
			count++;
		}
		refresh_predicate1(false);
		return true;
    }
    public static boolean createNewPredicate2() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		System.out.println("提示：如果要退出，请输入XXX；如果要撤销错误的数据，请输XX");
		System.out.print("谓词名称=");
		Scanner sc=new Scanner(System.in);
		String pName=sc.next();
		Class argType;
		Integer count=1;
		//Vector<Array> datas=new Vector<Array>(); 
		Vector<Predicate> pSet=new Vector<Predicate>();
		while(true)
		{
			boolean quitAndSave=false;
			String inp;//用户输入的文本
			Array data=new Array(Object.class);//数组，用来保存一组数据
			Object arg1,arg2;
			System.out.print("第"+count+"组数据：参数1=");
			inp=sc.next();
			if(inp.equals("XX")) continue;//输入XX撤销
			if(inp.equals("XXX"))  break;//输入XXX退出
			arg1=getVarByName(inp);
			argType=arg1.getClass();
			data.add(arg1);
			System.out.print("　　　　　");System.out.print(Convenient.blanks(count.toString().length()));
			System.out.print("参数2=");
			inp=sc.next();
			if(inp.equals("XX")) continue;
			else{
				arg2=getVarByName(inp);
				data.add(arg2);
			}
			
			Tree tree1,tree2;
			if(quitAndSave==false) //用户没有输入过XXX
			{
				if(argType.equals(Human.class)||argType.equals(Student.class)||argType.equals(_Class.class)){
					tree1=getTree(data.get(0));
					tree2=getTree(data.get(1));
				}
				else{
					tree1=new Tree(data.get(0));
					tree2=new Tree(data.get(1));
				}
				Vector<Predicate> thisSet=compare_findSuitableP(tree1,tree2);//符合当前这组数据的所有谓词
				if(1==count) pSet.addAll(thisSet);//原始集合是空的，就把全部谓词加进去
				else pSet=PFSet.P_retainAll(pSet, thisSet); //否则就取交集
			}
			
			if(pSet.size()==0)
			{
				System.out.println("没有任何满足条件的谓词，循环结束。");
				return false;
			}
			else if(pSet.size()>1){ //输出淘汰结果
				for(int i=0;i<pSet.size();i++)
				{
					System.out.println((i+1)+":");
					System.out.println("谓词形式："+pSet.get(i));
					System.out.println("比较结果："+pSet.get(i).toString(data.get(0), data.get(1)));
				}
			}
			else if(pSet.size()==1)
			{
				System.out.println("谓词形式："+pSet.firstElement());
				System.out.println("比较结果："+pSet.firstElement().toString(data.get(0), data.get(1)));
			}
			
			String choice="Y";
			if(pSet.size()>1)
			{
				System.out.print("是否保存当前结果？（Y/N）");
				choice=sc.next();
			}
			
			if(choice.equals("Y"))
			{
				if(pSet.size()==1)//如果是单个的谓词
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
				System.out.println("您已成功创建二元谓词，名为："+predicates2.lastElement().name);
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
		System.out.print("请输入函数的名称：");
		String fName=sc.next();
		Class argType,returnType;//参数类型，返回值类型
		Integer count=1;//计数变量，表示“这是第几组数据”
		
		Vector<Function> fSet=new Vector<Function>();//函数集合，当前剩余的所有可行方案
		while(true)
		{
			String inp;//记录用户输入的文字
			Object arg,returnValue;//用户输入的参数和返回值
			System.out.println("请输入第"+count+"组数据：");
			System.out.print("参数=");
			inp=sc.next();
			arg=getVarByName(inp);  //根据变量名返回变量，如果输入的是坐标、日期，则临时生成一个坐标或日期；如果不属于任何类型，则当作字符串或整数
			argType=arg.getClass();
			Tree argTree=new Tree(arg);//为参数变量生成一棵树
			System.out.print("返回值=");
			inp=sc.next();
			returnValue=getVarOrStringByName(inp);
			returnType=returnValue.getClass();
			Vector<Function> thisSet=findSuitableFunctions(argTree,returnValue);//根据用户刚才输入的数据得到的方案
			
			if(1==count) fSet.addAll(thisSet);//输入第一组数据时，集合为空，此时把所有的可行方案全都加入集合中
			else fSet=PFSet.F_retainAll(fSet, thisSet); //否则，就对集合做“交”运算
			
			if(fSet.size()==0) //如果集合为空，即所有的方案都被淘汰了
			{
				System.out.println("没有任何满足条件的函数，循环结束");
				return false;
			}
			else
			{
				System.out.println("找到以下函数：");
				for(int i=0;i<fSet.size();i++)
					System.out.println((i+1)+"："+fSet.get(i).toString());
			}
			String choice;//用户的选择，Y或者N
			if(fSet.size()>1) //如果存在多种方案，则表明有可能存在不正确的方案，这时候应该询问用户
			{
				System.out.print("是否保存？(Y/N)");
				choice=sc.next();
			}
			else choice="Y"; //如果只有一种方案，则直接保存
			
			if("Y".equals(choice))
			{
				if(fSet.size()==1)
					functions.add(fSet.firstElement());
				else //多种方案里面选一种
				{
					System.out.println("您只能选取其中的一种结果，请输入方案的序号：");
					int number=sc.nextInt();
					functions.add(fSet.get(number-1)); //放到全局数组变量里面
				}
				functions.lastElement().name=fName;
				functions.lastElement().argType=argType;
				functions.lastElement().returnType=returnValue.getClass();
				refresh();
				System.out.println("您已成功创建函数，名为："+functions.lastElement().name+
						"，参数类型："+functions.lastElement().argType.getSimpleName()+
						"，返回值类型："+functions.lastElement().returnType.getSimpleName());
				break;
			}
			count++;
		}
		
		refresh();//刷新路线
		refresh_predicate1(true);//刷新一元谓词路线
		refresh_predicate2();//刷新二元谓词路线
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
		System.out.print("请输入函数名称=");
		String fName=sc.next();
		System.out.print("参数=");
		Object arg=getVarByName(sc.next());
		Class argType=arg.getClass();
		System.out.print("返回值=");
		String returnValue=sc.next();
		System.out.print("返回的字符串里面包含了多少个成分（子参数）？");
		int num=sc.nextInt();
		Vector<Route> routes=new Vector<Route>();
		for(int i=0;i<num;i++)
		{
			System.out.print("参数"+(i+1)+"=");
			routes.add(getShortestRoute(arg,sc.next()));
		}
		StringProcessor sp=new StringProcessor(returnValue,arg,routes);
		System.out.println("\n"+sp);
		System.out.println("\n"+sp.toString(new Date(1997,7,1)));
	}
	public static void createNewConditionalStrProcessor() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		System.out.print("请输入函数的名称=");
		Scanner sc=new Scanner(System.in);
		String fName=sc.next();
		Class argType = null;
		System.out.println(
				"您将建立一个具有条件判断功能的函数，在不同情况下，计算机将会执行不同的分支，请务必输入所有可能的情况。");
		System.out.println("一种情况输入完毕后，请键入NEXT；输入END即可退出函数建立的过程。");
		Vector<StringProcessor> sps=new Vector<StringProcessor>();
		Vector<Vector<Predicate>> statuses=new Vector<Vector<Predicate>>();
		for(int i=0;true;i++)
		{
			System.out.println("情况"+(i+1)+"：");
			boolean exit=false;
			for(int j=0;true;j++)
			{
				int num;//子参数的个数
				Vector<Route> routes=new Vector<Route>();
				System.out.println("【参数——返回值】样本"+(j+1)+"：");
				System.out.print("参数=");
				String inp=sc.next();
				if(inp.equals("NEXT")) 
				{
					String yesOrNo;//是否结束当前的样本输入
					System.out.println("您刚才输入的几个样本全部满足以下条件：");
					for(Predicate p:statuses.lastElement())
						System.out.println(p);
					System.out.print("是否结束当前的输入？请确认(Y/N)");
					yesOrNo=sc.next();
					if(yesOrNo.equals("Y")) break;
					else
					{
						j--;
						continue;//跳回去
					}
				}
				if(inp.equals("END")) { System.out.println("\n");exit=true;break; }
				Object arg=getVarByName(inp);
				argType=arg.getClass();
				System.out.print("返回值=");
				String returnValue=sc.next();
				if(j==0) //一个子函数里面的第一个参数样本
				{
					String same; //字符串，表示“是否与上一次格式相同”
					if(i>0) //不是第一个子函数
					{
						System.out.print("参数形式是不是和刚才的函数一样？（Y/N）");
						same=sc.next();
					}
					else //i==0 第一个子函数
						same="N";
					if(same.equals("N"))
					{
						System.out.print("返回的字符串里面包含了多少个成分（子参数）？");
						num=sc.nextInt();
						for(int k=0;k<num;k++)
						{
							System.out.print("参数"+(k+1)+"=");
							routes.add(getShortestRoute(arg,sc.next()));
						}
					}
					else // 用户输入了“Y”
						routes.addAll(sps.lastElement().routes);//和上次一样
				}
				
				if(j==0) sps.add(new StringProcessor(returnValue,arg,routes));//新建StringProcessor
				if(j==0)
					statuses.add( status_property_findSuitableP(new Tree(arg)) );
				else
				{
					Vector<Predicate> temp=new Vector<Predicate>();
					temp.addAll(status_property_findSuitableP(new Tree(arg)));
					temp=PFSet.P_retainAll(temp, statuses.lastElement()); //取交集
					statuses.lastElement().clear();
					statuses.lastElement().addAll(temp);
				}
			}
			if(exit==true) break;
		}
		
		Vector<Branch> branches=new Vector<Branch>();//把函数和条件集合打包成Branch（分支）
		for(int i=0;i<sps.size();i++)
			branches.add(new Branch(new Function("",sps.get(i)), statuses.get(i),FunctionTree.MARK_1D));
		
		FunctionTree functionTree=new FunctionTree(String.class,argType,branches);
		functionTree.removeSamePart();
		Function newFunction=functionTree.toFunction();
		newFunction.generateSubsets();
		newFunction.name=fName;
		Function.sortByNumOfConditions(newFunction);
		
		System.out.println("函数创建成功！名为:"+newFunction.name);
		System.out.println("它有"+newFunction.subFunctions.size()+"个子函数");
		for(int i=0;i<newFunction.subFunctions.size();i++)
		{
			System.out.println("子函数"+(i+1)+"：\n"+newFunction.subFunctions.get(i).toString());
			System.out.println("\n执行条件如下：");
			for(Predicate p: newFunction.subFunctions.get(i).conditions )
				System.out.println(p);
			System.out.println();
		}
		
		Main.functions.add(newFunction);//放入全局数组中
		refresh();
		refresh_predicate1(true);
		refresh_predicate2();
	}
    public static void createNewConditionalFunction() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
    {
    	System.out.print("请输入函数的名称=");
		Scanner sc=new Scanner(System.in);
		String fName=sc.next();
		Class argType = null;//参数类型
		Class returnType = null;//返回值类型：只能是Integer或String
		System.out.println(
				"您将建立一个具有条件判断功能的函数，在不同情况下，计算机将会执行不同的分支，请务必输入所有可能的情况。");
		System.out.println("一种情况输入完毕后，请键入NEXT；输入END即可退出函数建立的过程。");
		Vector<Function> subFunctions=new Vector<Function>();
		Vector<Vector<Predicate>> statuses=new Vector<Vector<Predicate>>();

		for(int i=0;true;i++)
		{
			System.out.println("情况"+(i+1)+"：");
			boolean exit=false;
			Vector<Function> suitableFunctions=new Vector<Function>();//候选函数
			Vector<Predicate> statusesOfArgOfThisSubFunction=new Vector<Predicate>();
			for(int j=0;true;j++)
			{
				int num;//子参数的个数
				Vector<Route> routes=new Vector<Route>();
				System.out.println("【参数——返回值】样本"+(j+1)+"：");
				System.out.print("参数=");
				String inp=sc.next();
				if(inp.equals("NEXT")) 
				{
					System.out.println("\n");
					if(suitableFunctions.size()==0) 
					{
						System.out.println("没有任何可用的方案，子函数创建失败！请重试");
						i--; //退回去
						exit=true;
						break;
					}
					else if(suitableFunctions.size()==1)
					{
						subFunctions.add(suitableFunctions.firstElement());//创建子函数
						statuses.add(statusesOfArgOfThisSubFunction);//加条件
					}
					else// if(suitableFunctions.size()>1)
					{
						System.out.println("备选方案如下，请输入数字：");
						for(int k=0;k<suitableFunctions.size();k++)
							System.out.println( (k+1)+"："+suitableFunctions.get(k) );
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
						Convenient.showln("调试：您刚才输入了一个整数。");
					}
					else arg=inp.toString();
				}
				
				argType=arg.getClass();
				System.out.print("返回值=");
				String str_returnValue=sc.next();
				Object returnValue;
				if(Convenient.isNumeric(str_returnValue)) //类型判断并转换：整数型或字符串型
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
					statusesOfArgOfThisSubFunction=PFSet.P_deleteSuperset(statusesOfArgOfThisSubFunction);//删除超集
				}
				else 
				{
					suitableFunctions=PFSet.F_retainAll(suitableFunctions, findSuitableFunctions(new Tree(arg), returnValue) );
					statusesOfArgOfThisSubFunction=PFSet.P_retainAll(   //做“交运算”
							statusesOfArgOfThisSubFunction, status_property_findSuitableP(new Tree(arg)));
					statusesOfArgOfThisSubFunction=PFSet.P_deleteSuperset(statusesOfArgOfThisSubFunction);//删除超集
				}
				
				System.out.println("满足要求的函数如下：");
				for(Function f:suitableFunctions)
					System.out.println(f);
				System.out.println("当参数满足以下全部条件时，子函数才会被执行：");
				for(Predicate p:statusesOfArgOfThisSubFunction)
					System.out.println(p);
					
			}
			if(exit==true) break;
		}
		
		Vector<Branch> branches=new Vector<Branch>();//把函数和条件集合打包成Branch（分支）
		for(int i=0;i<subFunctions.size();i++)
			branches.add(new Branch(subFunctions.get(i), statuses.get(i),FunctionTree.MARK_1D));//一维数组二维化
		
		FunctionTree functionTree=new FunctionTree(returnType,argType,branches);
		functionTree.removeSamePart();
		Function newFunction=functionTree.toFunction();
		newFunction.generateSubsets();
		newFunction.name=fName;
		
		System.out.println("函数创建成功！名为:"+newFunction.name);
		Convenient.showln("参数类型为"+newFunction.argType.getSimpleName()+
				"，返回值类型为"+newFunction.returnType.getSimpleName());
		System.out.println("它有"+newFunction.subFunctions.size()+"个子函数");
		for(int i=0;i<newFunction.subFunctions.size();i++)
		{
			System.out.println("子函数"+(i+1)+"：\n"+newFunction.subFunctions.get(i).toString());
			System.out.println("\n执行条件如下：");
			for(Predicate p: newFunction.subFunctions.get(i).conditions )
				System.out.println(p);
			System.out.println();
		}
		
		Main.functions.add(newFunction);//放入全局数组中
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
    		System.out.println(classes[i].getSimpleName()+"类：");
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
    		System.out.println(classes[i].getSimpleName()+"类：");
    		for(PredicateAndRoutes[] outside:predicate1_routes)
    		{
    			for(PredicateAndRoutes inner:outside)
    			{
    				for(int j=0;j<inner.routes.size();j++)
    					System.out.println(inner.predicate.name+"："+inner.routes.get(j));
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
    		System.out.println(classes[i].getSimpleName()+"类：");
    		for(PredicateAndRoutes[] outside:predicate2_routes)
    		{
    			for(PredicateAndRoutes inner:outside)
    			{
    				for(int j=0;j<inner.routes.size();j++)
    					System.out.println(inner.predicate.name+"："+inner.routes.get(j));
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
			
			if(inp.equals("新建函数"))  createNewFunction();
			else if(inp.equals("新建一元谓词"))  createNewPredicate1();
			else if(inp.equals("新建二元谓词")) createNewPredicate2();
			else if(inp.equals("新建字符串函数"))  createNewStrProcessor();
			else if(inp.equals("新建带条件的字符串函数"))  createNewConditionalStrProcessor();
			else if(inp.equals("新建带条件的函数")) createNewConditionalFunction();
			else if(inp.equals("查看路线")) showRoutes();
			else if(inp.equals("查看一元谓词路线"))  showPredicate1Routes();
			else if(inp.equals("查看二元谓词路线"))  showPredicate2Routes();
			else
			{
				Predicate p=getPredicateByName(inp);
				if(p!=null)
				{
					if(p.numOfArgs==1)
					{
						System.out.print("参数=");
						Object arg=getVarOrValueByName(sc.next());
						if(arg==null)
						{
							System.out.println("无效的参数！");
							continue;
						}
						else
						{
							boolean judgement=p.judge(arg);
							if(judgement) System.out.println("真");
							else System.out.println("假");
						}
					}
					else if(p.numOfArgs==2)
					{
						System.out.print("参数1=");
						Object arg1=getVarOrValueByName(sc.next());
						System.out.print("参数2=");
						Object arg2=getVarOrValueByName(sc.next());
						if(arg1==null||arg2==null)
						{
							System.out.println("无效的参数！");
							continue;
						}
						else
						{
							boolean judgement=p.judge(arg1,arg2);
							if(judgement) System.out.println("真");
							else System.out.println("假");
						}
					}
				}
				else //不是谓词，可能是函数
				{
					Function f=getFunctionByName(inp);
					if(f==null)
					{
						System.out.println("无效的命令");
						continue;
					}
					else
					{
						System.out.print("参数=");
						Object arg=getVarOrValueByName(sc.next());
						if(arg==null)
						{
							System.out.println("无效的参数");
							continue;
						}
						else
						{
							if(arg.getClass()!=f.argType)
							{
								System.out.println("参数类型不匹配");
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
