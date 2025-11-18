package Proyecto.automata;

import arbol.*;
import thompson.*;

public class Evaluador {

    public boolean evaluarCadena(String cadena, String er)
            throws ExcepcionCadena {

        // 1. Construcción del árbol
        ConstructorArbol cons = new ConstructorArbol(er);
        NodoArbol raiz = cons.construir();

        // 2. Propiedades
        CalculadoraPropiedades cp = new CalculadoraPropiedades();
        cp.calcular(raiz);

        // 3. Followpos
        CalculadoraFollowPos cfp = new CalculadoraFollowPos();
        TablaFollowPos tabla = cfp.calcular(raiz);

        // 4. Imprimir árbol y datos
        ImpresorArbol.imprimir(raiz);
        tabla.imprimir();

        // 5. Thompson
        ConstructorThompson ct = new ConstructorThompson(er);
        AutomataThompson afn = ct.construir();
        ImpresorThompson.imprimir(afn);

        // 6. Validación simple (temporal)
        if (!cadena.endsWith(".")) {
            throw new ExcepcionCadena("La cadena debe terminar en punto.");
        }

        // Pendiente: integración con AFD
        return true;
    }
}
