package ca.csf.connect4;

import ca.csf.connect4.ui.UiText;
import ca.csf.connect4.ui.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Connect4Controller implements Observer {
    private static final String iconsPath = "/resources/";
    private static final String[] iconsName = { "RedToken.png", "BlackToken.png" };

    private static final int DEFAULT_SIZE_X = 7;
    private static final int DEFAULT_SIZE_Y = 6;
    private static final int DEFAULT_NB_CELLS_TO_WIN = 4;

    private int sizeX;
    private int sizeY;
    private int nbCellsToWin;

    private View view;
    private Game game;
    private ImageIcon[] icons;

    public Connect4Controller(int sizeX, int sizeY, int nbCellsToWin) throws IOException {
        view = new View(this);
        game = new Game(sizeX, sizeY);
        game.registerObserver(this);
        game.setNbCellsToWin(nbCellsToWin);

        view.initBoard(sizeY, sizeX);

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.nbCellsToWin = nbCellsToWin;
        initIcons();
    }

    public Connect4Controller() throws IOException {
        this(DEFAULT_SIZE_X, DEFAULT_SIZE_Y, DEFAULT_NB_CELLS_TO_WIN);
    }

    private void initIcons() throws IOException {
        int numberPlayers = Game.DEFAULT_NB_PLAYERS;
        icons = new ImageIcon[numberPlayers];
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 0; i < numberPlayers; ++i) {
            String path = pathBuilder.append(iconsPath).append(iconsName[i]).toString();
            icons[i] = new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path)));
            pathBuilder.delete(0, pathBuilder.length());
        }
    }
    public void dropToken(int pos) {
        try {
            game.playTurn(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resign() {
        game.resign();
    }
    private void restartGame() {
        game = new Game(sizeX, sizeY);
        game.registerObserver(this);
        game.setNbCellsToWin(nbCellsToWin);
        view.initBoard(game.getSizeY(), game.getSizeX());
        view.enableAllControlButtons();
    }

    @Override
    public void updateCell(int x, int y, Cell.CellType type) {
        ImageIcon lastPlayedIcon = icons[type.ordinal()];
        view.setIcon(x, y, lastPlayedIcon);
        if (!view.getMessage().getText().equals("")) {
            view.getMessage().setText(UiText.YOUR_TURN + game.playerNumberToColor(game.getPlayerTurn()) + " !");
        }
    }

    @Override
    public void gameWon(String winner) {
        StringBuilder sb = new StringBuilder();
        sb.append(UiText.GAME_OVER)
                .append(winner)
                .append(UiText.WINS_THE_GAME);
        view.getMessage().setText(sb.toString());
        restartGame();
    }

    @Override
    public void stackFull(int x) {
        view.disableControlButton(x);
    }

    @Override
    public void boardFull() {
        view.getMessage().setText(UiText.BOARD_FULL);
        restartGame();
    }

    @Override
    public void gameResigned(String winner) {
        StringBuilder sb = new StringBuilder();
        sb.append(UiText.GAME_RESIGNED)
                .append(winner)
                .append(UiText.WINS_THE_GAME);
        view.getMessage().setText(sb.toString());
        restartGame();
    }
}
