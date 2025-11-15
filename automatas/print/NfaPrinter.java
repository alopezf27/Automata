package print;

import parser.NFA;
import parser.State;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class NfaPrinter {

    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public static void print(NFA nfa) {
        if (nfa == null || nfa.start == null) {
            System.out.println("NFA inválido.");
            return;
        }

        System.out.println("\n" + BOLD + "=== NFA (Thompson) ===" + RESET);

        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();

        queue.add(nfa.start);
        visited.add(nfa.start);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // If a state has no transitions, it's a sink. Note it.
            if (current.transitions.isEmpty()) {
                String finalMarker = current.isFinal ? " " + YELLOW + "[FINAL]" + RESET : "";
                System.out.println(CYAN + "(S" + current.id + ")" + RESET + finalMarker + " (estado sin transiciones de salida)");
            }

            // Print all transitions from the current state
            for (State.Transition t : current.transitions) {
                String symbol = t.symbol == null ? "ε" : String.valueOf(t.symbol);
                String finalMarker = t.to.isFinal ? " " + YELLOW + "[FINAL]" + RESET : "";

                System.out.println(
                    CYAN + "(S" + current.id + ")" + RESET +
                    " --" + symbol + "--> " +
                    CYAN + "(S" + t.to.id + ")" + RESET +
                    finalMarker
                );

                if (!visited.contains(t.to)) {
                    visited.add(t.to);
                    queue.add(t.to);
                }
            }
        }
    }
}