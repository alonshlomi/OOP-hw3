package gameClient;

import java.util.Hashtable;
import java.util.List;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.node_data;

/**
 * This class is the auto-play thread implements a basic algorithim for collection fruits
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 *
 */
public class AutoGame extends Thread {

	private GameArena arena;	// arena object
	private Graph_Algo g_algo;	// graph_algo for shortest path method
	private Hashtable<Integer, List<node_data>> paths;	// auxiliary hashtable

	/**
	 * Constructor initiate the arena to be played in.
	 * @param arena
	 */
	public AutoGame(GameArena arena) {
		this.arena = arena;
		this.g_algo = new Graph_Algo(arena.getGraph());
		this.paths = new Hashtable<Integer, List<node_data>>();
	}
	
	/**
	 * Thread that moving the robots while game is running.
	 */
	@Override
	public void run() {
		try {
			game_service g = arena.getGame();
			Thread.sleep(10);
			initPaths();	// initiate first path
			while (g.timeToEnd() >= 50) {
				for (int i = 0; i < arena.numOfRobots(); i++) {	// take all robots
					Robot robot = arena.getRobots().get(i);
					if (robot != null && robot.getDest() == -1) {
						int next = nextNode(i);	// choose next node algorithm
						g.chooseNextEdge(i, next); // server call
					}
				}
			}
		} catch (Exception e) {}
	}

	// Initiate the first path to the robots:
	private void initPaths() {
		for (int i = 0; i < arena.numOfRobots(); i++) {
			Robot robot = arena.getRobots().get(i);
			Fruit fruit = arena.getFruits().get(i);
			List<node_data> path = g_algo.shortestPath(robot.getSrc(), fruit.getEdge().getSrc());
			paths.put(robot.getID(), path);
		}
	}

	/**
	 * Algorithm sending the robot to the highest value fruit.
	 * @param robot_id which going to be send
	 * @return next node
	 */
	public int nextNode(int robot_id) {
		if (!arena.getGame().isRunning())
			return -1;
		List<node_data> robot_path = paths.get(robot_id);
		Robot robot = arena.getRobots().get(robot_id);
		// Re-initiate the robot path to go:
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

		// Moving forword in path:
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
