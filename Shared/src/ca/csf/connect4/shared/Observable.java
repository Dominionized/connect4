package ca.csf.connect4.shared;

/**
 * Created by Anthony on 06/10/2015.
 */
public interface Observable {
    int registerObserver(Observer observer);
    void unregisterObserver(int id) throws Exception;
}
