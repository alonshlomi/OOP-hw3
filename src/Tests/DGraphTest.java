package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;

class DGraphTest {

	static DGraph graph, empty_graph;
	final static int NUM_OF_NODES = 10;
	static int rand_key1, rand_key2, rand_key3;

	@BeforeAll
	static void init() {
		rand_key1 = (int) (Math.random() * NUM_OF_NODES);
		rand_key2 = (int) (Math.random() * NUM_OF_NODES);
		rand_key3 = (int) (Math.random() * NUM_OF_NODES);

		graph = new DGraph(NUM_OF_NODES);
		graph.connect(rand_key1, rand_key2, 50);
		graph.connect(rand_key1, rand_key3, 60);
		graph.connect(rand_key2, rand_key3, 70);

		empty_graph = new DGraph();

	}

	@Test
	void testGetNode() {
		node_data actual_node1 = graph.getNode(rand_key1);
		node_data actual_node2 = graph.getNode(rand_key2);
		node_data actual_node3 = graph.getNode(rand_key3);
		node_data null_node = empty_graph.getNode(rand_key1);

		assertEquals(rand_key1, actual_node1.getKey());
		assertEquals(rand_key2, actual_node2.getKey());
		assertEquals(rand_key3, actual_node3.getKey());
		assertNull(null_node);

	}

	@Test
	void testGetEdge() {
		edge_data e0 = graph.getEdge(rand_key1, rand_key2);
		edge_data e1 = graph.getEdge(rand_key2, rand_key3);

		edge_data null_e = empty_graph.getEdge(rand_key1, rand_key2);

		assertEquals(rand_key1, e0.getSrc());
		assertEquals(rand_key2, e0.getDest());
		assertEquals(rand_key2, e1.getSrc());
		assertEquals(rand_key3, e1.getDest());
		assertNull(null_e);

	}

	@Test
	void testAddNode() {
		Node new_node = new Node(100);
		graph.addNode(new_node);
		node_data get_node = graph.getNode(new_node.getKey());

		assertEquals(new_node, get_node);
	}

	@Test
	void testConnect() {
		node_data src_node = graph.getNode(rand_key3);
		node_data dest_node = graph.getNode(rand_key2);
		double w = 100;

		Edge expected_edge = new Edge(src_node, dest_node, w);
		graph.connect(src_node.getKey(), dest_node.getKey(), w);

		edge_data actual_edge = graph.getEdge(rand_key3, rand_key2);

		assertEquals(expected_edge.getSrc(), actual_edge.getSrc());
		assertEquals(expected_edge.getDest(), actual_edge.getDest());
		assertEquals(expected_edge.getWeight(), actual_edge.getWeight());

	}

	@Test
	void testGetV() {
		Collection<node_data> actual = graph.getV();
		int expected = graph.nodeSize();

		assertEquals(expected, actual.size());
	}

	@Test
	void testGetE() {
		int actual_edges = 0;
		for (node_data node : graph.getV()) {
			Collection<edge_data> edges = graph.getE(node.getKey());
			if (edges != null)
				actual_edges += edges.size();
		}

		assertEquals(graph.edgeSize(), actual_edges);
	}

	@Test
	void testRemoveNode() {
		node_data new_node = new Node(200);
		graph.addNode(new_node);
		int nodes_before = graph.nodeSize();
		
		
		
		graph.removeNode(new_node.getKey());
		
		int nodes_after = graph.nodeSize();
		assertFalse(graph.getV().contains(new_node.getKey()));	
		assertEquals(nodes_before - 1, nodes_after);
		
	}

	@Test
	void testRemoveEdge() {
		node_data new_node0 = new Node(300);
		node_data new_node1 = new Node(400);
		graph.addNode(new_node0);
		graph.addNode(new_node1);
		
		graph.connect(new_node0.getKey(), new_node1.getKey(), 100);
		int edges_before = graph.edgeSize();
		
		graph.removeEdge(new_node0.getKey(), new_node1.getKey());
		int edges_after = graph.edgeSize();

		assertEquals(edges_before - 1, edges_after);
		
	}

	@Test
	void performance() throws InterruptedException {
		int million = 1000000;
		assertTimeout(Duration.ofSeconds(10), () -> {
			DGraph graph = new DGraph(million);
			for (node_data n : graph.getV()) {
				for (int i = 1; i <= 10; i++) {
					try {
						graph.connect(n.getKey(), n.getKey() + i, i * 5);
					} catch (Exception e) {
					}
				}
			}
		});
	}

}
