package Proyecto.thompson;

import Proyecto.automata.ExcepcionCadena;
import java.util.Stack;

public class ConstructorThompson {

    private String er;
    private int indice = 0;
    private int contadorEstados = 1;

    public ConstructorThompson(String er) {
        this.er = er.replace(" ", "");
    }

    public AutomataThompson construir() throws ExcepcionCadena {
        Stack<AutomataThompson> pila = new Stack<>();

        while (indice < er.length()) {
            char c = er.charAt(indice++);

            switch (c) {
                case '+':
                    unir(pila);
                    break;

                case '·':
                    concatenar(pila);
                    break;

                case '*':
                    estrella(pila);
                    break;

                case '(':
                case ')':
                    // Los ignoramos aquí; se manejan en el parser del árbol
                    break;

                default:
                    pila.push(simbolo(c));
                    break;
            }
        }

        if (pila.size() != 1) {
            throw new ExcepcionCadena("Expresión inválida para Thompson");
        }

        return pila.pop();
    }


    // ---------- OPERACIONES DE THOMPSON ----------

    private AutomataThompson simbolo(char c) {
        EstadoThompson i = new EstadoThompson(contadorEstados++);
        EstadoThompson f = new EstadoThompson(contadorEstados++);
        i.agregarTransicion(f, c);

        return new AutomataThompson(i, f);
    }

    private void concatenar(Stack<AutomataThompson> pila) throws ExcepcionCadena {
        if (pila.size() < 2) throw new ExcepcionCadena("Falta operando en concatenación");

        AutomataThompson b = pila.pop();
        AutomataThompson a = pila.pop();

        // enlace epsilon del fin de A al inicio de B
        a.aceptacion.agregarTransicion(b.inicial, TransicionThompson.EPSILON);

        AutomataThompson nuevo = new AutomataThompson(a.inicial, b.aceptacion);
        nuevo.agregarEstados(a);
        nuevo.agregarEstados(b);

        pila.push(nuevo);
    }

    private void unir(Stack<AutomataThompson> pila) throws ExcepcionCadena {
        if (pila.size() < 2) throw new ExcepcionCadena("Falta operando en unión");

        AutomataThompson b = pila.pop();
        AutomataThompson a = pila.pop();

        EstadoThompson i = new EstadoThompson(contadorEstados++);
        EstadoThompson f = new EstadoThompson(contadorEstados++);

        i.agregarTransicion(a.inicial, TransicionThompson.EPSILON);
        i.agregarTransicion(b.inicial, TransicionThompson.EPSILON);

        a.aceptacion.agregarTransicion(f, TransicionThompson.EPSILON);
        b.aceptacion.agregarTransicion(f, TransicionThompson.EPSILON);

        AutomataThompson nuevo = new AutomataThompson(i, f);
        nuevo.agregarEstados(a);
        nuevo.agregarEstados(b);
        nuevo.estados.add(i);
        nuevo.estados.add(f);

        pila.push(nuevo);
    }

    private void estrella(Stack<AutomataThompson> pila) throws ExcepcionCadena {
        if (pila.isEmpty()) throw new ExcepcionCadena("Falta operando en estrella");

        AutomataThompson a = pila.pop();

        EstadoThompson i = new EstadoThompson(contadorEstados++);
        EstadoThompson f = new EstadoThompson(contadorEstados++);

        i.agregarTransicion(a.inicial, TransicionThompson.EPSILON);
        i.agregarTransicion(f, TransicionThompson.EPSILON);

        a.aceptacion.agregarTransicion(a.inicial, TransicionThompson.EPSILON);
        a.aceptacion.agregarTransicion(f, TransicionThompson.EPSILON);

        AutomataThompson nuevo = new AutomataThompson(i, f);
        nuevo.agregarEstados(a);
        nuevo.estados.add(i);
        nuevo.estados.add(f);

        pila.push(nuevo);
    }
}
