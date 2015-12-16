package ca.csf.connect4.shared;

/**
 * Created by Anthony on 06/10/2015.
 */
public interface Observable {
    public void registerObserver(Observer observer);
    public void unregisterObserver(Observer observer) throws Exception;
}
