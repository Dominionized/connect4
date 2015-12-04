package ca.csf.connect4.server.model;
import ca.csf.connect4.server.model.Cell.CellType;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int sizeX;
    private int sizeY;

    private List<Integer> filledStacks;

    private Cell[][] cellArray;
    private CellType lastChangedCellType;
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
        this.lastChangedCellType = CellType.EMPTY;
        initializeCells();
    }

    private void initializeCells() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                cellArray[i][j] = new Cell(Cell.CellType.EMPTY);
            }
        }
    }

    public Cell[][] getCellArray() {
        return cellArray;
    }

    public void dropToken(int posX, CellType cellType) throws Exception {
        if (cellArray[posX][0].cellType != Cell.CellType.EMPTY) {
            throw new Exception("Stack full");
        }

        if (cellArray[posX][1].cellType != Cell.CellType.EMPTY) {
            filledStacks.add(posX); // Stack is filled. You can no longer add items to it.
        }

        for (int i = 0; i < sizeY; i++) {
            if (!inBounds(posX, i + 1) || (cellArray[posX][i + 1].cellType != Cell.CellType.EMPTY)) {
                cellArray[posX][i].cellType = cellType;
                lastChangedCellType = cellArray[posX][i].cellType;
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
                if (cell.cellType == CellType.EMPTY) return false;
            }
        }
        return true;
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
                System.out.print(Integer.toString(i)+Integer.toString(j));
            }
            System.out.print("\n");
        }
        System.out.println();
    }

    public CellType getLastChangedCellType() {
        return lastChangedCellType;
    }

    public int getLastChangedCellX() {
        return lastChangedCellX;
    }

    public int getLastChangedCellY() {
        return lastChangedCellY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public List<Integer> getFilledStacks() {
        return filledStacks;
    }

}
