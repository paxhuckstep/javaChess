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
    public List<int[]> handleIsPinnedToKing(int column, int row, Piece[][] boardData, List<int[]> noObstacles) {
        return noObstacles;
    }


}
