package ca.csf.connect4.server.models;

public class Cell {
    public CellType cellType;

    public enum CellType {
        RED, BLACK, EMPTY
    }

    public Cell(CellType cellType) {
        this.cellType = cellType;
    }
}
