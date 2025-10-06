package chess;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ChessGUI extends JPanel {
    private ChessSquareButton[][] boardButtons = new ChessSquareButton[8][8];
    private Piece[][] boardData = new Piece[8][8];
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
//        Piece[][] boardData = StartingBoardData.getStartingBoardData(isWhitePov);
        boardData = StartingBoardData.getStartingBoardData(isWhitePov);

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

                int c = column;
                int r = row;
                squareButton.addActionListener(e -> {
                    if (boardButtons[c][r].getIsLegal()) { // is a legal move

                        //handle EnPessant capture
                        if (lastClickedPiece instanceof Pawn && lastClickedSquare[0] != c && boardData[c][r] == null) {
                            boardData[c][r - (isWhitePovGlobal == lastClickedPiece.getIsWhite() ? -1 : 1)] = null;
                        }

                        //move piece
                        boardData[lastClickedSquare[0]][lastClickedSquare[1]] = null;
                        boardData[c][r] = lastClickedPiece;

                        //EnPessant Rights Update
                        if (lastClickedPiece instanceof Pawn && Math.abs(r - lastClickedSquare[1]) == 2) {
                            enPessantColumn = c;
                        } else {
                            enPessantColumn = -1;
                        }

                        //Castling Rights Update
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

                        //handlePromotion
                        if (lastClickedPiece instanceof Pawn && (r == 0 || r == 7)) {
                            String promotionPiece = PromotionGUI.openPromotionPopup(BigGUI.bigGuiReference, lastClickedPiece.getIsWhite());
                            if (promotionPiece != null) {
                                switch (promotionPiece) {
                                    case "queen":
                                        boardData[c][r] = new Queen(lastClickedPiece.getIsWhite());
                                        break;
                                    case "rook":
                                        boardData[c][r] = new Rook(lastClickedPiece.getIsWhite());
                                        break;
                                    case "bishop":
                                        boardData[c][r] = new Bishop(lastClickedPiece.getIsWhite());
                                        break;
                                    case "knight":
                                        boardData[c][r] = new Knight(lastClickedPiece.getIsWhite());
                                        break;
                                }
                            }
                        }

                        // repaint
                        refreshBoard(boardData);

                    } else { //clicked non-legal square
                        //Clean off old legal moves
                        for (ChessSquareButton[] cleanRow : boardButtons) {
                            for (ChessSquareButton button : cleanRow) {
                                button.setIsLegal(false);
                            }
                        }
                        Piece clickedPiece = boardData[c][r];
                        if (clickedPiece != null) { // #GetMoves
                            List<int[]> candidateMoves = clickedPiece.getCandidateMoves(c, r);
                            List<int[]> obstaclesHandled = clickedPiece.handleObstacles(c, r, boardData, candidateMoves);
                            List<int[]> noSelfChecks = clickedPiece.handleNoSelfChecks(c, r, boardData, obstaclesHandled);
                            List<int[]> legalMoves = clickedPiece.handleBlocksCheck(c, r, boardData, noSelfChecks);

                            for (int[] legalMove : legalMoves) {
                                boardButtons[legalMove[0]][legalMove[1]].setIsLegal(true);
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

    public void resetBoardData() {
        Piece[][] resetBoardData = StartingBoardData.getStartingBoardData(isWhitePovGlobal);

        for (int resetColumn = 0; resetColumn < 8; resetColumn++) {
            for (int resetRow = 2; resetRow < 6; resetRow++) {
                resetBoardData[resetColumn][resetRow] = null;
            }
        }
        topKingMoved = false;
        bottomKingMoved = false;
        topLeftRookMoved = false;
        topRightRookMoved = false;
        bottomLeftRookMoved = false;
        bottomRightRookMoved = false;
        enPessantColumn = -1;

        boardData = resetBoardData;
        refreshBoard(resetBoardData);
    }

    public void flipBoard() {
        Piece[][]  flippedBoardData = new Piece[8][8];
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                flippedBoardData[column][row] = boardData[7-column][7 - row];
            }
        }
        boolean oldTopKingMoved = topKingMoved;
        boolean oldTopLeftRookMoved = topLeftRookMoved;
        boolean oldTopRightRookMoved = topRightRookMoved;
        topKingMoved = bottomKingMoved;
        topLeftRookMoved = bottomRightRookMoved;
        topRightRookMoved = bottomLeftRookMoved;
        bottomKingMoved = oldTopKingMoved;
        bottomLeftRookMoved = oldTopRightRookMoved;
        bottomRightRookMoved = oldTopLeftRookMoved;

        isWhitePovGlobal = !isWhitePovGlobal;
        enPessantColumn = 7 - enPessantColumn;

        boardData = flippedBoardData;
        refreshBoard(flippedBoardData);

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