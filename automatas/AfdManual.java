import java.util.*;

public class AfdManual {

    // ============================================================
    //     UTILIDADES BÁSICAS
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
    //     AFD PARA NÚMEROS
    // ============================================================

    public static boolean afdNum(String raw) {
        String s = preprocess(raw);
        if (s.isEmpty()) return false;

        char state = 'S';
        for (char ch : s.toCharArray()) {
            switch (state) {
                case 'S':
                    if (isDigit(ch)) state = 'N';
                    else return false;
                    break;
                case 'N':
                    if (isDigit(ch)) state = 'N';
                    else if (ch == '.') state = 'A';
                    else return false;
                    break;
                case 'A':
                    return false;
            }
        }
        return state == 'A';
    }

    // ============================================================
    //     AFD PARA IDENTIFICADORES
    // ============================================================

    public static boolean afdIdentifier(String raw) {
        String s = preprocess(raw);
        if (s.isEmpty()) return false;

        char state = 'S';
        for (char ch : s.toCharArray()) {
            switch (state) {
                case 'S':
                    if (isLetter(ch)) state = 'I';
                    else return false;
                    break;
                case 'I':
                    if (isAlnum(ch)) state = 'I';
                    else if (ch == '.') state = 'A';
                    else return false;
                    break;
                case 'A':
                    return false;
            }
        }
        return state == 'A';
    }

    // ============================================================
    //     AFD PARA INICIO MAYÚSCULA
    // ============================================================

    public static boolean afdInitUpper(String raw) {
        String s = preprocess(raw);
        if (s.isEmpty()) return false;

        char state = 'S';
        for (char ch : s.toCharArray()) {
            switch (state) {
                case 'S':
                    if (isUpper(ch)) state = 'T';
                    else return false;
                    break;
                case 'T':
                    if (isAlnum(ch)) state = 'T';
                    else if (ch == '.') state = 'A';
                    else return false;
                    break;
                case 'A':
                    return false;
            }
        }
        return state == 'A';
    }

    // ============================================================
    //     CONDICIONES DE ENTRADA
    // ============================================================

    public static void showConditions() {
        System.out.println("===============================================");
        System.out.println("        CONDICIONES PARA ACEPTAR CADENAS");
        System.out.println("===============================================");
        System.out.println("1. Cadenas numéricas:");
        System.out.println("   - Solo dígitos [0-9]");
        System.out.println("   - Mínimo un dígito");
        System.out.println("   - Deben terminar con punto '.'");
        System.out.println("   => ER: [0-9]+.");
        System.out.println();
        System.out.println("2. Identificadores:");
        System.out.println("   - Inician con letra [A-Za-z]");
        System.out.println("   - Luego letras o números");
        System.out.println("   - Terminan con '.'");
        System.out.println("   => ER: [A-Za-z][A-Za-z0-9]*.");
        System.out.println();
        System.out.println("3. Inician en mayúscula:");
        System.out.println("   - Primer carácter [A-Z]");
        System.out.println("   - Luego letras o números");
        System.out.println("   - Terminan con '.'");
        System.out.println("   => ER: [A-Z][A-Za-z0-9]*.");
        System.out.println("===============================================");
    }

    // ============================================================
    //     CONSTRUCCIÓN DEL ÁRBOL FORMAL (a·b·c…·.·#)
    // ============================================================

    public static Nodo construirArbolFormal(String cadena) {
        cadena = preprocess(cadena);
        if (cadena.isEmpty()) return null;

        List<Nodo> hojas = new ArrayList<>();
        int pos = 1;

        // Hojas para cada carácter de la cadena (incluye el '.')
        for (char c : cadena.toCharArray()) {
            hojas.add(new Nodo(String.valueOf(c), pos++));
        }

        // Concatenación izquierda-lineal: (((a·b)·c)·...)
        Nodo root = hojas.get(0);
        for (int i = 1; i < hojas.size(); i++) {
            root = new Nodo("·", root, hojas.get(i));
        }

        // Hoja final #
        Nodo hojaFinal = new Nodo("#", pos++);
        root = new Nodo("·", root, hojaFinal);

        return root;
    }

    // ============================================================
    //     CÁLCULO DE FIRSTPOS / LASTPOS (VERSIÓN B)
    //     FIRSTPOS = todas las posiciones del subárbol
    //     LASTPOS  = posiciones finales del subárbol (normalmente la última hoja)
    // ============================================================

    public static void calcularPropiedades(Nodo n) {
        if (n == null) return;

        // Hoja: ya está inicializada en el constructor
        if (n.izquierdo == null && n.derecho == null) {
            // firstpos = {pos}, lastpos = {pos}, anulable = false
            return;
        }

        if (n.izquierdo != null) calcularPropiedades(n.izquierdo);
        if (n.derecho != null) calcularPropiedades(n.derecho);

        n.firstpos.clear();
        n.lastpos.clear();

        // En esta versión (B) NO usamos la definición formal de firstpos.
        // Queremos que firstpos muestre TODAS las posiciones del subárbol.
        if (n.izquierdo != null) {
            n.firstpos.addAll(n.izquierdo.firstpos);
        }
        if (n.derecho != null) {
            n.firstpos.addAll(n.derecho.firstpos);
        }

        // Para lastpos: tomamos las posiciones finales del subárbol;
        // en un árbol lineal de concatenaciones será la última hoja.
        if (n.derecho != null && !n.derecho.lastpos.isEmpty()) {
            n.lastpos.addAll(n.derecho.lastpos);
        } else if (n.izquierdo != null && !n.izquierdo.lastpos.isEmpty()) {
            n.lastpos.addAll(n.izquierdo.lastpos);
        }

        // En este proyecto nada es anulable (no usamos ε), así que:
        n.anulable = false;
    }

