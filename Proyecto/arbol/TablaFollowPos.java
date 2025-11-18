package Proyecto.arbol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TablaFollowPos {

    private Map<Integer, Set<Integer>> tabla = new HashMap<>();

    public void agregar(int pos, int valor) {
        tabla.putIfAbsent(pos, new HashSet<>());
        tabla.get(pos).add(valor);
    }

    public void agregar(int pos, Set<Integer> valores) {
        tabla.putIfAbsent(pos, new HashSet<>());
        tabla.get(pos).addAll(valores);
    }

    public Set<Integer> get(int pos) {
        return tabla.getOrDefault(pos, new HashSet<>());
    }

    public void imprimir() {
        System.out.println("\n=== FOLLOWPOS ===");
        for (int pos : tabla.keySet()) {
            System.out.println(pos + " → " + tabla.get(pos));
        }
        System.out.println("=================\n");
    }
}
