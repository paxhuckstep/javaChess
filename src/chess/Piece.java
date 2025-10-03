package chess;

import java.util.List;
import java.util.ArrayList;

public abstract class Piece {
    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean getIsWhite() {
        return this.isWhite;
    }

    public List<int[]> getCandidateMoves(int column, int row) {
        System.out.println("This pieces candidate moves haven't been coded");
        return null;
    }

    // needs to be overwritten for pawns and maybe kings, works for bishop, rook, knight, queen
    public List<int[]> handleObstacles(int column, int row, Piece[][] boardData, List<int[]> candidateMoves) {

        List<int[]> blockedSquares = new ArrayList<>();
        List<int[]> noObstacles = new ArrayList<>();

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] direction : allDirections) {
            int newColumn = column + direction[0];
            int newRow = row + direction[1];

            boolean isBlocked = false;

            while (newColumn >= 0 && newColumn < 8 && newRow >= 0 && newRow < 8) {
                Piece potentialPinner = boardData[newColumn][newRow];
                if (potentialPinner != null && !isBlocked) {
                    isBlocked = true;
                } else if (isBlocked) {
                    blockedSquares.add(new int[]{newColumn, newRow});
                }
                newColumn += direction[0];
                newRow += direction[1];

            }

        }

        for (int[] possibleMove : candidateMoves) {
            boolean isBlocked = false;
            for (int[] blocked : blockedSquares) {
                if (possibleMove[0] == blocked[0] && possibleMove[1] == blocked[1]) {
                    isBlocked = true;
                    break;
                }
            }
            if (!isBlocked) {
                Piece maybeCapture = boardData[possibleMove[0]][possibleMove[1]];
                if (maybeCapture == null || maybeCapture.getIsWhite() != this.getIsWhite()) {
                    noObstacles.add(possibleMove);
                }


            }
        }

        return noObstacles;

    }

    public List<int[]> handleIsPinnedToKing(int column, int row, Piece[][] boardData, List<int[]> noObstacles) {
        List<int[]> pinHandledMoves = new ArrayList<>();

        // Step 1: find my King
        int myKingColumn = -1, myKingRow = -1;
        for (int columnCheck = 0; columnCheck < 8; columnCheck++) {
            for (int rowCheck = 0; rowCheck < 8; rowCheck++) {
                Piece maybeKing = boardData[columnCheck][rowCheck];
                if (maybeKing != null && maybeKing instanceof King && maybeKing.getIsWhite() == this.getIsWhite()) {
                    myKingColumn = columnCheck;
                    myKingRow = rowCheck;
                }
            }
        }
        if (myKingColumn == -1) { //Shouldn't happen but who knows
            System.out.println("Can't Find King");
            return noObstacles; // no king found
        }

        // Step 2: check alignment with King
        int pieceKingColumnOffset = myKingColumn - column;
        int pieceKingRowOffset = myKingRow - row;
        boolean aligned = (pieceKingColumnOffset == 0 || pieceKingRowOffset == 0 || Math.abs(pieceKingColumnOffset) == Math.abs(pieceKingRowOffset));
        if (!aligned) {
            return noObstacles; // not even aligned, impossible to pin
        }

        // Step 3: scan from piece
        int columnDirection = Integer.compare(0, pieceKingColumnOffset); // 1, 0, or -1
        int rowDirection = Integer.compare(0, pieceKingRowOffset); // 1, 0, or -1

        int scanColumn = column + columnDirection;
        int scanRow = row + rowDirection;

        boolean isKingClosest = false;
        boolean isPinnerClosest = false;

        while (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
            Piece potentialPinner = boardData[scanColumn][scanRow];
            if (potentialPinner != null) {
                if (potentialPinner.getIsWhite() != this.getIsWhite()) {
                    if ((columnDirection == 0 || rowDirection == 0) && (potentialPinner instanceof Rook || potentialPinner instanceof Queen)) {
                        isPinnerClosest = true;
//                        System.out.println("Rooky Pin Possible");
                    }
                    if (Math.abs(columnDirection) == 1 && Math.abs(rowDirection) == 1 && (potentialPinner instanceof Bishop || potentialPinner instanceof Queen)) {
                        isPinnerClosest = true;
//                        System.out.println("Bishopy Pin possible");
                    }
                }
                break;
            }
            scanColumn += columnDirection;
            scanRow += rowDirection;
        }

        scanColumn = column - columnDirection;
        scanRow = row - rowDirection;

        while (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
            Piece maybeKing = boardData[scanColumn][scanRow];
            if (maybeKing != null) {
                if (maybeKing.getIsWhite() == this.getIsWhite() && maybeKing instanceof King) {
                    isKingClosest = true;
//                        System.out.println("King Pin possible");
                }
                break;
            }
            scanColumn -= columnDirection;
            scanRow -= rowDirection;
        }

        // Step 4: filter legal moves (only along the actual pin line)
        if (isKingClosest && isPinnerClosest) {
            for (int[] move : noObstacles) {
                int newColumn = move[0], newRow = move[1];
                int moveKingColumnOffset = newColumn - myKingColumn;
                int moveKingRowOffset = newRow - myKingRow;

                boolean allowed = false;

                if (columnDirection == 0 && rowDirection != 0) {
                    // vertical pin
                    allowed = (newColumn == myKingColumn);
                } else if (rowDirection == 0 && columnDirection != 0) {
                    // horizontal pin
                    allowed = (newRow == myKingRow);
                } else {
                    // diagonal pin
                    if (Math.abs(pieceKingColumnOffset) == Math.abs(pieceKingRowOffset) &&
                            Math.abs(moveKingColumnOffset) == Math.abs(moveKingRowOffset)) {
                        int moveColumnDirection = Integer.compare(moveKingColumnOffset, 0);
                        int moveRowDirection = Integer.compare(moveKingRowOffset, 0);
                        // either direction along the same diagonal
                        allowed = ((moveColumnDirection == columnDirection && moveRowDirection == rowDirection) ||
                                (moveColumnDirection == -columnDirection && moveRowDirection == -rowDirection));
                    }
                }
                if (allowed) pinHandledMoves.add(move);
            }
            return pinHandledMoves;
        }
        return noObstacles;
    }
}
