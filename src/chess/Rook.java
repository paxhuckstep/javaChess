package chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
        System.out.println("This is a Rook and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] straightDirections = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int[] direction : straightDirections) {
            int newColumn = column + direction[0];
            int newRow = row + direction[1];


            while (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidateMoves.add(new int[]{newColumn, newRow});
                newColumn += direction[0];
                newRow += direction[1];

            }

        }
        return candidateMoves;

    }

}
