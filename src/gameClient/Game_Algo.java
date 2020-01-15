package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Server.game_service;
import dataStructure.edge_data;
import oop_dataStructure.oop_edge_data;

public class Game_Algo extends Thread {

	private GameArena arena;
	private boolean auto_game;

	@Override
	public void run() {
		game_service g = arena.getGame();
		System.out.println("aaa");

		while (g.isRunning()) {
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			ArrayList<Robot> tmp = (ArrayList<Robot>) arena.getRobots().clone();
//			synchronized (tmp) {

				for (int i = 0 ;i < arena.numOfRobots();i++) {
					Robot robot = arena.getRobots().get(i);
					if (robot != null && robot.getDest() == -1) {
						int next = nextNode(robot.getSrc());
						g.chooseNextEdge(robot.getID(), next);
					}
				}
//			}
		}
	}

	public Game_Algo(GameArena arena) {
		this.arena = arena;
		// start();
	}

	public int nextNode(int src) {
//		Robot r = arena.getRobots().get(robot_id);
//		Collection<edge_data> robot_edges = arena.getGraph().getE(r.getSrc());
//		for (edge_data edge : robot_edges) {
//			if(edge.getDest() == next_node) {
//				return next_node;
//			}
//		}	
//		return -1;

		int ans = -1;
		Collection<edge_data> ee = arena.getGraph().getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int) (Math.random() * s);
		int i = 0;
		while (i < r) {
			itr.next();
			i++;
		}
		ans = itr.next().getDest();
		return ans;
	}

}
