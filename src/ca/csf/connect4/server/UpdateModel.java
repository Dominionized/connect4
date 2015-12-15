package ca.csf.connect4.server;

import ca.csf.connect4.shared.models.Cell.CellType;

/**
 * Created by Anthony on 15/12/2015.
 */
public class UpdateModel {

    private final int lastChangedCellX;
    private final int lastChangedCellY;
    private final CellType lastChangedCellType;

    public UpdateModel(final int lastChangedCellX, final int lastChangedCellY, final CellType lastChangedCellType) {
        this.lastChangedCellX = lastChangedCellX;
        this.lastChangedCellY = lastChangedCellY;
        this.lastChangedCellType = lastChangedCellType;
    }

    public int getLastChangedCellX() {
        return lastChangedCellX;
    }

    public int getLastChangedCellY() {
        return lastChangedCellY;
    }

    public CellType getLastChangedCellType() {
        return lastChangedCellType;
    }

}
