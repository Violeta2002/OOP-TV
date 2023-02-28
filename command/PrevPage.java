package command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import static pages.CurrentPage.getInstance;

public final class PrevPage implements Command {
    private String movie;

    private String feature;
    private ObjectNode node;

    public PrevPage(final String movie, final String feature, final ObjectNode node) {
        this.movie = movie;
        this.feature = feature;
        this.node = node;
    }

    @Override
    public int execute() {
        return back();
    }

    private int back() {
        String backPage = CommandHistory.getInstance().getHistory().pop();
        if (backPage.equals("Homepage autentificat")
                || backPage.equals("Homepage neautentificat")) {
            return 1;
        }
        if (backPage.equals("movies")) {
            if (CommandHistory.getInstance().getHistory().peek().equals("Homepage autentificat")) {
                getInstance().setName("Homepage autentificat");
                CommandHistory.getInstance().getHistory().pop();

                return 2;
            }
            if (CommandHistory.getInstance().getHistory().peek().equals("upgrades")) {
                NextPage nextPage = new NextPage("upgrades", this.movie, this.feature, node);
                nextPage.execute();
                CommandHistory.getInstance().getHistory().pop();
                return 2;
            }
            if (CommandHistory.getInstance().getHistory().peek().equals("login")) {
                NextPage nextPage;
                nextPage = new NextPage("Homepage autentificat", this.movie, this.feature, node);
                nextPage.execute();
                CommandHistory.getInstance().getHistory().pop();

                return 2;
            }
            return 2;
        }
        if (backPage.equals("see details")) {
            if (CommandHistory.getInstance().getHistory().peek().equals("movies")) {
                NextPage nextPage = new NextPage("movies", this.movie, this.feature, node);
                nextPage.execute();
                CommandHistory.getInstance().getHistory().pop();
                return 3;
            }
            if (CommandHistory.getInstance().getHistory().peek().equals("upgrades")) {
                NextPage nextPage = new NextPage("upgrades", this.movie, this.feature, node);
                nextPage.execute();
                CommandHistory.getInstance().getHistory().pop();
                return 2;
            }
            return 2;
        }
        if (backPage.equals("upgrades")) {
            if (CommandHistory.getInstance().getHistory().peek().equals("see details")) {
                getInstance().setName("see details");
                CommandHistory.getInstance().getHistory().pop();
                return 2;
            }
            if (CommandHistory.getInstance().getHistory().peek().equals("movies")) {
                NextPage nextPage = new NextPage("movies", this.movie, this.feature, node);
                nextPage.execute();
                CommandHistory.getInstance().getHistory().pop();
                return 3;
            }
            if (CommandHistory.getInstance().getHistory().peek().equals("Homepage autentificat")) {
                getInstance().setName("Homepage autentificat");
                CommandHistory.getInstance().getHistory().pop();
                return 2;
            }
            if (CommandHistory.getInstance().getHistory().peek().equals("register")) {
                getInstance().setName("Homepage autentificat");
                CommandHistory.getInstance().getHistory().pop();
                return 2;
            }
            return 2;
        }
        return 1;
    }
}

