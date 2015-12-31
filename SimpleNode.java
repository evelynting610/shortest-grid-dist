import java.util.LinkedList;

public class SimpleNode {

	public int weight;
	public LinkedList<String> path;

	public SimpleNode(int w, LinkedList<String> p){
		weight=w;
		path=p;
	}

	public String toString(){
		String s= path.toString();
		return s+"  ";
	}
	
}