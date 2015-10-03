package ca.csf.connect4;

import ca.csf.connect4.ui.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by dom on 25/09/15.
 */
public class Connect4Controller {
    private View view;
    private Game game;

    public Connect4Controller() {
        view = new View(this);
        game = new Game(2);

        view.initBoard(game.getSizeX(),game.getSizeY());
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
        String imageName = game.getLastChangedCell().cellType.toString() + ".png";
        try {
            view.setIcon(
                    game.getLastChangedCellX(),
                    game.getLastChangedCellY(),
                    new ImageIcon(ImageIO.read(new File("resources/" + imageName)))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
