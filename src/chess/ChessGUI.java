package chess;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {
    private JButton[][] boardSquares = new JButton[8][8];

    Piece[][] boardData = StartingBoardData.getStartingBoardData(true);

    public ChessGUI() {
        setTitle("Chess");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                JButton squareButton = new JButton();
                squareButton.setOpaque(true);
                squareButton.setBorderPainted(false);

                if ((row + column) % 2 == 0) {
                    squareButton.setBackground(Color.WHITE);
                } else {
                    squareButton.setBackground(Color.GRAY);
                }

                if (boardData[row][column] != null) {
                    squareButton.setText(boardData[row][column].getClass().getSimpleName());
                }

                int r = row;
                int c = column;

                squareButton.addActionListener(e -> {
                    System.out.println("Clicked square: " + r + "," + c);
                    Piece piece = boardData[r][c];
                    if (piece != null) {
                        System.out.println("This square has a " + piece.getClass().getSimpleName());
                    } else {
                        System.out.println("This square is empty.");
                    }
                });

                boardSquares[row][column] = squareButton;


                add(squareButton);
            }
        }
    }
}