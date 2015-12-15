package ca.csf.connect4.server;

/**
 * Created by Anthony on 14/12/2015.
 */

public class ServerConfig {

    public static final int DEFAULT_LISTEN_PORT = 7331;
    public static final long DEFAULT_POLL_INTERVAL_MS = 500;
    public static final int DEFAULT_ROW_COUNT = 6;
    public static final int DEFAULT_COLUMN_COUNT = 7;
    public static final int DEFAULT_NB_CELLS_TO_WIN = 4;

    private final int rows;
    private final int columns;
    private final int tokenCountWin;

    public ServerConfig(int rows, int columns, int tokenCountWin) {
        this.rows = rows;
        this.columns = columns;
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
