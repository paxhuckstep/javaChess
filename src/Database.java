package chess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:sqlite:chess_game.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // quick test
//    public static void main(String[] args) {
//        try (Connection conn = connect()) {
//            if (conn != null) {
//                System.out.println("Connected to SQLite!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
