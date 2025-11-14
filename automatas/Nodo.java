import java.util.HashSet;
import java.util.Set;

public class Nodo {

    public String simbolo;        // Operador o símbolo: a, b, ., +, *, #
    public Nodo izquierdo;        // Hijo izquierdo
    public Nodo derecho;          // Hijo derecho

    public boolean anulable;      // Si es anulable
    public int pos;               // Número de posición (solo hojas)

    public Set<Integer> firstpos; // FIRSTPOS
    public Set<Integer> lastpos;  // LASTPOS

    // ------------------------------------------------------
    // Constructor binario (para +, ., concatenación, unión)
    // ------------------------------------------------------
    public Nodo(String simbolo, Nodo izquierdo, Nodo derecho) {
        this.simbolo = simbolo;
        this.izquierdo = izquierdo;
        this.derecho = derecho;
        this.firstpos = new HashSet<>();
        this.lastpos = new HashSet<>();
        this.pos = -1;
        this.anulable = false;
    }

    // ------------------------------------------------------
    // Constructor unario (para *)
    // ------------------------------------------------------
    public Nodo(String simbolo, Nodo hijo) {
        this.simbolo = simbolo;
        this.izquierdo = hijo;
        this.derecho = null;
        this.firstpos = new HashSet<>();
        this.lastpos = new HashSet<>();
        this.pos = -1;
        this.anulable = false;
    }

    // ------------------------------------------------------
    // Constructor para hojas (símbolos terminales)
    // ------------------------------------------------------
    public Nodo(String simbolo, int pos) {
        this.simbolo = simbolo;
        this.pos = pos;
        this.izquierdo = null;
        this.derecho = null;

        this.firstpos = new HashSet<>();
        this.lastpos = new HashSet<>();
        this.firstpos.add(pos);
        this.lastpos.add(pos);

        this.anulable = false; // Siempre false en hojas
    }

    // ------------------------------------------------------
    // Para depuración visual
    // ------------------------------------------------------
    @Override
    public String toString() {
        if (pos != -1) return simbolo + "(" + pos + ")";
        return simbolo;
    }
}
