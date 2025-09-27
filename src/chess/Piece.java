package chess;

public abstract class Piece {
    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

//    public abstract List<Square> generateMoveCandidates(int startRow, int startColumn);
//
//    public boolean isPathClear(int startRow, int startColumn, int endRow, int endColumn) {
//        int rowStep = Integer.compare(endRow, startRow);       // +1, 0, or -1
//        int colStep = Integer.compare(endColumn, startColumn); // +1, 0, or -1
//
//        int currentRow = startRow + rowStep;
//        int currentColumn = startColumn + colStep;
//
//        while (currentRow != endRow || currentColumn != endColumn) {
//            if (squares[currentRow][currentColumn] != null) { // square is occupied
//                return false;
//            }
//            currentRow += rowStep;
//            currentColumn += colStep;
//        }
//
//        return true; // path is clear
//    }


//    public abstract boolean canMove(int startRow, int startColumn, int endRow, int endColumn);

}
