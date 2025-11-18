package Proyecto.arbol;

public class TransicionAFD {

    public EstadoAFD origen;
    public EstadoAFD destino;
    public char simbolo;

    public TransicionAFD(EstadoAFD o, EstadoAFD d, char s) {
        origen = o;
        destino = d;
        simbolo = s;
    }

    @Override
    public String toString() {
        return origen.nombre + " --" + simbolo + "--> " + destino.nombre;
    }
}
