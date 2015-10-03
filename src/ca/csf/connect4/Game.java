package ca.csf.connect4;

/**
 * Created by dom on 25/09/15.
 */
public class Game {

    private final int NB_PLAYERS = 2;
    private int playerTurn;

    public Game(int nbPlayers) {
        board = new Board();
    }
    private Board board;

    public void start() {
        playerTurn = 0;
    }

    public void playTurn(int dropCoord) throws Exception {
        Cell.CellType tokenToPlay = Cell.CellType.EMPTY;

        switch (playerTurn) {
            case 0:
                tokenToPlay = Cell.CellType.RED;
                playerTurn = 1;
                break;
            case 1:
                tokenToPlay = Cell.CellType.BLACK;
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
