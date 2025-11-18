package Proyecto.arbol;

public class ImpresorAFD {

    public static void imprimir(AutomataAFD afd) {

        System.out.println("\n============== AFD (MÉTODO DIRECTO) ==============\n");

        System.out.println("Estado inicial: " + afd.inicial.nombre);
        System.out.print("Estados de aceptación: ");

        for (EstadoAFD e : afd.estados) {
            if (e.esAceptacion) {
                System.out.print(e.nombre + " ");
            }
        }
        System.out.println("\n--------------------------------------------------");

        for (TransicionAFD t : afd.transiciones) {
            System.out.println(t);
        }

        System.out.println("==================================================\n");
    }
}
