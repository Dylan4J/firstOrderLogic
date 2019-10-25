import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convenient {
	public static boolean debug=false;//debug��ʾ���Ƿ��ڵ���״̬�������򷢲���ʱ��Ҫ��������ر�������false
	public static void showln(String str)
	{
		if(debug) System.out.println(str);
	}
	public static void show(String str)
	{
		if(debug) System.out.print(str);
	}
	public static Vector<Field> GetFields(Class cls) //��ȡĳ������������ԣ�������Object�౾����еģ�Ҳ������name����
	{
		if(cls.getSuperclass()==Object.class) //���̳��κ��Զ������
		{
			Field[] temp=cls.getDeclaredFields();
			Vector<Field> result=new Vector<Field>();
			for(Field f:temp)
				if(f.getName().equals("name")==false) result.add(f);
			return result;
		}
		else //�̳���ĳ���Զ������
		{
			//��� һ���̳�
			Field[] fieldsOfThis=cls.getDeclaredFields();//�Ѿ��ų���Object����߱�������
			Vector<Field> fieldsOfSuper=GetFields(cls.getSuperclass());//�ų���name���Ժ�Object����߱�������
			if(fieldsOfThis==null) return fieldsOfSuper;
			else
			{
				Vector<Field> result=new Vector<Field>();//result  = �౾������Լ��� + ��������Լ���
				for(Field f:fieldsOfThis)
					result.add(f);
				for(Field f:fieldsOfSuper)
					result.add(f);
				return result;
			}
		}
	}
    public static Vector<String> GetFieldNames(Class cls)
    {
    	Vector<String> names=new Vector<String>();
    	Vector<Field> fields=GetFields(cls);
    	for(Field field:fields)
    		names.add(field.getName());
    	return names;
    }
	public static Vector<Method> GetMethods(Class cls) //��ȡĳ��������з�������������meta_��ͷ��
    {
    	Vector<Method> result=new Vector<Method>();
    	Method[] temp=cls.getMethods();//�����˴Ӹ�������̳����ķ���
    	for(Method m:temp)
    		if(m.getName().startsWith("meta_")) //meta_��һ����ǣ������÷�����һ����Ԫ���������� 
    			result.add(m);
    	return result;
    }
	public static Method GetMethod(Class cls,String mName)//�������ֻ�ȡ������meta_��ʡ��
	{
		Method[] fields=cls.getMethods();//�����˴Ӹ�������̳����ķ���
		for(Method m:fields)
			if(m.getName().equals(mName) || 
					m.getName().equals("meta_"+mName) ) //��������mata_  ����ʡ��
				return m;
		return null;
	}
	public static Field GetField(Class cls,String fName)//�������ֻ�ȡ����
	{
		Field[] fields=cls.getFields();
		if(fields==null) return null;
		for(Field f:fields)
			if(f.getName().equals(fName)) return f;
		return null;
	}
	public static String getName(Object obj) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		if(obj==null) return "<δ����>";
		if(obj.getClass()==Integer.class||obj.getClass()==String.class)
			return obj.toString();
		else if(obj.getClass()==Array.class)
			return "����";
		else if(obj.getClass()==Position.class)
			return "����";
		else
		{
			showln("���ԣ�"+obj.getClass().getSimpleName());
			String name=obj.getClass().getField("name").get(obj).toString();
			if(name.equals("")) return "δ����"+obj.getClass().getSimpleName()+"�Ͷ���";
			else return name;
		}
	}
	public static Vector Set2Vec(HashSet set) //����Setת��ΪVector
	{
		Vector vec=new Vector();
		for(Object e:set)
			vec.add(e);
		return vec;
	}
	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++)
		{
			if(str.length()>1&&str.charAt(i)=='-') continue;//����
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
	}
	public static boolean isPosition(String str)//�Ƿ�Ϊ����
	{
		// ������ʽ����
	    String regEx = "(-{0,1}\\d*,-{0,1}\\d*)";
	 // ����������ʽ
	    Pattern pattern = Pattern.compile(regEx);
	    Matcher matcher = pattern.matcher(str);
	    // �����ַ������Ƿ���ƥ��������ʽ���ַ�/�ַ���
	    return matcher.find();
	}
	public static boolean isDate(String str)
	{
		String[] tmp=str.split("[.]");
		if(tmp.length!=3) return false;
		for(int i=0;i<=2;i++)
		{
			if(isNumeric(tmp[i])==false) return false;
		}
		return true;
	}
	public static String blanks(int n)
	{
		String result="";
		for(int i=1;i<=n;i++)
			result+=" ";
		return result;
	}
}
