package chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<int[]> getCandidateMoves(int column, int row) {
//        System.out.println("This is a King and candidate moves are actively being coded");

        List<int[]> candidateMoves = new ArrayList<>();

        int[][] allDirections = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] directionection : allDirections) {
            int newRow = row + directionection[0];
            int newColumn = column + directionection[1];

            if (newRow >= 0 && newRow < 8 && newColumn >= 0 && newColumn < 8) {
                candidateMoves.add(new int[]{newColumn, newRow});
//                newRow +=directionection[0];
//                newColumn +=directionection[1];
            }

        }
        return candidateMoves;
    }

    @Override
    public List<int[]> handleObstacles(int column, int row, Piece[][] boardData, List<int[]> candidateMoves) {
        List<int[]> noObstacles = super.handleObstacles(column, row, boardData, candidateMoves);

        boolean isTopKing = (row == 0);
        boolean isBottomKing = (row == 7);

        // Handle bottom king (row 7)
        if (isBottomKing && !ChessGUI.bottomKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // White POV (bottom king at column 4)
                // Short castle: king -> (6,7), requires columns 5,6 empty
                if (!ChessGUI.bottomRightRookMoved && boardData[5][7] == null && boardData[6][7] == null) {
                    noObstacles.add(new int[]{6, 7});
                }
                // Long castle: king -> (2,7), requires columns 1,2,3 empty
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null && boardData[3][7] == null) {
                    noObstacles.add(new int[]{2, 7});
                }
            } else { // Black POV (bottom king at column 3)
                // Short castle: king -> (1,7), requires columns 1,2 empty
                if (!ChessGUI.bottomLeftRookMoved && boardData[1][7] == null && boardData[2][7] == null) {
                    noObstacles.add(new int[]{1, 7});
                }
                // Long castle: king -> (5,7), requires columns 4,5,6 empty
                if (!ChessGUI.bottomRightRookMoved && boardData[4][7] == null && boardData[5][7] == null && boardData[6][7] == null) {
                    noObstacles.add(new int[]{5, 7});
                }
            }
        }

        // Handle top king (row 0)
        else if (isTopKing && !ChessGUI.topKingMoved) {
            if (ChessGUI.isWhitePovGlobal) { // White POV (top king at column 4)
                // Short castle: king -> (6,0), requires columns 5,6 empty
                if (!ChessGUI.topRightRookMoved && boardData[5][0] == null && boardData[6][0] == null) {
                    noObstacles.add(new int[]{6, 0});
                }
                // Long castle: king -> (2,0), requires columns 1,2,3 empty
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null && boardData[3][0] == null) {
                    noObstacles.add(new int[]{2, 0});
                }
            } else { // Black POV (top king at column 3)
                // Short castle: king -> (1,0), requires columns 1,2 empty
                if (!ChessGUI.topLeftRookMoved && boardData[1][0] == null && boardData[2][0] == null) {
                    noObstacles.add(new int[]{1, 0});
                }
                // Long castle: king -> (5,0), requires columns 4,5,6 empty
                if (!ChessGUI.topRightRookMoved && boardData[4][0] == null && boardData[5][0] == null && boardData[6][0] == null) {
                    noObstacles.add(new int[]{5, 0});
                }
            }
        }
        return noObstacles;
    }

    @Override
    public List<int[]> handleNoSelfChecks(int column, int row, Piece[][] boardData, List<int[]> noObstacles) {
        List<int[]> safeMoves = new ArrayList<>();

        for (int[] noObstacle : noObstacles) {

            int targetColumn = noObstacle[0];
            int targetRow = noObstacle[1];

            // Step 1: make a simulated board for this move
            Piece[][] simulatedBoard = new Piece[8][8];
            for (int c = 0; c < 8; c++) {
                for (int r = 0; r < 8; r++) {
                    simulatedBoard[c][r] = boardData[c][r];
                }
            }
            simulatedBoard[column][row] = null;
//            simulatedBoard[targetColumn][targetRow] = this; // place king in new square

            // Step 2: check if that square is seen by any enemy piece
            boolean isSquareSeen = getIsSquareSeen(targetColumn, targetRow, !this.getIsWhite(), simulatedBoard);
            boolean isCastleThroughCheck = false;
            if (Math.abs(targetColumn - column) == 2) {
                isCastleThroughCheck = getIsSquareSeen((targetColumn + column )/ 2, targetRow, !this.getIsWhite(), simulatedBoard);
            }

            // Step 3: if not seen, add to safeMoves
            if (!isSquareSeen && !isCastleThroughCheck) {
                safeMoves.add(noObstacle);
            }
        }
        return safeMoves;
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////

    private boolean getIsSquareSeen(int column, int row, boolean isWhiteAttack, Piece[][] simulatedBoardData) {
        // Used to check if a square is attacked by the opposing color (isWhiteAttack = attacker color)

        int[][] allDirections = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},   // straight lines (rook/queen)
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}  // diagonals (bishop/queen)
        };

        // --- Sliding pieces (Rooks, Bishops, Queens) ---
        for (int[] direction : allDirections) {
            int scanColumn = column + direction[0];
            int scanRow = row + direction[1];

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
            int scanColumn = column + knightMove[0];
            int scanRow = row + knightMove[1];
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
                int scanColumn = column + columnOffset;
                int scanRow = row + rowOffset;
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
        int pawnAttackDirection = ChessGUI.isWhitePovGlobal
                ? (isWhiteAttack ? 1 : -1)
                : (isWhiteAttack ? -1 : 1);
        int[][] pawnAttackOffsets = {{-1, pawnAttackDirection}, {1, pawnAttackDirection}};

        for (int[] pawnOffset : pawnAttackOffsets) {
            int scanColumn = column + pawnOffset[0];
            int scanRow = row + pawnOffset[1];
            if (scanColumn >= 0 && scanColumn < 8 && scanRow >= 0 && scanRow < 8) {
                Piece maybePawnAttacker = simulatedBoardData[scanColumn][scanRow];
                if (maybePawnAttacker != null &&
                        maybePawnAttacker.getIsWhite() == isWhiteAttack &&
                        maybePawnAttacker instanceof Pawn) {
                    return true;
                }
            }
        }
        return false; // no attackers found
    }
}
