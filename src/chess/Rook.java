package chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSay() {
        return " Rook I fuck shit";
    }

    @Override
    public List<int[]> getCandidateMoves(int row, int column) {
        System.out.println("This is a Rook and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] straightDirections = {
                {-1, 0},  // up
                {1, 0},   // down
                {0, -1},  // left
                {0, 1}    // right
        };

        for (int[] direction : straightDirections) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];

            while (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidateMoves.add(new int[]{newRow, newColumn});
                newRow +=direction[0];
                newColumn +=direction[1];
            }

        }
        return candidateMoves;

    }

}
