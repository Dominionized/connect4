package ca.csf.connect4.server;

import ca.csf.connect4.shared.Observable;
import ca.csf.connect4.shared.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by Anthony on 15/12/2015.
 */
public class ObserverHandler implements Observable {

    private List<Observer> observers;

    public ObserverHandler() {
        this.observers = new ArrayList<Observer>();
    }



}
