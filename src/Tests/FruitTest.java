package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import gameClient.*;
import utils.Point3D;

class FruitTest {

	static Fruit f1, f2;
	static Point3D f1_pos, f2_pos;
	static double f1_value, f2_value;

	@BeforeAll
	static void init() {
		double f1_x = Math.random() * 1000;
		double f1_y = Math.random() * 1000;
		f1_pos = new Point3D(f1_x, f1_y, 0.0);

		f1_value = (Math.random() * 5);
		int f1_type = 1;
		String f1_json = fruitToJSON(f1_value, f1_type, f1_pos);

		double f2_x = Math.random() * 1000;
		double f2_y = Math.random() * 1000;
		f2_pos = new Point3D(f2_x, f2_y, 0.0);

		f2_value = (Math.random() * 10 + 5);
		int f2_type = -1;
		String f2_json = fruitToJSON(f2_value, f2_type, f2_pos);

		
		f1 = new Fruit(f1_json);
		f2 = new Fruit(f2_json);

	}

	static String fruitToJSON(double value, int type, Point3D pos) {
		String s = "{\"Fruit\":{\"value\":" + value + "," + "\"type\":" + type + "," + "\"pos\":\"" + pos.toString()
				+ "\"" + "}" + "}";
		return s;
	}

	@Test
	void testGetType() {
		assertNotEquals(f1.getType(), f2.getType());
		assertEquals(1, f1.getType());
		assertEquals(-1, f2.getType());
	}

	@Test
	void testGetLocation() {
		assertEquals(f1_pos, f1.getLocation());
		assertEquals(f2_pos, f2.getLocation());
		assertNotEquals(f1.getLocation(), f2.getLocation());
	}

	@Test
	void testGetValue() {
		assertEquals(f1_value, f1.getValue());
		assertEquals(f2_value, f2.getValue());
		assertNotEquals(f1.getValue(), f2.getValue());
	}

	@Test
	void testSetGetEdge() {
		node_data n1 = new Node((int) f1_value);
		node_data n2 = new Node((int) f2_value);
		edge_data f1_edge = new Edge(n1, n2, f1.getLocation().x());
		edge_data f2_edge = new Edge(n2, n1, f2.getLocation().y());

		f1.setEdge(f1_edge);
		f2.setEdge(f2_edge);

		assertNotEquals(f1_edge, f2_edge);
		assertEquals(f1_edge, f1.getEdge());
		assertEquals(f2_edge, f2.getEdge());
	}

}
