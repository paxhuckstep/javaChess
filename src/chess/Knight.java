package chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int knightColumn, int knightRow) {
//        System.out.println("This is a knight and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] knightHops = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
        };

        for (int[] thisHop : knightHops) {
            int moveColumn = knightColumn + thisHop[0];
            int moveRow = knightRow + thisHop[1];


            if (moveColumn >= 0 && moveColumn < 8 && moveRow >= 0 && moveRow < 8) {
                candidateMoves.add(new int[]{moveColumn, moveRow});
            }
        }
        return candidateMoves;

    }

}
