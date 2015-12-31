import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

public class GraphSquare {
	
	private ArrayList<String> corners;
	private ArrayList<String> vertices;
	HashMap<String, Integer> dist;
	HashMap<String, String> pred;

	public GraphSquare(ArrayList<String>gInSquare, ArrayList<String> vInSquare) {
		corners = gInSquare;
		vertices = vInSquare;
	}
	public AdjacencyListGraph<String> djikstraOnCorners(AdjacencyListGraph<String> graph, AdjacencyListGraph<String> cornerEdges){
		for(int i=0; i<4; i++){
			dist = new HashMap<String, Integer>();
			pred = new HashMap<String, String>();
			String u = corners.get(i);
			djikstra(graph, u);
			for(int c=0; c<4; c++){
				String v = corners.get(c);
				int weight = dist.get(v);
				if(i!=c){
					LinkedList<String> path = new LinkedList<String>();
					path.add(v);
					String curr =v;
					while (pred.get(curr)!=null){
						String prev = pred.get(curr);
						path.addFirst(prev);
						curr =prev;
					}
					cornerEdges.addCornerEdge(u, v, weight, path);
				}
			}
		}
		//System.out.println(cornerEdges.toString());
		return cornerEdges;
	}

	public HashMap<String, SimpleNode> djikstraOnVertices(AdjacencyListGraph<String> graph, String u){
		HashMap<String, SimpleNode> pathsToCorners = new HashMap<String, SimpleNode>();
		dist = new HashMap<String, Integer>();
		pred = new HashMap<String, String>();
		djikstra(graph, u);

		for(int c=0; c<4; c++){
			String v = corners.get(c);
			LinkedList<String> path = new LinkedList<String>();
			int weight = dist.get(v);
			path.add(v);
			String curr =v;
			while (pred.get(curr)!=null){
				String prev = pred.get(curr);
				path.addFirst(prev);
				curr =prev;
			}
			SimpleNode node = new SimpleNode(weight, path);
			pathsToCorners.put(v, node);
		}
		return pathsToCorners;
	}

	public String djikstraOnSquare(AdjacencyListGraph<String> graph, String u, String v){
		String output = new String();
		dist = new HashMap<String, Integer>();
		pred = new HashMap<String, String>();
		djikstra(graph, u);

		int weight = dist.get(v);
		LinkedList<String> path = new LinkedList<String>();
		path.add(v);
		String curr =v;
		while (pred.get(curr)!=null){
			String prev = pred.get(curr);
			path.addFirst(prev);
			curr =prev;
		}
		output = weight+" "+path.toString();
		return output;
	}
	
	private void djikstra(AdjacencyListGraph<String> graph, String s) {	
		for(String v: vertices){
			dist.put(v, Integer.MAX_VALUE);
			pred.put(v, null);
		}
		dist.put(s, 0);
		PriorityQueue<String, Integer> q = new PriorityQueue<String, Integer>();
		for (String v: vertices){
			q.addItem(v, dist.get(v));
		}
		
		while(q.getSize()!=0){
			String u = q.removeItem();
			for(Node<String> node : graph.neighbors(u)){
				String v=node.name;
				if(q.contains(v)){
					int altRoute = dist.get(u)+node.weight;
					if(altRoute<dist.get(v) && altRoute>=0){
						dist.put(v, altRoute);
						pred.put(v, u);
						q.decreasePriority(v, altRoute);
					}
				}
			}
		}
		/*System.out.println("neighbors of v0.0.3");
		for(Node<String> node : graph.neighbors("v0.0.3")){
			System.out.println(node+", ");
		}*/
		
	}
	public ArrayList<String> getVertices() {
		return vertices;
	}
	
	public ArrayList<String> getCorners() {
		return corners;
	}
}
