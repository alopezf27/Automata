package model;

import java.util.HashSet;
import java.util.Set;

public class Nodo {

    public String simbolo;
    public Nodo izquierdo;
    public Nodo derecho;

    public boolean anulable;
    public int pos;

    // OPCIÓN B — firstpos del subárbol completo
    public Set<Integer> firstpos = new HashSet<>();

    // lastpos formal
    public Set<Integer> lastpos = new HashSet<>();

    // conjunto total del subárbol (interno)
    public Set<Integer> subtree = new HashSet<>();

    public Nodo(String simbolo, int pos) {
        this.simbolo = simbolo;
        this.pos = pos;
        this.anulable = false;

        this.firstpos.add(pos);
        this.lastpos.add(pos);
        this.subtree.add(pos);
    }

    public Nodo(String simbolo, Nodo izq, Nodo der) {
        this.simbolo = simbolo;
        this.izquierdo = izq;
        this.derecho = der;
        this.pos = -1;
    }
}
