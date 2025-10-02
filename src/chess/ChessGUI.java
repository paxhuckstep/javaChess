package chess;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class ChessGUI extends JPanel {
    private JButton[][] boardButtons = new JButton[8][8];


    public ChessGUI(Color lightSquareColor, Color darkSquareColor, boolean isWhitePov) {
        setSize(600, 600);
        setLayout(new GridLayout(8, 8));
        Piece[][] boardData = StartingBoardData.getStartingBoardData(isWhitePov);

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                JButton squareButton = new JButton();
                squareButton.setOpaque(true);
                squareButton.setBorderPainted(false);

                if ((row + column) % 2 == 0) {
                    squareButton.setBackground(lightSquareColor);
                } else {
                    squareButton.setBackground(darkSquareColor);
                }

                if (boardData[column][row] != null) {
                    squareButton.setText(boardData[column][row].getClass().getSimpleName());
                }


                int c = column;
                int r = row;

                squareButton.addActionListener(e -> {
                    System.out.println("Clicked square: " + c + "," + r);
                    Piece clickedPiece = boardData[c][r];
                    if (clickedPiece != null) {
                        System.out.println("This square has a " + clickedPiece.getClass().getSimpleName());


                        for (int[] move : clickedPiece.getCandidateMoves(c, r)) {
                            System.out.println("A candidate move is: " + Arrays.toString(move));
                        }

                    } else {
                        System.out.println("This square is empty.");
                    }
                });

                boardButtons[column][row] = squareButton;


                add(squareButton);
            }
        }
    }
}