package chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int bishopColumn, int bishopRow) {
        List<int[]> candidateMoves = new ArrayList<>();

        int[][] diagonalDirections = {
                {-1, -1}, {-1, 1},
                {1, -1},  {1, 1}
        };

        for (int[] diagonalDirection : diagonalDirections) {
            int moveRow = bishopRow + diagonalDirection[0];
            int moveColumn = bishopColumn + diagonalDirection[1];

            while (moveRow >= 0 && moveRow < 8 && moveColumn >= 0 && moveColumn < 8) {
                candidateMoves.add(new int[]{moveColumn, moveRow});
                moveRow +=diagonalDirection[0];
                moveColumn +=diagonalDirection[1];
            }
        }
        return candidateMoves;
    }
}
