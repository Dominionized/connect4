package ca.csf.connect4;

public class Cell {

    public Cell(CellType cellType) {
        this.cellType = cellType;
    }
    public enum CellType {
        EMPTY, RED, BLACK
    }

    public CellType cellType;
}
