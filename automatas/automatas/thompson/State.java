package automatas.thompson;

import java.util.ArrayList;
import java.util.List;

public class State {
    public static int lastId = 0;
    public int id;
    public List<Transition> transitions = new ArrayList<>();
    public boolean isFinal = false;

    public State() {
        this.id = lastId++;
    }

    public void addTransition(Transition t) {
        transitions.add(t);
    }

    public static void reset() {
        lastId = 0;
    }
    
    // Nested Transition class
    public static class Transition {
        public final Character symbol; // null for epsilon
        public final State to;

        public Transition(State to, Character symbol) {
            this.symbol = symbol;
            this.to = to;
        }
    }
}
