package chess;

public class StartingBoardData {
    private static Piece[][] startingBoardData = new Piece[8][8];

    public static Piece[][] getStartingBoardData(boolean isWhitePov) {
        startingBoardData[0][0] = new Rook(!isWhitePov);
        startingBoardData[0][7] = new Rook(!isWhitePov);
        startingBoardData[0][1] = new Knight(!isWhitePov);
        startingBoardData[0][6] = new Knight(!isWhitePov);
        startingBoardData[0][2] = new Bishop(!isWhitePov);
        startingBoardData[0][5] = new Bishop(!isWhitePov);
        startingBoardData[0][3] = new Queen(!isWhitePov);
        startingBoardData[0][4] = new King(!isWhitePov);

        for (int i = 0; i < 8; i++) {
            startingBoardData[1][i] = new Pawn(!isWhitePov);
            startingBoardData[6][i] = new Pawn(isWhitePov);
        }

        startingBoardData[7][0] = new Rook(isWhitePov);
        startingBoardData[7][7] = new Rook(isWhitePov);
        startingBoardData[7][1] = new Knight(isWhitePov);
        startingBoardData[7][6] = new Knight(isWhitePov);
        startingBoardData[7][2] = new Bishop(isWhitePov);
        startingBoardData[7][5] = new Bishop(isWhitePov);
        startingBoardData[7][3] = new Queen(isWhitePov);
        startingBoardData[7][4] = new King(isWhitePov);


        return startingBoardData;
    }
}
