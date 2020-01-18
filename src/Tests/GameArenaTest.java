package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
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
		game_service expected = Game_Server.getServer(scenario);
		assertEquals(expected.toString(), game.getGame().toString());
	}

	@Test
	void testInitGetFruits() {
		game_service expected = Game_Server.getServer(scenario);
		for (String fruit_str : expected.getFruits()) {
			Fruit fruit = new Fruit(fruit_str);
			assertTrue(game.getFruits().contains(fruit));
		}
	}

	@Test
	void testInitGetRobots() {
		game_service expected = Game_Server.getServer(scenario);
		expected.addRobot(0);
		assertEquals(expected.getRobots().size(),game.getRobots().size());
	}

	@Test
	void testNumOfRobots() {
		game_service expected = Game_Server.getServer(scenario);
		expected.addRobot(0);
		int actual = game.numOfRobots();
		assertEquals(expected.getRobots().size(), actual);
	}

	@Test
	void testGetGraph() {
		game_service expected = Game_Server.getServer(scenario);
		graph expected_g = new DGraph(expected.getGraph());
		assertEquals(expected_g.edgeSize(), game.getGraph().edgeSize());
		assertEquals(expected_g.nodeSize(), game.getGraph().nodeSize());
	}

}
