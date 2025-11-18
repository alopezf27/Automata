package Proyecto.arbol;

public class ImpresorArbol {

    public static void imprimir(NodoArbol nodo) {
        System.out.println("\n======= ÁRBOL DE EXPRESIÓN =======\n");
        imprimirRec(nodo, 0);
        System.out.println("\n==================================\n");
    }

    private static void imprimirRec(NodoArbol nodo, int nivel) {
        if (nodo == null) return;

        imprimirRec(nodo.derecho, nivel + 1);
        for (int i = 0; i < nivel; i++) System.out.print("   ");
        System.out.println(formato(nodo));
        imprimirRec(nodo.izquierdo, nivel + 1);
    }

    private static String formato(NodoArbol n) {
        if (n.posicionHoja != null) {
            return n.simbolo + " (" + n.posicionHoja + ")";
        }
        return n.simbolo;
    }
}
