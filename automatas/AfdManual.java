
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

import parser.NFA;
import parser.RegexParser;
import parser.RegexParser.RegexParseException;
import print.ArbolPrinter;
import model.Nodo;
import model.Thompson;
import print.NfaPrinter;

public class AfdManual {

    // ============================================================
    // COLORES ANSI
    // ============================================================
    private static final String RESET = "\u001B[0m";
    private static final String RED   = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BOLD  = "\u001B[1m";

    // ============================================================
    // UTILERÍAS
    // ============================================================

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLower(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean isUpper(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private static boolean isLetter(char c) {
        return isLower(c) || isUpper(c);
    }

    private static boolean isAlnum(char c) {
        return isLetter(c) || isDigit(c);
    }

    private static String preprocess(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r')
                sb.append(c);
        }
        return sb.toString();
    }

    // ============================================================
    // AFDs MANUALES
    // ============================================================

    public static boolean afdNumeric(String input) {
        String s = preprocess(input);
        if (s.isEmpty()) return false;

        char state = 'S';

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (state == 'S') {
                if (isDigit(ch)) state = 'N';
                else return false;
            }
            else if (state == 'N') {
                if (isDigit(ch)) state = 'N';
                else if (ch == '.') state = 'A';
                else return false;
            }
            else if (state == 'A') {
                return false;
            }
        }
        return state == 'A';
    }

    public static boolean afdIdentifier(String input) {
        String s = preprocess(input);
        if (s.isEmpty()) return false;

        char state = 'S';

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (state == 'S') {
                if (isLetter(ch)) state = 'I';
                else return false;
            }
            else if (state == 'I') {
                if (isAlnum(ch)) state = 'I';
                else if (ch == '.') state = 'A';
                else return false;
            }
            else if (state == 'A') {
                return false;
            }
        }
        return state == 'A';
    }

    public static boolean afdStartsUpper(String input) {
        String s = preprocess(input);
        if (s.isEmpty()) return false;

        char state = 'S';

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (state == 'S') {
                if (isUpper(ch)) state = 'T';
                else return false;
            }
            else if (state == 'T') {
                if (isAlnum(ch)) state = 'T';
                else if (ch == '.') state = 'A';
                else return false;
            }
            else if (state == 'A') return false;
        }
        return state == 'A';
    }

    // ============================================================
    // TEXTO DE CONDICIONES / TIPOS DE CADENA
    // ============================================================

    private static void mostrarCondiciones() {
        System.out.println("===============================================");
        System.out.println("              CONDICIONES DEL AUTÓMATA");
        System.out.println("===============================================");
        System.out.println("1. Cadenas numéricas:");
        System.out.println("   - Contienen solo dígitos [0-9]");
        System.out.println("   - Deben tener al menos un dígito");
        System.out.println("   - Finalizan con un punto '.'");
        System.out.println("   - ER: [0-9]+.");
        System.out.println();
        System.out.println("2. Identificadores:");
        System.out.println("   - Inician con una letra [A-Za-z]");
        System.out.println("   - Pueden contener letras o números después");
        System.out.println("   - Finalizan con un punto '.'");
        System.out.println("   - ER: [A-Za-z][A-Za-z0-9]*.");
        System.out.println();
        System.out.println("3. Cadenas que inician en mayúscula:");
        System.out.println("   - Primer símbolo debe ser una letra mayúscula [A-Z]");
        System.out.println("   - Pueden seguir letras o números");
        System.out.println("   - Finalizan con un punto '.'");
        System.out.println("   - ER: [A-Z][A-Za-z0-9]*.");
        System.out.println("===============================================");
        System.out.println();
    }

    // ============================================================
    // CÁLCULO DE PROPIEDADES DEL ÁRBOL
    // ============================================================

    private static void calcularPropiedades(Nodo n) {
        if (n == null) return;

        if (n.izquierdo == null && n.derecho == null) {
            // hoja: firstpos, lastpos y subtree ya están en el constructor
            return;
        }

        if (n.izquierdo != null) calcularPropiedades(n.izquierdo);
        if (n.derecho != null)  calcularPropiedades(n.derecho);

        Set<Integer> sub = new HashSet<>();

        if (n.izquierdo != null) sub.addAll(n.izquierdo.subtree);
        if (n.derecho != null)  sub.addAll(n.derecho.subtree);

        switch (n.simbolo) {
            case "·":
                n.anulable = n.izquierdo.anulable && n.derecho.anulable;

                n.firstpos.clear();
                if (n.izquierdo.anulable)
                    n.firstpos.addAll(n.derecho.firstpos);
                n.firstpos.addAll(n.izquierdo.firstpos);

                n.lastpos.clear();
                if (n.derecho.anulable)
                    n.lastpos.addAll(n.izquierdo.lastpos);
                n.lastpos.addAll(n.derecho.lastpos);
                break;

            case "+":
            case "|":
                n.anulable = n.izquierdo.anulable || n.derecho.anulable;

                n.firstpos.clear();
                n.firstpos.addAll(n.izquierdo.firstpos);
                n.firstpos.addAll(n.derecho.firstpos);

                n.lastpos.clear();
                n.lastpos.addAll(n.izquierdo.lastpos);
                n.lastpos.addAll(n.derecho.lastpos);
                break;

            case "*":
                n.anulable = true;

                n.firstpos.clear();
                n.firstpos.addAll(n.izquierdo.firstpos);

                n.lastpos.clear();
                n.lastpos.addAll(n.izquierdo.lastpos);
                break;

            default:
                break;
        }

        n.subtree = sub;
    }

    // ============================================================
    // IMPRESIÓN DE ERRORES DE PARSING
    // ============================================================

    private static void mostrarError(String expr, RegexParseException e) {
        System.out.println();
        System.out.println(RED + BOLD + "ERROR EN LA EXPRESIÓN" + RESET);
        System.out.println("  " + expr);

        StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < e.index; i++) sb.append(' ');
        sb.append("^");

        System.out.println(sb);
        System.out.println("Se esperaba: " + e.expected);
        System.out.println();
    }

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Título + condiciones
        System.out.println(BOLD + "AUTOMATA" + RESET);
        System.out.println();
        mostrarCondiciones();
        System.out.println("AFD Manual. Escribe la cadena terminada en '.' (quit para salir)");

        RegexParser parser = new RegexParser();

        while (true) {
            System.out.print("\nEntrada> ");
            String line = sc.nextLine();
            if (line == null || line.equalsIgnoreCase("quit")) break;

            String pre = preprocess(line);
            System.out.println("(preprocesada) -> \"" + pre + "\"");

            boolean isNum = afdNumeric(line);
            boolean isId  = afdIdentifier(line);
            boolean isMay = afdStartsUpper(line);

            System.out.println("Número?        : " + (isNum ? GREEN + "Sí" + RESET : "No"));
            System.out.println("Identificador? : " + (isId  ? GREEN + "Sí" + RESET : "No"));
            System.out.println("Inicia mayúsc. : " + (isMay ? GREEN + "Sí" + RESET : "No"));

            try {
                Nodo raiz = parser.parse(pre);
                calcularPropiedades(raiz);

                System.out.println("\n=== MÉTODO DEL ÁRBOL (centrado real) ===");
                ArbolPrinter.print(raiz, pre);

                // Construir y mostrar el NFA de Thompson
                NFA nfa = Thompson.fromRegexTree(raiz);
                NfaPrinter.print(nfa);

            } catch (RegexParseException e) {
                mostrarError(pre, e);
            }
        }

        sc.close();
        System.out.println("\nFin del programa.");
    }
}
