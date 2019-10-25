import java.util.Stack;
import java.util.Vector;

import selfprogramming_exp24.Route.Association;

public class Node {
	Object data; //��������
	Node parent;//���ڵ�
	Vector<Node> children=new Vector<Node>();//���ӽ��
	Association relationshipWithParent=new Association();//�븸�ڵ�Ĺ�ϵ
	public Node(){}
	public Node(Object data,Node parent)
	{
		this.data=data;
		this.parent=parent;
	}
	Node(Object data,Node parent,Association assoc)
	{
		this(data,parent);
		this.relationshipWithParent=assoc;
	}
	public Route getRoute()
	{
		if(this.parent==null)  //�Լ����Ǹ��ڵ�
		{
			return new Route(true);//���ء�����
		}
		Stack<Association> stack=new Stack<Association>();
		Route result=new Route();
		Node temp=this;
		while(temp!=null)
		{
			if(temp.relationshipWithParent.type!=null) stack.push(temp.relationshipWithParent);
			temp=temp.parent; //��������
		}
		while(stack.empty()==false) //ת��Ϊ�����϶��¡�
			result.steps.add(stack.pop());
		
		return result;
	}
}
