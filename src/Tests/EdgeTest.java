package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.Edge;
import dataStructure.Node;

class EdgeTest {

	static Node n0, n1, n2;
	static Edge e0, e1, e2;

	@BeforeAll
	static void init() {
		n0 = new Node(0);
		n1 = new Node(1);
		n2 = new Node(2);

		e0 = new Edge(n0, n1, 10);
		e1 = new Edge(n0, n2, 20);
		e2 = new Edge(n1, n2, 30);
	}

	@Test
	void testEdge() {
		assertEquals(n0.getKey(), e0.getSrc());
		assertEquals(n1.getKey(), e0.getDest());
		assertEquals(10, e0.getWeight());

		assertEquals(n0.getKey(), e1.getSrc());
		assertEquals(n2.getKey(), e1.getDest());
		assertEquals(20, e1.getWeight());

		assertEquals(n1.getKey(), e2.getSrc());
		assertEquals(n2.getKey(), e2.getDest());
		assertEquals(30, e2.getWeight());
	}

	@Test
	void testSetGetInfo() {
		String e0_i = "e0 Info";
		String e1_i = "e1 Info";
		String e2_i = "e2 Info";

		e0.setInfo(e0_i);
		e1.setInfo(e1_i);
		e2.setInfo(e2_i);

		assertEquals(e0_i, e0.getInfo());
		assertEquals(e1_i, e1.getInfo());
		assertEquals(e2_i, e2.getInfo());
	}

	@Test
	void testSetGetTag() {
		int e0_t = 0;
		int e1_t = 1;
		int e2_t = 2;

		e0.setTag(e0_t);
		e1.setTag(e1_t);
		e2.setTag(e2_t);

		assertEquals(e0_t, e0.getTag());
		assertEquals(e1_t, e1.getTag());
		assertEquals(e2_t, e2.getTag());
	}

}
