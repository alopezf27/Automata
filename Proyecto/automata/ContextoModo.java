package Proyecto.automata;

import java.util.*;

public class ContextoModo {

    public enum Modo { NUMERICO, LOGICO, MIXTO }

    private Modo modoActual = Modo.NUMERICO;

    private Map<Modo, List<String>> expresiones = new HashMap<>();

    public ContextoModo() {
        inicializar();
    }

    private void inicializar() {

        // MODO GENERAL (NUMERICO)
        expresiones.put(Modo.NUMERICO, new ArrayList<>());
        expresiones.get(Modo.NUMERICO).add("[0-9]*.");
        expresiones.get(Modo.NUMERICO).add("[A-Za-z][A-Za-z0-9]*.");
        expresiones.get(Modo.NUMERICO).add("[A-Z][A-Za-z0-9]*.");

        // MODO LOGICO
        expresiones.put(Modo.LOGICO, new ArrayList<>());
        expresiones.get(Modo.LOGICO).add("A+B*C.");

        // MODO MIXTO
        expresiones.put(Modo.MIXTO, new ArrayList<>());
        expresiones.get(Modo.MIXTO).add("(A+B+0+1)*.");
    }

    public void cambiarModo(Modo nuevo) {
        modoActual = nuevo;
    }

    public Modo getModo() {
        return modoActual;
    }

    public List<String> getExpresionesModo() {
        return expresiones.get(modoActual);
    }

    public void cambiarER(int index, String nueva) {
        expresiones.get(modoActual).set(index, nueva);
    }

    public Map<Modo, List<String>> todas() {
        return expresiones;
    }
}
