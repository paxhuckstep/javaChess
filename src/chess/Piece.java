package chess;

import java.util.List;

public abstract class Piece {
    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public String getSay() {
        return " Something uncoded";
    }

    public List<int[]> getCandidateMoves(int column, int row) {
        System.out.println("This pieces candidate moves haven't been coded");
        return null;
    }

}
