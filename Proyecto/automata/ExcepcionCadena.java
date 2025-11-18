package Proyecto.automata;

public class ExcepcionCadena extends Exception {

    public ExcepcionCadena(String mensaje) {
        super(mensaje);
    }

    public ExcepcionCadena(String mensaje, int posicion, String esperado) {
        super(mensaje + " (posición " + posicion + ", se esperaba: " + esperado + ")");
    }
}
