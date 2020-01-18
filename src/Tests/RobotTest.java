package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gameClient.*;
import utils.Point3D;

class RobotTest {

	static Robot r1, r2;
	static int r1_id, r2_id;
	static double r1_value, r2_value;
	static int r1_src, r2_src;
	static int r1_dest, r2_dest;
	static double r1_speed, r2_speed;
	static Point3D r1_pos, r2_pos;

	@BeforeAll
	static void init() {
		r1_id = 0;
		r1_value = Math.random() * 500;
		r1_src = (int) (Math.random() * 3);
		r1_dest = (int) (Math.random() * 3);
		r1_speed = Math.random() * 10;
		double r1_x = Math.random() * 1000;
		double r1_y = Math.random() * 1000;
		r1_pos = new Point3D(r1_x, r1_y, 0.0);
		String r1_json = robotToJSON(r1_id, r1_value, r1_src, r1_dest, r1_speed, r1_pos);

		r2_id = 1;
		r2_value = Math.random() * 500;
		r2_src = (int) (Math.random() * 3 + 3);
		r2_dest = (int) (Math.random() * 3 + 3);
		r2_speed = Math.random() * 10;
		double r2_x = Math.random() * 1000;
		double r2_y = Math.random() * 1000;
		r2_pos = new Point3D(r2_x, r2_y, 0.0);
		String r2_json = robotToJSON(r2_id, r2_value, r2_src, r2_dest, r2_speed, r2_pos);

		r1 = new Robot(r1_json);
		r2 = new Robot(r2_json);
	}

	private static String robotToJSON(int id, double value, int src, int dest, double speed, Point3D pos) {
		String s = "{\"Robot\":{\"id\":" + id + ",\"value\":" + value + ",\"src\":" + src + ",\"dest\":" + dest
				+ ",\"speed\":" + speed + ",\"pos\":\"" + pos.toString() + "\"}}";
		return s;
	}

	@Test
	void testGetID() {
		assertEquals(r1_id, r1.getID());
		assertEquals(r2_id, r2.getID());
		assertNotEquals(r1.getID(), r2.getID());
	}

	@Test
	void testGetLocation() {
		assertEquals(r1_pos, r1.getLocation());
		assertEquals(r2_pos, r2.getLocation());
		assertNotEquals(r1.getLocation(), r2.getLocation());
	}

	@Test
	void testGetSrc() {
		assertEquals(r1_src, r1.getSrc());
		assertEquals(r2_src, r2.getSrc());
		assertNotEquals(r1.getSrc(), r2.getSrc());
	}

	@Test
	void testGetDest() {
		assertEquals(r1_dest, r1.getDest());
		assertEquals(r2_dest, r2.getDest());
		assertNotEquals(r1.getDest(), r2.getDest());
	}

	@Test
	void testGetSpeed() {
		assertEquals(r1_speed, r1.getSpeed());
		assertEquals(r2_speed, r2.getSpeed());
		assertNotEquals(r1.getSpeed(), r2.getSpeed());	}

}
