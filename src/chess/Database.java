package chess;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:C:/Users/paxhu/IdeaProjects/ChessAppJava/chess_game.db";

    public static void createMovesTable() {
        String statementStringCreate = "CREATE TABLE IF NOT EXISTS moves (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "opening_name TEXT," +
                "move_text TEXT" +
                ");";

        try (Connection connected = DriverManager.getConnection(URL);
             Statement sqlStatement = connected.createStatement()) {
            sqlStatement.execute(statementStringCreate);
            System.out.println("Moves table ready.");
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }



    public static void saveMove(String openingName, String move) {
        String statementStringSave = "INSERT INTO moves(opening_name, move_text) VALUES(?, ?)";

        try (Connection connected = DriverManager.getConnection(URL);
             PreparedStatement sqlStatement = connected.prepareStatement(statementStringSave)) {

            sqlStatement.setString(1, openingName);
            sqlStatement.setString(2, move);
            sqlStatement.executeUpdate();

        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }
}
