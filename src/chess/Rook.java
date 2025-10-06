package chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int rookColumn, int rookRow) {
        List<int[]> candidateMoves = new ArrayList<>();

        int[][] straightDirections = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int[] straightDirection : straightDirections) {
            int moveColumn = rookColumn + straightDirection[0];
            int moveRow = rookRow + straightDirection[1];

            while (moveRow >= 0 && moveRow < 8 && moveColumn >= 0 && moveColumn < 8) {
                candidateMoves.add(new int[]{moveColumn, moveRow});

                moveColumn += straightDirection[0];
                moveRow += straightDirection[1];
            }
        }
        return candidateMoves;
    }
}
