package gameClient;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Server.game_service;

public class ClientThread extends Thread {

	private MyGameGUI window;
	private GameArena arena;

	private static int num = 0;
	private static boolean b = true;
	
	public ClientThread() {
		arena = new GameArena(num);
		window = new MyGameGUI(arena, b);

		if (b) {
			Game_Algo autogame = new Game_Algo(num);
			autogame.start();
		}
	}

	@Override
	public void run() {
		game_service g = arena.getGame();
		g.startGame();
		while(g.isRunning()) {
			g.move();
			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arena.update();
			window.repaint();
		}
		
	}

	public static void init() {
		try {
		JFrame frame = new JFrame();
		frame.setBounds(200, 0, 500, 500);
		
		String[] modes = {"Manual","Auto"};
		String stage = JOptionPane.showInputDialog(frame, "Please insert a scenerio [0-23]");
		int mode = JOptionPane.showOptionDialog(frame, "Choose option", "The Maze of Waze", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, modes, modes[1]);
		
		
			num = Integer.parseInt(stage);
			if(mode == 0) {
				b = false;
			} else {
				b = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	public static void main(String[] args) {
		init();
		System.out.println(num);
		System.out.println(b);
		ClientThread client = new ClientThread();
		client.start();
		

	}

}
