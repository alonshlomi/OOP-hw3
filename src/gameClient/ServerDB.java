package gameClient;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

/**
 * This class implements the connection between the game and data-base and
 * allows to get info from it.
 * 
 * @author Alon Perlmuter.
 * @author Shlomi Daari.
 *
 */
public class ServerDB {
	// Connection parameters:
	public static final String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser = "student";
	public static final String jdbcUserPassword = "OOP2020student";

	// Levels list of maximum moves:
	public static final int MAX_MOVES = Integer.MAX_VALUE;
	public static final int[] LEVELS = { 290, 580, MAX_MOVES, 580, MAX_MOVES, 500, MAX_MOVES, MAX_MOVES, MAX_MOVES, 580,
			MAX_MOVES, 580, MAX_MOVES, 580, MAX_MOVES, MAX_MOVES, 290, MAX_MOVES, MAX_MOVES, 580, 290, MAX_MOVES,
			MAX_MOVES, 1140 };

	private static Connection connection; // for connecting to database

	/**
	 * Returns the num of game user_id played.
	 * 
	 * @param user_id
	 * @return number of plays
	 * @throws SQLException
	 */
	public static int gamesPlayedCount(int user_id) throws SQLException {
		String games_count_query = "SELECT COUNT(*) AS count FROM Logs WHERE userID='" + user_id + "';";

		int ans = -1;
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(games_count_query);
		if (resultSet.next()) {
			ans = resultSet.getInt("count");
		}

		resultSet.close();
		statement.close();
		connection.close();

		return ans;
	}

	/**
	 * Returns the current level user_id is need to play.
	 * 
	 * @param user_id
	 * @return current level
	 * @throws SQLException
	 */
	public static int getCurrentLevel(int user_id) throws SQLException {
		String curr_level_query = "SELECT levelNum FROM Users WHERE userID = '" + user_id + "';";

		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(curr_level_query);

		if (resultSet.next()) {
			return resultSet.getInt("levelNum");
		}
		return -1;
	}

	/**
	 * Returns a 2 dimension String array of all users data in given level, without
	 * duplicates.
	 * 
	 * @param level_id
	 * @return 2 dimension array of data
	 * @throws SQLException
	 */
	public static String[][] bestScores(int level_id) throws SQLException {
		String best_score_query = "SELECT * FROM Logs WHERE  levelID = '" + level_id
				+ "' AND userID > '199999999' AND moves <= '" + LEVELS[level_id] + "' ORDER BY score DESC;";

		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(best_score_query);
		LinkedHashMap<Integer, String> data = new LinkedHashMap<Integer, String>();

		int i = 1;
		while (resultSet.next()) {
			int userID = resultSet.getInt("userID");
			if (data.containsKey(userID))
				continue;

			double score = resultSet.getDouble("score");
			int moves = resultSet.getInt("moves");
			Date when = resultSet.getDate("time");
			String user_data = i++ + "," + userID + "," + score + "," + moves + "," + when.toString();

			data.put(userID, user_data);
		}

		return scoresToArray(data);
	}

	// For bestScores() use:
	private static String[][] scoresToArray(LinkedHashMap<Integer, String> data) {
		int users_size = data.size();
		int info_size = 5;
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

	/**
	 * Returns the user_id best score in given level_id.
	 * 
	 * @param user_id
	 * @param level_id
	 * @return ans[0] - my best score
	 * @return ans[1] - number of moves
	 * @throws SQLException
	 */
	public static double[] myBestScore(int user_id, int level_id) throws SQLException {
		String my_best_query = "SELECT * FROM Logs WHERE userID='" + user_id + "' AND levelID = '" + level_id
				+ "' AND moves <= '" + LEVELS[level_id] + "' ORDER BY score DESC;";

		double[] ans = new double[2];
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(my_best_query);

		double score = -1;
		double moves = -1;
		if (resultSet.next()) {
			score = resultSet.getDouble("score");
			moves = resultSet.getDouble("moves");
		}
		ans[0] = score;
		ans[1] = moves;

		return ans;
	}

	/**
	 * Returns user_id position in given level_id in relation to the class.
	 * 
	 * @param user_id
	 * @param level_id
	 * @return ans[0] - user_id position
	 * @return ans[1] - total players of the class
	 * @throws SQLException
	 */
	public static int[] myPosition(int user_id, int level_id) throws SQLException {
		int[] ans = new int[2];
		String[][] best_scores = bestScores(level_id);

		ans[0] = -1;
		ans[1] = best_scores.length;
		for (int i = 0; i < best_scores.length; i++) {
			int curr_id = Integer.parseInt(best_scores[i][1]);

			if (curr_id == user_id) {
				ans[0] = i + 1;
				break;
			}
		}
		return ans;
	}

}
