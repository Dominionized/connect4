package ca.csf.connect4.client;

import ca.csf.connect4.client.ui.View;
import ca.csf.connect4.server.models.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

/**
 * Created by dom on 14/12/15.
 */
public class ClientController {
    private static final String iconsPath = "/resources/";
    private static final String[] iconsName = { "RedToken.png", "BlackToken.png" };

    private View view;

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

}
