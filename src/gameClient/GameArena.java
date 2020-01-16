package gameClient;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.*;
import utils.Point3D;

/**
 * This class represents the Game arena which going to be draw in our game. 
 * The arena holds the server, graph, fruits and robots.
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 *
 */
public class GameArena {

	private game_service game; // server object.
	
	private graph game_graph;
	private ArrayList<Fruit> fruits;
	private Hashtable<Integer, Robot> robots;
	private KML_Logger kml;

	private double maxX, minX, maxY, minY; // graph parameters.
	private int num_of_robots; // num of robots.
	
	private static final double EPS = 0.0003; // epsilon
	
	/**
	 * Constructor that initiate the arena with data given by the server.
	 * Initiate KML file, graph parameters and places the robots.
	 * @param scenario number.
	 */
	public GameArena(int scenario) {
		kml = KML_Logger.getInstance(scenario); // initiate KML
		game = Game_Server.getServer(scenario); // initiate the game
		game_graph = new DGraph(game.getGraph());
		addNodesToKML();
		setScaleParameters();
		fruits = new ArrayList<Fruit>();
		robots = new Hashtable<Integer, Robot>();
		initFruits();
		setRobotsPositions();
		initRobots();
	}
	
/*
 * Auxiliary methods: 
 */
	
	// Initiate fruits from json string to Fruit object:
	private void initFruits() {
		synchronized (fruits) { // to avoid sync exceptions
			fruits.clear();
			if (fruits.isEmpty()) {
				for (String fruit_str : game.getFruits()) {
					Fruit fruit = new Fruit(fruit_str);

					String f_type = "apple";
					if (fruit.getType() == -1) {
						f_type = "banana";
						kml.addPlacemark(fruit.getLocation(), f_type); // add placemark to kml
					}
					setEdgeToFruits(fruit);
					fruits.add(fruit);
				}
				fruits.sort(Fruit._Comp);
			}
		}
	}
	
	// Initiate robots from json string to Robot object:

	private void initRobots() {
		for (String robot_str : game.getRobots()) {
			Robot robot = new Robot(robot_str);
			kml.addPlacemark(robot.getLocation(), "robot");
			robots.put(robot.getID(), robot);
		}
	}


	// Fit specific fruit to an edge is sitting on:
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


	// Returns the num of robots in this game:
	private int robotsNum() {
		String game_str = game.toString();
		int robots_num = 0;
		try {
			JSONObject json_game = new JSONObject(game_str).getJSONObject("GameServer");
			robots_num = json_game.getInt("robots");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		num_of_robots = robots_num;
		return robots_num;
	}


	// Initiate starting position, near the highest value fruits: (strategic decision)
	private void setRobotsPositions() {
		int robots_num = robotsNum();

		for (int i = 0; i < robots_num; i++) {
			int src_node = fruits.get(i).getEdge().getSrc();
			game.addRobot(src_node);
		}
	}


	// Initiate min/max X's and Y's for scaling:
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

	// Adding placemarks on nodes positions.
	private void addNodesToKML() {
		for (node_data node : game_graph.getV()) {
			kml.addPlacemark(node.getLocation(), "node");
		}
	}

/*
 * Public methods:
 */
	
	/**
	 * Re-initiate the fruits and robots from the server and updates the lists.
	 */
	public void update() {
		initFruits();
		initRobots();
	}

	
	/**
	 * 
	 * @return game object.
	 */
	public game_service getGame() {
		return game;
	}


	/**
	 * 
	 * @return fruits list.
	 */
	public ArrayList<Fruit> getFruits() {
		return fruits;
	}


	/**
	 * 
	 * @return robots hashtable.
	 */
	public Hashtable<Integer, Robot> getRobots() {
		return robots;
	}


	/**
	 * 
	 * @return maximum X on graph.
	 */
	public double maxX() {
		return maxX;
	}


	/**
	 * 
	 * @return minimum X on graph.
	 */
	public double minX() {
		return minX;
	}


	/**
	 * 
	 * @return maximum Y on graph.
	 */
	public double maxY() {
		return maxY;
	}


	/**
	 * 
	 * @return minimum Y on graph.
	 */
	public double minY() {
		return minY;
	}


	/**
	 * 
	 * @return num of robots.
	 */
	public int numOfRobots() {
		return num_of_robots;
	}


	/**
	 * Returns id of a robot which is approximately places in (x,y) coordinates.
	 * @param x coordinate.
	 * @param y coordinate.
	 * @return robot id.
	 */
	public int getRobotFromCoordinates(double x, double y) {
		if (!game.isRunning())
			return -1;
		Point3D p = new Point3D(x, y);

		for (int i = 0; i < num_of_robots; i++) {
			Robot robot = robots.get(i);
			if (robot.getDest() == -1) {

				double dist = robot.getLocation().distance2D(p);
				if (dist <= EPS) {
					return robot.getID();
				}

			}
		}
		return -1;
	}



	/**
	 * 
	 * @return game graph.
	 */
	public graph getGraph() {
		return game_graph;
	}
}