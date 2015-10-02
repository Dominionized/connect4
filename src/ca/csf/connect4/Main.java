package ca.csf.connect4;

/**
 * Created by dom on 25/09/15.
 */
public class Main {
    public static void main(String[] args) {
        Board board = new Board();

        board.debugCellArray();

        try {
            board.dropToken(0, Cell.CellType.RED);
            board.dropToken(0, Cell.CellType.BLACK);
            board.dropToken(0, Cell.CellType.BLACK);
            board.dropToken(0, Cell.CellType.RED);
            board.dropToken(1, Cell.CellType.RED);
            board.dropToken(1, Cell.CellType.BLACK);
            board.dropToken(1, Cell.CellType.RED);
            board.dropToken(2, Cell.CellType.RED);
            board.dropToken(2, Cell.CellType.RED);
            board.dropToken(3, Cell.CellType.RED);
            board.debugCellArray();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(board.checkAround(0, 2, 4));
    }
}
