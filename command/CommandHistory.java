package command;

import java.util.Stack;

public final class CommandHistory {
    private Stack<String> history = new Stack<>();
    private int current = 0;

    private static CommandHistory instance = null;

    private CommandHistory() {
    }

    /**
     * @return the instance of the CommandHistory class
     */
    public static CommandHistory getInstance() {
        if (instance == null) {
            instance = new CommandHistory();
        }
        return instance;
    }

    public Stack<String> getHistory() {
        return history;
    }

}
