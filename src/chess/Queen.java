package chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int row, int column) {
        List<int[]> candidateMoves = new ArrayList<>();

        Rook queenStraights = new Rook(this.isWhite);
        candidateMoves.addAll(queenStraights.getCandidateMoves(row, column));

        Bishop queenDiagonals = new Bishop(this.isWhite);
        candidateMoves.addAll(queenDiagonals.getCandidateMoves(row, column));

        return candidateMoves;
    }

}
