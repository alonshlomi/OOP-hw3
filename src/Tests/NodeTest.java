package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.Node;
import utils.Point3D;

class NodeTest {
	
	static Node n0,n1,n2;
	
	@BeforeAll
	static void init() {
		n0 = new Node(0);
		n1 = new Node(1);
		n2 = new Node(2);
	}
	
	@Test
	void testNodeNode_data() {
		n0.setInfo("Info");
		n0.setLocation(new Point3D(1, 2, 3));
		n0.setTag(1);
		n0.setWeight(10);
		
		Node copy = new Node(n0);
		
		assertEquals(n0.getKey(), copy.getKey());
		assertEquals(n0.getInfo(), copy.getInfo());
		assertEquals(n0.getLocation(), copy.getLocation());
		assertEquals(n0.getTag(), copy.getTag());
		assertEquals(n0.getWeight(), copy.getWeight());
		
		n0.setInfo("Other Info");
		n0.setLocation(new Point3D(3, 2, 1));
		n0.setTag(0);
		n0.setWeight(20);
		
		assertEquals(n0.getKey(), copy.getKey());
		assertNotEquals(n0.getInfo(), copy.getInfo());
		assertNotEquals(n0.getLocation(), copy.getLocation());
		assertNotEquals(n0.getTag(), copy.getTag());
		assertNotEquals(n0.getWeight(), copy.getWeight());
		
	}

	@Test
	void testEqualsObject() {
		Node n = n0;
		Node copy = new Node(n);
		Node other_n = new Node(4);
		
		assertTrue(n.equals(copy));
		assertFalse(n.equals(other_n));
		assertFalse(copy.equals(other_n));
	
	}

	@Test
	void testNode() {
		Node n = new Node(5);
		for(int i = n.getKey()+1; i<=50;i++) {
			Node node = new Node(i);
			assertEquals(i, node.getKey());
		}
	}
	
	@Test
	void testGetKey() {
		assertEquals(0, n0.getKey());
		assertEquals(1, n1.getKey());
		assertEquals(2, n2.getKey());
		
	}
	
	@Test
	void testSetGetLocation() {
		Point3D n0_l = new Point3D(1, 2, 3);
		Point3D n1_l = new Point3D(4, 5, 6);
		
		n0.setLocation(n0_l);
		n1.setLocation(n1_l);
		n2.setLocation(n0_l);
		
		assertEquals(n0_l, n0.getLocation());
		assertEquals(n1_l, n1.getLocation());
		assertEquals(n0.getLocation(), n2.getLocation());

	}
	
	@Test
	void testSetGetInfo() {		
		String n0_i = "n0 Info";
		String n1_i = "n1 Info";
		
		n0.setInfo(n0_i);
		n1.setInfo(n1_i);
		n2.setInfo(n0_i);
		
		assertEquals(n0_i, n0.getInfo());
		assertEquals(n1_i, n1.getInfo());
		assertEquals(n0.getInfo(), n2.getInfo());
		
	}
	
	@Test
	void testSetGetTag() {
		int n0_t = 0;
		int n1_t = 1;
		
		n0.setTag(n0_t);
		n1.setTag(n1_t);
		n2.setTag(n0_t);
		
		assertEquals(n0_t, n0.getTag());
		assertEquals(n1_t, n1.getTag());
		assertEquals(n0.getTag(), n2.getTag());
	}

	@Test
	void testSetGetWeight() {
		double n0_w = 10;
		double n1_w= 20;
		
		n0.setWeight(n0_w);
		n1.setWeight(n1_w);
		n2.setWeight(n0_w);
		
		assertEquals(n0_w, n0.getWeight());
		assertEquals(n1_w, n1.getWeight());
		assertEquals(n0.getWeight(), n2.getWeight());
	}
}
