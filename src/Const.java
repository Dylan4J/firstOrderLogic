import java.lang.reflect.Method;

public class Const {
	public static boolean MARK_2D=true;//��ά�����ǣ�����OR��AND��ϵ�����������
	
	public static Predicate ������=Main.predicates1.get(0);
	public static Predicate �Ǹ���=Main.predicates1.get(1);
	public static Predicate Ϊ��=Main.predicates1.get(2);
	public static Predicate ��ż��=Main.predicates1.get(3);
	public static Predicate ������=Main.predicates1.get(4);
	public static Predicate С��10=Main.predicates1.get(5);
	
	public static Predicate ����=Main.predicates2.get(0);
	public static Predicate ����=Main.predicates2.get(1);
	public static Predicate С��=Main.predicates2.get(2);
	public static Predicate �ַ�������=Main.predicates2.get(3);
	public static Predicate �ַ���С��=Main.predicates2.get(4);
	public static Predicate �ַ�������=Main.predicates2.get(5);
	
	public static Function ��һ=Main.functions.get(0);
	public static Function ����=Main.functions.get(1);
	public static Function ��һ=Main.functions.get(2);
	public static Function �Ӷ�=Main.functions.get(3);
	public static Function ���෴��=Main.functions.get(4);
	public static Function ����һ�߼�=Main.functions.get(5);
	
	public static Method returnSelf=Convenient.GetMethod(Basic.class, "returnSelf");
}
