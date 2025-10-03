package chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
//        System.out.println("This is a Pawn and candidate possibleMoves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int upOrDown = (getIsWhite() == ChessGUI.isWhitePovGlobal) ? -1 : 1;

//        if(upOrDown == 1) {
//            System.out.println("It's going down??");
//        } else {
//            System.out.println("It's going up?");
//        }

        // one possibleMove forward
        int inFront = row + upOrDown;
        if (inFront >= 0 && inFront < 8) {
            candidateMoves.add(new int[]{column, inFront});

            // double jumps
            int startingRow = (getIsWhite() == ChessGUI.isWhitePovGlobal) ? 6 : 1;

            if (row == startingRow) {
                int doubleInFront = row + (2 * upOrDown);
                candidateMoves.add(new int[]{column, doubleInFront});
            }

            // possible captures
            int[] eitherSide = {column - 1, column + 1};
            for (int side : eitherSide) {
                if (inFront >= 0 && inFront < 8 && side >= 0 && side < 8) {
                    candidateMoves.add(new int[]{side, inFront});
                }
            }
        }
        return candidateMoves;
    }

    @Override
    public List<int[]> handleObstacles(int column, int row, Piece[][] boardData, List<int[]> candidateMoves) {
        List<int[]> noObstacles = new ArrayList<>();

        for (int[] possibleMove : candidateMoves) {
            int newColumn = possibleMove[0];
            int newRow = possibleMove[1];
            Piece target = boardData[newColumn][newRow];

            // Forward possibleMoves: must be empty
            if (newColumn == column) {
                if (target == null) {
                    // double jump: check both squares are clear
                    if (Math.abs(newRow - row) == 2) {
                        int midRow = (row + newRow) / 2;
                        if (boardData[newColumn][midRow] == null) {
                            noObstacles.add(possibleMove);
                        }
                    } else {
                        noObstacles.add(possibleMove);
                    }
                }
            }
            // Diagonal captures: must be enemy piece
            else if (target != null && target.getIsWhite() != this.getIsWhite()) {
                noObstacles.add(possibleMove);
            }
        }
        return noObstacles;
    }

}
