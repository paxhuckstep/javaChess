package chess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void createOpeningTable(String openingName, boolean isWhiteOpening) {
        String stringToAddOpeningTable =
                "CREATE TABLE IF NOT EXISTS " + openingName +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, is_white_opening BOOLEAN DEFAULT " + (isWhiteOpening ? "1" : "0") + ", is_white_turn BOOLEAN, move_text TEXT);";

        try (Connection sqlConnectionObject = DriverManager.getConnection(URL);
             Statement statementObject = sqlConnectionObject.createStatement()) {
            statementObject.execute(stringToAddOpeningTable);
            System.out.println(openingName + " table ready.");
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
    public static List<String> getAllOpenings() {
        List<String> openingNames = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'")) {


            while (rs.next()) {
                openingNames.add(rs.getString("name"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return openingNames;
    }


}
