package Proyecto.automata;

import Proyecto.arbol.*;
import Proyecto.thompson.*;

public class EvaluadorLog implements Evaluador {

    @Override
    public boolean evaluarCadena(String cadena, String er)
            throws ExcepcionCadena {

        if (!cadena.endsWith(".")) {
            throw new ExcepcionCadena("La cadena debe terminar en punto.");
        }

        ConstructorArbol cons = new ConstructorArbol(er);
        NodoArbol raiz = cons.construir();

        CalculadoraPropiedades cp = new CalculadoraPropiedades();
        cp.calcular(raiz);

        ConstructorArbol.numerarHojas(raiz);

        CalculadoraFollowPos cfp = new CalculadoraFollowPos();
        TablaFollowPos tabla = cfp.calcular(raiz);

        ImpresorArbol.imprimir(raiz);
        tabla.imprimir();

        ConstructorThompson ct = new ConstructorThompson(er);
        AutomataThompson afn = ct.construir();
        ImpresorThompson.imprimir(afn);

        return true;
    }
}
