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

        @Override
        public void clientConnected(Socket socket) {
            System.out.format("[%s] : Client connected from %s%n", LocalDateTime.now().toString(), socket.getInetAddress().toString());
        }

        @Override
        public void clientDisconnected(Socket socket) {
            System.out.format("[%s] : Client disconnected from %s%n", LocalDateTime.now().toString(), socket.getInetAddress().toString());
        }
    }

    public static final long DEFAULT_POLL_INTERVAL_MS = 500;

    private CallHandler handler;
    private IServerListener serverListener;

    private GameConfig config;
    private Game game;

    public ServerController(GameConfig config) throws IOException {
        this.config = config;
        this.game = new Game(this.config);
        this.handler = new CallHandler();
        try {
            this.handler.registerGlobal(Connect4Server.class, this);
            this.bind(NetworkConfig.DEFAULT_LISTEN_PORT, handler);
            this.serverListener = new ServerListener();
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

    @Override
    public void resign() {
        game.resign();
    }

    @Override
    public int registerObserver(Observer observer) {
        return this.game.registerObserver(observer);
    }

    @Override
    public void unregisterObserver(int id) {
        this.game.unregisterObserver(id);
    }

}
