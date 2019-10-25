import java.util.Vector;

public class FunctionTree { 
	public static boolean MARK_1D=true;
    public static class ConditionSet //һ������
	{
		public Vector<Predicate> predicates=new Vector<Predicate>(); //�����ɸ�ν����ɣ��������ⶼ������ν��(����)==TRUE��ʱ�����Żᱻִ��
		public Vector<Predicate> intersect(ConditionSet other) //������
		{
			Vector<Predicate> result=new Vector<Predicate>();
			result.addAll(predicates);
			result=PFSet.P_retainAll(result, other.predicates);
			return result;
		}
		public ConditionSet(Vector<Predicate> predicates)
		{
			this.predicates.addAll(predicates);
		}
	}
	public static class Branch //������֧
	{
		public Vector<ConditionSet> conditions=new Vector<ConditionSet>();//�����κ�һ�����������������Դ���function������ִ��
		public Function function;
		public Vector<Predicate> intersect()//����������С��������ϡ����� �����㣬�����������ȴ��û�ȷ�ϣ�Main����ʵ�֣�
		{
			if(conditions.isEmpty()) return new Vector<Predicate>();//���ؿռ���
			else
			{
				Vector<Predicate> result=new Vector<Predicate>();
				result.addAll(conditions.get(0).predicates);//����һ���������뼯����
				for(ConditionSet cond:conditions)
				{
					result=PFSet.P_retainAll(result, cond.predicates);//���������� ���н�����
					if(result.isEmpty()) return result; //���ؿռ���
				}
				return result;
			}
		}
		public Vector<Predicate> intersect(Branch other)
		{
			Vector<Predicate> intersection1=new Vector<Predicate>();//����֧�µ�ȫ��ν��
			intersection1.addAll(this.conditions.get(0).predicates);
			Vector<Predicate> intersection2=new Vector<Predicate>();//��һ��֧�µ�ȫ��ν��
			intersection2.addAll(other.conditions.get(0).predicates);
			if(this.conditions.size()>1) //����ڶ�������£������ᱻ����
				intersection1=this.intersect(); //�Ա���֧�µ�ν�ʼ�����������
			if(other.conditions.size()>1)
				intersection2=other.intersect();//����һ��֧�µ�ν�ʼ�����������
			intersection1=PFSet.P_retainAll(intersection1, intersection2);//��ȡ��������
			return intersection1;
		}
	    public Branch(Function f,Vector<ConditionSet> statuses)
	    {
	    	function=f;
	    	conditions.addAll(statuses);
	    }
	    public Branch(Function f,Vector<Predicate> predicates,boolean mark_1D)
	    {
	    	this.function=f;
	    	ConditionSet condSet=new ConditionSet(predicates);
	    	this.conditions.add(condSet);
	    }
	}
    public Class returnType;
    public Class argType;
	public Vector<Branch> branches=new Vector<Branch>();
    public FunctionTree(Class returnType,Class argType,Vector<Branch> branches)
    {
    	this.returnType=returnType;
    	this.argType=argType;
    	this.branches.addAll(branches);
    }
    public Vector<Predicate> removeSamePart()//�����з�֧�µ�����ν�ʽ��н����㣬��ȡ����ͬ���ּ�ΪS��Ȼ������з�֧���µ���������������S������
    {
    	Vector<Predicate> S=new Vector<Predicate>();
    	S.addAll(branches.get(0).intersect()); //��һ����֧
    	for(Branch branch:branches)
    		S=PFSet.P_retainAll(S, branch.intersect());//�������㣬��ȡ����ͬ����
    	for(Branch branch:branches)
    	{
    		for(ConditionSet condition:branch.conditions)
    			condition.predicates=PFSet.P_removeAll(condition.predicates,S);//������S������
    	}
    	return S;
    }
    public Function toFunction()
    {
    	Function result=new Function("",returnType,argType,new Vector<Function>());
    	Vector<Function> subFunctions=new Vector<Function>();
    	for(Branch branch:branches)
    	{
    		for(ConditionSet condition:branch.conditions)
    		{
    			Function newFunc=branch.function;
    			newFunc.conditions=(condition.predicates);
    			subFunctions.add(newFunc);
    		}
    	}
    	result.subFunctions=subFunctions;
    	return result;
    }
}
