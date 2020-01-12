package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		game = new MyGame(num, true);
		initGUI();
	}

	private void initGUI() {
		this.setTitle("Graph GUI");
		this.setBounds(200, 0, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("Move");
		MenuItem item = new MenuItem("Move");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				game.moveRobots();
				repaint();
			}
		});
		
		menu.add(item);
		
		menubar.add(menu);
		this.setMenuBar(menubar);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
				}
			}
		});
		t.start();
		
		this.setVisible(true);

	}

	public void paint(Graphics g) {
		super.paint(g);
		paintGraph(g);

		paintFruits(g);

		paintRobots(g);
	}

	private void paintGraph(Graphics g) {
		double minX = game.minX();
		double minY = game.minY();
		double maxX = game.maxX();
		double maxY = game.maxY();
		for (node_data node : game.getGraph().getV()) {
			
			int node_x = (int) scale(node.getLocation().x(), minX, maxX, 50, WIDTH-50);
			int node_y = (int) scale(node.getLocation().y(), minY, maxY, 70, HEIGHT-70);
			
			g.setColor(Color.BLUE);
			g.fillOval(node_x - 5, node_y - 5, 10, 10);

			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(node.getKey() + "", node_x + 5, node_y + 5);

			if (game.getGraph().getE(node.getKey()) != null) {
				for (edge_data edge : game.getGraph().getE(node.getKey())) {

					node_data src = game.getGraph().getNode(edge.getSrc());
					node_data dest = game.getGraph().getNode(edge.getDest());

					int src_x = (int) scale(src.getLocation().x(), minX, maxX, 50, WIDTH-50);
					int src_y = (int) scale(src.getLocation().y(), minY, maxY, 70, HEIGHT-70);
					int dest_x = (int) scale(dest.getLocation().x(), minX, maxX, 50, WIDTH-50);
					int dest_y = (int) scale(dest.getLocation().y(), minY, maxY, 70, HEIGHT-70);			
					
					g.setColor(Color.RED);
					g.drawLine(src_x, src_y, dest_x, dest_y);

					g.setColor(Color.YELLOW);
					int dir_x = (((((((src_x + dest_x) / 2) + dest_x) / 2) + dest_x) / 2) + dest_x) / 2;
					int dir_y = (((((((src_y + dest_y) / 2) + dest_y) / 2) + dest_y) / 2) + dest_y) / 2;

					g.fillOval(dir_x - 5, dir_y - 5, 10, 10);

				}
			}

		}
	}

	private void paintFruits(Graphics g) {
		double minX = game.minX();
		double minY = game.minY();
		double maxX = game.maxX();
		double maxY = game.maxY();
		for (Fruit fruit : game.getFruits()) {
			g.setColor(Color.ORANGE);
			if (fruit.getType() == 1) {
				g.setColor(Color.GREEN);
			}
			int fruit_x = (int) scale(fruit.getLocation().x(), minX, maxX, 50, WIDTH-50);
			int fruit_y = (int) scale(fruit.getLocation().y(), minY, maxY, 70, HEIGHT-70);
			
			g.fillOval(fruit_x - 7, fruit_y - 7, 15, 15);
			g.setColor(Color.BLACK);
			g.drawString(fruit.getValue() + "", fruit_x + 10, fruit_y + 10);
		}
	}

	private void paintRobots(Graphics g) {
		double minX = game.minX();
		double minY = game.minY();
		double maxX = game.maxX();
		double maxY = game.maxY();
		for (Robot robot : game.getRobots()) {
			int robot_x = (int) scale(robot.getLocation().x(), minX, maxX, 50, WIDTH-50);
			int robot_y = (int) scale(robot.getLocation().y(), minY, maxY, 70, HEIGHT-70);
			
			
			g.setColor(Color.GRAY);
			g.drawOval(robot_x - 15, robot_y - 15, 30, 30);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(robot.getID() + "", robot_x - 5, robot_y + 5);
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

		// Scale Robots location
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
