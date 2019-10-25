import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import selfprogramming_exp24.Convenient;

public class Route {
	public static class Association {
		public String type;//ȡֵ��{ �±�,������,������,ȫ�ֺ����� }
		public String childName;//���ӣ��¼���������
		public Association(){}
		public Association(String t,String cn)
		{
			type=t;
			childName=cn;
		}
		public boolean equals(Association other)
		{
			return type.equals(other.type)&&childName.equals(other.childName);
		}
		public String toString()
		{
			return "��"+type+"��"+(
					childName.startsWith("meta_")?childName.substring(5):childName )+"��";
		}
	}
	public Vector<Association> steps=new Vector<Association>();
	private Boolean returnSelfOrNull=null;//Ϊtrueʱ���ز�������Ϊfalseʱ����null
	
	public Object get(Object begin) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		if(isSelf()) //���ز�������
			return begin;
		else if(isNull())
			return null;
		Object result=begin; //����ʼ�㣨ͨ����һ�����ĸ��ڵ㣩��ʼ����������ֵ����result
		
		for(Association step:steps)
		{
			if(step.type.equals("�±�"))
			{
				if(result.getClass()!=Array.class) continue;
				result=((Array)result).get(new Integer(step.childName));
			}
			else if(step.type.equals("������"))
			{
				if(result.getClass()==String.class||result.getClass()==Integer.class||result.getClass()==Array.class) continue;
				if(PFSet.in( step.childName , Convenient.GetFieldNames(result.getClass()) )==false) continue;//���Ͱ�ȫ���
				result=result.getClass().getField(step.childName).get(result);
			}
			else if(step.type.equals("������"))
				result=Convenient.GetMethod(result.getClass(),step.childName).invoke(result);
			else if(step.type.equals("ȫ�ֺ�����"))
			{
				Function func=Main.getFunction(step.childName);
				if(null==func) continue;
				result=func.execute(result)[1];
			}
		}
		return result;
	}
    public boolean isSelf() //�Ƿ񷵻ز�������
	{
		if(returnSelfOrNull==null) return false;
		else return returnSelfOrNull==true;
	}
	public boolean isNull() //�Ƿ񷵻ؿ�ֵ
	{
		if(returnSelfOrNull==null) return false;
		else return returnSelfOrNull==false;
	}
	public boolean isNormal() //�Ƿ�������·��ִ��
	{
		return returnSelfOrNull==null;
	}
	public String toString()
	{
		if(isSelf()) return "������";
		String result="";
		//Convenient.show("��"+steps.size()+"��");
		for(Association step:steps)
			result+=step.toString()+"����>";
		return result.substring(0, result.length()-3);//ȥ�����һ����ͷ
	}
    public Route(boolean b)
	{
		returnSelfOrNull=b;
	}
	public Route(){}
	public boolean equals(Route other)
	{
		return this.toString().equals( other.toString() );
		/*
		if(this.isNormal()==false)
		{
			if(other.isSelf()!=this.isSelf()) return false;
		}
		if(this.steps.size()!=other.steps.size()) return false;
		for(int i=0;i<steps.size();i++)
		{
			if(steps.get(i).equals(other.steps.get(i))==false) return false;
		}
		return true;
		*/
	}
}
