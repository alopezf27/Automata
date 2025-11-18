package Proyecto.thompson;

public class ImpresorThompson {

    public static void imprimir(AutomataThompson afn) {

        System.out.println("\n========= THOMPSON (AFN) =========\n");
        System.out.println("Estado inicial: q" + afn.inicial.id);
        System.out.println("Estado final:   q" + afn.aceptacion.id);
        System.out.println("----------------------------------");

        for (EstadoThompson e : afn.estados) {
            for (TransicionThompson t : e.transiciones) {
                System.out.println("q" + e.id + " --" + t.simbolo + "--> q" + t.destino.id);
            }
        }

        System.out.println("==================================\n");
    }
}
