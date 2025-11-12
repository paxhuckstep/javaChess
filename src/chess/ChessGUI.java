package chess;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class ChessGUI extends JPanel {
    private final List<String> moveHistory = new ArrayList<>();
    private ChessSquareButton[][] boardButtons = new ChessSquareButton[8][8];
    private Piece[][] boardData;
    private boolean isWhiteTurn = true;
    public static boolean isWhitePovGlobal;
    private static Piece lastClickedPiece = null;
    public static int[] lastClickedSquare = new int[2];
    public static int enPessantColumn = -1;
    private static int halfMoveCount;

    public static boolean topLeftRookMoved = false;
    public static boolean topRightRookMoved = false;
    public static boolean bottomLeftRookMoved = false;
    public static boolean bottomRightRookMoved = false;
    public static boolean topKingMoved = false;
    public static boolean bottomKingMoved = false;

    private static int lineNumber;
    public static boolean isFreePlay = false;
    public static boolean isDrill = false;

    public ChessGUI(Color lightSquareColor, Color darkSquareColor, boolean isWhitePov, String mode, String openingName) {
        ChessGUI.isWhitePovGlobal = isWhitePov;
        setSize(600, 600);
        setLayout(new GridLayout(8, 8));
        boardData = StartingBoardData.getStartingBoardData(isWhitePov);

        if (mode.equals("freePlay")) {
            //free play
            isFreePlay = true;
            System.out.println("We are in freePlay Mode");
        } else if (mode.equals("addLine")) {
            //add line

            lineNumber = Database.getMaxLineNumber(openingName) + 1;
            System.out.println("We are in addLine mode, lineNumber is: " + lineNumber);
        } else if (mode.equals("drill")) {
//            drill
            isDrill = true;
            System.out.println("We are in drill mode!");
        }

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
                    if (boardButtons[c][r].getIsLegal()) { // We're moving a piece

                        //handle En Pessant capture
                        if (lastClickedPiece instanceof Pawn && lastClickedSquare[0] != c && boardData[c][r] == null) {
                            boardData[c][r - (isWhitePovGlobal == lastClickedPiece.getIsWhite() ? -1 : 1)] = null;
                        }
                        //"50 move rule" counter
                        if (lastClickedPiece instanceof Pawn || boardData[c][r] != null) {
                            halfMoveCount = 0;
                        } else {
                            halfMoveCount++;
                        }
                        //move piece
                        boardData[lastClickedSquare[0]][lastClickedSquare[1]] = null;
                        boardData[c][r] = lastClickedPiece;

                        int moveStartColumn = isWhitePovGlobal ? lastClickedSquare[0] : 7 - lastClickedSquare[0];
                        int moveStartRow = isWhitePovGlobal ? lastClickedSquare[1] : 7 - lastClickedSquare[1];
                        int moveEndCol   = isWhitePovGlobal ? c : 7 - c;
                        int moveEndRow   = isWhitePovGlobal ? r : 7 - r;

                        System.out.println("isFreePlay?: " + isFreePlay);
                        if (!isFreePlay) {
                            System.out.println("it isn't free play");
                            String moveNotation = String.format("%c%d to %c%d",
                                    'a' + moveStartColumn, 8 - moveStartRow,
                                    'a' + moveEndCol, 8 - moveEndRow);
                            System.out.println("moveNotation: " + moveNotation);
                            if (mode.equals("addLine")) {
                                System.out.println("It is addLine mode");
                                System.out.println("openingName: " + openingName + ".. lineNumber: " + lineNumber + "... isWhiteTurn: " + isWhiteTurn + "...moveNotation: " + moveNotation);

                                chess.Database.saveMoveToOpening(openingName, lineNumber, isWhiteTurn, moveNotation);
//                                System.out.println("openingName: " + openingName + ".. lineNumber: " + lineNumber + "... isWhiteTurn: " + isWhiteTurn + "...moveNotation: " + moveNotation);
                            }
                        moveHistory.add(moveNotation);
                            System.out.println("Move recorded: " + isWhiteTurn + " " + moveNotation);
                        }

                        System.out.println("Changes turns");
                        isWhiteTurn = !isWhiteTurn;


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
                            if (previousRow == 7) {
                                if (isWhitePovGlobal) {
                                    if (c == 6) {
                                        boardData[5][7] = boardData[7][7];
                                        boardData[7][7] = null;
                                        bottomRightRookMoved = true;
                                    } else if (c == 2) {
                                        boardData[3][7] = boardData[0][7];
                                        boardData[0][7] = null;
                                        bottomLeftRookMoved = true;
                                    }
                                } else {
                                    if (c == 1) {
                                        boardData[2][7] = boardData[0][7];
                                        boardData[0][7] = null;
                                        bottomLeftRookMoved = true;
                                    } else if (c == 5) {
                                        boardData[4][7] = boardData[7][7];
                                        boardData[7][7] = null;
                                        bottomRightRookMoved = true;
                                    }
                                }
                            } else if (previousRow == 0) {
                                if (isWhitePovGlobal) {
                                    if (c == 6) {
                                        boardData[5][0] = boardData[7][0];
                                        boardData[7][0] = null;
                                        topRightRookMoved = true;
                                    } else if (c == 2) {
                                        boardData[3][0] = boardData[0][0];
                                        boardData[0][0] = null;
                                        topLeftRookMoved = true;
                                    }
                                } else {
                                    if (c == 1) {
                                        boardData[2][0] = boardData[0][0];
                                        boardData[0][0] = null;
                                        topLeftRookMoved = true;
                                    } else if (c == 5) {
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

                        // repaint
                        refreshBoard(boardData);

                        // end of game check
                        if (isStalemate()) {
                            String endGameMessage;
                            if (isCheck()) {
                                if (isWhiteTurn) {
                                    endGameMessage = "Black Wins by Checkmate!";
                                } else {
                                    endGameMessage = "White Wins by Checkmate!";
                                }
                            } else {
                                endGameMessage = "Stalemate!";
                            }
                            JOptionPane.showMessageDialog(BigGUI.bigGuiReference, endGameMessage);
                        } else if (halfMoveCount == 100) {
                            JOptionPane.showMessageDialog(BigGUI.bigGuiReference, "Draw, 50 move rule");
                        }

                    } else { // Clicked non-legal square (Not moving a piece)
                        // Clean off old legal moves
                        for (ChessSquareButton[] cleanRow : boardButtons) {
                            for (ChessSquareButton button : cleanRow) {
                                button.setIsLegal(false);
                            }
                        }
                        // See if clicked piece
                        Piece clickedPiece = boardData[c][r];
                        if (clickedPiece != null && clickedPiece.getIsWhite() == isWhiteTurn && halfMoveCount < 100) { // #GetMoves
                            List<int[]> candidateMoves = clickedPiece.getCandidateMoves(c, r);
                            List<int[]> obstaclesHandled = clickedPiece.handleObstacles(c, r, boardData, candidateMoves);
                            List<int[]> noSelfChecks = clickedPiece.handleNoSelfChecks(c, r, boardData, obstaclesHandled);
                            List<int[]> legalMoves = clickedPiece.handleBlocksCheck(c, r, boardData, noSelfChecks);
                            // Show legal moves
                            for (int[] legalMove : legalMoves) {
                                boardButtons[legalMove[0]][legalMove[1]].setIsLegal(true);
                            }
                        }
                        // Store what piece we're showing legal moves for
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

    //update the ChessGUI with latest boardData
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

    public void resetGame() { // Start a new game
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
        isWhiteTurn = true;
        halfMoveCount = 0;

        boardData = resetBoardData;
        refreshBoard(resetBoardData);
    }

    public void flipBoard() { // change pov from white to black or black to white.
        Piece[][] flippedBoardData = new Piece[8][8];
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 8; row++) {
                flippedBoardData[column][row] = boardData[7 - column][7 - row];
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

    private boolean isStalemate() {
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                Piece piece = boardData[c][r];
                if (piece != null && piece.getIsWhite() == isWhiteTurn) {
                    List<int[]> candidateMoves = piece.getCandidateMoves(c, r);
                    List<int[]> obstaclesHandled = piece.handleObstacles(c, r, boardData, candidateMoves);
                    List<int[]> noSelfChecks = piece.handleNoSelfChecks(c, r, boardData, obstaclesHandled);
                    List<int[]> legalMoves = piece.handleBlocksCheck(c, r, boardData, noSelfChecks);
                    if (!legalMoves.isEmpty()) return false; // has a move → not stalemate
                }
            }
        }
        return true;
    }

    private boolean isCheck() { //distinguishes stalemate and checkmate
        int[] kingCoordinates = Piece.findMyKing(boardData, isWhiteTurn);
        if (kingCoordinates[0] == -1) return false; // no king found

        return Piece.getIsSquareSeen(kingCoordinates[0], kingCoordinates[1], !isWhiteTurn, boardData);
    }

    private void updateSquareIcon(ChessSquareButton square, Piece piece) { // show picture of piece on square
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