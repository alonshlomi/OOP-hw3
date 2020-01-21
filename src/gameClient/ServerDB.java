package gameClient;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.LinkedHashMap;

public class ServerDB {
	public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser = "student";
	public static final String jdbcUserPassword = "OOP2020student";
	public static final int MAX_MOVES = Integer.MAX_VALUE;
	public static final int[] LEVELS = { 290, 580, MAX_MOVES, 580, MAX_MOVES, 500, MAX_MOVES, MAX_MOVES, MAX_MOVES, 580,
			MAX_MOVES, 580, MAX_MOVES, 580, MAX_MOVES, MAX_MOVES, 290, MAX_MOVES, MAX_MOVES, 580, 290, MAX_MOVES,
			MAX_MOVES, 1140 };

	private static Connection connection;

	public static void printLog() throws SQLException {
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		String allCustomersQuery = "SELECT * FROM Logs;";
		ResultSet resultSet = statement.executeQuery(allCustomersQuery);

		while (resultSet.next()) {
			System.out.println("Id: " + resultSet.getInt("UserID") + ",level: " + resultSet.getInt("levelID")
					+ ",score: " + resultSet.getInt("score") + ",moves: " + resultSet.getInt("moves") + ",time :"
					+ resultSet.getDate("time"));
		}
		resultSet.close();
		statement.close();
		connection.close();
	}

	public static int gamesPlayedCount(int user_id) throws SQLException {
		int ans = -1;
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		String games_count_query = "SELECT COUNT(*) AS count FROM Logs WHERE userID='" + user_id + "';";
		ResultSet resultSet = statement.executeQuery(games_count_query);
		resultSet.next();
		ans = resultSet.getInt("count");

		resultSet.close();
		statement.close();
		connection.close();

		return ans;
	}

	public static int getCurrentLevel(int user_id) throws SQLException {
		String curr_level_query = "SELECT levelNum FROM Users WHERE userID = '" + user_id + "'";
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(curr_level_query);

		resultSet.next();
		return resultSet.getInt("levelNum");
	}

	public static String[][] bestScores(int level_id) throws SQLException {
		String best_score_query = "SELECT * FROM Logs WHERE  levelID = '" + level_id
				+ "' AND userID > '199999999' AND moves <= '" + LEVELS[level_id] + "' ORDER BY score DESC;";
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(best_score_query);
		LinkedHashMap<Integer, String> data = new LinkedHashMap<Integer, String>();

		while (resultSet.next()) {
			int userID = resultSet.getInt("userID");
			if (data.containsKey(userID))
				continue;

			double score = resultSet.getDouble("score");
			int moves = resultSet.getInt("moves");
			Date when = resultSet.getDate("time");
			String user_data = userID + "," + score + "," + moves + "," + when.toString();

			data.put(userID, user_data);
		}

		return scoresToArray(data);

	}

	private static String[][] scoresToArray(LinkedHashMap<Integer, String> data) {
		int users_size = data.size();
		int info_size = 4;
		String[][] ans = new String[users_size][info_size];

		int i = 0;
		for (String _data : data.values()) {
			if (i == users_size)
				break;
			String[] user_data = _data.split(",");
			ans[i++] = user_data;
		}

		return ans;
	}

	// return ans[0]=my best score, in ans[1] moves
	public static double[] myBestScore(int user_id, int level_id) throws SQLException {
		double[] ans = new double[2];
		String my_best_query = "SELECT * FROM Logs WHERE userID='" + user_id + "' AND levelID = '" + level_id
				+ "' AND moves <= '" + LEVELS[level_id] + "' ORDER BY score DESC;";
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(my_best_query);
		resultSet.next();
		double score = resultSet.getDouble("score");
		double moves = resultSet.getDouble("moves");
		ans[0] = score;
		ans[1] = moves;

		return ans;
	}

	
	// ans[0] - my position, ans[1] - total players
	public static int[] myPosition(int user_id, int level_id) throws SQLException {
		int[] ans = new int[2];
		String[][] best_scores = bestScores(level_id);
		ans[0] = best_scores.length;
		ans[1] = best_scores.length;
		for (int i = 0; i < best_scores.length; i++) {
			int curr_id = Integer.parseInt(best_scores[i][0]);

			if (curr_id == user_id) {
				ans[0] = i+1;
				break;
			}
		}
		return ans;
	}

}
