package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;

import Server.*;
import Server.game_service;
import dataStructure.*;
import utils.Point3D;

public class MyGameGUI extends JFrame {

	private static final int WIDTH = 1200;
	private static final int HEIGHT = 800;

	private MyGame game;
	private int game_mc;
	
	public MyGameGUI(int num) {
		game = new MyGame(num,true);
		game_mc = game.getMC();	
		game.startAutoGame();
		initGUI();
		
	}

	private void initGUI() {
		this.setTitle("Graph GUI");
		this.setBounds(200, 0, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(game_mc != game.getMC()) {
						game_mc = game.getMC();
						repaint();
					}
				}
				
			}
		});
		
		t.start();
		
		this.setVisible(true);

	}

	public void paint(Graphics g) {
		super.paint(g);
		scaleLocations(HEIGHT, WIDTH);
		paintGraph(g);

		paintFruits(g);

		paintRobots(g);
	}

	private void paintGraph(Graphics g) {
		for (node_data node : game.getGraph().getV()) {

			g.setColor(Color.BLUE);
			g.fillOval(node.getLocation().ix() - 5, node.getLocation().iy() - 5, 10, 10);

			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(node.getKey() + "", node.getLocation().ix() + 5, node.getLocation().iy() + 5);

			if (game.getGraph().getE(node.getKey()) != null) {
				for (edge_data edge : game.getGraph().getE(node.getKey())) {

					node_data src = game.getGraph().getNode(edge.getSrc());
					node_data dest = game.getGraph().getNode(edge.getDest());

					g.setColor(Color.RED);

					g.drawLine(src.getLocation().ix(), src.getLocation().iy(), dest.getLocation().ix(),
							dest.getLocation().iy());

					int mid_x = (src.getLocation().ix() + dest.getLocation().ix()) / 2;
					int mid_y = (src.getLocation().iy() + dest.getLocation().iy()) / 2;
					g.setFont(new Font("Arial", Font.BOLD, 12));
					// g.drawString(edge.getWeight() + "", mid_x, mid_y);

					g.setColor(Color.YELLOW);

					int src_x = src.getLocation().ix(), src_y = src.getLocation().iy();
					int dest_x = dest.getLocation().ix(), dest_y = dest.getLocation().iy();

					int dir_x = (((((((src_x + dest_x) / 2) + dest_x) / 2) + dest_x) / 2) + dest_x) / 2;
					int dir_y = (((((((src_y + dest_y) / 2) + dest_y) / 2) + dest_y) / 2) + dest_y) / 2;

					g.fillOval(dir_x - 5, dir_y - 5, 10, 10);

				}
			}

		}
	}

	private void paintFruits(Graphics g) {
		for (Fruit fruit : game.getFruits()) {
			g.setColor(Color.ORANGE);
			if (fruit.getType() == 1) {
				g.setColor(Color.GREEN);
			}
			g.fillOval(fruit.getLocation().ix() - 7, fruit.getLocation().iy() - 7, 15, 15);
			g.setColor(Color.BLACK);
			g.drawString(fruit.getValue() + "", fruit.getLocation().ix() + 10, fruit.getLocation().iy() + 10);
		}
	}

	private void paintRobots(Graphics g) {
		for (Robot robot : game.getRobots()) {
			g.setColor(Color.GRAY);
			g.drawOval(robot.getLocation().ix() - 15, robot.getLocation().iy() - 15, 30, 30);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(robot.getID()+"", robot.getLocation().ix()-5, robot.getLocation().iy()+5);
		}
	}
	
	// Scale function:
	public void scaleLocations(int height, int width) {
		double max_x = Double.MIN_VALUE;
		double min_x = Double.MAX_VALUE;
		double max_y = Double.MIN_VALUE;
		double min_y = Double.MAX_VALUE;

		for (node_data node : game.getGraph().getV()) {
			max_x = Math.max(max_x, node.getLocation().x());
			max_y = Math.max(max_y, node.getLocation().y());
			min_x = Math.min(min_x, node.getLocation().x());
			min_y = Math.min(min_y, node.getLocation().y());
		}

		// Scale nodes locations
		for (node_data node : game.getGraph().getV()) {
			double new_x = scale(node.getLocation().x(), min_x, max_x, 50, width - 50);
			double new_y = scale(node.getLocation().y(), min_y, max_y, 70, height - 70);
			node.setLocation(new Point3D(new_x, new_y));
		}

		// Scale fruits locations
		for (Fruit fruit : game.getFruits()) {
			double new_x = scale(fruit.getLocation().x(), min_x, max_x, 50, width - 50);
			double new_y = scale(fruit.getLocation().y(), min_y, max_y, 70, height - 70);
			fruit.setLocation(new Point3D(new_x, new_y));
		}
		
		//Scale Robots location
		for (Robot robot : game.getRobots()) {
			double new_x = scale(robot.getLocation().x(), min_x, max_x, 50, width - 50);
			double new_y = scale(robot.getLocation().y(), min_y, max_y, 70, height - 70);
			robot.setLocation(new Point3D(new_x, new_y));
		}
		
	}

	/**
	 * 
	 * @param data  denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {

		double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
		return res;
	}
	//

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyGameGUI mg = new MyGameGUI(2);
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		
		
	}

}
