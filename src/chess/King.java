package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int kingColumn, int kingRow) {
//        System.out.println("This is a King and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] directionection : allDirections) {
            int newRow = kingRow + directionection[0];
            int newColumn = kingColumn + directionection[1];

            if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidateMoves.add(new int[]{newColumn, newRow});
            }

        }
        return candidateMoves;
    }

    @Override
    public List<int[]> handleObstacles(int kingColumn, int kingRow, Piece[][] boardData, List<int[]> candidateMoves) {
        List<int[]> noObstacles = super.handleObstacles(kingColumn, kingRow, boardData, candidateMoves);

        boolean isTopKing = (kingRow == 0);
        boolean isBottomKing = (kingRow == 7);

        // Handle bottom king (row 7)
        if (isBottomKing && !ChessGUI.bottomKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // White POV (bottom king at column 4)
                // Short castle: king -> (6,7), requires columns 5,6 empty
                if (!ChessGUI.bottomRightRookMoved && boardData[5][7] == null && boardData[6][7] == null) {
                    noObstacles.add(new int[]{6, 7});
                }
                // Long castle: king -> (2,7), requires columns 1,2,3 empty
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null && boardData[3][7] == null) {
                    noObstacles.add(new int[]{2, 7});
                }
            } else { // Black POV (bottom king at column 3)
                // Short castle: king -> (1,7), requires columns 1,2 empty
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null) {
                    noObstacles.add(new int[]{1, 7});
                }
                // Long castle: king -> (5,7), requires columns 4,5,6 empty
                if (!ChessGUI.bottomRightRookMoved && boardData[4][7] == null && boardData[5][7] == null && boardData[6][7] == null) {
                    noObstacles.add(new int[]{5, 7});
                }
            }
        }

        // Handle top king (row 0)
        else if (isTopKing && !ChessGUI.topKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // White POV (top king at column 4)
                // Short castle: king -> (6,0), requires columns 5,6 empty
                if (!ChessGUI.topRightRookMoved && boardData[5][0] == null && boardData[6][0] == null) {
                    noObstacles.add(new int[]{6, 0});
                }
                // Long castle: king -> (2,0), requires columns 1,2,3 empty
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null && boardData[3][0] == null) {
                    noObstacles.add(new int[]{2, 0});
                }
            } else { // Black POV (top king at column 3)
                // Short castle: king -> (1,0), requires columns 1,2 empty
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null) {
                    noObstacles.add(new int[]{1, 0});
                }
                // Long castle: king -> (5,0), requires columns 4,5,6 empty
                if (!ChessGUI.topRightRookMoved && boardData[4][0] == null && boardData[5][0] == null && boardData[6][0] == null) {
                    noObstacles.add(new int[]{5, 0});
                }
            }
        }
        return noObstacles;
    }

    @Override
    public List<int[]> handleNoSelfChecks(int kingColumn, int kingRow, Piece[][] boardData, List<int[]> noObstacles) {
        List<int[]> safeMoves = new ArrayList<>();

        for (int[] noObstacle : noObstacles) {

            int targetColumn = noObstacle[0];
            int targetRow = noObstacle[1];

            // Step 1: make a simulated board for this move
            Piece[][] simulatedBoard = new Piece[8][8];
            for (int c = 0; c < 8; c++) {
                for (int r = 0; r < 8; r++) {
                    simulatedBoard[c][r] = boardData[c][r];
                }
            }
            simulatedBoard[kingColumn][kingRow] = null;
//            simulatedBoard[targetColumn][targetRow] = this; // place king in new square

            // Step 2: check if that square is seen by any enemy piece
            boolean isSquareSeen = getIsSquareSeen(targetColumn, targetRow, !this.getIsWhite(), simulatedBoard);
            boolean isCastleThroughCheck = false;
            if (Math.abs(targetColumn - kingColumn) == 2) {
                isCastleThroughCheck = getIsSquareSeen((targetColumn + kingColumn) / 2, targetRow, !this.getIsWhite(), simulatedBoard);
            }

            // Step 3: if not seen, add to safeMoves
            if (!isSquareSeen && !isCastleThroughCheck) {
                safeMoves.add(noObstacle);
            }
        }
        return safeMoves;
    }

    @Override // is functional without this override, just does some useless work.
    public List<int[]> handleBlocksCheck(int pieceColumn, int pieceRow, Piece[][] boardData, List<int[]> noSelfCheckMoves) {
        return noSelfCheckMoves;
    }
}

