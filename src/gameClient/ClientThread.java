package gameClient;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;

public class ClientThread extends Thread {

	private MyGameGUI window;
	private GameArena arena;

	private static int scenario = 0;
	private static boolean auto_game = true;

	public ClientThread() {
		arena = new GameArena(scenario);
		window = new MyGameGUI(arena, auto_game);

		if (auto_game) {
			Game_Algo autogame = new Game_Algo(scenario);
			autogame.start();
		}
	}

	@Override
	public void run() {
		try {
			game_service g = arena.getGame();
			g.startGame();
			while (g.isRunning()) {
				Thread.sleep(100);
				g.move();
				arena.update();
				window.repaint();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double grade = getGrade();
		int moves = getMoves();
		JOptionPane.showMessageDialog(window, "Game Over!\nPoints earned: "+grade+" in "+moves+" moves.");
		window.setVisible(false);	
		System.exit(1);
		
		
	}

	private int getMoves() {
		int moves = -1;
		try {
			JSONObject game_obj = new JSONObject(arena.getGame().toString()).getJSONObject("GameServer");
			moves = game_obj.getInt("moves");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return moves;
	}

	private double getGrade() {
		int grade = -1;
		try {
			JSONObject game_obj = new JSONObject(arena.getGame().toString()).getJSONObject("GameServer");
			grade = game_obj.getInt("grade");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grade;
	}
	
	private static void init() {
		JFrame frame = new JFrame();
		frame.setBounds(200, 0, 500, 500);
		try {


			String[] modes = { "Manual", "Auto" };
			String stage = JOptionPane.showInputDialog(frame, "Please insert a scenerio [0-23]");
			int mode = JOptionPane.showOptionDialog(frame, "Choose option", "The Maze of Waze",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modes, modes[1]);

			scenario = Integer.parseInt(stage);
			
			if(scenario >23 || scenario < 0) throw new RuntimeException();
			
			if (mode == 0) {
				auto_game = false;
			} else {
				auto_game = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Invalid input.\nPlaying default game", "Error", JOptionPane.ERROR_MESSAGE);
			scenario = 0;
			auto_game =true;
		}

	}

	public static void main(String[] args) {
		init();
		ClientThread client = new ClientThread();
		client.start();

	}

}
