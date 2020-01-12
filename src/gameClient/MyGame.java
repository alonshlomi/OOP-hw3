package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import utils.Point3D;

public class MyGame {
	
	private game_service game;
	private graph game_graph;
	private ArrayList<Fruit> fruits;
	private ArrayList<Robot> robots;
	private boolean auto_game;
	private int mc;
	
	
	private void initFruits() {
		fruits.clear();
		for (String f_string : game.getFruits()) {

			Fruit fruit = new Fruit(f_string);
			this.fitFruitToEdge(fruit);
			fruits.add(fruit);

		}
	}
	
	public void initRobots() {
		robots.clear();
		for (String r_string : game.getRobots()) {
			Robot robot = new Robot(r_string);
			robots.add(robot);
		}
	}

	public MyGame(int num, boolean auto) {
		game = Game_Server.getServer(num);
		game_graph = new DGraph(game.getGraph());
		fruits = new ArrayList<Fruit>();
		robots = new ArrayList<Robot>();
		auto_game = auto;
		mc = 0;

	}
	
	public void initAutoGame() {
		initFruits();
		
		if (auto_game) {
			setAutoRobots();
			initRobots();
		}
	}
	
	public boolean fitFruitToEdge(Fruit fruit) {
		Point3D fruit_location = fruit.getLocation();

		for (node_data src : game_graph.getV()) {
			int node_id = src.getKey();

			for (edge_data edge : game_graph.getE(node_id)) {
				node_data dest = game_graph.getNode(edge.getDest());
				Point3D src_location = src.getLocation();
				Point3D dest_location = dest.getLocation();

				double to_fruit = src_location.distance3D(fruit_location);
				double from_fruit = fruit_location.distance3D(dest_location);
				double edge_dist = src_location.distance3D(dest_location);
				double total = edge_dist - (to_fruit + from_fruit);

				if (Math.abs(total) <= Point3D.EPS2) {
					// System.out.println("type: "+fruit.getType()+", src: "+edge.getSrc()+",dest:
					// "+edge.getDest());
					if (fruit.getType() > 0 && edge.getSrc() < edge.getDest())
						continue;
					fruit.setEdge(edge);
					return true;
				}
			}
		}

		return false;
	}

	public graph getGraph() {
		return game_graph;
	}
	
	public List<Fruit> getFruits() {
		return fruits;
	}
	
	public  List<Robot> getRobots() {
		return robots;
	}

	public int getMC() {
		return mc;
	}
	
	private int RobotsNum() {
		String game_s = game.toString();
		int robots_num = 0;
		try {
			JSONObject game_obj = new JSONObject(game_s).getJSONObject("GameServer");
			robots_num = game_obj.getInt("robots");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return robots_num;
	}

	private void setAutoRobots() {
		fruits.sort(Fruit._Comp);

		int robots_num = RobotsNum();
		
		for(int i = 0 ; i< robots_num ; i++) {
			int src_node = fruits.get(i).getEdge().getSrc();
			game.addRobot(src_node);
		}
		
	}
	
	public game_service getGame() {
		return game;
	}
	
	public void startAutoGame() {
		initAutoGame();
		game.startGame();
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (game.isRunning()) {
					moveRobots(game, game_graph);
					mc++;
				}
				String results = game.toString();
				System.out.println("Game Over: " + results);				
			}
		});
		// should be a Thread!!!
		t.start();
	}
	
	
	/**
	 * Moves each of the robots along the edge, in case the robot is on a node the
	 * next destination (next edge) is chosen (randomly).
	 * 
	 * @param game
	 * @param gg
	 * @param log
	 */
	private static void moveRobots(game_service game, graph gg) {
		List<String> log = game.move();
		if (log != null) {
			long t = game.timeToEnd();
			for (int i = 0; i < log.size(); i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
				//	System.out.println(robot_json);
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if (dest == -1) {
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
						System.out.println(ttt);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 * 
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
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
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		

		
		
	}
}
