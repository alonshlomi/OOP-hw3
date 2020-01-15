package dataStructure;

import java.io.Serializable;

import utils.Point3D;

public class Node implements node_data, Serializable {

	private int key, tag;
	private Point3D location;
	private double weight;
	private String info;
	
	public static final Node_Comparator _Comp = new Node_Comparator(); //Compare by weight.
	
	public Node() {}
	
	public Node(int key) {
		this.key = key;
		setWeight(Double.POSITIVE_INFINITY);
		setTag(-1);
		setInfo(null);
	}
	
	//Copy Constructor:
	public Node(node_data other) {
		key = other.getKey();
		weight = other.getWeight();
		location = (other.getLocation() == null) ? null : new Point3D(other.getLocation());
		info = (other.getInfo() == null) ? null : new String(other.getInfo());
		tag = other.getTag();
	}
	
	//Boaz's code:
	public Node(int key, Point3D p) {
		this.key = key;
		setWeight(Double.POSITIVE_INFINITY);
		setTag(-1);
		setInfo(null);
		setLocation(p);
	}
	//

	//Methods, Getters and Setters:
	@Override
	public boolean equals(Object o) {
		/*
		 * Return's true if and only if the keys are equal
		 */
		if (o instanceof node_data) {
			node_data other = (node_data) o;
			return this.key == other.getKey();
		}
		return false;
	}

	public String toString() {
		String ans = "[" + key;
//		ans+= "("+info+")";
//		ans+= "{"+tag+"}";
//		ans += ", "+location;
		ans += "]";
		return ans;
	}

	@Override
	public int getKey() {
		return key;
	}

	@Override
	public Point3D getLocation() {
		return location;
	}

	@Override
	public void setLocation(Point3D p) {
		location = new Point3D(p);
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public void setWeight(double w) {
		weight = w;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String s) {
		info = (s == null) ? null : new String(s);
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		tag = t;
	}

	//JSON. Boaz's code:
	public String toJSON() {
		String ans = "";
		ans = ans + "{id:" + this.getKey() + ",info:" + this.getInfo() + "}";
		return ans;
	}
	//
}
