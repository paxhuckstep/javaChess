package chess;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {
    private JButton[][] boardSquares = new JButton[8][8];

    Piece[][] board = StartingBoardData.getStartingBoardData(true);

    public ChessGUI() {
        setTitle("Chess");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                JButton square = new JButton();
                square.setOpaque(true);
                square.setBorderPainted(false);

                if ((row + column) % 2 == 0) {
                    square.setBackground(Color.WHITE);
                } else {
                    square.setBackground(Color.GRAY);
                }
//
//                if (row == 0 && column == 3) {
//                    square.setBackground(Color.PINK);
//                }
                if (board[row][column] != null) {
                    square.setText(board[row][column].getClass().getSimpleName());
                }

                boardSquares[row][column] = square;
                add(square);
            }
        }
    }
}
