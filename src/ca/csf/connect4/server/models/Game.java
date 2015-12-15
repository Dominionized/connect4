package ca.csf.connect4.server.models;

import ca.csf.connect4.Observer;
import ca.csf.connect4.server.models.Cell;
import ca.csf.connect4.server.models.Cell.CellType;
import ca.csf.connect4.server.Observable;
import ca.csf.connect4.client.ui.UiText;
import ca.csf.connect4.server.models.Board;

import java.util.ArrayList;

/**
 * Created by dom on 25/09/15.
 */
public class Game implements Observable {
    public static final int DEFAULT_NB_PLAYERS = 2;
    private int nbCellsToWin = 4;

    private static final String BLACK_PLAYER_NAME = "Black player";
    private static final String RED_PLAYER_NAME = "Red player";

    private Board board;

    private int playerTurn;
    private ArrayList<Observer> observers;

    private boolean won;
    private boolean resigned;

    public Game(int sizeX, int sizeY) {
        board = new Board(sizeX, sizeY);
        observers = new ArrayList<Observer>();
        won = false;
        resigned = false;
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
        if (won(board.getLastChangedCellX(),board.getLastChangedCellY())) {
            won = true;
        }
        notifyObservers();
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

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        if (!observers.remove(observer))
            System.err.println("Could not remove given observer");
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            if (board.getLastChangedCellType() != CellType.EMPTY) {
                observer.updateCell(board.getLastChangedCellX(),
                        board.getLastChangedCellY(),
                        board.getLastChangedCellType());
            }
            for (int stack : board.getFilledStacks()) {
                if (stack == board.getLastChangedCellX()) {
                    observer.stackFull(stack);
                }
            }
            if (won) {
                observer.gameWon(whoWins());
            }
            if (board.isFull()) {
                observer.boardFull();
            }
            if (resigned) {
                observer.gameResigned(whoWins());
            }
        }
    }

    public boolean won(int x, int y) {
        return board.checkAround(x, y, nbCellsToWin);
    }

    public void resign() {
        this.resigned = true;
        notifyObservers();
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

    public void setNbCellsToWin(int nbCellsToWin) {
        this.nbCellsToWin = nbCellsToWin;
    }
}
