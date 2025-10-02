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

    public List<int[]> getCandidateMoves(int row, int column) {
        System.out.println("This pieces candidate moves haven't been coded");
        return null;
    }

    // needs to be overwritten for pawns and maybe kings, works for bishop, rook, knight, queen
    public List<int[]> handleObstacles(int row, int column, Piece[][] boardData, List<int[]> candidateMoves) {

        List<int[]> blockedSquares = new ArrayList<>();
        List<int[]> noObstacles = new ArrayList<>();

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] direction : allDirections) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];
            boolean isBlocked = false;

            while (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                Piece blocker = boardData[newRow][newColumn];
                if (blocker != null && !isBlocked) {
                    isBlocked = true;
                } else if (isBlocked) {
                    blockedSquares.add(new int[]{newRow, newColumn});
                }
                newRow += direction[0];
                newColumn += direction[1];
            }

        }

        for (int[] move : candidateMoves) {
            boolean isBlocked = false;
            for (int[] blocked : blockedSquares) {
                if (move[0] == blocked[0] && move[1] == blocked[1]) {
                    isBlocked = true;
                    break;
                }
            }
            if (!isBlocked) {
                noObstacles.add(move);
            }
        }

        return noObstacles;

    }

}
