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
        System.out.println("\n=== CAMBIAR EXPRESIONES ===\n");

        List<String> lista = ctx.getExpresionesModo();

        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ") " + lista.get(i));
        }

        System.out.println("\nSeleccione la ER a modificar:");
        int num = Integer.parseInt(sc.nextLine()) - 1;

        System.out.print("Nueva ER: ");
        String nueva = sc.nextLine();

        ctx.cambiarER(num, nueva);

        System.out.println("ER modificada correctamente.\n");
    }
}
