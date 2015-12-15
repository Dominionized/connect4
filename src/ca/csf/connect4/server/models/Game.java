package ca.csf.connect4.server.models;

// Shared module dependencies
import ca.csf.connect4.server.UpdateModel;
import ca.csf.connect4.shared.Observable;
import ca.csf.connect4.shared.models.Cell;
import ca.csf.connect4.shared.models.Cell.CellType;

import ca.csf.connect4.shared.Observer;
import ca.csf.connect4.server.ObserverHandler;
import ca.csf.connect4.client.ui.UiText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by dom on 25/09/15.
 */
public class Game implements Observable {

    public static final int DEFAULT_NB_PLAYERS = 2;
    private static final String BLACK_PLAYER_NAME = "Black player";
    private static final String RED_PLAYER_NAME = "Red player";

    private Board board;
    private int tokenCountWin;
    private List<Observer> observers;

    private int playerTurn;

    public Game(int columns, int rows, int tokenCountWin) {
        this.board = new Board(columns, rows);
        this.tokenCountWin = tokenCountWin;
        this.observers = new ArrayList<Observer>();
        this.playerTurn = 0;
    }

    public void dropToken(int columnIndex) throws Exception {
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

    private void update() {
        if (board.getLastChangedCellType() != CellType.EMPTY) {
            UpdateModel updateModel = new UpdateModel(this.board.getLastChangedCellX(),
                                                      this.board.getLastChangedCellY(),
                                                      this.board.getLastChangedCellType());
            this.observers.forEach((observer) ->);
            this.observers.forEach((observer) -> observer.columnFull(column));
        }
    }

    public void executeVerifications() {
        isStackFull();
        isBoardFull();
        isGameWon();
    }

    private void isGameWon() {
        int lastColumnChanged = this.board.getLastChangedCellX();
        int lastRowChanged = this.board.getLastChangedCellY();
        if (won(lastColumnChanged, lastRowChanged)) {
            this.observers.forEach(ObserverHandler::gameOver, whoWins());
        }
    }

    private void isStackFull() {
        for (int column : board.getFilledColumns()) {
            if (column == board.getLastChangedCellX()) {
                this.observers.forEach(ObserverHandler::columnFull, column);
            }
        }
    }

    private void isBoardFull() {
        if (board.isFull()) {
            this.observers.forEach(ObserverHandler::boardFull, null);
        }
    }

    public void resign() {
        this.observers.forEach(ObserverHandler::gameResigned, whoWins());
    }

    public Cell[][] getBoardArray() {
        return board.getCellArray();
    }

    public int getSizeX() {
        return board.getSizeX();
    }

    public int getSizeY() {
        return board.getSizeY();
    }

    public boolean won(int column, int row) {
        return board.checkAround(column, row, tokenCountWin);
    }

    private String whoWins() {
        return (board.getLastChangedCellType() == CellType.BLACK) ? BLACK_PLAYER_NAME : RED_PLAYER_NAME;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public String playerNumberToColor(int playerNumber) {
        switch(playerNumber) {
            case 0:
                return UiText.RED;
            case 1:
                return UiText.BLACK;
        }
        return "";
    }

    public void setTokenCountWin(int tokenCountWin) {
        this.tokenCountWin = tokenCountWin;
    }

    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        if (!observers.remove(observer))
            System.err.println("Could not remove given observer");
    }

}
