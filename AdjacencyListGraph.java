import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

/**
 * An undirected, unweighted, simple graph.  The vertex set is static; edges
 * may be added but not removed.
 *
 * @param K the type of the vertices
 * @author Jim Glenn
 * @version 0.1 2015-10-27
 */

public class AdjacencyListGraph<K>
{
    /**
     * This graph's vertex set.
     */
    private Set<K> verts;

    /**
     * This graph's adjacency lists.
     */
    private Map<K, List<Node<K>>> adjLists;

    /**
     * Creates a graph with the given vertex set and no edges.
     *
     * @param v a collection of vertices
     */
    public AdjacencyListGraph(Collection<K> v)
	{
	    // make ourselves a private copy of the vertex set
	    verts = new HashSet<K>(v);

	    // set up empty adkacency lists for each vertex
	    adjLists = new HashMap<K, List<Node<K>>>();
	    for (K src : verts)
		{
		    adjLists.put(src, new ArrayList<Node<K>>());
		}
	}

    public void addCornerEdge(K u, K v, int weight, LinkedList<K> path)
    {
    if (u.equals(v))
        {
        throw new IllegalArgumentException("adding self loop");
        }

    Node<K> node = new Node<K>(v, weight, path);
    LinkedList<K> pathBackwards = new LinkedList<K>();
    Iterator<K> iter = path.descendingIterator();
    while(iter.hasNext()){
        pathBackwards.add(iter.next());
    }
    node.setPathBackwards(pathBackwards);


    // get u's adjacency list
    List<Node<K>> adj = adjLists.get(u);

    for(Node<K> oldNode : adj){
        if(oldNode.name.equals(v)){
            if(weight<oldNode.weight){
                oldNode=node;
                Node<K> node2 = new Node<K>(u, weight, node.pathBackwards);
                adjLists.get(v).add(node2); 
            }else{
                Node<K> node2 = new Node<K>(u, oldNode.weight, oldNode.pathBackwards);
            }
        }
    }
    adj.add(node);
    }

    /**
     * Adds the given edge to this graph if it does not already exist.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     */
    public void addEdge(K u, K v, int weight, LinkedList<K> path)
    {
	if (u.equals(v))
	    {
		throw new IllegalArgumentException("adding self loop");
	    }

	// get u's adjacency list
	List<Node<K>> adj = adjLists.get(u);

    /*boolean hasEdge=false;
    for(Node<K> n : adj){
        if(n.name.equals(v)){
            hasEdge =true;
        }
    }*/


	Node<K> node = new Node<K>(v, weight, path);

    LinkedList<K> pathBackwards = new LinkedList<K>();
    Iterator<K> iter = path.descendingIterator();
    while(iter.hasNext()){
        pathBackwards.add(iter.next());
    }
    Node<K> node2 = new Node<K>(u, weight, pathBackwards);


	adj.add(node);
    adjLists.get(v).add(node2); 
    }

    /**
     * Determines if the given edge is present in this graph.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     * @return true if and only if the edge (u, v) is in this graph
     */
    public boolean hasEdge(K u, K v)
	{
	       // get u's adjacency list
    List<Node<K>> adj = adjLists.get(u);

    // check for edge already being there
    for(Node<K> n : adj){
        if(n.name.equals(v)){
            return true;
        }
	}
    return false;
    }

    /**
     * Returns an iterator over the vertices in this graph.
     *
     * @return an iterator over the vertices in this graph
     */
    public Iterator<K> iterator()
    {
	return (new HashSet<K>(verts)).iterator();
    }

    /**
     * Returns an iterator over the neighbors of the vertices in this graph.
     *
     * @param v a vertex in this graph
     * @return an iterator over the vertices in this graph
     */
    public Iterable<Node<K>> neighbors(K v)
    {
    return new ArrayList<Node<K>>(adjLists.get(v));
    }

    /**
     * Returns a printable representation of this graph.
     *
     * @return a printable representation of this graph
     */
    public String toString()
    {
	return adjLists.toString();
    }
}