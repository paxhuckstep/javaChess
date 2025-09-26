package chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Square> generateMoveCandidates(int startRow, int startColumn) {
        List<Square> candidates = new ArrayList<>();
        int[][] moves = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        for (int[] move : moves) {
            int newRow = startRow + move[0];
            int newColumn = startColumn + move[1];

            // make sure the square is on the board
            if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidates.add(new Square(newRow, newColumn));
            }
        }

        return candidates;
    }
}
