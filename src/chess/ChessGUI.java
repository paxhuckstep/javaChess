package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChessGUI extends JPanel {
    private ChessSquareButton[][] boardButtons = new ChessSquareButton[8][8];

    private static Piece lastClickedPiece = null;
    public static int[] lastClickedSquare = new int[2];

    public ChessGUI(Color lightSquareColor, Color darkSquareColor, boolean isWhitePov) {
        setSize(600, 600);
        setLayout(new GridLayout(8, 8));
        Piece[][] boardData = StartingBoardData.getStartingBoardData(isWhitePov);

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquareButton squareButton = new ChessSquareButton();
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
//                    System.out.println("Clicked square: " + c + "," + r);
                    Piece clickedPiece = boardData[c][r];

                    if (boardButtons[c][r].getIsLegal()) {
                        //move piece
                        System.out.println("Move " + lastClickedPiece.getClass().getSimpleName() + " to " + c + ", " + r);

                        boardData[lastClickedSquare[0]][lastClickedSquare[1]] = null;
                        boardData[c][r] = lastClickedPiece;

                        // repaint
                        refreshBoard(boardData);

                    } else {

                        for (ChessSquareButton[] cleanRow : boardButtons) {
                            for (ChessSquareButton button : cleanRow) {
                                button.setShowCircle(false);
                            }
                        }

                        lastClickedPiece = clickedPiece;
                        lastClickedSquare = new int[]{c, r};

                        if (clickedPiece != null) {

                            List<int[]> candidateMoves = clickedPiece.getCandidateMoves(c, r);

//                            for (int[] move : candidateMoves) {
//                                System.out.println("A candidate move is: " + Arrays.toString(move));
//                            }

                            List<int[]> simpleObstaclesHandled = clickedPiece.handleObstacles(c, r, boardData, candidateMoves);


                            for (int[] move : simpleObstaclesHandled) {
//                                System.out.println("A more realistic move is: " + Arrays.toString(move));
                                boardButtons[move[0]][move[1]].setShowCircle(true);
                            }

                        }
                    }


                });

                boardButtons[column][row] = squareButton;


                add(squareButton);
            }
        }


    }

    public void refreshBoard(Piece[][] boardData) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquareButton square = boardButtons[column][row];
                Piece piece = boardData[column][row];

                if (piece != null) {
                    square.setText(piece.getClass().getSimpleName());
                } else {
                    square.setText("");
                }

                square.setShowCircle(false); // clear indicators on refresh
            }
        }

        revalidate();
        repaint();
    }
}