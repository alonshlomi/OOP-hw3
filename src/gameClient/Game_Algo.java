package gameClient;

import java.util.Hashtable;
import java.util.List;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.node_data;

public class Game_Algo extends Thread {

	private GameArena arena;
	private Graph_Algo g_algo;
	private Hashtable<Integer, List<node_data>> paths;

	@Override
	public void run() {
		game_service g = arena.getGame();
		initPaths();
		while (g.isRunning()) {

			for (int i = 0; i < arena.numOfRobots(); i++) {
				Robot robot = arena.getRobots().get(i);
				if (robot != null && robot.getDest() == -1) {

					int next = nextNode(i);
					g.chooseNextEdge(i, next);
				}
			}
			
		}
	}

	private void initPaths() {
		for (int i = 0; i < arena.numOfRobots(); i++) {
			Robot robot = arena.getRobots().get(i);
			Fruit fruit = arena.getFruits().get(i);
			List<node_data> path = g_algo.shortestPath(robot.getSrc(), fruit.getEdge().getDest());
			paths.put(robot.getID(), path);
		}
	}

	public Game_Algo(GameArena arena) {
		this.arena = arena;
		this.g_algo = new Graph_Algo(arena.getGraph());
		this.paths = new Hashtable<Integer, List<node_data>>();
	}

	public int nextNode(int robot_id) {
		if (!arena.getGame().isRunning())
			return -1;
		List<node_data> robot_path = paths.get(robot_id);
		Robot robot = arena.getRobots().get(robot_id);
		if (robot_path.isEmpty()) {
			synchronized (arena.getFruits()) {
				if (arena.getFruits().size() > 0) {
					Fruit fruit = arena.getFruits().get(robot_id);
					robot_path = g_algo.shortestPath(robot.getSrc(), fruit.getEdge().getSrc());
					node_data des = arena.getGraph().getNode(fruit.getEdge().getDest());
					robot_path.add(des);
					paths.put(robot.getID(), robot_path);
				}
			}
		}

		for (int i = 0; i < robot_path.size(); i++) {
			node_data curr = robot_path.get(i);
			robot_path.remove(i);
			if (curr.getKey() == robot.getSrc()) {
				continue;
			}
			return curr.getKey();
		}
		return -1;
	}

}
