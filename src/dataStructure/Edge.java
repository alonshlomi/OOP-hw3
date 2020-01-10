package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data, Serializable {

	private node_data src;
	private node_data dest;
	private double weight;
	private int tag;
	private String info;
	
	//Constructors:
	public Edge() {}
	
	public Edge(node_data src, node_data dest, double weight) {
		if (weight < 0)
			throw new RuntimeException("Weight need to be a positive number!");
		this.src = src;
		this.dest = dest;
		this.weight = weight;
	}

	//Getters, Setters and toString:
	@Override
	public int getSrc() {
		return src.getKey();
	}

	@Override
	public int getDest() {
		return dest.getKey();
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public String getInfo() {
		return new String(info);
	}

	@Override
	public void setInfo(String s) {
		info = new String(s);
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		tag = t;
	}

	public String toString() {
		return "[" + src.getKey() + "->" + dest.getKey() + " (" + weight + ")]";
	}

	//JSON. Boaz's code:
	public String toJSON() {
		String ans = "";
		ans = ans + "{src:" + this.getSrc() + ",dest:" + this.getDest() + ",weight:" + this.getWeight() + "}";
		return ans;
	}
	//
}
