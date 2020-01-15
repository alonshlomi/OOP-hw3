package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.node_data;
import oop_dataStructure.oop_edge_data;

public class Game_Algo extends Thread {

	private GameArena arena;
	private boolean auto_game;
	private Graph_Algo g_algo;
	private Hashtable<Integer, List<node_data>> paths;

	@Override
	public void run() {
		game_service g = arena.getGame();
		initPaths();
		while (g.isRunning()) {
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			ArrayList<Robot> tmp = (ArrayList<Robot>) arena.getRobots().clone();
//			synchronized (tmp) {

			for (int i = 0; i < arena.numOfRobots(); i++) {
				Robot robot = arena.getRobots().get(i);
				if (robot != null && robot.getDest() == -1) {
					
					int next = nextNode(i);
					g.chooseNextEdge(i, next);
				}
			}
//			}
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

//        node_data n=robot_path.get(0);
//        robot_path.remove(n);
//        return n.getKey();

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
