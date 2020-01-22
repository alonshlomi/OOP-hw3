package gameClient;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
	private int user_id; // for database use

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
		this.setTitle("The Maze of Waze - Scenario " + arena.getScenario());
		this.setBounds(200, 0, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MenuBar menubar = new MenuBar();
		Menu stats_menu = new Menu("Stats");
		MenuItem my_stats = new MenuItem("My Stats");
		MenuItem class_stats = new MenuItem("Class Stats");

		my_stats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				myStats();
			}
		});

		class_stats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				classStats();
			}
		});

		stats_menu.add(my_stats);
		stats_menu.add(class_stats);
		menubar.add(stats_menu);

		this.setMenuBar(menubar);

		if (!auto_game) { // use mouse-listener if manual mode has been chosen
			this.addMouseListener(this);
		}

		this.setVisible(true);
	}

	// Class-stats dialog: 
	private void classStats() {
		try {
			int scenario = arena.getScenario();
			JFrame frame = new JFrame("Class Stats - Scenario " + scenario);

			frame.setLayout(new FlowLayout());
			frame.setBounds(200, 0, 600, 600);

			int[] my_pos = ServerDB.myPosition(user_id, scenario);

			String[] col = { "Pos.", "UserID", "Score", "Moves", "Data" };
			String[][] data = ServerDB.bestScores(scenario);

			JLabel pos_lbl = new JLabel("Your position is " + my_pos[0] + " of " + my_pos[1] + " players.");
			if (my_pos[0] == -1) {
				pos_lbl.setText("You are not ranked!");
			}

			DefaultTableModel model = new DefaultTableModel(data, col);
			JTable data_tbl = new JTable(model);
			JScrollPane scroll = new JScrollPane(data_tbl);

			pos_lbl.setFont(new Font("Arial", Font.BOLD, 15));
			frame.add(pos_lbl);
			frame.add(scroll);

			frame.setVisible(true);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// My-stats dialog:
	private void myStats() {
		try {
			int scenario = arena.getScenario();
			JFrame frame = new JFrame("My Stats - " + user_id);
			frame.setLayout(new GridLayout(3, 1));
			frame.setBounds(200, 0, 500, 300);

			int games_played = ServerDB.gamesPlayedCount(user_id);
			int curr_level = ServerDB.getCurrentLevel(user_id);
			double[] my_best_score = ServerDB.myBestScore(user_id, scenario);

			JLabel games_played_lbl = new JLabel("Number of games you played: " + games_played + "\n");
			JLabel curr_level_lbl = new JLabel("Your current level: " + curr_level + "\n");
			JLabel my_best_lbl = new JLabel("Your best score for scenario " + scenario + " is " + my_best_score[0]
					+ " in " + (int) my_best_score[1] + " moves.");

			if (my_best_score[0] == -1) {
				my_best_lbl.setText("You dont have a record for scenario " + scenario + " yet");
			}

			games_played_lbl.setFont(new Font("Arial", Font.BOLD, 15));
			curr_level_lbl.setFont(new Font("Arial", Font.BOLD, 15));
			my_best_lbl.setFont(new Font("Arial", Font.BOLD, 15));

			frame.add(games_played_lbl);
			frame.add(curr_level_lbl);
			frame.add(my_best_lbl);

			frame.setVisible(true);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Draw time-to-end, score and moves:
	private void setTimeAndScore(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Time: " + arena.getGame().timeToEnd() / 1000, 50, 70);
		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString("Score: " + arena.getGrade(), 50, 90);
		g.drawString("Moves: " + arena.getMoves(), 50, 110);
	}

	/**
	 * Painting arena using buffered image to avoid frame flashing:
	 */
	public void paint(Graphics g) {
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.setBackground(new Color(240, 240, 240));
		g2d.clearRect(0, 0, WIDTH, HEIGHT);

		paintGraph(g2d);

		setTimeAndScore(g2d);
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
	private void paintRobots(Graphics2D g) { // NEED TO REMOVE FOR!!
		WIDTH = getWidth();
		HEIGHT = getHeight();
		double minX = arena_minX;
		double minY = arena_minY;
		double maxX = arena_maxX;
		double maxY = arena_maxY;

		List<String> rob = arena.getGame().getRobots(); // NEED TO REMOVE!!!
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

	/**
	 * Set the user id.
	 * @param id of the user
	 */
	public void setUserID(int id) {
		user_id = id;
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
