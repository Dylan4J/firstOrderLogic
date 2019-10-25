import java.util.Vector;

public class Array {
	public Class elemType;//Ԫ������
	public Vector<Object> elements=new Vector<Object>();
	public static class Type //������ʾ���������
	{
		public Class elemType;
		public Type(){}
		public Type(Class cls){elemType=cls;}
	}
	public Array(){}
	public Array(Class type)
	{
		elemType=type;
	}
	public Array(Class type,Vector<Object> arr)
	{
		elemType=type;
		elements.addAll(arr);
	}
	public Object get(Integer i)
	{
		return elements.get(i);
	}
	public Integer _Ԫ�ظ���() 
	{
		return elements.size();
	}
	public Integer meta_���() //�����Ԫ���������
	{
		if(elemType!=Integer.class) return null;
		Integer sum=0;
		for(Object e:elements)
			sum+=(Integer)e;
		return sum;
	}
	public void add(Object e)
	{
		elements.add(e);
	}
}
