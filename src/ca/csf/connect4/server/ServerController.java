package ca.csf.connect4.server;

import ca.csf.connect4.client.Observer;
import ca.csf.connect4.server.model.Game;
import ca.csf.connect4.shared.NetConfig;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.IServerListener;
import net.sf.lipermi.net.Server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController extends Server implements IServer{
    private static final int SLEEP_DURATION_MS = 500;

    private Game game;
    private int sizeX;
    private int sizeY;
    private int nbCellsToWin;

    private List<Observer> players;

    public ServerController() {
        this(NetConfig.DEFAULT_PORT);
    }

    public ServerController(int port) {
        players = new ArrayList<Observer>();

        game = new Game(sizeX, sizeY);
        game.setNbCellsToWin(nbCellsToWin);

        try {
            CallHandler callHandler = new CallHandler();
            callHandler.registerGlobal(IServer.class, this);
            this.bind(port, callHandler);
            this.addServerListener(new ServerListener());

            while (true) {
                Thread.sleep(SLEEP_DURATION_MS);
            }

        } catch (LipeRMIException e) {
            System.err.println("An error occured using LipeRMI.");
            e.printStackTrace();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private class ServerListener implements IServerListener {

        @Override
        public void clientConnected(Socket socket) {
            System.out.println(socket.getInetAddress() + " connected.");
        }

        @Override
        public void clientDisconnected(Socket socket) {
            System.out.println(socket.getInetAddress() + " disconnected.");
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

    @Override
    public void restartGame() {
        game = new Game(sizeX, sizeY);
        game.registerObserver(this);
        game.setNbCellsToWin(nbCellsToWin);
    }

    @Override
    public int getSizeY() {
        return sizeY;
    }

    @Override
    public int getSizeX() {
        return sizeX;
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            new ServerController(Integer.parseInt(args[0]));
        }
        else {
            new ServerController();
        }

    }
}
