package clases;

public class Nodo<T> {
    private T data;
    private Nodo<T> pNext;

    // Constructor
    public Nodo(T data) {
        this.data = data;
        this.pNext = null;
    }

    // Getters y Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Nodo<T> getpNext() {
        return pNext;
    }

    public void setpNext(Nodo<T> pNext) {
        this.pNext = pNext;
    }
}
