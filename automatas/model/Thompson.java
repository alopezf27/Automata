package model;

import parser.NFA;
import parser.State;

public class Thompson {

    public static NFA fromRegexTree(Nodo node) {
        State.reset(); // Reset state counter for each build
        return buildNFA(node);
    }

    private static NFA buildNFA(Nodo node) {
        if (node == null) {
            return null;
        }

        switch (node.simbolo) {
            case "·":
                return forConcatenation(node);
            case "+":
                return forUnion(node);
            case "*":
                return forKleene(node);
            default: // Symbol or epsilon
                return forSymbol(node);
        }
    }

    private static NFA forSymbol(Nodo node) {
        State start = new State();
        State end = new State();
        Character symbol = node.simbolo.equals("ε") ? null : node.simbolo.charAt(0);
        start.addTransition(new State.Transition(end, symbol));
        end.isFinal = true;
        return new NFA(start, end);
    }

    private static NFA forConcatenation(Nodo node) {
        NFA leftNFA = buildNFA(node.izquierdo);
        NFA rightNFA = buildNFA(node.derecho);

        leftNFA.end.isFinal = false;
        leftNFA.end.addTransition(new State.Transition(rightNFA.start, null)); // Epsilon transition

        return new NFA(leftNFA.start, rightNFA.end);
    }

    private static NFA forUnion(Nodo node) {
        NFA leftNFA = buildNFA(node.izquierdo);
        NFA rightNFA = buildNFA(node.derecho);

        State start = new State();
        State end = new State();

        start.addTransition(new State.Transition(leftNFA.start, null));
        start.addTransition(new State.Transition(rightNFA.start, null));

        leftNFA.end.isFinal = false;
        rightNFA.end.isFinal = false;
        leftNFA.end.addTransition(new State.Transition(end, null));
        rightNFA.end.addTransition(new State.Transition(end, null));
        
        end.isFinal = true;

        return new NFA(start, end);
    }

    private static NFA forKleene(Nodo node) {
        NFA nfa = buildNFA(node.izquierdo);

        State start = new State();
        State end = new State();

        start.addTransition(new State.Transition(nfa.start, null));
        start.addTransition(new State.Transition(end, null)); // Epsilon for zero occurrences

        nfa.end.isFinal = false;
        nfa.end.addTransition(new State.Transition(nfa.start, null)); // Loop back
        nfa.end.addTransition(new State.Transition(end, null));
        
        end.isFinal = true;

        return new NFA(start, end);
    }
}
