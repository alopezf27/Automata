package Proyecto.automata;

public class EvaluadorFactory {

    public static Evaluador get(ContextoModo.Modo modo) {

        switch (modo) {
            case NUMERICO:
                return new EvaluadorGen();

            case LOGICO:
                return new EvaluadorLog();

            case MIXTO:
                return new EvaluadorMix();

            default:
                return new EvaluadorGen();
        }
    }
}
