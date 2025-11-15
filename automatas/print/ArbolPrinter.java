package print;

import java.util.*;

import model.Nodo;

public class ArbolPrinter {

    // ============================================================
    // COLORES ANSI
    // ============================================================
    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";
    private static final String CYAN   = "\u001B[36m";
    private static final String BOLD   = "\u001B[1m";

    // ============================================================
    // OBJETO PARA REPRESENTAR NODO ASCII
    // ============================================================
    private static class AsciiNode {
        List<String> lines;  // líneas completas
        int width;           // ancho total
        int center;          // centro geométrico
    }

    // ============================================================
    // FUNCIONES AUXILIARES
    // ============================================================

    private static String spaces(int n) {
        return " ".repeat(Math.max(0, n));
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s;
        return s + spaces(width - s.length());
    }

    private static String padLeft(String s, int width) {
        if (width <= 0) return s;
        return spaces(width) + s;
    }

    private static String setToString(Set<Integer> set) {
        if (set == null || set.isEmpty()) return "";
        return set.toString().replace("[", "").replace("]", "");
    }

    // ============================================================
    // CONSTRUIR REPRESENTACIÓN ASCII DE UN NODO
    // ============================================================

    private static AsciiNode buildAscii(Nodo n) {

        // --------------------------------------------------------
        // Construcción del label con colores
        // --------------------------------------------------------
        String f = YELLOW + "{" + setToString(n.firstpos) + "}" + RESET;
        String l = GREEN  + "{" + setToString(n.lastpos)  + "}" + RESET;

        String symbol = CYAN + "(" + n.simbolo + ")" + RESET;

        String label = f + symbol + l;

        // Longitud visible sin contar los códigos ANSI
        int rawLength = ("{" + setToString(n.firstpos) + "}(" + n.simbolo + "){" + setToString(n.lastpos) + "}").length();

        int center = rawLength / 2;

        // --------------------------------------------------------
        // Líneas para pos y anulable
        // --------------------------------------------------------
        char[] posLine = new char[rawLength];
        char[] anulLine = new char[rawLength];

        Arrays.fill(posLine, ' ');
        Arrays.fill(anulLine, ' ');

        // Colocar la posición centrada
        if (n.pos != -1) {
            String p = Integer.toString(n.pos);
            int start = center - p.length() / 2;
            for (int i = 0; i < p.length(); i++)
                posLine[start + i] = p.charAt(i);
        }

        // Colocar anulable (V / F) centrado
        anulLine[center] = (n.anulable ? 'V' : 'F');

        // --------------------------------------------------------
        // Convertir a líneas coloreadas
        // --------------------------------------------------------
        List<String> base = new ArrayList<>();
        base.add(label);
        base.add(BLUE + new String(posLine) + RESET);
        base.add((n.anulable ? GREEN : RED) + new String(anulLine) + RESET);

        // --------------------------------------------------------
        // Hoja → terminar
        // --------------------------------------------------------
        AsciiNode self = new AsciiNode();
        self.lines = base;
        self.width = rawLength;
        self.center = center;

        if (n.izquierdo == null && n.derecho == null) return self;

        // --------------------------------------------------------
        // Construir nodos hijo
        // --------------------------------------------------------
        AsciiNode L = (n.izquierdo != null) ? buildAscii(n.izquierdo) : emptyNode();
        AsciiNode R = (n.derecho != null) ? buildAscii(n.derecho)  : emptyNode();

        int gap = 4;
        int childrenWidth = L.width + gap + R.width;

        int leftCenter  = L.center;
        int rightCenter = L.width + gap + R.center;

        int childrenCenter = (leftCenter + rightCenter) / 2;

        int shiftNode = 0;
        int shiftChildren = 0;

        if (childrenCenter > center) {
            shiftNode = childrenCenter - center;
        } else if (childrenCenter < center) {
            shiftChildren = center - childrenCenter;
        }

        int finalCenter = center + shiftNode;
        int finalWidth = Math.max(rawLength + shiftNode, childrenWidth + shiftChildren);

        List<String> out = new ArrayList<>();

        // Ajustar cada línea del nodo padre
        for (String s : base) {
            String line = spaces(shiftNode) + s;
            out.add(padRight(line, finalWidth));
        }

        // Línea con los conectores / \
        char[] connector = new char[finalWidth];
        Arrays.fill(connector, ' ');
        connector[leftCenter + shiftChildren] = '/';
        connector[rightCenter + shiftChildren] = '\\';

        out.add(new String(connector));

        // --------------------------------------------------------
        // Combinar líneas de los hijos
        // --------------------------------------------------------
        int depth = Math.max(L.lines.size(), R.lines.size());
        for (int i = 0; i < depth; i++) {

            String leftLine  = (i < L.lines.size()) ? L.lines.get(i) : spaces(L.width);
            String rightLine = (i < R.lines.size()) ? R.lines.get(i) : spaces(R.width);

            String combined = leftLine + spaces(gap) + rightLine;
            combined = padLeft(combined, shiftChildren);

            out.add(padRight(combined, finalWidth));
        }

        AsciiNode result = new AsciiNode();
        result.lines = out;
        result.width = finalWidth;
        result.center = finalCenter;

        return result;
    }

    private static AsciiNode emptyNode() {
        AsciiNode n = new AsciiNode();
        n.lines = List.of("");
        n.width = 0;
        n.center = 0;
        return n;
    }

    // ============================================================
    // FUNCIÓN PÚBLICA PARA IMPRIMIR EL ÁRBOL
    // ============================================================
    public static void print(Nodo root, String original) {
        if (root == null) return;

        AsciiNode a = buildAscii(root);

        int indent = Math.max(0, original.length() / 2);

        for (String line : a.lines)
            System.out.println(spaces(indent) + line);
    }
}
