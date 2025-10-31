package chess;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:C:/Users/paxhu/IdeaProjects/ChessAppJava/chess_game.db";

    public static void createMovesTable() {
        String stringToCreateTable = "CREATE TABLE IF NOT EXISTS This_Game (id INTEGER PRIMARY KEY AUTOINCREMENT ,is_white_turn BOOLEAN ,move_text TEXT);";

        try (Connection sqlConnectionObject = DriverManager.getConnection(URL);
             Statement statementObject = sqlConnectionObject.createStatement()) {
            statementObject.execute(stringToCreateTable);
            System.out.println("This_Game table ready.");
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }


    public static void saveMove(Boolean isWhiteTurn, String move) {
        String recordMoveStructure = "INSERT INTO This_Game(is_white_turn ,move_text) VALUES(? ,?)";

        try (Connection sqlConnectionObject = DriverManager.getConnection(URL);
             PreparedStatement preparedStatementObject = sqlConnectionObject.prepareStatement(recordMoveStructure)) {

            preparedStatementObject.setBoolean(1, isWhiteTurn);
            preparedStatementObject.setString(2, move);
            preparedStatementObject.executeUpdate();

        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }
}
