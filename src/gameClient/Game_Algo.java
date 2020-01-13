package gameClient;

import java.util.Collection;

import dataStructure.edge_data;

public class Game_Algo extends Thread{

	GameArena arena;
	boolean auto;
	
	@Override
	public void run() {
		super.run();
	}
	
	
	public Game_Algo(int num) {
		arena = new GameArena(num);
		//start();
	}
	
	public int nextNode(int robot_id, int next_node) {
		Robot r = arena.getRobots().get(robot_id);
		Collection<edge_data> robot_edges = arena.getGraph().getE(r.getSrc());
		for (edge_data edge : robot_edges) {
			if(edge.getDest() == next_node) {
				return next_node;
			}
		}	
		return -1;
	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
