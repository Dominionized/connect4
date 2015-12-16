package ca.csf.connect4.client;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by Anthony on 15/12/2015.
 */
public class ClientLauncher {

    private static final String[] ERRORS = new String[] {
        "Could not fetch localhost IP address. No IP address specified to connect to server. Exiting." };

    public ClientLauncher(String[] args) {
        if (args.length == 0) {
            try {
                exec();
            } catch (UnknownHostException e) {
                System.out.println(ERRORS[1]);
                System.exit(1);
            }
        }
        exec(args[0]);
    }

    private void exec() throws UnknownHostException {
        String localhost = Inet4Address.getLocalHost().toString();
        exec(localhost);
    }

    private void exec(String hostname) {
        new ClientController(hostname);
    }

    public static void main(String[] args) {
        new ClientLauncher(args);
    }
}
