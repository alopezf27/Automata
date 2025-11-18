package Proyecto.arbol;

import java.util.ArrayList;
import java.util.List;

public class AutomataAFD {

    public EstadoAFD inicial;
    public List<EstadoAFD> estados = new ArrayList<>();
    public List<TransicionAFD> transiciones = new ArrayList<>();

    public void agregarEstado(EstadoAFD s) {
        estados.add(s);
    }

    public void agregarTransicion(TransicionAFD t) {
        transiciones.add(t);
    }
}
