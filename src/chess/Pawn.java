package chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
//        System.out.println("This is a Pawn and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int upOrDown = getIsWhite() ? -1 : 1;
//        if(upOrDown == 1) {
//            System.out.println("It's going down??");
//        } else {
//            System.out.println("It's going up?");
//        }

        // one move forward
        int inFront = row + upOrDown;
        if (inFront >= 0 && inFront < 8) {
            candidateMoves.add(new int[]{column, inFront});

            // double jumps
            if ((getIsWhite() && row == 6) || (!getIsWhite() && row == 1)) {
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

}
