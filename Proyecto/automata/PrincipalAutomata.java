package Proyecto.automata;

import java.util.Scanner;

public class PrincipalAutomata {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ContextoModo ctx = new ContextoModo();
        GestorExpresiones gestor = new GestorExpresiones(ctx, sc);

        while (true) {

            limpiar();

            // ======================================================
            //                  ENCABEZADO DEL MENÚ
            // ======================================================
            System.out.println("AUTÓMATA\n");

            System.out.println("===============================================");
            System.out.println("           CONDICIONES DEL AUTÓMATA");
            System.out.println("===============================================\n");

            // ================= TIPO 1 ============================
            System.out.println("1. Cadenas numéricas:");
            System.out.println("   - Contienen solo dígitos [0-9]");
            System.out.println("   - Pueden tener cero o más dígitos");
            System.out.println("   - Finalizan con un punto '.'");
            System.out.println("   - ER: [0-9]*.\n");

            // ================= TIPO 2 ============================
            System.out.println("2. Identificadores:");
            System.out.println("   - Inician con una letra [A-Za-z]");
            System.out.println("   - Pueden contener letras o números después");
            System.out.println("   - Finalizan con un punto '.'");
            System.out.println("   - ER: [A-Za-z][A-Za-z0-9]*.\n");

            // ================= TIPO 3 ============================
            System.out.println("3. Cadenas que inician en mayúscula:");
            System.out.println("   - Primer símbolo debe ser una letra mayúscula [A-Z]");
            System.out.println("   - Pueden seguir letras o números");
            System.out.println("   - Finalizan con un punto '.'");
            System.out.println("   - ER: [A-Z][A-Za-z0-9]*.\n");

            System.out.println("===============================================\n");

            // ======================================================
            //                  MODO ACTUAL
            // ======================================================
            System.out.println("MODO ACTUAL: " + ctx.getModo().name() + "\n");

            // ======================================================
            //            EXPRESIONES ACTIVAS DEL MODO
            // ======================================================
            System.out.println("Expresiones activas:");
            int count = 1;
            for (String er : ctx.getExpresionesModo()) {
                System.out.println("  " + (count++) + ") " + er);
            }

            System.out.println("\n===============================================");
            System.out.println("                 COMANDOS");
            System.out.println("===============================================");
            System.out.println(":salir      → Terminar programa");
            System.out.println(":limpiar    → Limpiar pantalla");
            System.out.println(":cambiarER  → Editar ER de este modo");
            System.out.println(":modoGen    → Cambiar a modo General");
            System.out.println(":modoLog    → Cambiar a modo Lógico");
            System.out.println(":modoMix    → Cambiar a modo Mixto");
            System.out.println("===============================================\n");

            // ======================================================
            //              ENTRADA DEL USUARIO
            // ======================================================
            System.out.println("AFD Manual. Escribe la cadena terminada en '.'  (quit para salir)\n");
            System.out.print("Entrada> ");

            String entrada = sc.nextLine().trim();

            if (entrada.equalsIgnoreCase("quit")) break;

            if (entrada.startsWith(":")) {
                procesarComando(entrada, ctx, gestor);
                continue;
            }

            Evaluador eval = EvaluadorFactory.get(ctx.getModo());

            boolean aceptada = false;

            for (String er : ctx.getExpresionesModo()) {

                try {

                    if (eval.evaluarCadena(entrada, er)) {
                        aceptada = true;
                        break;
                    }

                } catch (Exception e) {
                    // Ignorar errores de otras ER del modo
                }
            }

            if (aceptada)
                System.out.println("\nCADENA ACEPTADA.\n");
            else
                System.out.println("\nCADENA RECHAZADA.\n");

            System.out.println("Presiona Enter para continuar...");
            sc.nextLine();
        }

        sc.close();
    }

    // ============================================================
    //                  PROCESADOR DE COMANDOS
    // ============================================================
    private static void procesarComando(
            String cmd,
            ContextoModo ctx,
            GestorExpresiones gestor) {

        switch (cmd) {

            case ":salir":
                System.exit(0);
                break;

            case ":limpiar":
                limpiar();
                break;

            case ":cambiarER":
                gestor.mostrarMenu();
                break;

            case ":modoGen":
                ctx.cambiarModo(ContextoModo.Modo.NUMERICO);
                break;

            case ":modoLog":
                ctx.cambiarModo(ContextoModo.Modo.LOGICO);
                break;

            case ":modoMix":
                ctx.cambiarModo(ContextoModo.Modo.MIXTO);
                break;

            default:
                System.out.println("Comando no reconocido.");
        }
    }

    // ============================================================
    //                  LIMPIEZA VISUAL
    // ============================================================
    private static void limpiar() {
        for (int i = 0; i < 40; i++) System.out.println();
    }
}
