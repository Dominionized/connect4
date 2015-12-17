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
    private volatile GameState state;
    private int playerTurn;

    private List<Tuple<Integer, Observer>> observers;
    private Observer[] players;
    private Random random;

    public Game(GameConfig config) {
        state = GameState.WAITING;
        this.config = config;
        this.board = new Board(this.config.getColumns(), this.config.getRows());
        this.observers = new ArrayList<>();
        this.playerTurn = 0;
        this.players = new Observer[2];
        this.random = new Random(System.currentTimeMillis());
    }

    private enum GameState {
        WAITING, PLAYING, FINISHED
    }

    public void dropToken(int columnIndex) throws Exception {
        if (state != GameState.PLAYING) return;
        Cell tokenToPlay;
        switch (playerTurn) {
            case 0:
                tokenToPlay = Cell.RED;
                playerTurn = 1;
                players[1].setUIEnabled(true);
                players[0].setUIEnabled(false);
                break;
            case 1:
                tokenToPlay = Cell.BLACK;
                playerTurn = 0;
                players[0].setUIEnabled(true);
                players[1].setUIEnabled(false);
                break;
            default:
                throw new Exception("Player turn is not defined");
        }

        board.dropToken(columnIndex, tokenToPlay);
        executeVerifications();
        update();
    }
    public void resign() {
        if (state != GameState.PLAYING) return;
        String winner = whoWins();
        forEachObserver(observer -> observer.gameResigned(winner));
        resetGame();
    }
    
    private void executeVerifications() {
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
            forEachObserver(observer -> observer.columnFull(column));
        }
    }
    private void isBoardFull() {
        if (board.isFull()) {
            forEachObserver(observer -> observer.boardFull());
            resetGame();
        }
    }
    private void update() {
        if (board.getLastChangedCellType() != Cell.EMPTY) {
            int x = this.board.getLastChangedCellX();
            int y = this.board.getLastChangedCellY();
            Cell cellType = this.board.getLastChangedCellType();
            forEachObserver(observer -> observer.updateCell(x, y, cellType));
            String player = playerNumberToColor(this.playerTurn);
            forEachObserver(observer -> observer.updatePlayerTurn(player));
        }
    }
    private void resetGame() {
        int columns = this.config.getColumns();
        int rows = this.config.getRows();
        this.board = new Board(columns, rows);
        this.playerTurn = 0;
        this.players = new Observer[DEFAULT_NB_PLAYERS];
        state = GameState.WAITING;
        forEachObserver(observer -> observer.resetGame(columns, rows));
        tryStartGame();
    }
    private void tryStartGame() {
        if (this.state == GameState.WAITING && observers.size() >= DEFAULT_NB_PLAYERS) {
            forEachObserver(observer -> observer.setUIEnabled(false));
            for (int i = 0; i < DEFAULT_NB_PLAYERS; i++) {
                this.players[i] = observers.get(i).getRight();
            }
            this.state = GameState.PLAYING;
            this.players[0].setUIEnabled(true);
        }
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
    private boolean isIDUnique(int randomID) {
        for (Tuple<Integer, Observer> tuple : observers) {
            if (tuple.getLeft() == randomID) return false;
        }
        return true;
    }
    private int getRandomId() {
        return random.nextInt();
    }
    private void forEachObserver(Consumer<Observer> c) {
        this.observers.forEach(tuple -> c.accept(tuple.getRight()));
    }

    @Override
    public int registerObserver(Observer observer) {
        int randomID = getRandomId();
        while (!isIDUnique(randomID))
            randomID = getRandomId();
        this.observers.add(new Tuple(randomID, observer));
        // If in game : give state to observer
        observer.setUIEnabled(false);
        if (state == GameState.PLAYING) {
            observer.setGrid(board.getCellArray());
        } else  {
            tryStartGame();
        }
        return randomID;
    }

    @Override
    public void unregisterObserver(int id) {
        for (int i = 0; i < observers.size(); i++) {
            Tuple<Integer, Observer> observer = observers.get(i);
            if (observer.getLeft() == id) {
                observers.remove(i);
                break;
            }
        }
        if (this.state == GameState.PLAYING && observers.size() < DEFAULT_NB_PLAYERS) {
            resetGame();
        }
    }

}
