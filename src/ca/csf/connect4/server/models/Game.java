package ca.csf.connect4.server.models;

// Shared module dependencies
import ca.csf.connect4.shared.GameConfig;
import ca.csf.connect4.shared.Observable;
import ca.csf.connect4.shared.models.Cell;
import ca.csf.connect4.shared.models.Cell.CellType;

import ca.csf.connect4.shared.Observer;
import ca.csf.connect4.client.ui.UiText;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by dom on 25/09/15.
 */
public class Game implements Observable {

    public static final int DEFAULT_NB_PLAYERS = 2;
    private static final String BLACK_PLAYER_NAME = "Black player";
    private static final String RED_PLAYER_NAME = "Red player";

    private GameConfig config;
    private Board board;
    private HashMap<Integer, Observer> observers;

    private int playerTurn;
    private volatile boolean gameOver;
    private Random random;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new Board(this.config.getColumns(), this.config.getRows());
        this.observers = new HashMap<Integer, Observer>();
        this.playerTurn = 0;
        this.gameOver = false;
        this.random = new Random(System.currentTimeMillis());
    }

    public void dropToken(int columnIndex) throws Exception {
        if (gameOver) return;
        CellType tokenToPlay;
        switch (playerTurn) {
            case 0:
                tokenToPlay = CellType.RED;
                playerTurn = 1;
                break;
            case 1:
                tokenToPlay = Cell.CellType.BLACK;
                playerTurn = 0;
                break;
            default:
                throw new Exception("Player turn is not defined");
        }
        board.dropToken(columnIndex, tokenToPlay);
        executeVerifications();
        update();
    }

    public void disconnect(Observer disconnectedObserver) {
        //unregisterObserver(disconnectedObserver);
        this.observers.forEach((id, observer) -> observer.gameResigned(whoWins()));
    }

    private void update() {
        if (board.getLastChangedCellType() != CellType.EMPTY) {
            int x = this.board.getLastChangedCellX();
            int y = this.board.getLastChangedCellY();
            CellType cellType = this.board.getLastChangedCellType();
            this.observers.forEach((id, observer) -> observer.updateCell(x, y, cellType));
        }
    }

    public void executeVerifications() {
        isColumnFull();
        isBoardFull();
        isGameWon();
    }

    private void isGameWon() {
        int lastColumnChanged = this.board.getLastChangedCellX();
        int lastRowChanged = this.board.getLastChangedCellY();
        if (won(lastColumnChanged, lastRowChanged)) {
            this.gameOver = true;
            String winner = whoWins();
            this.observers.forEach((id, observer) -> observer.gameWon(winner));
            newGame();
        }
    }

    private void isColumnFull() {
        for (int column : board.getFilledColumns()) {
            if (column == board.getLastChangedCellX()) {
                this.observers.forEach((id, observer) -> observer.columnFull(column));
            }
        }
    }

    private void isBoardFull() {
        if (board.isFull()) {
            this.observers.forEach((id, observer) -> observer.boardFull());
            newGame();
        }
    }

    public void resign() {
        if (gameOver) return;
        String winner = whoWins();
        this.observers.forEach((id, observer) -> observer.gameResigned(winner));
        newGame();
    }

    private void newGame() {
        int columns = this.config.getColumns();
        int rows = this.config.getRows();
        this.board = new Board(columns, rows);
        this.gameOver = false;
        this.observers.forEach((id, observer) -> observer.newGame(columns, rows));
    }

    private boolean won(int column, int row) {
        return board.checkAround(column, row, this.config.getTokenCountWin());
    }
    private String whoWins() {
        return (board.getLastChangedCellType() == CellType.BLACK) ? BLACK_PLAYER_NAME : RED_PLAYER_NAME;
    }
    private String playerNumberToColor(int playerNumber) {
        switch(playerNumber) {
            case 0:
                return UiText.RED;
            case 1:
                return UiText.BLACK;
        }
        return "";
    }

    @Override
    public int registerObserver(Observer observer) {
        int randomID = getRand();
        while (!isIDUnique(randomID))
            randomID = getRand();
        this.observers.put(randomID, observer);
        return randomID;
    }

    private boolean isIDUnique(int randomID) {
        if (this.observers.containsKey(randomID)) return false;
        return true;
    }

    private int getRand() {
        return random.nextInt();
    }

    @Override
    public void unregisterObserver(int id) throws Exception {
        if (observers.remove(id) == null)
            throw new Exception("Observer not found");
    }
}
