import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

public class StringProcessor {
	public Class argType;
	public String beginning="";
	public Vector<Route> routes=new Vector<Route>();
	public Vector<String> textAfterRoutes=new Vector<String>();
	public StringProcessor(){}
	public StringProcessor(String beginning,Vector<Route> routes,Vector<String> textAfterRoutes,Class argtype)
	{
		this.beginning=beginning;
		this.routes.addAll(routes);
		this.textAfterRoutes.addAll(textAfterRoutes);
		this.argType=argtype;
	}
	public StringProcessor(String returnValue,Object arg,Vector<Route> routes )
	{
		this.argType=arg.getClass();
		this.routes.addAll(routes);
		Vector<String> actuals=new Vector<String>();
		for(Route r:routes)
			try {
				actuals.add(r.get(arg).toString());
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
					| InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		
		Vector<Integer> begins=new Vector<Integer>();
		Vector<Integer> ends=new Vector<Integer>();
		
		for(int i=0;i<actuals.size();i++)
		{
			if(i>0)
				begins.add(returnValue.indexOf(actuals.get(i), ends.get(i-1)+1));
			else begins.add(returnValue.indexOf(actuals.get(i)));
			
			ends.add(begins.lastElement()+actuals.get(i).length()-1);
		}
		
		if(begins.firstElement()>0)
			beginning=returnValue.substring(0, begins.firstElement()+1);
		
		for(int i=0;i<begins.size();i++)
		{
			if(i==begins.size()-1 ) break; //���һ��Ҫ���⴦��
			textAfterRoutes.add( returnValue.substring(ends.get(i)+1, begins.get(i+1)) );
		}
		if(ends.lastElement().equals(returnValue.length()-1))
			textAfterRoutes.add("");
		else
			textAfterRoutes.add(returnValue.substring(ends.lastElement()+1));
	}
    public String toStringSimple()
    {
    	String result="";result+=beginning;
		for(int i=0;i<routes.size();i++)
			result+="������"+(i+1)+"��"+textAfterRoutes.get(i);
		return result;
    }
    public String toString()
	{
		String result="";result+=beginning;
		//Convenient.showln("׼��toString ���鳤�ȣ�"+textAfterRoutes.size());
		for(int i=0;i<routes.size();i++)
			result+="������"+(i+1)+"��"+textAfterRoutes.get(i);
		result+="\n";
		for(int i=0;i<routes.size();i++)
		{
			result+="����"+(i+1)+" = "+routes.get(i).toString();
			if(i!=routes.size()-1) result+="\n";
		}
		return result;
	}
	public String toString(Object arg) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException
	{
		String result="";result+=beginning;
		Vector<String> actuals=new Vector<String>();
		for(Route route:routes)
			actuals.add(route.get(arg).toString());
		for(int i=0;i<routes.size();i++)
		{
			result+=actuals.get(i)+textAfterRoutes.get(i);
		}
		return result;
	}
    public boolean equals(StringProcessor other)
    {
    	if(argType!=other.argType) return false;
    	if(beginning.equals(other.beginning)==false) return false;
    	if(routes.size()!=other.routes.size()) return false;
    	for(int i=0;i<routes.size();i++)
    	{
    		if(routes.get(i).equals(other.routes.get(i))==false) return false;
    		if(textAfterRoutes.get(i).equals(other.textAfterRoutes.get(i))==false) return false;
    	}
    	return true;
    }
}
