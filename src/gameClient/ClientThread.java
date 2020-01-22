package gameClient;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	private KML_Logger kml;	// KML object.

	/**
	 * default scenario
	 */
	private static int scenario = 0;
	/**
	 * default game mode
	 */
	private static boolean auto_game = true;
	/**
	 * default user id
	 */
	private static int user_id = 999;
	
	/**
	 * Constructor initiate the GUI and arena.
	 * Sets userID to GUI.
	 */
	public ClientThread() {
		arena = new GameArena(scenario);
		window = new MyGameGUI(arena, auto_game);
		window.setUserID(user_id);
	}

	/**
	 * Running the game on the background
	 */
	@Override
	public void run() {

	//	Game_Server.login(user_id); 
		game_service g = arena.getGame();
		g.startGame(); // start game
		
		AutoGame auto = null;
		if (auto_game) { // initiate auto-game if auto mode has been chosen
			auto=new AutoGame(arena);
		}
		try {
			int dt = 90;
			int i = 0;
			while (g.isRunning()) {
				if(auto_game) {
					auto.moveRobots(g);
				}
				g.move(); // move robots
				arena.update(); // update arena
				window.repaint(); // repaint GUI
				if (i % 2 == 0) {
					
				}
				Thread.sleep(dt);
				i++;
			}
		} catch (Exception e) {e.printStackTrace();}

		kml = KML_Logger.getInstance(scenario); // close KML file
		kml.end();
		KMLDialog(g);
				
		double grade = arena.getGrade();
		int moves = arena.getMoves();

		// message with grade and moves
		JOptionPane.showMessageDialog(window, "Game Over!\nPoints earned: " + grade + " in " + moves + " moves.");
		
	}

	// KML dialog for the user to choose.
	private void KMLDialog(game_service g) {
		int ans = JOptionPane.showConfirmDialog(window, "Export to KML?");
		if (ans == 0) {
			kml.export();
			g.sendKML(kml.getKML());
		}
	}

	// Opening window for choosing scenario and mode:
	private static void init() {
		JFrame frame = new JFrame();
		frame.setBounds(200, 0, 500, 500);
		try {

			String id = JOptionPane.showInputDialog(frame, "Please insert your ID");
			String stage = JOptionPane.showInputDialog(frame, "Please insert a scenerio [0-23]");
			
			String[] modes = { "Manual", "Auto" };
			int mode = JOptionPane.showOptionDialog(frame, "Choose option", "The Maze of Waze",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modes, modes[1]);
			
			user_id = Integer.parseInt(id);
			
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
