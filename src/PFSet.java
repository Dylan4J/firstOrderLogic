import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Vector;

public class PFSet {
	public static boolean in_F(Function f,Vector<Function> set)
    {
    	for(int i=0;i<set.size();i++)
    		if(f.equals(set.get(i))==true) return true;
    	return false;
    }
	public static boolean in_P(Predicate p,Vector<Predicate> set)
	{
		for(int i=0;i<set.size();i++)
			if(p.equals(set.get(i)) ==true) return true;
		return false;
	}
	public static Vector<Predicate> P_retainAll(Vector<Predicate> set1,Vector<Predicate> set2)
	{
		Vector<Predicate> intersection=new Vector<Predicate>();
		for(Predicate e1:set1)
		{
			if(in_P(e1,set2)==true) intersection.add(e1);
		}
		return intersection;
	}
    public static Vector<Function> F_retainAll(Vector<Function> set1,Vector<Function> set2)
    {
    	Vector<Function> intersection=new Vector<Function>();//����
		for(Function e1:set1)
		{
			if(in_F(e1,set2)==true) intersection.add(e1);
		}
		
		return intersection;
    }
    public static Vector<Predicate> P_removeAll(Vector<Predicate> set1,Vector<Predicate> set2)//��set1��ɾȥ������set2�г��ֵ�Ԫ��
    {
    	Vector<Predicate> result=new Vector<Predicate>();
    	for(Predicate p1:set1)
    		if(in_P(p1,set2)==false) result.add(p1);
    	return result;
    }
    public static boolean PSet_equals(Vector<Predicate> set1,Vector<Predicate> set2) //����1���ڼ���2
    {
		if(set1.size()!=set2.size()) return false;
    	for(Predicate p1:set1)
			if(in_P(p1, set2)==false) return false;
		return true;
    }
    public static boolean PSet_contain(Vector<Predicate> set1,Vector<Predicate> set2) //����1���������2
    {
		if(set1.size()<=set2.size()) return false;
    	for(Predicate p2:set2)
			if(in_P(p2, set1)==false) return false;
		return true;
    }
	public static boolean in(Object e, Vector set) {
		for(int i=0;i<set.size();i++)
    		if(e.equals(set.get(i))==true) return true;
		return false;
	}
	public static int P_getIndex(Predicate p,Vector<Predicate> set)
	{
		for(int i=0;i<set.size();i++)
			if(p.equals(set.get(i))) return i;
		return -1;
	}
	public static Vector<Predicate> P_deleteSuperset(Vector<Predicate> set) //ɾ�����������硰С��10�� �� ���Ǹ������ĳ�������ɾ������Ϊ���Ǹ�������ν��
	{
		Vector<Predicate> result=new Vector<Predicate>();
		HashSet<Integer> shouldDelete=new HashSet<Integer>();
		if(in_P(Const.Ϊ��,set) || in_P(Const.�Ǹ���,set) )
			shouldDelete.add(P_getIndex(Const.С��10,set));
		if(in_P(Const.Ϊ��,set))
			shouldDelete.add(P_getIndex(Const.��ż��,set));
		for(int i=0;i<set.size();i++)
			if(shouldDelete.contains(i)==false) result.add(set.get(i));
		return result;
	}
}
