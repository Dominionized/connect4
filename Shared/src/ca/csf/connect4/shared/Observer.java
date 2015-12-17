package ca.csf.connect4.shared;

import ca.csf.connect4.shared.models.Cell;

/**
 * Created by Anthony on 06/10/2015.
 */
public interface Observer {
    void updateCell(int x, int y, Cell type);
    void setGrid(Cell[][] types);
    void updatePlayerTurn(String playerTurn);
    void gameWon(String winner);
    void columnFull(int x);
    void boardFull();
    void gameResigned(String winner);
    void newGame(int columns, int rows);
    int getObserverId();
}
