package Proyecto.automata;

public class AnalizadorCadena {

    public void validar(String cadena) throws ExcepcionCadena {

        if (cadena == null || cadena.isEmpty())
            throw new ExcepcionCadena("Cadena vacía.");

        if (!cadena.endsWith("."))
            throw new ExcepcionCadena("La cadena debe terminar con punto.");
    }
}
