package ca.csf.connect4;

import ca.csf.connect4.ui.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import ca.csf.connect4.Cell.CellType;

/**
 * Created by dom on 25/09/15.
 */
public class Connect4Controller {
    private static final String iconsPath = "resources/";
    private static final String[] iconsName = { "RedToken.png", "BlackToken.png" };

    private View view;
    private Game game;
    private ImageIcon[] icons;


    public Connect4Controller() throws IOException {
        view = new View(this);
        game = new Game();

        view.initBoard(game.getSizeX(), game.getSizeY());
        initIcons();
    }

    private void initIcons() throws IOException {
        int numberPlayers = Game.DEFAULT_NB_PLAYERS;
        icons = new ImageIcon[numberPlayers];
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 0; i < numberPlayers; ++i) {
            String path = pathBuilder.append(iconsPath).append(iconsName[i]).toString();
            icons[i] = new ImageIcon(ImageIO.read(new File(path)));
            pathBuilder.delete(0, pathBuilder.length());
        }
    }

    public void dropToken(int pos) {
        try {
            game.playTurn(pos);
            updateView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateView() {
        int lastCellPlayedIndex = game.getLastChangedCell().cellType.ordinal();
        ImageIcon lastPlayedIcon = icons[lastCellPlayedIndex];
        view.setIcon(game.getLastChangedCellX(),
                     game.getLastChangedCellY(),
                     lastPlayedIcon);
    }

}
