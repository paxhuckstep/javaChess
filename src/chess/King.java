package chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int kingColumn, int kingRow) {
        List<int[]> candidateMoves = new ArrayList<>();

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] direction : allDirections) {
            int moveRow = kingRow + direction[0];
            int moveColumn = kingColumn + direction[1];
            if (moveRow >= 0 && moveRow < 8 && moveColumn >= 0 && moveColumn < 8) {
                candidateMoves.add(new int[]{moveColumn, moveRow});
            }
        }
        return candidateMoves;
    }

    @Override
    public List<int[]> handleObstacles(int kingColumn, int kingRow, Piece[][] boardData, List<int[]> candidateMoves) {
        //adds maybeCanCastle Squares. the pov changes things a lot
        List<int[]> maybeCanCastle = super.handleObstacles(kingColumn, kingRow, boardData, candidateMoves);

        boolean isTopKing = (kingRow == 0);
        boolean isBottomKing = (kingRow == 7);

        // handles Bottom King
        if (isBottomKing && !ChessGUI.bottomKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // Bottom King && White POV
                if (!ChessGUI.bottomRightRookMoved && boardData[5][7] == null && boardData[6][7] == null) {
                    maybeCanCastle.add(new int[]{6, 7});
                }
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null && boardData[3][7] == null) {
                    maybeCanCastle.add(new int[]{2, 7});
                }
            } else { // Bottom King && Black POV
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null) {
                    maybeCanCastle.add(new int[]{1, 7});
                }
                if (!ChessGUI.bottomRightRookMoved && boardData[4][7] == null && boardData[5][7] == null && boardData[6][7] == null) {
                    maybeCanCastle.add(new int[]{5, 7});
                }
            }
        }
        // Handle top king
        else if (isTopKing && !ChessGUI.topKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // Top King && White POV
                if (!ChessGUI.topRightRookMoved && boardData[5][0] == null && boardData[6][0] == null) {
                    maybeCanCastle.add(new int[]{6, 0});
                }
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null && boardData[3][0] == null) {
                    maybeCanCastle.add(new int[]{2, 0});
                }
            } else { // Top King && Black POV
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null) {
                    maybeCanCastle.add(new int[]{1, 0});
                }
                if (!ChessGUI.topRightRookMoved && boardData[4][0] == null && boardData[5][0] == null && boardData[6][0] == null) {
                    maybeCanCastle.add(new int[]{5, 0});
                }
            }
        }
        return maybeCanCastle;
    }

    @Override
    public List<int[]> handleNoSelfChecks(int kingColumn, int kingRow, Piece[][] boardData, List<int[]> noObstacles) {
       // King doesn't walk in to check logic
        List<int[]> safeMoves = new ArrayList<>();
        for (int[] noObstacle : noObstacles) {

            int moveColumn = noObstacle[0];
            int moveRow = noObstacle[1];

            // make a simulated board, so we can make king invisible (won't block attacker's vision itself)
            Piece[][] simulatedBoard = new Piece[8][8];
            for (int c = 0; c < 8; c++) {
                System.arraycopy(boardData[c], 0, simulatedBoard[c], 0, 8);
            }
            simulatedBoard[kingColumn][kingRow] = null;

            // See if square is safe
            boolean isSquareSeen = getIsSquareSeen(moveColumn, moveRow, !this.getIsWhite(), simulatedBoard);
            boolean isCastleThroughCheck = false;
            if (Math.abs(moveColumn - kingColumn) == 2) { // little extra to prevent castling through check
                isCastleThroughCheck = getIsSquareSeen((moveColumn + kingColumn) / 2, moveRow, !this.getIsWhite(), simulatedBoard);
            }

            //  if not seen, add to safeMoves
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

