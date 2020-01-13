package gameClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Server.*;
import Server.game_service;
import dataStructure.*;
import utils.Point3D;

public class MyGameGUI extends JFrame implements MouseListener {

	private static final int WIDTH = 1200;
	private static final int HEIGHT = 800;

	private GameArena game;
	private boolean auto;


	public MyGameGUI(GameArena arena, boolean b) {
		this.game = arena;
		this.auto = b;
		initGUI();
	}

	private void initGUI() {
		this.setTitle("Graph GUI");
		this.setBounds(200, 0, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
		this.addMouseListener(this);

	}

	public void setTime(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Time: " + game.getGame().timeToEnd() / 1000,50,70);
	}

	public void paint(Graphics g) {
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setBackground(new Color(240, 240, 240));
		g2d.clearRect(0, 0, WIDTH, HEIGHT);

		setTime(g2d);
		paintFruits(g2d);
		paintRobots(g2d);
		paintGraph(g2d);
		Graphics2D g2dComponent = (Graphics2D) g;
		g2dComponent.drawImage(bufferedImage, null, 0, 0);
	}

	private void paintGraph(Graphics2D g) {
		double minX = game.minX();
		double minY = game.minY();
		double maxX = game.maxX();
		double maxY = game.maxY();
		for (node_data node : game.getGraph().getV()) {

			int node_x = (int) scale(node.getLocation().x(), minX, maxX, 50, WIDTH - 50);
			int node_y = (int) scale(node.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

			g.setColor(Color.BLUE);
			g.fillOval(node_x - 5, node_y - 5, 10, 10);

			if (game.getGraph().getE(node.getKey()) != null) {
				for (edge_data edge : game.getGraph().getE(node.getKey())) {

					node_data src = game.getGraph().getNode(edge.getSrc());
					node_data dest = game.getGraph().getNode(edge.getDest());

					int src_x = (int) scale(src.getLocation().x(), minX, maxX, 50, WIDTH - 50);
					int src_y = (int) scale(src.getLocation().y(), minY, maxY, 200, HEIGHT - 200);
					int dest_x = (int) scale(dest.getLocation().x(), minX, maxX, 50, WIDTH - 50);
					int dest_y = (int) scale(dest.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

					g.setColor(Color.RED);
					g.drawLine(src_x, src_y, dest_x, dest_y);

					g.setColor(Color.YELLOW);
					int dir_x = (((((((src_x + dest_x) / 2) + dest_x) / 2) + dest_x) / 2));
					int dir_y = (((((((src_y + dest_y) / 2) + dest_y) / 2) + dest_y) / 2));

					g.drawOval(dir_x - 5, dir_y - 5, 10, 10);

				}
			}
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(node.getKey() + "", node_x + 5, node_y + 5);


		}
	}

	private void paintFruits(Graphics2D g) {
		synchronized (game.getFruits()) {

			double minX = game.minX();
			double minY = game.minY();
			double maxX = game.maxX();
			double maxY = game.maxY();
//			Iterator<Fruit> it = game.getFruits().iterator();
//			while (it.hasNext()) {
//				Fruit fruit = it.next();
			for (Fruit fruit : game.getFruits()) {
				g.setColor(Color.ORANGE);
				if (fruit.getType() == 1) {
					g.setColor(Color.GREEN);
				}
				int fruit_x = (int) scale(fruit.getLocation().x(), minX, maxX, 50, WIDTH - 50);
				int fruit_y = (int) scale(fruit.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

				g.fillOval(fruit_x - 7, fruit_y - 7, 15, 15);
				g.setColor(Color.BLACK);
				g.drawString(fruit.getValue() + "", fruit_x + 10, fruit_y + 10);
				// }
			}
		}
	}

	private void paintRobots(Graphics2D g) {
			double minX = game.minX();
			double minY = game.minY();
			double maxX = game.maxX();
			double maxY = game.maxY();
			
			List<String> rob = game.getGame().getRobots();
	        for (int i = 0; i < rob.size(); i++) {
	            g.drawString(rob.get(i), WIDTH/5, 70 + (20 * i));
	        }
			synchronized (game.getRobots()) {

//			Iterator<Robot> it = game.getRobots().iterator();
//			while (it.hasNext()) {
//				Robot robot = it.next();
			for (Robot robot : game.getRobots()) {
				int robot_x = (int) scale(robot.getLocation().x(), minX, maxX, 50, WIDTH - 50);
				int robot_y = (int) scale(robot.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

				g.setColor(Color.GRAY);
				g.drawOval(robot_x - 15, robot_y - 15, 30, 30);
				g.setFont(new Font("Arial", Font.BOLD, 15));
				g.drawString(robot.getID() + "", robot_x - 5, robot_y + 5);
				// }
			}
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

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("aaa");
		ArrayList<Robot> tmp = (ArrayList<Robot>) game.getRobots().clone();

		synchronized (tmp) {

			// Iterator<Robot> it = game.getRobots().iterator();

			// while (it.hasNext()) {

			for (Robot robot : tmp) {

				// Robot robot = it.next();
				if (robot.getDest() == -1) {
					String dest_str = JOptionPane.showInputDialog("Enter next node for robot: " + robot.getID());
					int dest = -1;
					try {
					dest = Integer.parseInt(dest_str);
					}catch (Exception e1) {
						JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					game.getGame().chooseNextEdge(robot.getID(), dest);
				}
			}
			// }
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
