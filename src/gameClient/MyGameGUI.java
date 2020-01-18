package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dataStructure.*;

/**
 * This class is a GUI class which draw the game arena.
 * 
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 * 
 */
public class MyGameGUI extends JFrame implements MouseListener {

	/**
	 * default frame width
	 */
	private static int WIDTH = 1200;
	/**
	 * default frame height
	 */
	private static int HEIGHT = 800;
	private static double arena_maxX, arena_maxY, arena_minX, arena_minY; // game graph parameters for scaling

	private GameArena arena; // arena object
	private boolean auto_game; // variable indicates whether is auto game or not

	/**
	 * Constructor initiate the GUI.
	 * 
	 * @param arena
	 * @param b     - game mode
	 */
	public MyGameGUI(GameArena arena, boolean b) {
		this.arena = arena;
		this.auto_game = b;
		arena_maxX = arena.maxX();
		arena_maxY = arena.maxY();
		arena_minX = arena.minX();
		arena_minY = arena.minY();
		initGUI();
	}

	// Initiate GUI:
	private void initGUI() {
		this.setTitle("The Maze of Waze");
		this.setBounds(200, 0, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		if (!auto_game) { // use mouse-listener if manual mode has been chosen
			this.addMouseListener(this);
		}
	}

	// Draw time to end:
	private void setTime(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Time: " + arena.getGame().timeToEnd() / 1000, 50, 70);
	}

	/**
	 * Painting arena using buffered image to avoid frame flashing:
	 */
	public void paint(Graphics g) {
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setBackground(new Color(240, 240, 240));
		g2d.clearRect(0, 0, WIDTH, HEIGHT);

		paintGraph(g2d);

		setTime(g2d);
		paintFruits(g2d);
		paintRobots(g2d);

		Graphics2D g2dComponent = (Graphics2D) g;
		g2dComponent.drawImage(bufferedImage, null, 0, 0);

	}

	// Paint graph nodes and edges:
	private void paintGraph(Graphics2D g) {
		WIDTH = getWidth();
		HEIGHT = getHeight();
		double minX = arena_minX;
		double minY = arena_minY;
		double maxX = arena_maxX;
		double maxY = arena_maxY;
		for (node_data node : arena.getGraph().getV()) {

			if (arena.getGraph().getE(node.getKey()) != null) {
				for (edge_data edge : arena.getGraph().getE(node.getKey())) {

					node_data src = arena.getGraph().getNode(edge.getSrc());
					node_data dest = arena.getGraph().getNode(edge.getDest());

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

		}
		for (node_data node : arena.getGraph().getV()) {
			int node_x = (int) scale(node.getLocation().x(), minX, maxX, 50, WIDTH - 50);
			int node_y = (int) scale(node.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

			g.setColor(Color.BLUE);
			g.fillOval(node_x - 5, node_y - 5, 10, 10);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(node.getKey() + "", node_x + 5, node_y + 5);
		}
	}

	// Paint fruits:
	private void paintFruits(Graphics2D g) {
		synchronized (arena.getFruits()) {
			WIDTH = getWidth();
			HEIGHT = getHeight();
			double minX = arena_minX;
			double minY = arena_minY;
			double maxX = arena_maxX;
			double maxY = arena_maxY;
			for (Fruit fruit : arena.getFruits()) {
				g.setColor(Color.ORANGE);
				if (fruit.getType() == 1) {
					g.setColor(Color.GREEN);
				}
				int fruit_x = (int) scale(fruit.getLocation().x(), minX, maxX, 50, WIDTH - 50);
				int fruit_y = (int) scale(fruit.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

				g.fillOval(fruit_x - 7, fruit_y - 7, 15, 15);
				g.setColor(Color.BLACK);
				g.drawString(fruit.getValue() + "", fruit_x + 10, fruit_y + 10);
			}
		}
	}

	// Paint robots:
	private void paintRobots(Graphics2D g) {
		WIDTH = getWidth();
		HEIGHT = getHeight();
		double minX = arena_minX;
		double minY = arena_minY;
		double maxX = arena_maxX;
		double maxY = arena_maxY;

		List<String> rob = arena.getGame().getRobots();
		for (int i = 0; i < rob.size(); i++) {
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString(rob.get(i), WIDTH / 5, 70 + (20 * i));
		}

		for (int i = 0; i < arena.numOfRobots(); i++) {
			Robot robot = arena.getRobots().get(i);

			int robot_x = (int) scale(robot.getLocation().x(), minX, maxX, 50, WIDTH - 50);
			int robot_y = (int) scale(robot.getLocation().y(), minY, maxY, 200, HEIGHT - 200);

			g.setColor(Color.GRAY);
			if (robot.getDest() == -1) {
				g.setColor(Color.RED);
			}
			g.drawOval(robot_x - 15, robot_y - 15, 30, 30);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString(robot.getID() + "", robot_x - 5, robot_y + 5);
		}
	}

	// Scaling methods: (Yael's code)
	private double scale(double data, double r_min, double r_max, double t_min, double t_max) {

		double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	private double scaleBack(double scaled_data, double r_min, double r_max, double t_min, double t_max) {
		double res = ((scaled_data - t_min) / (t_max - t_min)) * (r_max - r_min) + r_min;
		return res;
	}
	//

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!arena.getGame().isRunning())
			return;
		WIDTH = getWidth();
		HEIGHT = getHeight();
		double minX = arena_minX;
		double minY = arena_minY;
		double maxX = arena_maxX;
		double maxY = arena_maxY;

		// Choose robot by click:
		int x = e.getX(), y = e.getY();
		double original_x = scaleBack(x, minX, maxX, 50, WIDTH - 50);
		double original_y = scaleBack(y, minY, maxY, 200, HEIGHT - 200);

		int rid = arena.getRobotFromCoordinates(original_x, original_y);
		//

		if (rid != -1) {
			// Enter node destination:
			String dest_str = JOptionPane.showInputDialog("Enter next node for robot: " + rid);
			int dest = -1;
			try {
				dest = Integer.parseInt(dest_str);
				// Server call:
				arena.getGame().chooseNextEdge(rid, dest);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
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
