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
        if (myKingColumn == -1) {
            System.out.println("Can't Find King");
            return noObstacles; // no king found
        }

        // Step 2: check alignment with King
        int columnGap = myKingColumn - column;
        int rowGap = myKingRow - row;
        boolean aligned = (columnGap == 0 || rowGap == 0 || Math.abs(columnGap) == Math.abs(rowGap));
        if (!aligned) {
            System.out.println("Not even aligned!");
            return noObstacles; // not pinned
        }

        // Step 3: scan past the king
        int columnStep = Integer.compare(0, columnGap); // 1, 0, or -1
        int rowStep = Integer.compare(0, rowGap); // 1, 0, or -1

        int scanColumn = column + columnStep;
        int scanRow = row + rowStep;

        boolean pinFound = false;
        while (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
            Piece potentialPinner = boardData[scanColumn][scanRow];
            if (potentialPinner != null) {
                if (potentialPinner.getIsWhite() != this.getIsWhite()) {
                    if ((columnStep == 0 || rowStep == 0) && (potentialPinner instanceof Rook || potentialPinner instanceof Queen)) {
                        pinFound = true;
                        System.out.println("Rooky Pin Found");
                    }
                    if (Math.abs(columnStep) == 1 && Math.abs(rowStep) == 1 && (potentialPinner instanceof Bishop || potentialPinner instanceof Queen)) {
                        pinFound = true;
                        System.out.println("Bishopy Pin Found");
                    }
                }
                break;
            }
            scanColumn += columnStep;
            scanRow += rowStep;
        }

        // Step 4: filter legal moves
        if (pinFound) {
            for (int[] move : noObstacles) {
                int newColumn = move[0];
                int newRow = move[1];
                int moveColumnDiff = newColumn - myKingColumn;
                int moveRowDiff = newRow - myKingRow;

                if ((columnGap == 0 && newColumn == myKingColumn) ||
                        (rowGap == 0 && newRow == myKingRow) ||
                        (Math.abs(moveColumnDiff) == Math.abs(moveRowDiff))) {
                    pinHandledMoves.add(move);
                }
            }
            return pinHandledMoves;
        }

        return noObstacles;
    }


}
