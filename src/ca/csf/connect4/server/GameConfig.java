package ca.csf.connect4.server;

/**
 * Created by Anthony on 14/12/2015.
 */

public class GameConfig {

    public static final int DEFAULT_ROW_COUNT = 6;
    public static final int DEFAULT_COLUMN_COUNT = 7;
    public static final int DEFAULT_NB_CELLS_TO_WIN = 4;

    private final int rows;
    private final int columns;
    private final int tokenCountWin;

    public GameConfig(int columns, int rows, int tokenCountWin) {
        this.columns = columns;
        this.rows = rows;
        this.tokenCountWin = tokenCountWin;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getTokenCountWin() {
        return tokenCountWin;
    }

}
