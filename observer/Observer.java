package observer;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Observer {
    protected Subject subject;

    /**
     *
     * @param node
     * @return
     */
    public abstract int update(ObjectNode node);
}

