package ca.csf.connect4.server;

// Shared module dependencies
import ca.csf.connect4.shared.Connect4Server;
import ca.csf.connect4.shared.GameConfig;
import ca.csf.connect4.shared.NetworkConfig;

import ca.csf.connect4.server.models.Game;
import ca.csf.connect4.shared.Observer;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.IServerListener;
import net.sf.lipermi.net.Server;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class ServerController extends Server implements Connect4Server {

    private class ServerListener implements IServerListener {

        private ServerController controller;

        public ServerListener(ServerController controller) {

        }

        @Override
        public void clientConnected(Socket socket) {
            System.out.format("[%s] : Client connected from %s%n", LocalDateTime.now().toString(), socket.getInetAddress().toString());
            this.controller.getGame().
        }

        @Override
        public void clientDisconnected(Socket socket) {
            System.out.format("[%s] : Client disconnected from %s%n", LocalDateTime.now().toString(), socket.getInetAddress().toString());
        }
    }

    public static final long DEFAULT_POLL_INTERVAL_MS = 500;

    private IServerListener serverListener;
    private CallHandler handler;
    private GameConfig config;
    private Game game;

    public ServerController(GameConfig config) throws IOException {
        this.config = config;
        this.game = new Game(this.config);
        this.handler = new CallHandler();
        try {
            this.handler.registerGlobal(Connect4Server.class, this);
            this.bind(NetworkConfig.DEFAULT_LISTEN_PORT, handler);
            this.serverListener = new ServerListener(this);
            this.addServerListener(this.serverListener);
            System.out.println("Server is listening on port " + NetworkConfig.DEFAULT_LISTEN_PORT);
            while (true) {
                Thread.sleep(DEFAULT_POLL_INTERVAL_MS);
            }
        } catch (LipeRMIException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public GameConfig getConfig() {
        return this.config;
    }

    @Override
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

    @Override
    public void connect(Observer observer) {
        this.serverListener.
    }

    @Override
    public void disconnect() {

    }

    // Split this method.
    // Have the client reset his UI.
    // Have the server reset the model.
    private void restartGame() {
        this.game = new Game(this.config);
//        game = new Game(this.config.getColumns(), this.config.getColumns());
//        game.registerObserver(this);
//        game.setTokenCountWin(nbCellsToWin);
//        view.initBoard(game.getSizeY(), game.getSizeX());
//        view.changeControlButtonsEnableState();
    }

    public Game getGame() {
        return game;
    }
}
