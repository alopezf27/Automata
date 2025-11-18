package Proyecto.arbol;

import java.util.*;

public class GeneradorAFD {

    private Map<Integer, String> simbolosHojas;

    public GeneradorAFD(Map<Integer, String> simbolosHojas) {
        this.simbolosHojas = simbolosHojas;
    }

    public AutomataAFD construir(NodoArbol raiz, TablaFollowPos tabla) {

        AutomataAFD afd = new AutomataAFD();
        Queue<EstadoAFD> pendientes = new LinkedList<>();

        // --- Estado inicial ---
        EstadoAFD inicial = new EstadoAFD("S0", raiz.firstpos);
        afd.inicial = inicial;
        afd.agregarEstado(inicial);
        pendientes.add(inicial);

        int contador = 1;

        while (!pendientes.isEmpty()) {

            EstadoAFD actual = pendientes.poll();

            // Para cada símbolo posible (letras, dígitos, punto)
            Set<Character> alfabeto = obtenerSimbolos(actual.posiciones);

            for (char a : alfabeto) {

                Set<Integer> U = new HashSet<>();

                // recolectar followpos para todas las hojas con símbolo 'a'
                for (int p : actual.posiciones) {
                    if (simbolosHojas.get(p).equals(String.valueOf(a))) {
                        U.addAll(tabla.get(p));
                    }
                }

                if (U.isEmpty()) continue;

                EstadoAFD existente = buscarEstado(afd.estados, U);

                if (existente == null) {
                    EstadoAFD nuevo = new EstadoAFD("S" + contador++, U);
                    afd.agregarEstado(nuevo);
                    pendientes.add(nuevo);
                    existente = nuevo;
                }

                afd.agregarTransicion(
                    new TransicionAFD(actual, existente, a)
                );
            }
        }

        marcarEstadosDeAceptacion(afd);

        return afd;
    }

    private EstadoAFD buscarEstado(List<EstadoAFD> lista, Set<Integer> pos) {
        for (EstadoAFD e : lista) {
            if (e.posiciones.equals(pos)) return e;
        }
        return null;
    }

    private Set<Character> obtenerSimbolos(Set<Integer> posiciones) {
        Set<Character> s = new HashSet<>();
        for (int p : posiciones) {
            String simb = simbolosHojas.get(p);
            if (simb.length() == 1) s.add(simb.charAt(0));
        }
        return s;
    }

    private void marcarEstadosDeAceptacion(AutomataAFD afd) {
        for (EstadoAFD e : afd.estados) {
            for (int p : e.posiciones) {
                if (simbolosHojas.get(p).equals(".")) {
                    e.esAceptacion = true;
                }
            }
        }
    }
}
