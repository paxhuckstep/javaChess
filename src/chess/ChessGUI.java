package chess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ChessGUI extends JPanel {
    private ChessSquareButton[][] boardButtons = new ChessSquareButton[8][8];
    public static boolean isWhitePovGlobal;
    private static Piece lastClickedPiece = null;
    public static int[] lastClickedSquare = new int[2];

    public ChessGUI(Color lightSquareColor, Color darkSquareColor, boolean isWhitePov) {
        ChessGUI.isWhitePovGlobal = isWhitePov;
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

//                Piece piece = boardData[column][row]; THESE I TOOK OUT
//                updateSquareIcon(squareButton, piece);


                int c = column;
                int r = row;
                squareButton.addActionListener(e -> {
//                    System.out.println("Clicked square: " + c + "," + r);
//                    Piece clickedPiece = boardData[c][r];
                    if (boardButtons[c][r].getIsLegal()) {
                        //move piece
//                        System.out.println("Move " + lastClickedPiece.getClass().getSimpleName() + " to " + c + ", " + r);
                        boardData[lastClickedSquare[0]][lastClickedSquare[1]] = null;
                        boardData[c][r] = lastClickedPiece;
                        // repaint
                        refreshBoard(boardData);

                    } else {
                        for (ChessSquareButton[] cleanRow : boardButtons) {
                            for (ChessSquareButton button : cleanRow) {
                                button.setIsLegal(false);
                            }
                        }
                        Piece clickedPiece = boardData[c][r];
                        if (clickedPiece != null) {
                            List<int[]> candidateMoves = clickedPiece.getCandidateMoves(c, r);
                            List<int[]> obstaclesHandled = clickedPiece.handleObstacles(c, r, boardData, candidateMoves);
                            // handleIsPinnedToKing
                            List<int[]> isPinnedMoves = clickedPiece.handleIsPinnedToKing(c,r,boardData, obstaclesHandled);
                            // handleBlocksCheck
                            for (int[] move : isPinnedMoves) {
//                                System.out.println("A more realistic move is: " + Arrays.toString(move));
                                boardButtons[move[0]][move[1]].setIsLegal(true);
                            }
//                            for (int[] move : candidateMoves) {
//                                System.out.println("A candidate move is: " + Arrays.toString(move));
//                            }
                        }
                        lastClickedPiece = clickedPiece;
                        lastClickedSquare = new int[]{c, r};
                    }
                });
                boardButtons[column][row] = squareButton;
                add(squareButton);
            }
        }
        refreshBoard(boardData);
    }

    public void refreshBoard(Piece[][] boardData) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquareButton square = boardButtons[column][row];
                Piece piece = boardData[column][row];
                updateSquareIcon(square, piece);
                square.setIsLegal(false);
            }
        }
        revalidate();
        repaint();
    }

    private void updateSquareIcon(ChessSquareButton square, Piece piece) {
        if (piece != null) {
            String color = piece.getIsWhite() ? "white" : "black";
            String name = piece.getClass().getSimpleName().toLowerCase();
            ImageIcon rawIcon = new ImageIcon(getClass().getResource("/chess/images/" + color + "_" + name + ".png"));
            Image scaled = rawIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            square.setIcon(new ImageIcon(scaled));
        } else {
            square.setIcon(null);
        }
    }

//    public boolean getIsWhitePov() {
//        return isWhitePov;
//    }

}