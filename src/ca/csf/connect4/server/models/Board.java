package ca.csf.connect4.server.models;
// Shared module dependencies
import ca.csf.connect4.shared.models.Cell;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int sizeX;
    private int sizeY;

    private List<Integer> filledStacks;

    private Cell[][] cellArray;
    private Cell lastChangedCellType;
    private int lastChangedCellX;
    private int lastChangedCellY;

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
        private Direction reverse;

        static {
            TOP_LEFT.reverse = BOTTOM_RIGHT;
            TOP.reverse = BOTTOM;
            TOP_RIGHT.reverse = BOTTOM_LEFT;
            LEFT.reverse = RIGHT;
            RIGHT.reverse = LEFT;
            BOTTOM_LEFT.reverse = TOP_RIGHT;
            BOTTOM.reverse = TOP;
            BOTTOM_RIGHT.reverse = TOP.LEFT;
        }

        Direction(int relativePosX, int relativePosY) {
            this.relativePosX = relativePosX;
            this.relativePosY = relativePosY;
        }

        public Direction getReverse() {
            return this.reverse;
        }
    }

    public Board(int sizeX, int sizeY) {
        cellArray = new Cell[sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.filledStacks = new ArrayList<Integer>();
        this.lastChangedCellType = Cell.EMPTY;
        initializeCells();
    }

    public Cell[][] getCellArray() {
        return cellArray;
    }
    public void dropToken(int posX, Cell cellType) throws Exception {
        if (cellArray[posX][0] != Cell.EMPTY) {
            throw new Exception("Stack full");
        }

        if (cellArray[posX][1] != Cell.EMPTY) {
            filledStacks.add(posX); // Stack is filled. You can no longer add items to it.
        }

        for (int i = 0; i < sizeY; i++) {
            if (!inBounds(posX, i + 1) || (cellArray[posX][i + 1] != Cell.EMPTY)) {
                cellArray[posX][i] = cellType;
                lastChangedCellType = cellArray[posX][i];
                lastChangedCellX = posX;
                lastChangedCellY = i;
                return;
            }
        }
    }
    public boolean checkAround(int x, int y, int numberOfConnectedCells) {
        for (Direction dir : Direction.values()) {
            if ((numberOfConnectedCells(x, y, dir) + numberOfConnectedCells(x, y, dir.getReverse())) - 1 >= numberOfConnectedCells) return true;
        }
        return false;
    }
    public boolean isFull() {
        for (Cell[] row : cellArray) {
            for (Cell cell : row) {
                if (cell == Cell.EMPTY) return false;
            }
        }
        return true;
    }

    public Cell getLastChangedCellType() {
        return lastChangedCellType;
    }
    public int getLastChangedCellX() {
        return lastChangedCellX;
    }
    public int getLastChangedCellY() {
        return lastChangedCellY;
    }
    public List<Integer> getFilledColumns() {
        return filledStacks;
    }

    private void initializeCells() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                cellArray[i][j] = Cell.EMPTY;
            }
        }
    }
    private int numberOfConnectedCells(int x, int y, Direction dir) {
        int nextX = x + dir.relativePosX;
        int nextY = y + dir.relativePosY;

        if (inBounds(nextX, nextY) && (cellArray[nextX][nextY] == cellArray[x][y])) {
            return 1 + numberOfConnectedCells(nextX, nextY, dir);
        }
        else {
            return 1;
        }
    }
    private boolean inBounds(int x, int y) {
        return (x >= 0 && y >= 0 && x < sizeX && y < sizeY);
    }
}
