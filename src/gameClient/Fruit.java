package gameClient;

import dataStructure.edge_data;
import org.json.JSONObject;
import utils.Point3D;

/**
 * This class represents a fruit in 'The Maze Of Waze' game, which holds data
 * coming from the game server.
 * 
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 *
 */
public class Fruit {

	private Point3D _pos;
	private double _value;
	private int _type;
	private edge_data _edge;

	// Comparator by value:
	public static final Fruit_Comparator _Comp = new Fruit_Comparator();

	public Fruit() {
	}

	/**
	 * Initiate a fruit from json.
	 * 
	 * @param json String
	 */
	public Fruit(String jsonSTR) {
		try {
			JSONObject fruit = new JSONObject(jsonSTR).getJSONObject("Fruit");
			double val = fruit.getDouble("value");
			this._value = val;
			int type = fruit.getInt("type");
			this._type = type;
			String pos = fruit.getString("pos");
			this._pos = new Point3D(pos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the type of the fruit, can be 1 or -1.
	 * 
	 * @return fruit's type.
	 */
	public int getType() {
		return _type;
	}

	/**
	 * Returns the position of the fruit on the graph.
	 * 
	 * @return fruit's position.
	 */
	public Point3D getLocation() {
		return new Point3D(this._pos);
	}

	/**
	 * Returns the value of the fruit.
	 * 
	 * @return fruit's value.
	 */
	public double getValue() {
		return this._value;
	}

	/**
	 * Returns the edge which this fruit is sitting on.
	 * 
	 * @return edge
	 */
	public edge_data getEdge() {
		return _edge;
	}

	/**
	 * Set edge to this fruit.
	 * 
	 * @param e - edge.
	 */
	public void setEdge(edge_data e) {
		_edge = e;
	}

	// equals by position:
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Fruit))
			return false;
		Fruit other = (Fruit) obj;
		return this._pos.equals(other.getLocation());
	}

}