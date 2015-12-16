package ca.csf.connect4.client;

import ca.csf.connect4.client.ui.UiText;
import ca.csf.connect4.client.ui.View;
import ca.csf.connect4.server.models.Game;
import ca.csf.connect4.shared.Connect4Server;
import ca.csf.connect4.shared.NetworkConfig;
import ca.csf.connect4.shared.models.Cell;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Client;
import net.sf.lipermi.net.IClientListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

/**
 * Created by dom on 14/12/15.
 */
public class ClientController implements Connect4Server {

    private class ClientListener implements IClientListener {
        @Override
        public void disconnected() {
            // Handle disconnection
        }
    }

    private String hostname;
    private View view;
    private CallHandler handler;
    private Client client;
    private Connect4Server server;

    public ClientController(String hostname) {
        this.hostname = hostname;
        this.view = new View(this);
        this.handler = new CallHandler();
        try {
            this.client = new Client(this.hostname, NetworkConfig.DEFAULT_LISTEN_PORT, this.handler);
            this.client.addClientListener(new ClientListener());
            this.server = client.getGlobal(Connect4Server.class);
        } catch (IOException e) {
            // Handle connection error
            e.printStackTrace();
        }

    }

    @Override
    public void dropToken(int column) {
        this.server.dropToken(column);
    }

    @Override
    public void resign() {

    }


}
