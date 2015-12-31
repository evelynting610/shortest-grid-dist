import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class PathAlgos {
	
	private GraphSquare[][] vMatrix;
	private AdjacencyListGraph<String> graph;
	private ArrayList<String> corners;
	private AdjacencyListGraph<String> cornerEdges;
	private GraphSquare fromSquare;
	private GraphSquare toSquare;
	HashMap<String, Integer> dist;
	HashMap<String, Node<String>> pred;

	public void initializeGraphs(int width, int height, int numVInSquare) {
		ArrayList<String> vertices = new ArrayList<String>();
		corners = new ArrayList<String>();
		vMatrix = new GraphSquare[height][width];
		for(int row = 0; row<height; row++){
			for(int col =0; col<width; col++){
				ArrayList<String> vInSquare = new ArrayList<String>();
				ArrayList<String> gInSquare = new ArrayList<String>();
				for (int k =0; k<numVInSquare; k++){
					String vName = "v"+row+"."+col+"."+k;
					vertices.add(vName);
					vInSquare.add(vName);
					}
				String [] cornerNames = {"g"+row+"."+col, "g"+row+"."+(col+1), "g"+(row+1)+"."+col, "g"+(row+1)+"."+(col+1)};
				for(int i=0;i<4;i++){
					gInSquare.add(cornerNames[i]);
					vInSquare.add(cornerNames[i]);
				}
				vMatrix[row][col] = new GraphSquare(gInSquare, vInSquare);
			}
		}
		for(int row = 0; row<=height; row++){
			for(int col =0; col<=width; col++){
				String gName = "g"+row+"."+col;
				corners.add(gName);
				vertices.add(gName);
			}
		}
		cornerEdges = new AdjacencyListGraph<String>(corners);
		graph  = new AdjacencyListGraph<String>(vertices);
		
		
	}

	public void addGraphEdge(String from, String to, int weight) {
		graph.addEdge(from, to, weight, new LinkedList<String>());	
	}
	
	public void findCornerPaths(int height, int width){
		for(int row = 0; row<height; row++){
			for(int col =0; col<width; col++){
				GraphSquare square =vMatrix[row][col];
				cornerEdges=square.djikstraOnCorners(graph, cornerEdges);
			}
		}
	}

	public SimpleNode[][] cornerToCorner(){
		ArrayList<String> fromCorners = fromSquare.getCorners();
		ArrayList<String> toCorners = toSquare.getCorners();
		SimpleNode[][] queryCornerGraph = new SimpleNode[4][4];
		for(int i=0; i<4; i++){
			dist = new HashMap<String, Integer>();
			pred = new HashMap<String, Node<String>>();
			String u = fromCorners.get(i);
			djikstra(cornerEdges, u);
			for(int c=0; c<4; c++){
				String v = toCorners.get(c);
				LinkedList<String> path = new LinkedList<String>();
				int weight = dist.get(v);
				String curr =v;
				while (pred.get(curr)!=null){
					Node<String> predNode = pred.get(curr);
					LinkedList<String> nodePath = predNode.path;
					Iterator<String> iter = nodePath.descendingIterator();
					if(iter.hasNext()) iter.next();
					while(iter.hasNext())
						path.addFirst(iter.next());
					String prev = predNode.name;
					curr =prev;
				}
				SimpleNode node = new SimpleNode(weight, path);
				queryCornerGraph[i][c] = node;
			}
		}
		return queryCornerGraph;
	}

	public String threePartAlgo(String vFrom, String vTo){
		if(fromSquare==toSquare){
			String output = fromSquare.djikstraOnSquare(graph, vFrom, vTo);
			return output;
		}else{
			int minDist = Integer.MAX_VALUE;
			int fromCorner= 0; int toCorner= 0;
			HashMap<String, SimpleNode> fromVertMap = fromSquare.djikstraOnVertices(graph, vFrom);
			HashMap<String, SimpleNode> toVertMap = toSquare.djikstraOnVertices(graph, vTo);
			SimpleNode[][] queryCorners = cornerToCorner();
			ArrayList<String> fromCorners = fromSquare.getCorners();
			ArrayList<String> toCorners = toSquare.getCorners();

			for(int r=0; r<4; r++){
				String u = fromCorners.get(r);
				int fromVertDist = fromVertMap.get(u).weight;
				for(int c=0; c<4; c++){
					String v = toCorners.get(c);
					int cornerToCornerDist = queryCorners[r][c].weight;
					int toVertDist = toVertMap.get(v).weight;
					int dist = fromVertDist+cornerToCornerDist+toVertDist;
					if(dist<minDist){
						minDist = dist;
						fromCorner=r;
						toCorner=c;
					}
				}
			}
			HashSet<String> vertsInPath= new HashSet<String>();
			String u = fromCorners.get(fromCorner);
			String v = toCorners.get(toCorner);
			ArrayList<String> finalPath = new ArrayList<String>();
			LinkedList<String> fromVertPath = fromVertMap.get(u).path;
			LinkedList<String> cToCPath = queryCorners[fromCorner][toCorner].path;
			LinkedList<String> toVertPath = toVertMap.get(v).path;
			Iterator<String> iter = toVertPath.descendingIterator();

			for(int i=0; i<fromVertPath.size()-1; i++){
				finalPath.add(fromVertPath.get(i));
			}
			
			for(String vert: cToCPath){
				finalPath.add(vert);
			}

			while (iter.hasNext()){
				String vert = iter.next();
				finalPath.add(vert);
			}
			String output = minDist+" "+finalPath;
			//Test.isValidPath(graph, finalPath, minDist);
			return output;
	}

	}

	private void djikstra(AdjacencyListGraph<String> graph, String s) {	
		for(String corner: corners){
			dist.put(corner, Integer.MAX_VALUE);
			pred.put(corner, null);
		}
		dist.put(s, 0);
		PriorityQueue<String, Integer> q = new PriorityQueue<String, Integer>();
		for (String c: corners){
			q.addItem(c, dist.get(c));
		}
		
		while(q.getSize()!=0){
			String u = q.removeItem();
			for(Node<String> node : graph.neighbors(u)){
				String v=node.name;
				if(q.contains(v)){
					int altRoute = dist.get(u)+node.weight;
					if(altRoute<dist.get(v) && altRoute>=0){
						dist.put(v, altRoute);
						Node<String> predNode = new Node<String>(u, altRoute, node.path);
						pred.put(v, predNode);
						q.decreasePriority(v, altRoute);
					}
				}
			}
		}
		
	}

	public void setFromSquare(String vert){
		String[] parts = vert.split("\\.");
		String r = parts[0].substring(1);
		int row = Integer.parseInt(r);
		int col = Integer.parseInt(parts[1]);
		fromSquare = vMatrix[row][col];
	}

	public void setToSquare(String vert){
		String[] parts = vert.split("\\.");
		String r = parts[0].substring(1);
		int row = Integer.parseInt(r);
		int col = Integer.parseInt(parts[1]);
		toSquare = vMatrix[row][col];
	}

}
