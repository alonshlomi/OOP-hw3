package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.node_data;

class Graph_AlgoTest {
	
	static DGraph g;
	static Graph_Algo algo;
	
	@BeforeAll
	static void init() {
		g = new DGraph(3);
		g.connect(0, 1, 10);
		g.connect(0, 2, 20);
		g.connect(1, 0, 30);
		g.connect(1, 2, 40);
		g.connect(2, 0, 50);
		g.connect(2, 1, 60);
		
		algo = new Graph_Algo();
		algo.init(g);
	}

	@Test
	void testInitSaveToFile() {
		algo.save("AlgoTest.txt");
		
		Graph_Algo copy = new Graph_Algo();
		copy.init("AlgoTest.txt");
		
		graph copy_g = copy.copy();
		
		assertEquals(g.nodeSize(), copy_g.nodeSize());
		
	}

	@Test
	void testIsConnected() {
		assertTrue(algo.isConnected());
		
		g.removeEdge(0, 2);
		g.removeEdge(1, 2);
		g.removeEdge(2, 1);

		assertFalse(algo.isConnected());
	}

	@Test
	void testShortestPathDist() {

		assertEquals(10, algo.shortestPathDist(0, 1));
		assertEquals(30, algo.shortestPathDist(1, 0));
		assertEquals(50, algo.shortestPathDist(2, 0));

	}

	@Test
	void testShortestPath() {
		g.removeEdge(0, 2);
		List<node_data> actual = algo.shortestPath(0, 2);
		ArrayList<Integer> expected = new ArrayList<>();
		expected.add(0);
		expected.add(1);
		expected.add(2);
		g.connect(0, 2, 20);
		for(int i=0;i<expected.size();i++) {
			int ex = expected.get(i);
			int ac = actual.get(i).getKey();
			assertEquals(ex, ac);
		}
	}

	@Test
	void testTSP() {
		g.removeEdge(0, 2);
		ArrayList<Integer> targets = new ArrayList<>();
		targets.add(0);
		targets.add(2);

		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(0);
		expected.add(1);
		expected.add(2);
		
		List<node_data> actual = algo.TSP(targets);

		for(int i=0;i<expected.size();i++) {
			int ex = expected.get(i);
			int ac = actual.get(i).getKey();
			assertEquals(ex, ac);
		}
		
	}

	@Test
	void testCopy() {
		graph copy = algo.copy();
		
		for (node_data node : g.getV()) {
			assertEquals(node, copy.getNode(node.getKey()));
		}
	}

}
