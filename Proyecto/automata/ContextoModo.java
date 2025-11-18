package Proyecto.automata;

import java.util.*;

public class ContextoModo {

    public enum Modo { NUMERICO, LOGICO, MIXTO }

    private Modo modoActual = Modo.NUMERICO;

    private Map<Modo, List<String>> expresiones = new HashMap<>();
    private Map<Modo, String> descripcionModo = new HashMap<>();

    public ContextoModo() {
        inicializar();
    }

    private void inicializar() {
        expresiones.put(Modo.NUMERICO, new ArrayList<>());
        expresiones.put(Modo.LOGICO, new ArrayList<>());
        expresiones.put(Modo.MIXTO, new ArrayList<>());

        // Default
        expresiones.get(Modo.NUMERICO).add("(0+1+2+3+4+5+6+7+8+9)+.");

        descripcionModo.put(Modo.NUMERICO,
                "Cadenas numéricas que:\n" +
                " - Usan solo dígitos 0-9\n" +
                " - Deben terminar con un punto (.)\n" +
                " - Mínimo un dígito");

        // Lógico
        expresiones.get(Modo.LOGICO).add("A+B.C*.");

        descripcionModo.put(Modo.LOGICO,
                "Cadenas lógicas con operadores:\n" +
                " - Letras A-Z\n" +
                " - Operadores +, concatenación y *\n" +
                " - Deben terminar en punto");

        // Mix
        expresiones.get(Modo.MIXTO).add("(0+1+A+B+C)*.");

        descripcionModo.put(Modo.MIXTO,
                "Modo combinado:\n" +
                " - Números + Letras + Operadores\n" +
                " - Debe terminar en punto");
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

    public String getERActiva() {
        return expresiones.get(modoActual).get(0);
    }

    public void cambiarER(int index, String nueva) {
        expresiones.get(modoActual).set(index, nueva);
    }

    public void agregarER(String nueva) {
        expresiones.get(modoActual).add(nueva);
    }

    public String getDescripcionModo() {
        return descripcionModo.get(modoActual);
    }

    public Map<Modo, List<String>> todas() {
        return expresiones;
    }
}
