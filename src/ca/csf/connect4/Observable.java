package ca.csf.connect4;

/**
 * Created by Anthony on 06/10/2015.
 */
public interface Observable {
    public void registerObserver(Observer observer);
    public void unregisterObserver(Observer observer);
    public void notifyObservers();
}
