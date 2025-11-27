package chess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:C:/Users/paxhu/IdeaProjects/ChessAppJava/chess_game.db";

    public static void createOpeningsIsWhiteTable() {
        String stringToCreateTable = "CREATE TABLE IF NOT EXISTS OpeningsIsWhite (" +
                "    name TEXT PRIMARY KEY," +
                "    is_white_opening BOOLEAN" +
                ");";

        try (Connection sqlConnectionObject = DriverManager.getConnection(URL);
             Statement statementObject = sqlConnectionObject.createStatement()) {
            statementObject.execute(stringToCreateTable);
            System.out.println("OpeningsIsWhite table ready.");

        } catch (SQLException e) {
//                e.printStackTrace();
        }
    }

public static void addNewOpening(String openingName, boolean isWhiteOpening) {
    String createOpeningTable =
            "CREATE TABLE IF NOT EXISTS " + openingName +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " line_number INTEGER, is_white_turn BOOLEAN, move_text TEXT);";

    String addToOpeningsIsWhite =
            "INSERT INTO OpeningsIsWhite (name, is_white_opening) VALUES (?, ?)";

    try (Connection sqlConnectionObject = DriverManager.getConnection(URL);
         Statement statementObject = sqlConnectionObject.createStatement();
         PreparedStatement pstmt = sqlConnectionObject.prepareStatement(addToOpeningsIsWhite)) {

        // Create the moves table for this opening
        statementObject.execute(createOpeningTable);
        System.out.println(openingName + " table ready.");

        // Register the opening in the metadata table
        pstmt.setString(1, openingName);
        pstmt.setBoolean(2, isWhiteOpening);
        pstmt.executeUpdate();

        System.out.println("OpeningIsWhite " + openingName + " added to Openings metadata.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public static void saveMoveToOpening(String openingName, int lineNumber, Boolean isWhiteTurn, String move) {
        String recordMoveStructure = "INSERT INTO " + openingName + "(line_number, is_white_turn ,move_text) VALUES(? ,? ,?)";

        try (Connection sqlConnectionObject = DriverManager.getConnection(URL);
             PreparedStatement preparedStatementObject = sqlConnectionObject.prepareStatement(recordMoveStructure)) {
            preparedStatementObject.setInt(1, lineNumber);
            preparedStatementObject.setBoolean(2, isWhiteTurn);
            preparedStatementObject.setString(3, move);
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

    public static int getMaxLineNumber(String openingName) {
        int maxLine = 0;
        System.out.println("opening that we're looking for maxLine: " + openingName);

        String query = "SELECT MAX(line_number) FROM " + openingName;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                maxLine = rs.getInt(1); // returns 0 if null
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("maxLine found: " + maxLine);

        return maxLine;
    }

    public static boolean getIsWhiteOpening(String openingName) {
        boolean isWhiteOpening = false;
        String query = "SELECT is_white_opening FROM OpeningsIsWhite WHERE name = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, openingName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    isWhiteOpening = rs.getBoolean("is_white_opening");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("isWhiteOpening found for " + openingName + ": " + isWhiteOpening);
        return isWhiteOpening;
    }


}
