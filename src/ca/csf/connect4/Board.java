package ca.csf.connect4;

/**
 * Created by dom on 25/09/15.
 */
public class Board {

    private final static int DEFAULT_SIZE_X = 7;
    private final static int DEFAULT_SIZE_Y = 6;

    private int sizeX;
    private int sizeY;

    public Board(int sizeX, int sizeY) {
        cellArray = new Cell[sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Board() {
        this(DEFAULT_SIZE_X, DEFAULT_SIZE_Y);
    }

    public Cell[][] cellArray;

    public boolean connected(int startingPointX, int startingPointY, int numberOfConnectedCells) {

    }

    public int numberOfConnectedCells(int x, int y, Direction dir, Cell.CellType type) {
        int nextX = x + dir.relativePosX;
        int nextY = y + dir.relativePosY;
        if (inBounds(nextX, nextY)
                && cellArray[nextX][nextY].cellType == type) {
            return numberOfConnectedCells(nextX, nextY, dir, type);
        }
        else {
            return 1;
        }
    }

    private enum Direction {
        TOP_LEFT(-1, -1),
        TOP(0,-1),
        TOP_RIGHT(1,-1),
        LEFT(-1, 0),
        RIGHT(1,0),
        BOTTOM_LEFT(-1, 1),
        BOTTOM(0, 1),
        BOTTOM_RIGHT(1, 1);

        public int relativePosX;
        public int relativePosY;

       Direction(int relativePosX, int relativePosY) {
            this.relativePosX = relativePosX;
            this.relativePosY = relativePosY;
        }
    }

    private boolean inBounds(int x, int y) {
        return (x >= 0 && y >= 0 && x < sizeX && y < sizeY);
    }
}
