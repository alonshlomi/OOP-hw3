package gameClient;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;

/**
 * This class is attaching the arena to GUI and using the server in thread.
 * 
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 */
public class ClientThread extends Thread {

	private MyGameGUI window; // GUI object.
	private GameArena arena; // arena object.
	private KML_Logger kml;

	/**
	 * default scenario
	 */
	private static int scenario = 0;
	/**
	 * default game mode
	 */
	private static boolean auto_game = true;

	/**
	 * Constructor initiate the GUI and arena.
	 */
	public ClientThread() {
		arena = new GameArena(scenario);
		window = new MyGameGUI(arena, auto_game);
	}

	/**
	 * Running the game on the background
	 */
	@Override
	public void run() {
		int id = 205487747;
		Game_Server.login(id); 
		game_service g = arena.getGame();
		g.startGame(); // start game
		
		AutoGame auto = null;
		if (auto_game) { // initiate auto-game if auto mode has been chosen
			auto=new AutoGame(arena);
		}
		try {
			int dt = 70;
			int i = 0;
			while (g.isRunning()) {
				if(auto_game) {
					auto.MoveRobots(g);
				}
				g.move(); // move robots
				arena.update(); // update arena
				if (i % 2 == 0) {
					window.repaint(); // repaint GUI
				}
				Thread.sleep(dt);
				i++;
			}
		} catch (Exception e) {e.printStackTrace();}

		kml = KML_Logger.getInstance(scenario); // close KML file
		kml.end();
		KMLDialog();
		
		g.sendKML(kml.getKML());
		
		double grade = getGrade();
		int moves = getMoves();

		// message with grade and moves
		JOptionPane.showMessageDialog(window, "Game Over!\nPoints earned: " + grade + " in " + moves + " moves.");
		window.setVisible(false);
		System.exit(0); // successfully exited
	}

	// KML dialog for the user to choose.
	private void KMLDialog() {
		int ans = JOptionPane.showConfirmDialog(window, "Export to KML?");
		if (ans == 0) {
			kml.export();
		}
	}

	// Returns the moves played in current game:
	private int getMoves() {
		int moves = -1;
		try {
			JSONObject game_obj = new JSONObject(arena.getGame().toString()).getJSONObject("GameServer");
			moves = game_obj.getInt("moves");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return moves;
	}

	// Returns the points earned in current game:
	private double getGrade() {
		int grade = -1;
		try {
			JSONObject game_obj = new JSONObject(arena.getGame().toString()).getJSONObject("GameServer");
			grade = game_obj.getInt("grade");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return grade;
	}

	// Opening window for choosing scenario and mode:
	private static void init() {
		JFrame frame = new JFrame();
		frame.setBounds(200, 0, 500, 500);
		try {

			String[] modes = { "Manual", "Auto" };
			String stage = JOptionPane.showInputDialog(frame, "Please insert a scenerio [0-23]");
			int mode = JOptionPane.showOptionDialog(frame, "Choose option", "The Maze of Waze",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modes, modes[1]);

			scenario = Integer.parseInt(stage);

			if (scenario > 23 || scenario < 0)
				throw new RuntimeException();

			auto_game = true;
			if (mode == 0) {
				auto_game = false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Invalid input.\nPlaying default game", "Error",
					JOptionPane.ERROR_MESSAGE);
			scenario = 0;
			auto_game = true;
		}

	}

	// Run program:
	public static void main(String[] args) {
		init();
		ClientThread client = new ClientThread();
		client.start();
	}
}