    // ============================================================
    //     ASCII-ART DEL ÁRBOL (CENTRADO REAL)
    // ============================================================

    private static class AsciiNode {
        List<String> lines;
        int width;
        int center;
    }

    private static String setToString(Set<Integer> s) {
        if (s == null || s.isEmpty()) return "";
        return s.toString().replace("[", "").replace("]", "");
    }

    private static String spaces(int n) {
        if (n <= 0) return "";
        return " ".repeat(n);
    }

    private static String padRight(String s, int w) {
        if (s.length() >= w) return s;
        return s + spaces(w - s.length());
    }

    private static String padLeft(String s, int n) {
        if (n <= 0) return s;
        return spaces(n) + s;
    }

    private static AsciiNode buildAscii(Nodo n) {
        String first = setToString(n.firstpos);
        String last  = setToString(n.lastpos);
        String label = "{" + first + "}(" + n.simbolo + "){" + last + "}";
        int w = label.length();
        int center = w / 2;

        char[] posLine = new char[w];
        char[] anulLine = new char[w];
        Arrays.fill(posLine, ' ');
        Arrays.fill(anulLine, ' ');

        if (n.pos != -1) {
            String ps = String.valueOf(n.pos);
            int start = center - ps.length() / 2;
            for (int i = 0; i < ps.length(); i++) {
                if (start + i >= 0 && start + i < w) {
                    posLine[start + i] = ps.charAt(i);
                }
            }
        }

        anulLine[center] = (n.anulable ? 'V' : 'F');

        List<String> myLines = new ArrayList<>();
        myLines.add(label);
        myLines.add(new String(posLine));
        myLines.add(new String(anulLine));

        AsciiNode self = new AsciiNode();
        self.lines = myLines;
        self.width = w;
        self.center = center;

        if (n.izquierdo == null && n.derecho == null) {
            return self;
        }

        AsciiNode L = (n.izquierdo != null) ? buildAscii(n.izquierdo) : null;
        AsciiNode R = (n.derecho != null) ? buildAscii(n.derecho) : null;

        if (L == null) {
            L = new AsciiNode();
            L.lines = List.of("");
            L.width = 0;
            L.center = 0;
        }
        if (R == null) {
            R = new AsciiNode();
            R.lines = List.of("");
            R.width = 0;
            R.center = 0;
        }

        int gap = 4;
        int childrenWidth = L.width + gap + R.width;

        int leftGlobalCenter = L.center;
        int rightGlobalCenter = L.width + gap + R.center;

        int childrenCenter = (leftGlobalCenter + rightGlobalCenter) / 2;

        int shiftNode = 0;
        int shiftChildren = 0;

        if (childrenCenter > center) {
            shiftNode = childrenCenter - center;
        } else if (childrenCenter < center) {
            shiftChildren = center - childrenCenter;
        }

        int finalCenter = center + shiftNode;
        int finalWidth = Math.max(w + shiftNode, childrenWidth + shiftChildren);

        List<String> newList = new ArrayList<>();
        for (String ln : myLines) {
            String padded = padLeft(ln, shiftNode);
            newList.add(padRight(padded, finalWidth));
        }

        char[] conn = new char[finalWidth];
        Arrays.fill(conn, ' ');

        conn[leftGlobalCenter + shiftChildren] = '/';
        conn[rightGlobalCenter + shiftChildren] = '\\';

        newList.add(new String(conn));

        int childLines = Math.max(L.lines.size(), R.lines.size());

        for (int i = 0; i < childLines; i++) {
            String ll = (i < L.lines.size()) ? L.lines.get(i) : spaces(L.width);
            String rr = (i < R.lines.size()) ? R.lines.get(i) : spaces(R.width);

            String combined = ll + spaces(gap) + rr;
            combined = padLeft(combined, shiftChildren);
            newList.add(padRight(combined, finalWidth));
        }

        AsciiNode result = new AsciiNode();
        result.lines = newList;
        result.width = finalWidth;
        result.center = finalCenter;

        return result;
    }

    public static void imprimirArbolCentrado(Nodo root, String original) {
        if (root == null) return;

        int indent = Math.max(0, original.length() / 2);
        AsciiNode ascii = buildAscii(root);

        for (String line : ascii.lines) {
            System.out.println(spaces(indent) + line);
        }
    }

    // ============================================================
    //     MAIN
    // ============================================================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        showConditions();

        System.out.println("AFD manual. Escribe una cadena terminada en '.'");
        System.out.println("Escribe 'quit' para salir.");

        while (true) {
            System.out.print("\nEntrada> ");
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine();
            if (line == null) break;
            if (line.trim().equalsIgnoreCase("quit")) break;

            String pre = preprocess(line);
            System.out.println("  (preprocesada) -> \"" + pre + "\"");

            boolean num = afdNum(line);
            boolean id  = afdIdentifier(line);
            boolean may = afdInitUpper(line);

            System.out.println("  Número?        : " + (num ? "Sí" : "No"));
            System.out.println("  Identificador? : " + (id  ? "Sí" : "No"));
            System.out.println("  Inicia mayúsc. : " + (may ? "Sí" : "No"));

            if (num || id || may) {
                System.out.println("\n=== MÉTODO DEL ÁRBOL (centrado real) ===");
                Nodo arbol = construirArbolFormal(pre);
                calcularPropiedades(arbol);   // versión B
                imprimirArbolCentrado(arbol, pre);
            }
        }

        sc.close();
        System.out.println("Fin del programa.");
    }
}
