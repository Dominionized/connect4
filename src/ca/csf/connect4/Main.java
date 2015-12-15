package ca.csf.connect4;

import ca.csf.connect4.server.ServerController;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ServerController ctrl;
        try {
            if (args.length == 3) {
                ctrl = new ServerController(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]));
            }
            else {
                ctrl = new ServerController();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load all resources. Exiting.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Arguments are not valid");
        }
    }
}
