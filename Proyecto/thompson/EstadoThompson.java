package Proyecto.thompson;

import java.util.ArrayList;
import java.util.List;

public class EstadoThompson {
    public int id;
    public List<TransicionThompson> transiciones = new ArrayList<>();

    public EstadoThompson(int id) {
        this.id = id;
    }

    public void agregarTransicion(EstadoThompson destino, char simbolo) {
        transiciones.add(new TransicionThompson(destino, simbolo));
    }
}
