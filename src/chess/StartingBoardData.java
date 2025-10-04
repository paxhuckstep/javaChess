package chess;

public class StartingBoardData {
    private static Piece[][] startingBoardData = new Piece[8][8];

    public static Piece[][] getStartingBoardData(boolean isWhitePov) {
        startingBoardData[0][0] = new Rook(!isWhitePov);
        startingBoardData[7][0] = new Rook(!isWhitePov);
        startingBoardData[1][0] = new Knight(!isWhitePov);
        startingBoardData[6][0] = new Knight(!isWhitePov);
        startingBoardData[2][0] = new Bishop(!isWhitePov);
        startingBoardData[5][0] = new Bishop(!isWhitePov);


        for (int i = 0; i < 8; i++) {
            startingBoardData[i][1] = new Pawn(!isWhitePov);
            startingBoardData[i][6] = new Pawn(isWhitePov);
        }

        startingBoardData[0][7] = new Rook(isWhitePov);
        startingBoardData[7][7] = new Rook(isWhitePov);
        startingBoardData[1][7] = new Knight(isWhitePov);
        startingBoardData[6][7] = new Knight(isWhitePov);
        startingBoardData[2][7] = new Bishop(isWhitePov);
        startingBoardData[5][7] = new Bishop(isWhitePov);

        startingBoardData[3][0] = isWhitePov ? new Queen(false) : new King(true);
        startingBoardData[4][0] = isWhitePov ? new King(false) : new Queen(true);
        startingBoardData[3][7] = isWhitePov ? new Queen(true) : new King(false);
        startingBoardData[4][7] = isWhitePov ? new King(true) : new Queen(false);


        return startingBoardData;
    }
}
