package gameClient;

import org.json.JSONObject;
import utils.Point3D;

/**
 * This class represents a robot in 'The Maze Of Waze' game, which holds data
 * coming from the game server.
 * 
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 *
 */
public class Robot {

	private int _id;
	private double _speed;
	private int _src, _dest;
	private Point3D _pos;

	public Robot() {
	}

	/**
	 * Initiate a robot from json.
	 * 
	 * @param json String
	 */
	public Robot(String json) {
		try {
			JSONObject robot = new JSONObject(json).getJSONObject("Robot");
			int id = robot.getInt("id");
			this._id = id;
			int src = robot.getInt("src");
			this._src = src;
			int dest = robot.getInt("dest");
			this._dest = dest;
			double speed = robot.getDouble("speed");
			this._speed = speed;
			String pos = robot.getString("pos");
			this._pos = new Point3D(pos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the robot id.
	 * 
	 * @return robot id.
	 */
	public int getID() {
		return _id;
	}

	/**
	 * Returns the position of the robot on the graph.
	 * 
	 * @return robot's position.
	 */
	public Point3D getLocation() {
		return new Point3D(this._pos);
	}

	/**
	 * Returns the source node of the robot.
	 * 
	 * @return robot's source node.
	 */
	public int getSrc() {
		return _src;
	}

	/**
	 * Returns the destination node of the robot. -1 if not destination given.
	 * 
	 * @return robot's destination node.
	 */
	public int getDest() {
		return _dest;
	}

	/**
	 * Returns the current speed of the robot.
	 * 
	 * @return robot's speed.
	 */
	public double getSpeed() {
		return _speed;
	}

	// toString:
	public String toString() {
		return "id:" + _id + ", src: " + _src + ", dest: " + _dest + ",pos: " + _pos;
	}

	// equals by ID:
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Robot))
			return false;
		Robot other = (Robot) obj;
		return this.getID() == other.getID();
	}

}
