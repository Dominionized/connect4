package ca.csf.connect4.shared;

/**
 * Created by dom on 14/12/15.
 */
public interface Connect4Server {

    GameConfig getConfig();
    void dropToken(int column);
    void resign();
    void connect(Observer observer);
    void disconnect();
}
