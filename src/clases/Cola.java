package clases;

public class Cola<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano;

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato) { this.dato = dato; }
    }

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cola == null) {
            cabeza = cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamano++;
    }

    public T desencolar() {
        if (cabeza == null) return null;
        T dato = cabeza.dato;
        cabeza = cabeza.siguiente;
        if (cabeza == null) cola = null;
        tamano--;
        return dato;
    }

    public boolean estaVacia() { return cabeza == null; }
    public int getTamano() { return tamano; } // Con N
}