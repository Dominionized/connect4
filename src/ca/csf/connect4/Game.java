package ca.csf.connect4;

import ca.csf.connect4.Cell.CellType;

/**
 * Created by dom on 25/09/15.
 */
public class Game {
    public static final int DEFAULT_NB_PLAYERS = 2;

    private Board board;
    private int playerTurn;

    public Game() {
        board = new Board();
    }

    public void start() {
        playerTurn = 0;
    }

    public void playTurn(int dropCoord) throws Exception {
        CellType tokenToPlay = CellType.EMPTY;

        switch (playerTurn) {
            case 0:
                tokenToPlay = CellType.RED;
                playerTurn = 1;
                break;
            case 1:
                tokenToPlay = CellType.BLACK;
                playerTurn = 0;
                break;
        }

        board.dropToken(dropCoord, tokenToPlay);
    }

    public Cell[][] getBoardArray() {
        return board.getCellArray();
    }

    public Cell getLastChangedCell() {
        return board.getLastChangedCell();
    }

    public int getLastChangedCellX() {
        return board.getLastChangedCellX();
    }

    public int getLastChangedCellY() {
        return board.getLastChangedCellY();
    }

    public int getSizeX() {
        return board.getSizeX();
    }

    public int getSizeY() {
        return board.getSizeY();
    }

}
