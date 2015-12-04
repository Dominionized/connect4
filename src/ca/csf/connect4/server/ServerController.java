package ca.csf.connect4.server;

import ca.csf.connect4.server.model.Game;
import ca.csf.connect4.shared.NetConfig;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Server;

public class ServerController extends Server{
    private static final int SLEEP_DURATION_MS = 500;

    private Game game;

    public ServerController() {
        this(NetConfig.DEFAULT_PORT);
    }
    public ServerController(int port) {
        try {
            CallHandler callHandler = new CallHandler();
            callHandler.registerGlobal(IServer.class, this);
            this.bind(port, callHandler);

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

    public static void main(String[] args) {
        if (args.length == 1) {
            new ServerController(Integer.parseInt(args[0]));
        }
        else {
            new ServerController();
        }

    }
}
