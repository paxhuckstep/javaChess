package chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
//        System.out.println("This is a King and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] direction : allDirections) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];

            if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidateMoves.add(new int[]{newColumn, newRow});
//                newRow +=direction[0];
//                newColumn +=direction[1];
            }

        }
        return candidateMoves;

    }

    @Override
    public List<int[]> handleObstacles(int column, int row, Piece[][] boardData, List<int[]> candidateMoves) {
        List<int[]> noObstacles = super.handleObstacles(column, row, boardData, candidateMoves);

        boolean isTopKing = (row == 0);
        boolean isBottomKing = (row == 7);

        // Handle bottom king (row 7)
        if (isBottomKing && !ChessGUI.bottomKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // White POV (bottom king at col 4)
                // Short castle: king -> (6,7), requires cols 5,6 empty
                if (!ChessGUI.bottomRightRookMoved && boardData[5][7] == null && boardData[6][7] == null) {
                    noObstacles.add(new int[]{6, 7});
                }
                // Long castle: king -> (2,7), requires cols 1,2,3 empty
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null && boardData[3][7] == null) {
                    noObstacles.add(new int[]{2, 7});
                }
            } else { // Black POV (bottom king at col 3)
                // Short castle: king -> (1,7), requires cols 1,2 empty
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null) {
                    noObstacles.add(new int[]{1, 7});
                }
                // Long castle: king -> (5,7), requires cols 4,5,6 empty
                if (!ChessGUI.bottomRightRookMoved && boardData[4][7] == null && boardData[5][7] == null && boardData[6][7] == null) {
                    noObstacles.add(new int[]{5, 7});
                }
            }
        }

        // Handle top king (row 0)
        else if (isTopKing && !ChessGUI.topKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // White POV (top king at col 4)
                // Short castle: king -> (6,0), requires cols 5,6 empty
                if (!ChessGUI.topRightRookMoved && boardData[5][0] == null && boardData[6][0] == null) {
                    noObstacles.add(new int[]{6, 0});
                }
                // Long castle: king -> (2,0), requires cols 1,2,3 empty
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null && boardData[3][0] == null) {
                    noObstacles.add(new int[]{2, 0});
                }
            } else { // Black POV (top king at col 3)
                // Short castle: king -> (1,0), requires cols 1,2 empty
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null) {
                    noObstacles.add(new int[]{1, 0});
                }
                // Long castle: king -> (5,0), requires cols 4,5,6 empty
                if (!ChessGUI.topRightRookMoved && boardData[4][0] == null && boardData[5][0] == null && boardData[6][0] == null) {
                    noObstacles.add(new int[]{5, 0});
                }
            }
        }

        return noObstacles;
    }

    @Override
    public List<int[]> handleNoSelfChecks(int column, int row, Piece[][] boardData, List<int[]> noObstacles) {
        // will be dontWalkInToCheck method

        return noObstacles;
    }


}
