package Proyecto.arbol;

public class CalculadoraPropiedades {

    public void calcular(NodoArbol nodo) {
        if (nodo == null) return;

        calcular(nodo.izquierdo);
        calcular(nodo.derecho);

        switch (nodo.simbolo) {
            case "·": // concatenación
                nodo.anulable = nodo.izquierdo.anulable && nodo.derecho.anulable;

                nodo.firstpos.addAll(nodo.izquierdo.firstpos);
                if (nodo.izquierdo.anulable) nodo.firstpos.addAll(nodo.derecho.firstpos);

                nodo.lastpos.addAll(nodo.derecho.lastpos);
                if (nodo.derecho.anulable) nodo.lastpos.addAll(nodo.izquierdo.lastpos);
                break;

            case "+": // OR
                nodo.anulable = nodo.izquierdo.anulable || nodo.derecho.anulable;

                nodo.firstpos.addAll(nodo.izquierdo.firstpos);
                nodo.firstpos.addAll(nodo.derecho.firstpos);

                nodo.lastpos.addAll(nodo.izquierdo.lastpos);
                nodo.lastpos.addAll(nodo.derecho.lastpos);
                break;

            case "*": // estrella
                nodo.anulable = true;

                nodo.firstpos.addAll(nodo.izquierdo.firstpos);
                nodo.lastpos.addAll(nodo.izquierdo.lastpos);
                break;

            default: // HOJA
                nodo.anulable = false;
                if (nodo.posicionHoja != null) {
                    nodo.firstpos.add(nodo.posicionHoja);
                    nodo.lastpos.add(nodo.posicionHoja);
                }
                break;
        }
    }
}
