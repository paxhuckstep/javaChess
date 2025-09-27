//package chess;
//
//public class Board {
//    private Piece[][] squares;
//
//    public Board() {
//        squares = new Piece[8][8];
//    }
//
//    public Piece getPiece(int row, int column) {
//        return squares[row][column];
//    }
//
//    public void setPiece(int row, int column, Piece piece) {
//        squares[row][column] = piece;
//    }
//
//    /**
//     * Checks if all squares between start and end are empty.
//     * Does NOT check the start or end squares themselves.
//     */
//    public boolean isPathClear(int startRow, int startColumn, int endRow, int endColumn) {
//        int rowStep = Integer.compare(endRow, startRow);         // +1, 0, or -1
//        int columnStep = Integer.compare(endColumn, startColumn); // +1, 0, or -1
//
//        int currentRow = startRow + rowStep;
//        int currentColumn = startColumn + columnStep;
//
//        while (currentRow != endRow || currentColumn != endColumn) {
//            if (squares[currentRow][currentColumn] != null) {
//                return false; // something is blocking the path
//            }
//            currentRow += rowStep;
//            currentColumn += columnStep;
//        }
//
//        return true; // no obstacles
//    }
//}
