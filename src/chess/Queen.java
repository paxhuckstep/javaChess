package chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int queenColumn, int queenRow) {
        List<int[]> candidateMoves = new ArrayList<>();

        Rook queenStraights = new Rook(this.isWhite);
        candidateMoves.addAll(queenStraights.getCandidateMoves(queenColumn, queenRow));

        Bishop queenDiagonals = new Bishop(this.isWhite);
        candidateMoves.addAll(queenDiagonals.getCandidateMoves(queenColumn, queenRow));

        return candidateMoves;
    }

}
