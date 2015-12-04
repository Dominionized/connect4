package ca.csf.connect4.client;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ClientController ctrl;
        try {
            if (args.length == 3) {
                ctrl = new ClientController(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]));
            }
            else {
                ctrl = new ClientController();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load all resources. Exiting.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Arguments are not valid");
        }
    }
}
