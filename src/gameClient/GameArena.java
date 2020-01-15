package gameClient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import utils.Point3D;
import utils.Range;

public class GameArena {

	private game_service game;
	private graph game_graph;
	private ArrayList<Fruit> fruits;
	private Hashtable<Integer, Robot> robots;

	private double maxX, minX, maxY, minY;
	private int num_of_robots;

	public static final Fruit_Comparator _Comp = new Fruit_Comparator();

	public GameArena(int num) {
		game = Game_Server.getServer(num);
		game_graph = new DGraph(game.getGraph());
		setScaleParameters();
		fruits = new ArrayList<Fruit>();
		robots = new Hashtable<Integer, Robot>();
		initFruits();
		setRobotsPositions();
		initRobots();
	}

	private void initFruits() {
		synchronized (fruits) {

			fruits.clear();
//			Iterator<String> it = game.getFruits().iterator();
//
//			while (it.hasNext()) {
//
//			String fruit_str = it.next();

			for (String fruit_str : game.getFruits()) {

				Fruit fruit = new Fruit(fruit_str);
				setEdgeToFruits(fruit);
				fruits.add(fruit);
				// }

			}
			fruits.sort(_Comp);
		}
	}

	private void initRobots() {
//		synchronized (robots) {
//			robots.clear();

//			Iterator<String> it = game.getRobots().iterator();
//			while (it.hasNext()) {
//				String robot_str = it.next();

		for (String robot_str : game.getRobots()) {
			Robot robot = new Robot(robot_str);
//				robots.add(robot);
			robots.put(robot.getID(), robot);

			// }
		}
//		}
	}

	private void setEdgeToFruits(Fruit fruit) {
		for (node_data node : this.game_graph.getV()) {
			for (edge_data edge : this.game_graph.getE(node.getKey())) {
				node_data dst = this.game_graph.getNode(edge.getDest());
				double d1 = node.getLocation().distance2D(fruit.getLocation());
				double d2 = fruit.getLocation().distance2D(dst.getLocation());
				double dist = node.getLocation().distance2D(dst.getLocation());
				double tmp = dist - (d1 + d2);
				int t;
				if (node.getKey() > dst.getKey()) {
					t = -1;
				} else {
					t = 1;
				}

				if ((Math.abs(tmp) <= Point3D.EPS2) && (fruit.getType() == t)) {
					fruit.setEdge(edge);
					return;
				}
			}
		}
	}

	private int robotsNum() {
		String game_str = game.toString();
		int robots_num = 0;
		try {
			JSONObject json_game = new JSONObject(game_str).getJSONObject("GameServer");
			robots_num = json_game.getInt("robots");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		num_of_robots = robots_num;
		return robots_num;
	}

	private void setRobotsPositions() {
		int robots_num = robotsNum();

		for (int i = 0; i < robots_num; i++) {
			int src_node = fruits.get(i).getEdge().getSrc();
			game.addRobot(src_node);
		}
	}

	public void update() {
		initFruits();
		initRobots();
	}

	public game_service getGame() {
		return game;
	}

	public ArrayList<Fruit> getFruits() {
		return fruits;
	}

	public Hashtable<Integer, Robot> getRobots() {
		return robots;
	}

	public graph getGraph() {
		return game_graph;
	}

	private void setScaleParameters() {
		maxX = Double.MIN_VALUE;
		minX = Double.MAX_VALUE;
		maxY = Double.MIN_VALUE;
		minY = Double.MAX_VALUE;

		for (node_data node : game_graph.getV()) {
			maxX = Math.max(maxX, node.getLocation().x());
			maxY = Math.max(maxY, node.getLocation().y());
			minX = Math.min(minX, node.getLocation().x());
			minY = Math.min(minY, node.getLocation().y());
		}
	}

	public double maxX() {
		return maxX;
	}

	public double minX() {
		return minX;
	}

	public double maxY() {
		return maxY;
	}

	public double minY() {
		return minY;
	}

	public int numOfRobots() {
		return num_of_robots;
	}

	public int getRobotFromCoordinates(double original_x, double original_y) {
		if (!game.isRunning())
			return -1;
		Point3D p = new Point3D(original_x, original_y);
//		ArrayList<Robot> tmp = (ArrayList<Robot>) robots.clone();
//		synchronized (tmp) {

		for (int i = 0; i < num_of_robots; i++) {
			Robot robot = robots.get(i);
			if (robot.getDest() == -1) {

				double dist = robot.getLocation().distance2D(p);
				if (dist <= 0.0003) {
					return robot.getID();
				}

			}
//			}

		}
		return -1;
	}

}
