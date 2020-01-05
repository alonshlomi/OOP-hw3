package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DGraph implements graph,Serializable {

	private int num_of_nodes, num_of_edges,mode_count;
	private LinkedHashMap<Integer, node_data> nodes;
	private LinkedHashMap<Integer, LinkedHashMap<Integer, edge_data>> edges;
	
	//Constructors:
	public DGraph() {
		/*
		 * Initialize an empty graph.
		 */
		nodes = new LinkedHashMap<Integer, node_data>();
		edges = new LinkedHashMap<Integer, LinkedHashMap<Integer, edge_data>>();
		num_of_edges = 0;
		num_of_nodes = 0;
		mode_count = 0;
	}
	
	public DGraph(int num) {
		/*
		 * Initialize a graph with given num of nodes.
		 * The keys are 0,1,...,num-1.
		 */
		this();
		for (int i = 0; i < num; i++) {
			node_data new_node = new Node(i);
			addNode(new_node);
		}
	}
	
	//Methods:
	@Override
	public node_data getNode(int key) {
		return nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(edges.get(src) == null) return null;
		return edges.get(src).get(dest);
	}

	@Override
	public void addNode(node_data n) {
		/*
		 * Add node to this graph.
		 * throw exceptions if null or already exists.
		 */
		if(n==null) throw new RuntimeException("Cannot add null Node!");
		
		if(nodes.containsKey(n.getKey())) throw new RuntimeException("Node "+n.getKey()+" is already exists!");
		
		nodes.put(n.getKey(), n);
		num_of_nodes++;
		mode_count++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		node_data s = nodes.get(src);
		node_data d = nodes.get(dest);

		if (s == null || d == null)
			throw new RuntimeException("Cannot connect the nodes " + src + " and " + dest);

		edge_data edge = new Edge(s, d, w);
		LinkedHashMap<Integer, edge_data> tmp_edge;
		if (edges.get(src) == null) {
			tmp_edge = new LinkedHashMap<Integer, edge_data>();
		} else {
			tmp_edge = edges.get(src);
		}
		if(tmp_edge.get(dest) != null) {
			num_of_edges--;
		}
		tmp_edge.put(dest, edge);
		edges.put(src, tmp_edge);
		num_of_edges++;
		mode_count++;
	}

	@Override
	public Collection<node_data> getV() {
		Collection<node_data> vertex = nodes.values();
		return vertex;
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(edges.get(node_id) == null) return null;
		return edges.get(node_id).values();
	}

	@Override
	public node_data removeNode(int key) {
		//Remove node's edges:
		if (edges.get(key) != null)
			num_of_edges-=edges.get(key).size();
		edges.remove(key);

		//Search for all edges connected to the node:
		Collection<LinkedHashMap<Integer, edge_data>> tmp = edges.values();
		Iterator<LinkedHashMap<Integer, edge_data>> it = tmp.iterator();
		
		while (it.hasNext()) {
			LinkedHashMap<Integer, edge_data> curr = it.next();
			if (curr.get(key) != null)
				num_of_edges--;
			curr.remove(key);
		}
		
		//Remove node:
		if (nodes.get(key) != null)
			num_of_nodes--;
		mode_count++;
		return nodes.remove(key);
	}

	@Override
	public edge_data removeEdge(int src, int dest) { 
		if(getNode(src) == null || getNode(dest) == null) return null;
		if(src == dest) return null;
		if (edges.get(src) != null && edges.get(src).get(dest) != null)
			num_of_edges--;
		mode_count++;
		if(edges.get(src) == null) return null;
		return edges.get(src).remove(dest);
	}

	@Override
	public int nodeSize() {
		return num_of_nodes;
	}

	@Override
	public int edgeSize() {
		return num_of_edges;
	}

	@Override
	public int getMC() {
		return mode_count;
	}

}
