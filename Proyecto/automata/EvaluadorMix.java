package Proyecto.automata;

import Proyecto.arbol.*;
import Proyecto.thompson.*;

public class EvaluadorMix implements Evaluador {

    @Override
    public boolean evaluarCadena(String cadena, String er)
            throws ExcepcionCadena {

        if (!cadena.endsWith(".")) {
            throw new ExcepcionCadena("La cadena debe terminar en punto.");
        }

        String erReal = er.contains("[") ? NormalizadorER.normalizar(er) : er;

        ConstructorArbol cons = new ConstructorArbol(erReal);
        NodoArbol raiz = cons.construir();

        CalculadoraPropiedades cp = new CalculadoraPropiedades();
        cp.calcular(raiz);

        ConstructorArbol.numerarHojas(raiz);

        CalculadoraFollowPos cfp = new CalculadoraFollowPos();
        TablaFollowPos tabla = cfp.calcular(raiz);

        ImpresorArbol.imprimir(raiz);
        tabla.imprimir();

        ConstructorThompson ct = new ConstructorThompson(erReal);
        AutomataThompson afn = ct.construir();
        ImpresorThompson.imprimir(afn);

        return true;
    }
}
