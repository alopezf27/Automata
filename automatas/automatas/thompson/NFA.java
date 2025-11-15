package automatas.thompson;

public class NFA {
    public State start;
    public State end;

    public NFA(State start, State end) {
        this.start = start;
        this.end = end;
    }
}
