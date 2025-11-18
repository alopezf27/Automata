package Proyecto.arbol;

import java.util.Set;

public class CalculadoraFollowPos {

    private TablaFollowPos tabla = new TablaFollowPos();

    public TablaFollowPos calcular(NodoArbol nodo) {
        recorrer(nodo);
        return tabla;
    }

    private void recorrer(NodoArbol nodo) {
        if (nodo == null) return;

        recorrer(nodo.izquierdo);
        recorrer(nodo.derecho);

        if ("·".equals(nodo.simbolo)) {
            for (Integer i : nodo.izquierdo.lastpos) {
                tabla.agregar(i, nodo.derecho.firstpos);
            }
        }

        if ("*".equals(nodo.simbolo)) {
            for (Integer i : nodo.lastpos) {
                tabla.agregar(i, nodo.firstpos);
            }
        }
    }
}
