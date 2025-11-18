package Proyecto.arbol;

public class ImpresorArbol {

    public static void imprimir(NodoArbol raiz) {
        System.out.println("\n======= ÁRBOL DE EXPRESIÓN =======\n");
        imprimirRec(raiz, 0);
        System.out.println("\n==================================\n");
    }

    private static void imprimirRec(NodoArbol nodo, int nivel) {
        if (nodo == null) return;

        // imprimir primero los hijos derechos (para efecto árbol)
        imprimirRec(nodo.derecho, nivel + 4);

        // imprimir nodo
        String indent = " ".repeat(nivel);

        if (esHoja(nodo)) {
            System.out.println(indent + nodo.simbolo + " (" + nodo.posicionHoja + ")");
            System.out.println(indent + (nodo.anulable ? "V" : "F"));
        } else {
            System.out.println(indent + nodo.simbolo);
            System.out.println(indent + (nodo.anulable ? "V" : "F"));
        }

        // hijos izquierdos después
        imprimirRec(nodo.izquierdo, nivel + 4);
    }

    // Método auxiliar para saber si un nodo es hoja
    private static boolean esHoja(NodoArbol nodo) {
        return nodo.izquierdo == null && nodo.derecho == null;
    }
}
