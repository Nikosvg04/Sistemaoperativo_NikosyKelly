package estructuras;

// Implementación de una Cola estricta (First In, First Out)
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> finalCola;
    private int tamaño;

    public Cola() {
        this.frente = null;
        this.finalCola = null;
        this.tamaño = 0;
    }

    // Método para encolar (agregar al final)
    public void encolar(T data) {
        Nodo<T> nuevoNodo = new Nodo<>(data);
        if (estaVacia()) {
            frente = nuevoNodo;
            finalCola = nuevoNodo;
        } else {
            finalCola.siguiente = nuevoNodo;
            finalCola = nuevoNodo;
        }
        tamaño++;
    }

    // Método para desencolar (sacar el primero)
    public T desencolar() {
        if (estaVacia()) {
            return null;
        }
        T data = frente.data;
        frente = frente.siguiente;
        
        if (frente == null) {
            finalCola = null; // Si sacamos el último, la cola queda completamente vacía
        }
        tamaño--;
        return data;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public int tamaño() {
        return tamaño;
    }
}
