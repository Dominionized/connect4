package ca.csf.connect4.shared;

import ca.csf.connect4.shared.models.Cell;

/**
 * Created by Anthony on 06/10/2015.
 */
public interface Observer {
    void setUIEnabled(boolean enabled);
    void updateCell(int x, int y, Cell cell);
    void setGrid(Cell[][] cells);
    void updatePlayerTurn(String playerTurn);
    void gameWon(String winner);
    void columnFull(int x);
    void boardFull();
    void gameResigned(String winner);
    void resetGame(int columns, int rows);
}
