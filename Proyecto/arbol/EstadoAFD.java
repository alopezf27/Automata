package Proyecto.arbol;

import java.util.Set;

public class EstadoAFD {

    public String nombre;
    public Set<Integer> posiciones;
    public boolean esAceptacion = false;

    public EstadoAFD(String nombre, Set<Integer> posiciones) {
        this.nombre = nombre;
        this.posiciones = posiciones;
    }
}
