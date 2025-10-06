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

    public static int[] findMyKing(Piece[][] boardData, boolean isWhite) {
        for (int maybeKingColumn = 0; maybeKingColumn < 8; maybeKingColumn++) {
            for (int maybeKingRow = 0; maybeKingRow < 8; maybeKingRow++) {
                Piece maybeKing = boardData[maybeKingColumn][maybeKingRow];
                if (maybeKing != null && maybeKing instanceof King && maybeKing.getIsWhite() == isWhite) {
                    return new int[]{maybeKingColumn, maybeKingRow};
                }
            }
        }
        System.out.println("Can't find my king — something's wrong.");
        return new int[]{-1, -1};
    }


    public static boolean getIsSquareSeen(int targetColumn, int targetRow, boolean isWhiteAttack, Piece[][] simulatedBoardData) {
        // Used to check if a square is attacked by the opposing color (isWhiteAttack = attacker color)

        int[][] allDirections = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},   // straight lines (rook/queen)
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}  // diagonals (bishop/queen)
        };

        // --- Sliding pieces (Rooks, Bishops, Queens) ---
        for (int[] direction : allDirections) {
            int scanColumn = targetColumn + direction[0];
            int scanRow = targetRow + direction[1];

            while (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
                Piece potentialAttacker = simulatedBoardData[scanColumn][scanRow];
                if (potentialAttacker != null) {
                    if (potentialAttacker.getIsWhite() == isWhiteAttack) {
                        boolean isStraightAttack = (direction[0] == 0 || direction[1] == 0);
                        boolean isDiagonalAttack = Math.abs(direction[0]) == Math.abs(direction[1]);

                        if ((isStraightAttack && (potentialAttacker instanceof Rook || potentialAttacker instanceof Queen)) ||
                                (isDiagonalAttack && (potentialAttacker instanceof Bishop || potentialAttacker instanceof Queen))) {
                            return true;
                        }
                    }
                    break; // first piece blocks the rest
                }
                scanColumn += direction[0];
                scanRow += direction[1];
            }
        }

        // --- Knight attacks ---
        int[][] allKnightMoves = {
                {1, 2}, {2, 1}, {-1, 2}, {-2, 1},
                {1, -2}, {2, -1}, {-1, -2}, {-2, -1}
        };

        for (int[] knightMove : allKnightMoves) {
            int scanColumn = targetColumn + knightMove[0];
            int scanRow = targetRow + knightMove[1];
            if (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
                Piece maybeKnightAttacker = simulatedBoardData[scanColumn][scanRow];
                if (maybeKnightAttacker != null &&
                        maybeKnightAttacker.getIsWhite() == isWhiteAttack &&
                        maybeKnightAttacker instanceof Knight) {
                    return true;
                }
            }
        }

        // --- King attacks (adjacent squares) ---
        for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                if (columnOffset == 0 && rowOffset == 0) continue;
                int scanColumn = targetColumn + columnOffset;
                int scanRow = targetRow + rowOffset;
                if (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
                    Piece maybeKingAttacker = simulatedBoardData[scanColumn][scanRow];
                    if (maybeKingAttacker != null &&
                            maybeKingAttacker.getIsWhite() == isWhiteAttack &&
                            maybeKingAttacker instanceof King) {
                        return true;
                    }
                }
            }
        }

        // --- Pawn attacks ---
        int pawnAttackDirection = ChessGUI.isWhitePovGlobal == isWhiteAttack ? 1 : -1;
        int[][] pawnAttackOffsets = {{-1, pawnAttackDirection}, {1, pawnAttackDirection}};

        for (int[] pawnOffset : pawnAttackOffsets) {
            int scanColumn = targetColumn + pawnOffset[0];
            int scanRow = targetRow + pawnOffset[1];
            if (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
                Piece maybePawnAttacker = simulatedBoardData[scanColumn][scanRow];
                if (maybePawnAttacker != null && maybePawnAttacker.getIsWhite() == isWhiteAttack && maybePawnAttacker instanceof Pawn) {
                    return true;
                }
            }
        }
        return false; // no attackers found
    }

    public List<int[]> handleNoSelfChecks(int pieceColumn, int pieceRow, Piece[][] boardData, List<int[]> noObstacles) {
        List<int[]> pinHandledMoves = new ArrayList<>(); // returned at end (if pinned)

        // Step 1: find my King

        int[] myKingCoordinates = findMyKing(boardData, this.getIsWhite());

        int myKingColumn = myKingCoordinates[0], myKingRow = myKingCoordinates[1];
//
        if (myKingCoordinates[0] == -1) { //Shouldn't happen but who knows
            System.out.println("Can't Find King");
            return noObstacles; // f it we ball
        }

        // Step 2 (Optional but optimal): check alignment with King
        int pieceKingColumnOffset = myKingCoordinates[0] - pieceColumn; // these variable aren't optional
        int pieceKingRowOffset = myKingCoordinates[1] - pieceRow; // they do get used later
        boolean aligned = (pieceKingColumnOffset == 0 || pieceKingRowOffset == 0 || Math.abs(pieceKingColumnOffset) == Math.abs(pieceKingRowOffset));
        if (!aligned) {
            return noObstacles; // not even aligned, impossible to pin. Worth cut off... the rest gets crazy.
        }

        // Step 3: scan from piece
        int columnScanDirection = Integer.compare(0, pieceKingColumnOffset); // Go AWAY from king
        int rowScanDirection = Integer.compare(0, pieceKingRowOffset); // At least to start

        int scanColumn = pieceColumn + columnScanDirection;
        int scanRow = pieceRow + rowScanDirection;

        boolean isKingPinnable = false;
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

        scanColumn = pieceColumn - columnScanDirection; // now we go the other way
        scanRow = pieceRow - rowScanDirection; // towards the king from piece

        while (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
            Piece maybeKing = boardData[scanColumn][scanRow];
            if (maybeKing != null) {
                if (maybeKing.getIsWhite() == this.getIsWhite() && maybeKing instanceof King) {
                    isKingPinnable = true;
//                        System.out.println("King Pin possible");
                } else {
                    return noObstacles; //closest piece towards king isn't our king
                }
                break;
            }
            scanColumn -= columnScanDirection; // keep marching toward king
            scanRow -= rowScanDirection; // away from pinner
        }

        if (isKingPinnable && (isVerticalPin || isHorizontalPin || isDiagonalPin)) {
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

    public List<int[]> handleBlocksCheck(int pieceColumn, int pieceRow, Piece[][] boardData, List<int[]> noSelfCheckMoves) {
        List<int[]> blockingMoves = new ArrayList<>();

        int[] myKingCoordinates = findMyKing(boardData, this.getIsWhite());
        int kingColumn = myKingCoordinates[0];
        int kingRow = myKingCoordinates[1];
        if (kingColumn == -1) {
            System.out.println("handleBlocksCheck: Cannot find king.");
            return noSelfCheckMoves;
        }

        if (!getIsSquareSeen(kingColumn, kingRow, !this.getIsWhite(), boardData)) { // is not in check
            return noSelfCheckMoves;
        }

        for (int[] noSelfCheckMove : noSelfCheckMoves) {

            Piece[][] simulatedBoard = new Piece[8][8];
            for (int c = 0; c < 8; c++) {
                for (int r = 0; r < 8; r++) {
                    simulatedBoard[c][r] = boardData[c][r];
                }
            }

            simulatedBoard[noSelfCheckMove[0]][noSelfCheckMove[1]] = this;
            simulatedBoard[pieceColumn][pieceRow] = null;

            if (!getIsSquareSeen(kingColumn, kingRow, !this.getIsWhite(), simulatedBoard)) { // is now not in check
                blockingMoves.add(noSelfCheckMove);
            }
        }
        return blockingMoves;
    }
}