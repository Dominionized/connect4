package ca.csf.connect4.server.models;

// Shared module dependencies
// Shared module dependencies
import ca.csf.connect4.shared.GameConfig;
import ca.csf.connect4.shared.Observable;
import ca.csf.connect4.shared.models.Cell;

import ca.csf.connect4.shared.Observer;
import ca.csf.connect4.client.ui.UiText;
import ca.csf.connect4.shared.models.Tuple;

import java.util.*;
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
    private List<Tuple<Integer, Observer>> observers;

    private int playerTurn;
    private volatile GameState state;
    private Random random;

    public Game(GameConfig config) {
        state = GameState.WAITING;
        this.config = config;
        this.board = new Board(this.config.getColumns(), this.config.getRows());
        this.observers = new ArrayList<>();
        this.playerTurn = 0;
        this.random = new Random(System.currentTimeMillis());
    }

    private enum GameState {
        WAITING, PLAYING, FINISHED
    }

    public void dropToken(int columnIndex) throws Exception {
        if (state == GameState.FINISHED) return;
        Cell tokenToPlay;
        switch (playerTurn) {
            case 0:
                tokenToPlay = Cell.RED;
                playerTurn = 1;
                break;
            case 1:
                tokenToPlay = Cell.BLACK;
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
        forEachObserver(observer -> observer.gameResigned(whoWins()));
    }

    private void update() {
        if (board.getLastChangedCellType() != Cell.EMPTY) {
            int x = this.board.getLastChangedCellX();
            int y = this.board.getLastChangedCellY();
            Cell cellType = this.board.getLastChangedCellType();
            forEachObserver(observer -> observer.updateCell(x, y, cellType));
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
            state = GameState.FINISHED;
            String winner = whoWins();
            forEachObserver(observer -> observer.gameWon(winner));
            resetGame();
        }
    }

    private void isColumnFull() {
        for (int column : board.getFilledColumns()) {
            if (column == board.getLastChangedCellX()) {
                forEachObserver(observer -> observer.columnFull(column));
            }
        }
    }

    private void isBoardFull() {
        if (board.isFull()) {
            forEachObserver(observer -> observer.boardFull());
            resetGame();
        }
    }

    public void resign() {
        if (state == GameState.FINISHED) return;
        String winner = whoWins();
        forEachObserver(observer -> observer.gameResigned(winner));
        resetGame();
    }

    private void resetGame() {
        int columns = this.config.getColumns();
        int rows = this.config.getRows();
        this.board = new Board(columns, rows);
        state = GameState.WAITING;
        forEachObserver(observer -> observer.newGame(columns, rows));

    }

    private boolean won(int column, int row) {
        return board.checkAround(column, row, this.config.getTokenCountWin());
    }
    private String whoWins() {
        return (board.getLastChangedCellType() == Cell.BLACK) ? BLACK_PLAYER_NAME : RED_PLAYER_NAME;
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
        this.observers.add(new Tuple(randomID, observer));
        // If in game : give state to observer
        if (state == GameState.PLAYING) {
            observer.setGrid(board.getCellArray());
        } else if (state == GameState.WAITING && observers.size() == 2) {
            state = GameState.PLAYING;
        }
        return randomID;
    }

    private boolean isIDUnique(int randomID) {
        for (Tuple<Integer, Observer> tuple : observers) {
            if (tuple.getLeft() == randomID) return false;
        }
        return true;
    }

    private int getRand() {
        return random.nextInt();
    }

    @Override
    public void unregisterObserver(int id) {
        for (Tuple<Integer, Observer> tuple : observers) {
            if (tuple.getLeft() == id) {
                observers.remove(tuple);
                return;
            }
        }
    }

    private void forEachObserver(Consumer<Observer> c) {
        this.observers.forEach(tuple -> c.accept(tuple.getRight()));
    }
}
