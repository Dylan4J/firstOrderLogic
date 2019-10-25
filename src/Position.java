public class Position {
	public Integer x;
	public Integer y;
	public Position(){}
	public Position(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public String toString()
	{
		return "("+x+","+y+")";
	}
}
