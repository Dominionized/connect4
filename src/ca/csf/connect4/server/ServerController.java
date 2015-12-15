package ca.csf.connect4.server;

// Shared module dependencies
import ca.csf.connect4.shared.Connect4Server;
import ca.csf.connect4.shared.models.Cell;

import ca.csf.connect4.server.models.Game;
import ca.csf.connect4.client.ui.UiText;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.IServerListener;
import net.sf.lipermi.net.Server;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class ServerController extends Server implements Connect4Server {

    private class ServerListener implements IServerListener {
        @Override
        public void clientConnected(Socket socket) {
            System.out.format("[%s] : Client connected from %s%n", LocalDateTime.now().toString(), socket.getInetAddress().toString());
        }

        @Override
        public void clientDisconnected(Socket socket) {
            System.out.format("[%s] : Client disconnected from %s%n", LocalDateTime.now().toString(), socket.getInetAddress().toString());
        }
    }

    private CallHandler handler;
    private ServerConfig config;
    private Game game;

    public ServerController(ServerConfig config) throws IOException {
        this.config = config;
        try {
            this.handler.registerGlobal(Connect4Server.class, this);
            this.bind(ServerConfig.DEFAULT_LISTEN_PORT, handler);
            this.addServerListener(new ServerListener());
            while (true) {
                Thread.sleep(ServerConfig.DEFAULT_POLL_INTERVAL_MS);
            }
        } catch (LipeRMIException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void dropToken(int pos) {
        try {
            game.dropToken(pos);
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
        this.game = new Game(this.config.getColumns(),
                             this.config.getRows(),
                             this.config.getTokenCountWin());
        this.
//        game = new Game(this.config.getColumns(), this.config.getColumns());
//        game.registerObserver(this);
//        game.setTokenCountWin(nbCellsToWin);
//        view.initBoard(game.getSizeY(), game.getSizeX());
//        view.enableAllControlButtons();
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
