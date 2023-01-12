package org.example;

import net.CheckersClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstrakcyjna klasa planszy do warcabów
 */
public abstract class CheckersBoard {
    public static Square[][] board;
    public static int lim;
    public static int rows;

    /**
     * Metoda zwracająca rozmiar planszy
     * @return - rozmiar planszy
     */
    public int getSize() {
        return lim;
    }

    /**
     * Metoda tworząca i zwracająca planszę w początkowym stanie
     * @return - całą planszę
     */
    public Square[][] getBoard() {
        createBoard();
        return board;
    }
    public abstract boolean getKingLogic();
    public abstract boolean getBackwardsLogic();
    public abstract void isSuccessiveCaptureAvailable(int[] attemptedMove);

    /**
     * Metoda aktualizująca ruch
     * @param firstX - pierwsza współrzędna pierwszego kliknięcia
     * @param firstY - druga współrzędna pierwszego kliknięcia
     * @param secondX - pierwsza współrzędna drugiego kliknięcia
     * @param secondY - druga współrzędna pierwszego kliknięcia
     * @param yourMove - parametr sprawdzający od kogo jest ruch
     * @param successiveCapMode - parametr sprawdzający, czy włączony jest tryb wielokrotnego bicia
     * @return - zwraca współrzędne pól do zaktualizowania
     */
    public List<int[]> updateMove(int firstX, int firstY, int secondX, int secondY, boolean yourMove, boolean successiveCapMode) {
        List<int[]> squaresToUpdate = new ArrayList<>();
        board[firstX][firstY].setTaken(false);
        squaresToUpdate.add(new int[] {firstX, firstY});
        squaresToUpdate.add(new int[] {secondX, secondY});
        boolean addedKing = false;
        int[] potentiallyJumped = getJumpedPieceCoordinates(firstX, firstY, secondX, secondY);
        if (potentiallyJumped!=null) {
            board[potentiallyJumped[0]][potentiallyJumped[1]].setTaken(false);
            squaresToUpdate.add(potentiallyJumped);
        }
        if (yourMove && ((secondX==lim-1 && CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE) || (secondX==0 && CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK))){
            board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, Piece.PieceType.KING);
            addedKing = true;
        } else if (!yourMove && ((secondX==lim-1 && CheckersClient.frame.thisPlayerTeam == Piece.Team.BLACK) || (secondX==0 && CheckersClient.frame.thisPlayerTeam == Piece.Team.WHITE))) {
            board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, Piece.PieceType.KING);
            addedKing = true;
        }
        if (!addedKing && yourMove) {
            board[secondX][secondY].setPiece(CheckersClient.frame.thisPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
        } else if (!addedKing) {
            board[secondX][secondY].setPiece(CheckersClient.frame.anotherPlayerTeam, CheckersClient.frame.attemptedMovedPieceType);
        }
        if(!successiveCapMode) {

            if (yourMove) {
                CheckersClient.frame.setTitle("Opponent's turn!");
                System.out.println("Successful move!");
            }
            else {
                CheckersClient.frame.setTitle("Your turn!");
                System.out.println("Opponent made a move");
            }

        }
        else {
            if (yourMove) {
                System.out.println("Successive capture possible!");
            }
            else {
                System.out.println("You fell victim to a successive capture");
            }

        }
        return squaresToUpdate;
    }

    /**
     * Metoda zwracająca współrzędne zbitych bierek
     * @param firstClickX - pierwsza współrzędna pierwszego kliknięcia
     * @param firstClickY - druga współrzędna pierwszego kliknięcia
     * @param secondClickX - pierwsza współrzędna drugiego kliknięcia
     * @param secondClickY - druga współrzędna drugiego kliknięcia
     * @return - zwraca współrzędne zbitych bierek
     */
    public int[] getJumpedPieceCoordinates(int firstClickX, int firstClickY, int secondClickX, int secondClickY) {
        final int xDelta = secondClickX-firstClickX;
        final int yDelta = secondClickY-firstClickY;
        if (Math.abs(xDelta-yDelta)!=1) {
            if (xDelta>0) {
                if (yDelta>0) {
                    return new int[]{secondClickX-1, secondClickY-1};
                } else {
                    return new int[]{secondClickX-1, secondClickY+1};
                }
            }
            else {
                if (yDelta>0) {
                    return new int[]{secondClickX+1, secondClickY-1};
                }
                else {
                    return new int[]{secondClickX+1, secondClickY+1};
                }
            }
        }
        return null;
    }

    /**
     * Metoda sprawdzająca wszystkie legalne ruchy na planszy dla danego gracza
     * @param movingTeam - kolor gracza, który ma wykonać ruch
     * @return - zwraca listę współrzędnych dozwolonych ruchów
     */
    public List<int[]> checkForLegalMovesOnBoard(Piece.Team movingTeam) {
        List<int[]> notCaptureList = new ArrayList<>();
        List<int[]> captureList = new ArrayList<>();
        boolean captureAvailable = false;
        for (int i = 0; i<CheckersBoard.board.length; i++) {
            for (int j = 0; j < CheckersBoard.board.length; j++) {
                if ((i + j) % 2 != 0 && CheckersBoard.board[i][j].piece != null) {
                    Piece.PieceType type = CheckersBoard.board[i][j].piece.getPieceType();
                    boolean functionality;
                    if (type == Piece.PieceType.KING) {
                        functionality = this.getKingLogic();
                    } else {
                        functionality = this.getBackwardsLogic();
                    }
                    if (CheckersBoard.board[i][j].piece.checkLegalMoves(functionality) != null && CheckersBoard.board[i][j].isTaken() && CheckersBoard.board[i][j].getTeam().equals(movingTeam)) {
                        for (int[] move : CheckersBoard.board[i][j].piece.checkLegalMoves(functionality)) {
                            if (move[2] > 0) {
                                captureList.add(new int[]{i, j, move[0], move[1], move[2], move[3]});
                                captureAvailable = true;
                                notCaptureList.clear();
                            } else if (!captureAvailable) {
                                notCaptureList.add(new int[]{i, j, move[0], move[1], move[2], move[3]});
                            }
                        }
                    }
                }
            }
        }
        if (captureAvailable) {
            return captureList;
        }
        else {
            return notCaptureList;
        }
    }

    /**
     * Metoda sprawdzająca, czy dany ruch znajduje się na liście dozwolonych posunięć
     * @param moveList - lista dozwolonych posunięć
     * @param coordinates - współrzędne wykonanego ruchu
     * @return - w przypadku, gdy ruch jest dozwolony zwraca się jego współrzędne
     */
    public int[] checkForMoves(List<int[]> moveList, int[] coordinates) {
        for (int[] ints : moveList) {
            if (ints[0] == coordinates[0] && ints[1] == coordinates[1] && ints[2] == coordinates[2] && ints[3] == coordinates[3] ) {
                return ints;
            }
        }
        return null;
    }

    /**
     * Metoda aktualizująca planszę po wykonaniu wielokrotnego bicia
     * @param attemptedMove - współrzędne wykonanego ruchu
     * @param possibleNextMoves - lista ruchów po wykonaniu bicia
     */
    void successfulCaptureChanges(int[] attemptedMove, List<int[]> possibleNextMoves) {
        boolean goodMove = false;
        if (possibleNextMoves!=null) {
            for (int[] move : possibleNextMoves) {
                if (move[2]>0) {
                    goodMove=true;
                    for (int[] jumpedXY : CheckersClient.frame.successiveJumpedXYs) {
                        if (Arrays.equals(getJumpedPieceCoordinates(attemptedMove[2], attemptedMove[3], move[0], move[1]),jumpedXY)) {
                            goodMove=false;
                            break;
                        }
                    }
                    if (goodMove) {
                        CheckersClient.frame.successiveCaptureMode = true;
                        CheckersClient.frame.successiveX=attemptedMove[2];
                        CheckersClient.frame.successiveY=attemptedMove[3];
                        break;
                    }
                }
            }
        }
        if (!goodMove) {
            for (int[] move : CheckersClient.frame.successiveJumpedXYs) {
                CheckersBoard.board[move[0]][move[1]].setTaken(false);
            }
            clearSuccessive();
        }
    }

    /**
     * Metoda resetująca flagi wielokrotnego bicia
     */
    void clearSuccessive() {
        CheckersClient.frame.successiveCaptureMode = false;
        CheckersClient.frame.successiveX=-1;
        CheckersClient.frame.successiveY=-1;
        CheckersClient.frame.successiveJumpedXYs.clear();
    }

    /**
     * Metoda tworząca początkową planszę oraz jej strukturę
     */
    void createBoard() {
        // Tworzenie pól i ich współrzędnych
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if ((i+j)%2 != 0) {
                    board[i][j] = new Square(lim);
                    board[i][j].setCoordinates(i, j);
                }
                else {
                    board[i][j] = null;
                }
            }
        }
        // Ustawianie sąsiadów dla każdego grywalnego pola
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if ((i+j)%2 != 0) {
                    board[i][j].setNeighbours();
                }
            }
        }
        // Ustawianie początkowych pionków
        for (int i = 0; i < lim; i++) {
            for (int j = 0; j < lim; j++) {
                if ((i+j)%2 != 0 && i < rows) {
                    board[i][j].setPiece(Piece.Team.WHITE, Piece.PieceType.MAN);
                }
                else if ((i+j)%2 != 0 && i > lim-rows-1) {
                    board[i][j].setPiece(Piece.Team.BLACK, Piece.PieceType.MAN);
                }
            }
        }
    }

}
