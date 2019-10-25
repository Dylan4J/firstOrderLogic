import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import selfprogramming_exp24.Route.Association;

public class Tree {
	public Node root; //���ڵ�
	private static Vector<Route> tmp_routes=new Vector<Route>();
	public Tree(Object rootObj) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		root=new Node(rootObj,null);
		createTree(rootObj,null,root);
	}
	private static void generateRoutes(Node node) //����ÿ����㣬������·��
	{
		tmp_routes.add(node.getRoute());
		for(Node child:node.children)
			tmp_routes.add(node.getRoute());
	}
	public Vector<Route> generateRoutes()
	{
		tmp_routes.clear();
		generateRoutes(this.root);
		return tmp_routes;
	}
	public void createTree(Object obj,Node parent,Node thisNode) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException 
	//objΪ��ǰ���ʵ�Ԫ�أ�parentΪobjԪ�ص��ϼ������ڵ㣩��thisNodeΪ��ǰ�������ɵĽ��
	{	
		thisNode.data=obj;
		thisNode.parent=parent;
		if(obj.getClass()==Integer.class||obj.getClass()==String.class)
		{
			for(Function f:Main.functions)
			{
				if(f.argType!=obj.getClass()) continue;
				Node newNode=new Node();//�������ַ����ĺ��ӽ��
				newNode.relationshipWithParent.type="ȫ�ֺ�����";
				newNode.relationshipWithParent.childName=f.name;
				newNode.parent=thisNode;
				newNode.data=f.execute(obj)[1];
				thisNode.children.addElement(newNode);
				//���ټ����ݹ�
			}
		}
		else if(obj.getClass()==Array.class) //Ĭ��Array����ֻ����StringԪ�غ�IntegerԪ��
		{
			for(Integer i=0;i<((Array)obj)._Ԫ�ظ���();i++)
			{
				Node newNode=new Node();//����ĺ��ӽ��
				newNode.relationshipWithParent.type="�±�";
				newNode.relationshipWithParent.childName=i.toString();
				thisNode.children.addElement(newNode);
				createTree(  ((Array)obj).get(i), thisNode, thisNode.children.lastElement());
			}
			Vector<Method> methods=Convenient.GetMethods(Array.class);
			for(Method method:methods)
			{
				Node newNode=new Node();
				newNode.relationshipWithParent.type="������";
				newNode.relationshipWithParent.childName=method.getName();
				newNode.parent=thisNode;
				newNode.data=method.invoke(obj);
				thisNode.children.addElement(newNode);
				//����Ҫ��Array�ķ������ص��������еݹ�
			}
		}
		else //���Ӷ���
		{
			Vector<Field> fields=Convenient.GetFields(obj.getClass());
			Vector<Method> methods=Convenient.GetMethods(obj.getClass());
			for(Field field:fields)
			{
				//if(field.getName().equals("name")) continue;
				thisNode.children.add(new Node());
				thisNode.children.lastElement().relationshipWithParent.type="������";
				thisNode.children.lastElement().relationshipWithParent.childName=field.getName();
				int index=thisNode.children.size()-1;
				createTree(field.get(obj),thisNode,thisNode.children.get(index));
			}
			for(Method method:methods)
			{
				//if(method.getName().startsWith("meta_")==false) continue;//����Ԫ����������
				thisNode.children.add(new Node());
				thisNode.children.lastElement().relationshipWithParent.type="������";
				thisNode.children.lastElement().relationshipWithParent.childName=method.getName();
				int index=thisNode.children.size()-1;
				createTree(method.invoke(obj) , thisNode,thisNode.children.get(index));
			}
			for(Function f:Main.functions)
			{
				if(f.argType!=obj.getClass()) continue;
				thisNode.children.add(new Node());
				thisNode.children.lastElement().relationshipWithParent.type="ȫ�ֺ�����";
				thisNode.children.lastElement().relationshipWithParent.childName=f.name;
				//��ֹ�ݹ�
				thisNode.children.lastElement().parent=thisNode;
				thisNode.children.lastElement().data=f.execute(obj)[1];
			}
		}
		
	}
	private static void getAllRoutes_recur(Node node,Vector<Route> allRoutes)
    {
    	//nodeΪ���ڷ��ʵĽ�㣬allRoutes�������ս��
    	Route thisRoute=node.getRoute();//��ǰ����·��
    	allRoutes.add(thisRoute);
    	
    	for(Node child:node.children) //���α����������ӽ��
    		getAllRoutes_recur(child,allRoutes);
    }
	public Vector<Route> getAllRoutes()
	{
		Vector<Route> result=new Vector<Route>();
		getAllRoutes_recur(root,result);
		return result;
	}
}
