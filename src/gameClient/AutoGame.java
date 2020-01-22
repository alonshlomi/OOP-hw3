package gameClient;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.node_data;

import java.util.Hashtable;
import java.util.List;

/**
 * This class represents the 'Auto player which choose next node to go for each
 * robot, using algorithm that finds the closest fruit to that robot.
 * 
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 *
 */
public class AutoGame {

	private GameArena arena; // the 'game board' to play on
	private Graph_Algo algo_g; // for shortest path alogrithms
	private Hashtable<Integer, List<node_data>> robots_paths; // holds the robots paths
	private Hashtable<Integer, Fruit> fruits_status; // indicates whether the fruits are targeted or not

	/**
	 * Constructor that initiate the arena to the 'auto player'.
	 * 
	 * @param arena
	 */
	public AutoGame(GameArena arena) {
		this.arena = arena;
		robots_paths = new Hashtable<>();
		fruits_status = new Hashtable<Integer, Fruit>();
		algo_g = new Graph_Algo(arena.getGraph());
		initRobotPath();
	}

	/**
	 * Methods that choose the next node for each robot.
	 * 
	 * @param game - server object
	 */
	public void moveRobots(game_service game) {
		int dest = -1;
		for (int i = 0; i < arena.numOfRobots(); i++) {
			Robot robot = arena.getRobots().get(i);
			if (robot.getDest() == -1) {
				dest = nextNode(i);
				Fruit fruit = fruits_status.get(robot.getID());
				if (robot.getSpeed() >= 2) {
					if (robot.getSrc() == fruit.getEdge().getSrc() && dest == fruit.getEdge().getDest()) {
						
						// Calculate the dt for eating the fruit for sure:
						node_data src = arena.getGraph().getNode(fruit.getEdge().getSrc());
						node_data dst = arena.getGraph().getNode(fruit.getEdge().getDest());
						double dist_src_fruit = src.getLocation().distance2D(fruit.getLocation());
						double dist_src_dst = src.getLocation().distance2D(dst.getLocation());
						double n = dist_src_fruit / dist_src_dst;
						double w = fruit.getEdge().getWeight();
						double s = robot.getSpeed();

						int dt = (int) (w * n / s * 100);
						ClientThread.dt = dt;
						//
					}
				}
				game.chooseNextEdge(i, dest);

			}
		}
	}

	// Move to the next node in rid path if there is one,
	// Otherwise, choose the next path:
	private int nextNode(int rid) {
		Robot robot = arena.getRobots().get(rid);
		List<node_data> tmp = robots_paths.get(rid);
		
		if (tmp.isEmpty()) {
			fruits_status.remove(rid);
			synchronized (arena.getFruits()) {
				if (arena.getFruits().size() > 0) {
					Fruit fruit = findClosestFruit(robot);
					tmp = algo_g.shortestPath(robot.getSrc(), fruit.getEdge().getSrc());
					node_data dest = arena.getGraph().getNode(fruit.getEdge().getDest());
					tmp.add(dest);
					robots_paths.put(robot.getID(), tmp);
					fruits_status.put(rid, fruit);
				}
			}
		}

		node_data n = tmp.get(0);
		tmp.remove(0);
		return n.getKey();
	}

	// Finds the closest fruit to given robot:
	private Fruit findClosestFruit(Robot robot) {
		double min_dist = Double.MAX_VALUE;
		Fruit ans = null;
		for (int i = 0; i < arena.getFruits().size(); i++) {
			Fruit fruit = arena.getFruits().get(i);
			if (fruits_status.containsValue(fruit))
				continue;

			double dist = algo_g.shortestPathDist(robot.getSrc(), fruit.getEdge().getSrc());
			if (dist < min_dist) {
				min_dist = dist;
				ans = fruit;
			}
		}
		return ans;
	}

	// Initiate the first path of the robots:
	public void initRobotPath() {
		for (int i = 0; i < arena.numOfRobots(); i++) {
			Robot robot = arena.getRobots().get(i);
			Fruit fruit = arena.getFruits().get(i);
			List<node_data> tmp = algo_g.shortestPath(robot.getSrc(), fruit.getEdge().getDest());
			robots_paths.put(robot.getID(), tmp);
			fruits_status.put(robot.getID(), fruit);
		}
	}
}