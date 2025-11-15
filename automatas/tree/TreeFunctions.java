package tree;

import java.util.*;
import model.Nodo;

public class TreeFunctions {

    // ----------------------------------------------------------
    // 1) AGREGAR NODO RAÍZ · Y HOJA FINAL #
    // ----------------------------------------------------------
    public static Nodo attachEndMarker(Nodo original, int lastPos) {
        Nodo hash = new Nodo("#", lastPos + 1);
        return new Nodo("·", original, hash);
    }

    // ----------------------------------------------------------
    // 2) CALCULAR FIRSTPOS / LASTPOS / ANULABLE / SUBTREE
    // ----------------------------------------------------------
    public static void computeProperties(Nodo nodo) {
        if (nodo == null) return;

        // hojas
        if (nodo.izquierdo == null && nodo.derecho == null && nodo.pos != -1) {
            nodo.anulable = false;
            nodo.firstpos = new HashSet<>();
            nodo.lastpos  = new HashSet<>();
            nodo.subtree  = new HashSet<>();

            if (!nodo.simbolo.equals("ε")) {
                nodo.firstpos.add(nodo.pos);
                nodo.lastpos.add(nodo.pos);
                nodo.subtree.add(nodo.pos);
            }
            return;
        }

        // recursivo
        computeProperties(nodo.izquierdo);
        computeProperties(nodo.derecho);

        nodo.firstpos = new HashSet<>();
        nodo.lastpos  = new HashSet<>();
        nodo.subtree  = new HashSet<>();

        // unir subtree
        if (nodo.izquierdo != null) nodo.subtree.addAll(nodo.izquierdo.subtree);
        if (nodo.derecho  != null) nodo.subtree.addAll(nodo.derecho.subtree);

        String op = nodo.simbolo;

        switch (op) {

            // -------------------------
            // CONCATENACIÓN (·)
            // -------------------------
            case "·":
                nodo.anulable = nodo.izquierdo.anulable && nodo.derecho.anulable;

                // firstpos
                if (nodo.izquierdo.anulable)
                    nodo.firstpos.addAll(union(nodo.izquierdo.firstpos, nodo.derecho.firstpos));
                else
                    nodo.firstpos.addAll(nodo.izquierdo.firstpos);

                // lastpos
                if (nodo.derecho.anulable)
                    nodo.lastpos.addAll(union(nodo.izquierdo.lastpos, nodo.derecho.lastpos));
                else
                    nodo.lastpos.addAll(nodo.derecho.lastpos);

                break;

            // -------------------------
            // ALTERNANCIA (+ | |)
            // -------------------------
            case "+":
                nodo.anulable =
                        (nodo.izquierdo != null && nodo.izquierdo.anulable) ||
                        (nodo.derecho  != null && nodo.derecho.anulable);

                if (nodo.izquierdo != null) {
                    nodo.firstpos.addAll(nodo.izquierdo.firstpos);
                    nodo.lastpos.addAll(nodo.izquierdo.lastpos);
                }
                if (nodo.derecho != null) {
                    nodo.firstpos.addAll(nodo.derecho.firstpos);
                    nodo.lastpos.addAll(nodo.derecho.lastpos);
                }
                break;

            // -------------------------
            // KLEENE (*)
            // -------------------------
            case "*":
                nodo.anulable = true;
                nodo.firstpos.addAll(nodo.izquierdo.firstpos);
                nodo.lastpos.addAll(nodo.izquierdo.lastpos);
                break;

            // -------------------------
            // OPERADORES UNARIOS EPSILON
            // -------------------------
            case "ε":
                nodo.anulable = true;
                break;
        }
    }

    private static Set<Integer> union(Set<Integer> a, Set<Integer> b) {
        Set<Integer> out = new HashSet<>(a);
        out.addAll(b);
        return out;
    }

    // ----------------------------------------------------------
    // 3) NUMERA LAS HOJAS (si el parser no lo hizo)
    // ----------------------------------------------------------
    public static int assignPositions(Nodo nodo, int next) {
        if (nodo == null) return next;

        if (nodo.izquierdo == null && nodo.derecho == null) {
            if (nodo.simbolo.equals("ε")) {
                nodo.pos = -1;
            } else {
                nodo.pos = next;
                next++;
            }
            return next;
        }

        next = assignPositions(nodo.izquierdo, next);
        next = assignPositions(nodo.derecho, next);
        return next;
    }

    // ----------------------------------------------------------
    // 4) DEVUELVE UNA REPRESENTACIÓN DE TEXTO PARA IMPRIMIR
    // ----------------------------------------------------------
    public static String compactLabel(Nodo n) {
        String fp  = n.firstpos.toString();
        String lp  = n.lastpos.toString();
        String op  = n.simbolo;
        return "{" + fp + "}" + op + "{" + lp + "}";
    }
}
