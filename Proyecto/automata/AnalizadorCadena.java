package Proyecto.automata;

public class AnalizadorCadena {

    public void validar(String cadena) throws ExcepcionCadena {
        if (cadena.trim().isEmpty())
            throw new ExcepcionCadena("La cadena está vacía.");

        if (cadena.contains(" "))
            throw new ExcepcionCadena("Los espacios no son válidos.");
    }
}
