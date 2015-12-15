package ca.csf.connect4.server;

import ca.csf.connect4.server.models.Cell;
import ca.csf.connect4.server.models.Game;
import ca.csf.connect4.Observer;
import ca.csf.connect4.client.ui.UiText;
import ca.csf.connect4.client.ui.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class ServerController implements Server{

    private ServerConfig config;
    private Game game;

    public ServerController(ServerConfig config) throws IOException {
        this.config = config;
    }

    public void dropToken(int pos) {
        try {
            game.playTurn(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Notify all observers of winner.
    public void resign() {
        game.resign();
    }

    // Split this method.
    // Have the client reset his UI.
    // Have the server reset the model.
    private void restartGame() {
        game = new Game(this.config.getColumns(), this.config.getColumns());
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
