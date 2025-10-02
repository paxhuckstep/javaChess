package chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
        List<int[]> candidateMoves = new ArrayList<>();

        Rook queenStraights = new Rook(this.isWhite);
        candidateMoves.addAll(queenStraights.getCandidateMoves(column, row));

        Bishop queenDiagonals = new Bishop(this.isWhite);
        candidateMoves.addAll(queenDiagonals.getCandidateMoves(column, row));

        return candidateMoves;
    }

}
