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
        initializeCells();
    }

    private void initializeCells() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                cellArray[i][j] = new Cell(Cell.CellType.EMPTY);
            }
        }
    }

    public Board() {
        this(DEFAULT_SIZE_X, DEFAULT_SIZE_Y);
    }

    public Cell[][] cellArray;

    public void dropToken(int posX, Cell.CellType cellType) throws Exception {
        if (cellArray[posX][0].cellType != Cell.CellType.EMPTY) {
            throw new Exception("Stack full");
        }

        for (int i = 0; i < sizeY; i++) {
            if (!inBounds(posX, i + 1) || (cellArray[posX][i + 1].cellType != Cell.CellType.EMPTY)) {
                cellArray[posX][i].cellType = cellType;
                return;
            }
        }
    }

    public boolean checkAround(int x, int y, int numberOfConnectedCells) {
        for (Direction dir : Direction.values()) {
            if (numberOfConnectedCells(x, y, dir) >= numberOfConnectedCells) return true;
        }
        return false;
    }

    private int numberOfConnectedCells(int x, int y, Direction dir) {
        int nextX = x + dir.relativePosX;
        int nextY = y + dir.relativePosY;

        if (inBounds(nextX, nextY) && (cellArray[nextX][nextY].cellType == cellArray[x][y].cellType)) {
            return 1 + numberOfConnectedCells(nextX, nextY, dir);
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

    public void debugCellArray() {
        for (int j = 0; j < sizeY; j++) {
            for (int i = 0; i < sizeX; i++) {
                switch(cellArray[i][j].cellType) {
                    case BLACK:
                        System.out.print("B");
                        break;
                    case RED:
                        System.out.print("R");
                        break;
                    case EMPTY:
                        System.out.print(".");
                        break;
                }
            }
            System.out.print("\n");
        }
        System.out.println();
    }
}
