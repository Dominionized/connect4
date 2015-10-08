package ca.csf.connect4;

/**
 * Created by Anthony on 06/10/2015.
 */
public interface Observer {
    void updateCell(int x, int y, Cell.CellType type);
    void gameWon(String winner);
    void stackFull(int x);
    void boardFull();
    void gameResigned(String winner);
}
