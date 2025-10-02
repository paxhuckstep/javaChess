package chess;

import java.util.List;
import java.util.ArrayList;

public abstract class Piece {
    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean getIsWhite() {
        return this.isWhite;
    }

    public List<int[]> getCandidateMoves(int column, int row) {
        System.out.println("This pieces candidate moves haven't been coded");
        return null;
    }

    // needs to be overwritten for pawns and maybe kings, works for bishop, rook, knight, queen
    public List<int[]> handleObstacles(int column, int row, Piece[][] boardData, List<int[]> candidateMoves) {

        List<int[]> blockedSquares = new ArrayList<>();
        List<int[]> noObstacles = new ArrayList<>();

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] direction : allDirections) {
            int newColumn = column + direction[0];
            int newRow = row + direction[1];

            boolean isBlocked = false;

            while (newColumn >= 0 && newColumn < 8 && newRow >= 0 && newRow < 8) {
                Piece blocker = boardData[newColumn][newRow];
                if (blocker != null && !isBlocked) {
                    isBlocked = true;
                } else if (isBlocked) {
                    blockedSquares.add(new int[]{newColumn, newRow});
                }
                newColumn += direction[0];
                newRow += direction[1];

            }

        }

        for (int[] possibleMove : candidateMoves) {
            boolean isBlocked = false;
            for (int[] blocked : blockedSquares) {
                if (possibleMove[0] == blocked[0] && possibleMove[1] == blocked[1]) {
                    isBlocked = true;
                    break;
                }
            }
            if (!isBlocked) {
                noObstacles.add(possibleMove);
            }
        }

        return noObstacles;

    }

}
