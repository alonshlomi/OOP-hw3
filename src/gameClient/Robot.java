package gameClient;

import org.json.JSONObject;

import dataStructure.node_data;
import utils.Point3D;

public class Robot {
	
	private int _id;
	private double _speed;
	private int _src,_dest;
	private Point3D _pos;

	public Robot() {
	}
	
	public Robot(String json) {
		this();
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
	
	public int getID() {
		return _id;
	}
	
	public void setLocation(Point3D p) {
		this._pos = new Point3D(p);
	}
	
	public Point3D getLocation() {
		return new Point3D(this._pos);
	}
	
	public int getSrc() {
		return _src;
	}
	
	public int getDest() {
		return _dest;
	}
	public String toString() {
		return "id:"+_id+", src: "+_src+", dest: "+_dest+",pos: "+_pos;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Robot)) return false;
		Robot other = (Robot) obj;
		return this.getID() == other.getID();
	}

	public double getSpeed() {
		return _speed;
	}
}
