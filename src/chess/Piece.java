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

    public List<int[]> getCandidateMoves(int column, int row) {  // always overwritten
        return null;
    }

    // Is overwritten for pawns, supered for kings, works for bishop rook knight queen
    public List<int[]> handleObstacles(int column, int row, Piece[][] boardData, List<int[]> candidateMoves) {

        List<int[]> blockedSquares = new ArrayList<>();
        List<int[]> noObstacles = new ArrayList<>(); // returned at the end

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] direction : allDirections) {
            int moveColumn = column + direction[0];
            int moveRow = row + direction[1];

            boolean isBlocked = false;

            while (moveColumn >= 0 && moveColumn < 8 && moveRow >= 0 && moveRow < 8) {
                Piece potentialBlocker = boardData[moveColumn][moveRow];
                if (potentialBlocker != null && !isBlocked) {
                    isBlocked = true; // doesn't remove first blocker, instead checks team kill later
                } else if (isBlocked) {
                    blockedSquares.add(new int[]{moveColumn, moveRow}); //only added AFTER finding first blocker
                }
                moveColumn += direction[0];
                moveRow += direction[1];
            }
        }

        for (int[] candidateMove : candidateMoves) {
            boolean mightBeAdded = true;
            for (int[] blockedSquare : blockedSquares) { //filtering out blockedSquares from candidateMoves
                if (candidateMove[0] == blockedSquare[0] && candidateMove[1] == blockedSquare[1]) {
                    mightBeAdded = false;
                    break;
                }
            }
            if (mightBeAdded) { // isn't blocked
                Piece maybeCapture = boardData[candidateMove[0]][candidateMove[1]];
                if (maybeCapture == null || maybeCapture.getIsWhite() != this.getIsWhite()) { // no teamkill
                    noObstacles.add(candidateMove); // the big Added
                }
            }
        }
        return noObstacles;
    }

    public List<int[]> handleNoSelfChecks(int column, int row, Piece[][] boardData, List<int[]> noObstacles) {
        List<int[]> pinHandledMoves = new ArrayList<>(); // returned at end (if pinned)

        // Step 1: find my King
        int myKingColumn = -1, myKingRow = -1;
        for (int columnCheck = 0; columnCheck < 8; columnCheck++) {
            for (int rowCheck = 0; rowCheck < 8; rowCheck++) {
                Piece maybeKing = boardData[columnCheck][rowCheck];
                if (maybeKing instanceof King && maybeKing.getIsWhite() == this.getIsWhite()) {
                    myKingColumn = columnCheck;
                    myKingRow = rowCheck;
                }
            }
        }
        if (myKingColumn == -1) { //Shouldn't happen but who knows
            System.out.println("Can't Find King");
            return noObstacles; // f it we ball
        }

        // Step 2 (Optional but optimal): check alignment with King
        int pieceKingColumnOffset = myKingColumn - column; // these variable aren't optional
        int pieceKingRowOffset = myKingRow - row; // they do get used later
        boolean aligned = (pieceKingColumnOffset == 0 || pieceKingRowOffset == 0 || Math.abs(pieceKingColumnOffset) == Math.abs(pieceKingRowOffset));
        if (!aligned) {
            return noObstacles; // not even aligned, impossible to pin. Worth cut off... the rest gets crazy.
        }

        // Step 3: scan from piece
        int columnScanDirection = Integer.compare(0, pieceKingColumnOffset); // Go AWAY from king
        int rowScanDirection = Integer.compare(0, pieceKingRowOffset); // At least to start

        int scanColumn = column + columnScanDirection;
        int scanRow = row + rowScanDirection;

        boolean isKingClosest = false;
        boolean isVerticalPin = false;
        boolean isHorizontalPin = false;
        boolean isDiagonalPin = false;

        while (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
            Piece potentialPinner = boardData[scanColumn][scanRow];
            if (potentialPinner != null) {
                if (potentialPinner.getIsWhite() != this.getIsWhite()) {
                    // Vertical pin
                    if (pieceKingColumnOffset == 0 && (potentialPinner instanceof Rook || potentialPinner instanceof Queen)) {
                        isVerticalPin = true;
                    }
                    // Horizontal pin
                    else if (pieceKingRowOffset == 0 && (potentialPinner instanceof Rook || potentialPinner instanceof Queen)) {
                        isHorizontalPin = true;
                    }
                    // Diagonal pin: moving along both col + row
                    else if (columnScanDirection != 0 && rowScanDirection != 0 &&
                            (potentialPinner instanceof Bishop || potentialPinner instanceof Queen)) {
                        isDiagonalPin = true;
                    } else {
                        return noObstacles; // closest piece wasn't a pinner, lets close up shop.
                    }
                } else {
                    return noObstacles; // closest piece wasn't even an enemy
                }
                break;
            }
            scanColumn += columnScanDirection;
            scanRow += rowScanDirection;
        }

        scanColumn = column - columnScanDirection; // now we go the other way
        scanRow = row - rowScanDirection; // towards the king from piece

        while (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
            Piece maybeKing = boardData[scanColumn][scanRow];
            if (maybeKing != null) {
                if (maybeKing.getIsWhite() == this.getIsWhite() && maybeKing instanceof King) {
                    isKingClosest = true;
//                        System.out.println("King Pin possible");
                } else {
                    return noObstacles; //closest piece towards king isn't our king
                }
                break;
            }
            scanColumn -= columnScanDirection; // keep marching toward king
            scanRow -= rowScanDirection; // away from pinner
        }

        if (isKingClosest && (isVerticalPin || isHorizontalPin || isDiagonalPin)) {
            for (int[] maybeOkPinnedMove : noObstacles) {
                int moveKingColumnOffset = maybeOkPinnedMove[0] - myKingColumn;
                int moveKingRowOffset = maybeOkPinnedMove[1] - myKingRow;

                boolean allowed = false;

                if (isVerticalPin) {
                    allowed = (moveKingColumnOffset == 0);
                } else if (isHorizontalPin) {
                    allowed = (moveKingRowOffset == 0);
                } else if (isDiagonalPin) {
                    if (Math.abs(moveKingColumnOffset) == Math.abs(moveKingRowOffset)) { // makes sure is a diagonal.
                        int moveColumnDirection = Integer.compare(moveKingColumnOffset, 0);
                        int moveRowDirection = Integer.compare(moveKingRowOffset, 0);

                        // Either along the scan direction or direct opposite. Remember (+) scan Direction is towards pinner away from king.
                        allowed = ((moveColumnDirection == columnScanDirection && moveRowDirection == rowScanDirection) ||
                                (moveColumnDirection == -columnScanDirection && moveRowDirection == -rowScanDirection));
                    }
                }

                if (allowed) {
                    pinHandledMoves.add(maybeOkPinnedMove);
                }
            }
            return pinHandledMoves;
        }
        return noObstacles; // will this ever even call? I don't think so but is safe.
    }
}
