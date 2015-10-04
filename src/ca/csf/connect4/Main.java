package ca.csf.connect4;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by dom on 25/09/15.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Connect4Controller ctrl = new Connect4Controller();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load all resources. Exiting.");
            System.exit(1);
        }
    }
}
