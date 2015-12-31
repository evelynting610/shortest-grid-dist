import java.util.Scanner;

public class ShortestPathsMain {
	
	public static void main (String[] args) {
		PathAlgos algos = new PathAlgos();
		Scanner scan = new Scanner(System.in);
		int width = scan.nextInt();
		int height = scan.nextInt();
		int numVInSquare = scan.nextInt();
		algos.initializeGraphs(width, height, numVInSquare);
		
		while(scan.hasNext()&&!scan.hasNext("queries")){
			String from = scan.next();
			String to = scan.next();
			int weight = scan.nextInt();
			algos.addGraphEdge(from, to, weight);
		}
		algos.findCornerPaths(height, width);
		String queries =scan.next();
		/**
		 * Test Cases
		 */
		while(scan.hasNext()){
			String from = scan.next();
			String to = scan.next();
			algos.setFromSquare(from);
			algos.setToSquare(to);
			String output = algos.threePartAlgo(from, to);
			System.out.println(output);

		}
		scan.close();
	}
}