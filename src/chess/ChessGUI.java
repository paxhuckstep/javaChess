package chess;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ChessGUI extends JPanel {
    private ChessSquareButton[][] boardButtons = new ChessSquareButton[8][8];
    public static boolean isWhitePovGlobal;
    private static Piece lastClickedPiece = null;
    public static int[] lastClickedSquare = new int[2];
    public static int enPessantColumn = -1;

    public static boolean topLeftRookMoved = false;
    public static boolean topRightRookMoved = false;
    public static boolean bottomLeftRookMoved = false;
    public static boolean bottomRightRookMoved = false;
    public static boolean topKingMoved = false;
    public static boolean bottomKingMoved = false;

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
                    if (boardButtons[c][r].getIsLegal()) { // is a legal move

                        //handleEnPessant
                        if (lastClickedPiece instanceof Pawn && lastClickedSquare[0] != c && boardData[c][r] == null) {
//                            System.out.println("It's an en pessant capture!");
                            boardData[c][r - (isWhitePovGlobal ? lastClickedPiece.getIsWhite() ? - 1 : 1 : lastClickedPiece.getIsWhite() ? 1 : -1)] = null;
                        }

                        //move piece
                        boardData[lastClickedSquare[0]][lastClickedSquare[1]] = null;
                        boardData[c][r] = lastClickedPiece;


//                        System.out.println("ClickedSquare: " + c + ", " + r);
//                        System.out.println("Square Behind: : " + c + ", " + (r - (isWhitePovGlobal ? lastClickedPiece.getIsWhite() ? - 1 : 1 : lastClickedPiece.getIsWhite() ? 1 : -1)));
//                        System.out.println("enPessant Column: " + enPessantColumn);



                        //EnPessant Rights Check
                        if (lastClickedPiece instanceof Pawn && Math.abs(r - lastClickedSquare[1]) == 2) {
                            enPessantColumn = c;
                        } else {
                            enPessantColumn = -1;
                        }


                        //Castling Rights Check
                        if (lastClickedPiece instanceof King) {
                            if (lastClickedSquare[1] == 0) {
                                topKingMoved = true;
                            } else if (lastClickedSquare[1] == 7) {
                                bottomKingMoved = true;
                            }
                        } else if (lastClickedPiece instanceof Rook) {
                            if (lastClickedSquare[1] == 0) { // top row
                                if (lastClickedSquare[0] == 0) topLeftRookMoved = true;
                                if (lastClickedSquare[0] == 7) topRightRookMoved = true;
                            } else if (lastClickedSquare[1] == 7) { // bottom row
                                if (lastClickedSquare[0] == 0) bottomLeftRookMoved = true;
                                if (lastClickedSquare[0] == 7) bottomRightRookMoved = true;
                            }
                        }

                        // Handle castling rook movement
                        if (lastClickedPiece instanceof King) {
                            int previousRow = lastClickedSquare[1];
                            // Bottom side castling
                            if (previousRow == 7) {
                                if (isWhitePovGlobal) { // White POV (king starts col 4)
                                    if (c == 6) { // short castle
                                        boardData[5][7] = boardData[7][7]; // move rook
                                        boardData[7][7] = null;
                                        bottomRightRookMoved = true;
                                    } else if (c == 2) { // long castle
                                        boardData[3][7] = boardData[0][7];
                                        boardData[0][7] = null;
                                        bottomLeftRookMoved = true;
                                    }
                                } else { // Black POV (king starts col 3)
                                    if (c == 1) { // short castle
                                        boardData[2][7] = boardData[0][7];
                                        boardData[0][7] = null;
                                        bottomLeftRookMoved = true;
                                    } else if (c == 5) { // long castle
                                        boardData[4][7] = boardData[7][7];
                                        boardData[7][7] = null;
                                        bottomRightRookMoved = true;
                                    }
                                }
                            }

                            // Top side castling
                            else if (previousRow == 0) {
                                if (isWhitePovGlobal) { // White POV (king starts col 4)
                                    if (c == 6) { // short castle
                                        boardData[5][0] = boardData[7][0];
                                        boardData[7][0] = null;
                                        topRightRookMoved = true;
                                    } else if (c == 2) { // long castle
                                        boardData[3][0] = boardData[0][0];
                                        boardData[0][0] = null;
                                        topLeftRookMoved = true;
                                    }
                                } else { // Black POV (king starts col 3)
                                    if (c == 1) { // short castle
                                        boardData[2][0] = boardData[0][0];
                                        boardData[0][0] = null;
                                        topLeftRookMoved = true;
                                    } else if (c == 5) { // long castle
                                        boardData[4][0] = boardData[7][0];
                                        boardData[7][0] = null;
                                        topRightRookMoved = true;
                                    }
                                }
                            }
                        }

                        // repaint
                        refreshBoard(boardData);

                    } else { //clicked non-legal square.#GetMoves
                        for (ChessSquareButton[] cleanRow : boardButtons) {
                            for (ChessSquareButton button : cleanRow) {
                                button.setIsLegal(false);
                            }
                        }
                        Piece clickedPiece = boardData[c][r];
                        if (clickedPiece != null) {
                            List<int[]> candidateMoves = clickedPiece.getCandidateMoves(c, r);
                            List<int[]> obstaclesHandled = clickedPiece.handleObstacles(c, r, boardData, candidateMoves);
                            List<int[]> noSelfChecks = clickedPiece.handleNoSelfChecks(c, r, boardData, obstaclesHandled);
                            List<int[]> blocksAnyChecks = clickedPiece.handleBlocksCheck(c, r, boardData, noSelfChecks);

                            for (int[] move : blocksAnyChecks) {
                                boardButtons[move[0]][move[1]].setIsLegal(true);
                            }
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
}