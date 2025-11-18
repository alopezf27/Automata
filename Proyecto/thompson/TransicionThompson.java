package Proyecto.thompson;

public class TransicionThompson {

    public static final char EPSILON = 'ε';

    public EstadoThompson destino;
    public char simbolo;

    public TransicionThompson(EstadoThompson destino, char simbolo) {
        this.destino = destino;
        this.simbolo = simbolo;
    }
}
