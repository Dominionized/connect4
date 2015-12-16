package ca.csf.connect4.client;

import ca.csf.connect4.client.ui.UiText;
import ca.csf.connect4.client.ui.View;
import ca.csf.connect4.server.models.Game;
import ca.csf.connect4.shared.Connect4Server;
import ca.csf.connect4.shared.GameConfig;
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
            JOptionPane.showMessageDialog(null,
                    UiText.Errors.CONNECTION_TO_SERVER_LOST,
                    UiText.Errors.ERROR_OCCURED,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String hostname;
    private GameConfig gameConfig;
    private View view;
    private CallHandler handler;
    private Client client;
    private Connect4Server server;

    private int observerId;

    @Override
    public void setObserverId(int id) {
        this.observerId = id;
    }

    public ClientController(String hostname) {
        this.hostname = hostname;
        this.view = new View(this);
        this.handler = new CallHandler();
        try {
            this.client = new Client(this.hostname, NetworkConfig.DEFAULT_LISTEN_PORT, this.handler);
            this.client.addClientListener(new ClientListener());
            this.server = client.getGlobal(Connect4Server.class);
            this.server.
            this.gameConfig = getConfig();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    UiText.Errors.CONNECTION_TO_SERVER_FAILED,
                    UiText.Errors.ERROR_OCCURED,
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }

    @Override
    public GameConfig getConfig() {
        return this.server.getConfig();
    }

    @Override
    public void dropToken(int column) {
        this.server.dropToken(column);
    }

    @Override
    public void resign() {
        this.server.resign();
    }


}
