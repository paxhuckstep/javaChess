package chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSay() {
        return " Neigh I jump like L";
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
        System.out.println("This is a knight and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] knightHops = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
        };

        for (int[] thisHop : knightHops) {
            int newRow = row + thisHop[0];
            int newColumn = column + thisHop[1];

            if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidateMoves.add(new int[]{newRow, newColumn});
            }
        }
        return candidateMoves;

    }

}
