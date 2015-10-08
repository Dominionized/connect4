package ca.csf.connect4;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Connect4Controller ctrl;
        try {
            if (args.length == 3) {
                ctrl = new Connect4Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]));
            }
            else {
                ctrl = new Connect4Controller();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load all resources. Exiting.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("Arguments are not valid");
        }
    }
}
