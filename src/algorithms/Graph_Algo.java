package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

/**
 * This empty class represents the set of graph-theory algorithms which should
 * be implemented as part of Ex2 - Do edit this class.
 * 
 * @author
 *
 */
public class Graph_Algo implements graph_algorithms {

	private graph g;
	
	//Constructors:
	public Graph_Algo() {
	}
	
	public Graph_Algo(graph g) {
		init(g);
	}
	
	//Methods:
	@Override
	public void init(graph g) {
		this.g = (g != null) ? g : new DGraph();
	}
	
	@Override
	public void init(String file_name) {
		/*
		 * Yael's code for deserialize:
		 */
		g = null;

		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);

			g = (graph) in.readObject();

			in.close();
			file.close();

		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
			ex.printStackTrace();
		}

		catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
			ex.printStackTrace();
		}
	}

	@Override
	public void save(String file_name) {
		/*
		 * Yael's code for serialize:
		 */
		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(g);

			out.close();
			file.close();

		} catch (IOException ex) {
			System.out.println("IOException is caught");
			ex.printStackTrace();
		}

	}

	@Override
	public boolean isConnected() {
		/*
		 * Check if the graph is strong connected by finding path between every 2 nodes.
		 */
		for (node_data node_src : g.getV()) {
			for (node_data node_dest : g.getV()) {

				if (shortestPathDist(node_src.getKey(), node_dest.getKey()) == Double.POSITIVE_INFINITY)
					return false;
				if (shortestPathDist(node_dest.getKey(), node_src.getKey()) == Double.POSITIVE_INFINITY)
					return false;

			}
		}

		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		/*
		 * Return's the weight that will cost in the shortest path between src and dest.
		 * In this function we implemented Dijkstra's algorithm.
		 */
		PriorityQueue<node_data> Q = new PriorityQueue<node_data>(Node._Comp);
		Collection<node_data> nodes = g.getV();
		node_data d = g.getNode(dest);

		for (node_data node : nodes) {
			node.setWeight(Double.POSITIVE_INFINITY);
			if (node.getKey() == src)
				node.setWeight(0);
			node.setTag(-1);
			Q.add(node);
		}

		if (d == null)
			return Double.POSITIVE_INFINITY;

		while (d.getTag() != 1) {
			node_data u = Q.poll();
			Collection<edge_data> u_edges = g.getE(u.getKey());

			if (u_edges != null) {
				for (edge_data edge : u_edges) {
					node_data v = g.getNode(edge.getDest());
					if (v.getTag() != 1) {
						double t = u.getWeight() + edge.getWeight();
						if (v.getWeight() > t) {
							v.setWeight(t);
							v.setInfo(u.getKey() + "");
							PriorityQueue<node_data> tmp = new PriorityQueue<node_data>(Node._Comp);
							while (!Q.isEmpty())
								tmp.add(Q.poll());
							Q = tmp;
						}
					}
				}
			}
			u.setTag(1);
		}
		return d.getWeight();
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		/*
		 * Return's list of nodes of the shortest path between src and dest.
		 * In this function we were helped by shortestPathDist function.
		 */
		if (g.getNode(src) == null || g.getNode(dest) == null)
			return null;
		LinkedList<node_data> path = new LinkedList<node_data>();
		shortestPathDist(src, dest);
		node_data curr_dest = g.getNode(dest);
		while (curr_dest.getKey() != src) {
			if (curr_dest.getInfo() == null)
				return null;
			int prev = Integer.parseInt(curr_dest.getInfo());
			node_data prev_n = g.getNode(prev);
			path.add(prev_n);
			curr_dest = prev_n;
		}

		path.add(g.getNode(dest));
		path.sort(Node._Comp);
		return path;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		/*
		 * Return's a list which represents a path that going over all nodes in targets.
		 */
		ArrayList<node_data> ans = new ArrayList<node_data>();
		if(targets.size() == 1) return null;
		for (int i = 1; i < targets.size(); i++) {
			int src = targets.get(i - 1);
			int dest = targets.get(i);
			Collection<node_data> path = shortestPath(src, dest);
			if (path == null)
				return null;
			if (ans.size() > 0)
				ans.remove(ans.size() - 1);
			ans.addAll(path);
		}
		return ans;
	}

	@Override
	public graph copy() {
		/*
		 * Return's a graph object with deep copy of our graph.
		 * In this function we deep-copied the Nodes and Edges.
		 */
		graph copy = new DGraph();
		if (g != null) {
			Collection<node_data> nodes = g.getV();

			for (node_data node : nodes)
				copy.addNode(new Node(node));

			for (node_data node : nodes) {
				Collection<edge_data> edges = g.getE(node.getKey());
				if (edges != null) {
					for (edge_data edge : edges) {
						copy.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
					}
				}
			}
		}
		return copy;
	}

}
