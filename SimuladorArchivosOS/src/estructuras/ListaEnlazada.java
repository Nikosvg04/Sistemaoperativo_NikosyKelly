package estructuras;

public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private int tamaño;

    private class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato) { this.dato = dato; this.siguiente = null; }
    }

    public ListaEnlazada() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    public int tamaño() { return tamaño; }

    public void insertarFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> temp = cabeza;
            while (temp.siguiente != null) temp = temp.siguiente;
            temp.siguiente = nuevo;
        }
        tamaño++;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamaño) return null;
        Nodo<T> temp = cabeza;
        for (int i = 0; i < indice; i++) temp = temp.siguiente;
        return temp.dato;
    }

    public void eliminar(int indice) {
        if (indice < 0 || indice >= tamaño) return;
        if (indice == 0) {
            cabeza = cabeza.siguiente;
        } else {
            Nodo<T> temp = cabeza;
            for (int i = 0; i < indice - 1; i++) temp = temp.siguiente;
            temp.siguiente = temp.siguiente.siguiente;
        }
        tamaño--;
    }
}