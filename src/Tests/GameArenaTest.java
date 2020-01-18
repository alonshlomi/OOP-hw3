package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataStructure.*;
import gameClient.*;

class GameArenaTest {

	static GameArena game;
	static int scenario;
	
	@BeforeAll
	static void init() {
		scenario = 2;
		game = new GameArena(scenario);
		
		
	}
	
	@Test
	void testGetGame() {
		fail("Not yet implemented");
	}

	@Test
	void testInitGetFruits() {
		fail("Not yet implemented");
	}

	@Test
	void testInitGetRobots() {
		fail("Not yet implemented");
	}

	@Test
	void testMaxX() {
		fail("Not yet implemented");
	}

	@Test
	void testMinX() {
		fail("Not yet implemented");
	}

	@Test
	void testMaxY() {
		fail("Not yet implemented");
	}

	@Test
	void testMinY() {
		fail("Not yet implemented");
	}

	@Test
	void testNumOfRobots() {
		fail("Not yet implemented");
	}

	@Test
	void testGetRobotFromCoordinates() {
		fail("Not yet implemented");
	}

	@Test
	void testGetGraph() {
		fail("Not yet implemented");
	}

}
