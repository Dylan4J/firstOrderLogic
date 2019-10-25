import java.util.Vector;

public class PredicateAndRoutes {
	public Predicate predicate;//һ��ν��
	public Vector<Route> routes=new Vector<Route>();//����·��
	public PredicateAndRoutes(){}
	public PredicateAndRoutes(Predicate p)
	{
		predicate=p;
	}
	public PredicateAndRoutes(Predicate p,Vector<Route> r)
	{
		predicate=p;
		routes=r;
	}
}
