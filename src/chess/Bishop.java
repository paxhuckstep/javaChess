package chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
//        System.out.println("This is a bishop and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] diagonalDirections = {
                {-1, -1}, {-1, 1},
                {1, -1},  {1, 1}
        };

        for (int[] direction : diagonalDirections) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];

            while (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidateMoves.add(new int[]{newColumn, newRow});
                newRow +=direction[0];
                newColumn +=direction[1];
            }

        }
        return candidateMoves;

    }

}
