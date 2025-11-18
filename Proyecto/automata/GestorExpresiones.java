package Proyecto.automata;

import java.util.List;
import java.util.Scanner;

public class GestorExpresiones {

    private ContextoModo ctx;
    private Scanner sc;

    public GestorExpresiones(ContextoModo ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void mostrarMenu() {
        System.out.println("\n====== CAMBIO DE EXPRESIONES ======\n");
        System.out.println("Modo actual: " + ctx.getModo());
        List<String> lista = ctx.getExpresionesModo();

        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i+1) + ") " + lista.get(i));
        }

        System.out.println("-----------------------------------");
        System.out.println("a) Agregar nueva ER");
        System.out.println("b) Regresar");
        System.out.print("\nOpción: ");

        String op = sc.nextLine();

        if (op.equalsIgnoreCase("b")) return;
        if (op.equalsIgnoreCase("a")) {
            agregarNueva();
            return;
        }

        try {
            int pos = Integer.parseInt(op) - 1;
            if (pos < 0 || pos >= lista.size()) {
                System.out.println("Índice inválido.");
                return;
            }
            editar(pos);
        } catch (Exception e) {
            System.out.println("Opción inválida.");
        }
    }

    private void editar(int idx) {
        System.out.println("ER actual: " + ctx.getExpresionesModo().get(idx));
        System.out.print("Nueva ER: ");
        String nueva = sc.nextLine();
        ctx.cambiarER(idx, nueva);
    }

    private void agregarNueva() {
        System.out.print("Ingrese nueva expresión: ");
        String nueva = sc.nextLine();
        ctx.agregarER(nueva);
    }
}
