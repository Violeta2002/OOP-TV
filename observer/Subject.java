package observer;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class Subject {
    private ArrayList<Observer> observers = new ArrayList<>();
    private String state;

    /**
     * @param observer
     */
    public void addObserver(final Observer observer) {
        observers.add(observer);
    }

    /**
     * @param node
     * @return
     */
    public int notifyObservers(final ObjectNode node) {
        int err = 0;
        for (Observer observer : observers) {
            err = observer.update(node);
        }
        return err;
    }

    /**
     * @param feature
     * @param node
     * @return
     */
    public int setState(final String feature, final ObjectNode node) {
        this.state = feature;
        return notifyObservers(node);
    }

    /**
     * @return
     */
    public Object getState() {
        return this.state;
    }
}
