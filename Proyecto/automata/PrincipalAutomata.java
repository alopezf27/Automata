package Proyecto.automata;

import java.util.Scanner;

public class PrincipalAutomata {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ContextoModo ctx = new ContextoModo();
        GestorExpresiones gestor = new GestorExpresiones(ctx, sc);
        Evaluador evaluador = new Evaluador();
        AnalizadorCadena analizador = new AnalizadorCadena();

        while (true) {
            limpiar();

            System.out.println("=======================================");
            System.out.println("               AUTÓMATA");
            System.out.println("=======================================\n");

            System.out.println("Modo actual: " + ctx.getModo());
            System.out.println(ctx.getDescripcionModo());
            System.out.println("\nExpresión activa: " + ctx.getERActiva());

            System.out.println("\nComandos:");
            System.out.println(":salir      → Terminar programa");
            System.out.println(":limpiar    → Limpiar pantalla");
            System.out.println(":regex      → Cambiar ER manual");
            System.out.println(":modoNum    → Modo numérico");
            System.out.println(":modoLog    → Modo lógico");
            System.out.println(":modoMix    → Modo mixto");
            System.out.println(":cambiarER  → Menú completo ER");

            System.out.print("\nIngresa cadena o comando: ");
            String entrada = sc.nextLine();

            if (entrada.startsWith(":")) {
                if (procesarComando(entrada, ctx, gestor)) break;
                continue;
            }

            try {
                analizador.validar(entrada);
                boolean ok = evaluador.evaluarCadena(entrada, ctx.getERActiva());

                System.out.println(ok ? "Cadena ACEPTADA." : "Cadena RECHAZADA.");

            } catch (ExcepcionCadena e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            System.out.print("\nPresiona Enter para continuar...");
            sc.nextLine();
        }

        sc.close();
    }

    private static boolean procesarComando(String cmd,
                                           ContextoModo ctx,
                                           GestorExpresiones gestor) {

        switch (cmd) {
            case ":salir": return true;
            case ":limpiar": limpiar(); return false;
            case ":regex":
                gestor.mostrarMenu();
                return false;
            case ":modoNum": ctx.cambiarModo(ContextoModo.Modo.NUMERICO); return false;
            case ":modoLog": ctx.cambiarModo(ContextoModo.Modo.LOGICO); return false;
            case ":modoMix": ctx.cambiarModo(ContextoModo.Modo.MIXTO); return false;
            case ":cambiarER":
                gestor.mostrarMenu();
                return false;
            default:
                System.out.println("Comando no válido.");
                return false;
        }
    }

    private static void limpiar() {
        for (int i = 0; i < 40; i++) System.out.println();
    }
}
