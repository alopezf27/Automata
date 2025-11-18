package Proyecto.arbol;

import java.util.HashSet;
import java.util.Set;

public class NodoArbol {

    public String simbolo;
    public NodoArbol izquierdo;
    public NodoArbol derecho;

    public boolean anulable = false;
    public Set<Integer> firstpos = new HashSet<>();
    public Set<Integer> lastpos = new HashSet<>();
    public Integer posicionHoja = null;

    public NodoArbol(String simbolo) {
        this.simbolo = simbolo;
    }
}
