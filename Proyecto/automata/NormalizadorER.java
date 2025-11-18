package Proyecto.automata;

import java.util.*;

public class NormalizadorER {

    public static String normalizar(String er) {

        if (!er.contains("[")) {
            return er;
        }

        StringBuilder out = new StringBuilder();

        for (int i = 0; i < er.length(); i++) {
            char c = er.charAt(i);

            if (c == '[') {
                int cierre = er.indexOf(']', i);
                String contenido = er.substring(i + 1, cierre);
                out.append("(");

                List<String> tokens = expandir(contenido);
                out.append(String.join("+", tokens));

                out.append(")");
                i = cierre;
            } else {
                out.append(c);
            }
        }

        return out.toString();
    }

    private static List<String> expandir(String contenido) {
        List<String> lista = new ArrayList<>();

        for (int i = 0; i < contenido.length(); i++) {
            char c = contenido.charAt(i);

            if (i + 2 < contenido.length() && contenido.charAt(i + 1) == '-') {
                char inicio = c;
                char fin = contenido.charAt(i + 2);
                for (char x = inicio; x <= fin; x++) {
                    lista.add(String.valueOf(x));
                }
                i += 2;
            } else {
                lista.add(String.valueOf(c));
            }
        }

        return lista;
    }
}
