import java.util.LinkedList;

public class Node <K>{
	
	public K name;
	int weight;
	LinkedList<K> path;
	public LinkedList<K> pathBackwards;

	public Node(K e, int w, LinkedList<K> p){
		name=e;
		weight=w;
		path=p;
	}

	public void setPathBackwards(LinkedList<K> p){
		pathBackwards=p;
	}

	public String toString(){
		String s= name+": "+weight;
		return s;
	}
	
}
