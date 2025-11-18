package Proyecto.arbol;

import java.util.ArrayList;
import java.util.List;

import Proyecto.automata.ExcepcionCadena;

public class ConstructorArbol {

    private String er;
    private int indice = 0;
    private int contadorPos = 1;

    public ConstructorArbol(String er) {
        this.er = er.replace(" ", "");
    }

    public NodoArbol construir() throws ExcepcionCadena {
        NodoArbol arbol = expresion();
        return arbol;
    }

    private NodoArbol expresion() throws ExcepcionCadena {
        NodoArbol nodo = termino();
        while (hay('+')) {
            consumir();
            NodoArbol der = termino();
            NodoArbol or = new NodoArbol("+");
            or.izquierdo = nodo;
            or.derecho = der;
            nodo = or;
        }
        return nodo;
    }

    private NodoArbol termino() throws ExcepcionCadena {
        NodoArbol nodo = factor();
        while (indice < er.length() && esConcatenable(er.charAt(indice))) {
            NodoArbol der = factor();
            NodoArbol concat = new NodoArbol("·");
            concat.izquierdo = nodo;
            concat.derecho = der;
            nodo = concat;
        }
        return nodo;
    }

    private NodoArbol factor() throws ExcepcionCadena {
        NodoArbol nodo = base();
        while (hay('*')) {
            consumir();
            NodoArbol estrella = new NodoArbol("*");
            estrella.izquierdo = nodo;
            nodo = estrella;
        }
        return nodo;
    }

    private NodoArbol base() throws ExcepcionCadena {
        if (hay('(')) {
            consumir();
            NodoArbol dentro = expresion();
            if (!hay(')')) throw new ExcepcionCadena("Falta )");
            consumir();
            return dentro;
        }
        return hoja();
    }

    private NodoArbol hoja() throws ExcepcionCadena {
        if (indice >= er.length()) throw new ExcepcionCadena("Fin inesperado");
        char c = er.charAt(indice++);
        NodoArbol hoja = new NodoArbol(String.valueOf(c));
        if (Character.isLetterOrDigit(c) || c == '.') {
            hoja.posicionHoja = contadorPos++;
        }
        return hoja;
    }

    private boolean hay(char c) {
        return indice < er.length() && er.charAt(indice) == c;
    }

    private void consumir() {
        indice++;
    }

    private boolean esConcatenable(char c) {
        return c != ')' && c != '+' && c != '*';
    }

    public static void numerarHojas(NodoArbol raiz) {
        List<NodoArbol> hojas = new ArrayList<>();
        recolectarHojas(raiz, hojas);

        int contador = 1;
        for (NodoArbol hoja : hojas) {
            hoja.posicionHoja = contador++;
        }
    }

    private static void recolectarHojas(NodoArbol nodo, List<NodoArbol> hojas) {
        if (nodo == null) return;

        if (esHoja(nodo)) {
            hojas.add(nodo);
            return;
        }

        recolectarHojas(nodo.izquierdo, hojas);
        recolectarHojas(nodo.derecho, hojas);
    }

    // Método auxiliar para saber si un nodo es hoja
    private static boolean esHoja(NodoArbol nodo) {
        return nodo.izquierdo == null && nodo.derecho == null;
    }

}