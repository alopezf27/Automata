package Proyecto.thompson;

import java.util.HashSet;
import java.util.Set;

public class AutomataThompson {

    public EstadoThompson inicial;
    public EstadoThompson aceptacion;
    public Set<EstadoThompson> estados = new HashSet<>();

    public AutomataThompson(EstadoThompson i, EstadoThompson f) {
        this.inicial = i;
        this.aceptacion = f;
        estados.add(i);
        estados.add(f);
    }

    public void agregarEstados(AutomataThompson sub) {
        estados.addAll(sub.estados);
    }
}
