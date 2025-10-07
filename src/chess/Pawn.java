package chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int pawnColumn, int pawnRow) {
        List<int[]> candidateMoves = new ArrayList<>();

        int upOrDown = (getIsWhite() == ChessGUI.isWhitePovGlobal) ? -1 : 1;

        // one square forward
        int inFront = pawnRow + upOrDown;
        if (inFront >= 0 && inFront < 8) {
            candidateMoves.add(new int[]{pawnColumn, inFront});

            // double jumps
            int startingRow = (getIsWhite() == ChessGUI.isWhitePovGlobal) ? 6 : 1;

            if (pawnRow == startingRow) {
                int doubleInFront = pawnRow + (2 * upOrDown);
                candidateMoves.add(new int[]{pawnColumn, doubleInFront});
            }

            // possible captures
            int[] eitherSide = {pawnColumn - 1, pawnColumn + 1};
            for (int side : eitherSide) {
                if (side >= 0 && side < 8) {
                    candidateMoves.add(new int[]{side, inFront});
                }
            }
        }
        return candidateMoves;
    }

    @Override // only time this gets overwritten completely!
    public List<int[]> handleObstacles(int pawnColumn, int pawnRow, Piece[][] boardData, List<int[]> candidateMoves) {
        List<int[]> noObstacles = new ArrayList<>();

        for (int[] candidateMove : candidateMoves) {
            int moveColumn = candidateMove[0];
            int moveRow = candidateMove[1];
            Piece targetSquarePiece = boardData[moveColumn][moveRow];

            // Forward squares must be free
            if (moveColumn == pawnColumn) {
                if (targetSquarePiece == null) {
                    // double jump: check both squares are clear
                    if (Math.abs(moveRow - pawnRow) == 2) {
                        if (boardData[moveColumn][(pawnRow + moveRow) / 2] == null) {
                            noObstacles.add(candidateMove);
                        }
                    } else {
                        noObstacles.add(candidateMove);
                    }
                }
            } else if ( // Diagonal captures:
                        // enemy piece
                    targetSquarePiece != null && targetSquarePiece.getIsWhite() != this.getIsWhite()

                            //en pessant
                            || ((candidateMove[0] == ChessGUI.enPessantColumn)
                            && (pawnRow == (ChessGUI.isWhitePovGlobal == this.getIsWhite() ? 3 : 4) ) )) {
                noObstacles.add(candidateMove);
            }
        }
        return noObstacles;
    }
}
