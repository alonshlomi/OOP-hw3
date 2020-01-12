package gameClient;

import dataStructure.edge_data;
import org.json.JSONObject;
import utils.Point3D;

public class Fruit {
	
	private Point3D original_pos;
	private Point3D _pos;
	private double _value;
	private int _type;
	private edge_data _edge;
	
	public static final Fruit_Comparator _Comp = new Fruit_Comparator();
	
	public Fruit() {
	}

	public Fruit(String jsonSTR) {
		this();
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

	public int getType() {
		return _type;
	}

	public Point3D getLocation() {
		return new Point3D(this._pos);
	}

	public String toString() {
		return this.toJSON();
	}

	public String toJSON() {

		String ans = "{\"Fruit\":{\"value\":" + this._value + "," + "\"type\":" + this._type + "," + "\"pos\":\""
				+ this._pos.toString() + "\"" + "}"  + "}";
		return ans;
	}

	public double getValue() {
		return this._value;
	}

	public void setLocation(Point3D p) {
		this.original_pos = this._pos;
		this._pos = (p != null) ? new Point3D(p) : null;
	}
	
	public void setEdge(edge_data e) {
		_edge = e;
	}
	
	public edge_data getEdge() {
		return _edge;
	}
}