package clases;

public class Cola<T> {
    private Nodo<T> pFirst;
    private Nodo<T> pLast;
    private int size;

    public Cola() {
        this.pFirst = null;
        this.pLast = null;
        this.size = 0;
    }

    // Método para agregar al final (Encolar)
    public void encolar(T dato) {
        Nodo<T> pNew = new Nodo<>(dato);
        if (this.isEmpty()) {
            this.pFirst = pNew;
            this.pLast = pNew;
        } else {
            this.pLast.setpNext(pNew);
            this.pLast = pNew;
        }
        this.size++;
    }

    // Método para sacar del inicio (Desencolar)
    public T desencolar() {
        if (this.isEmpty()) {
            return null;
        }
        T dato = this.pFirst.getData();
        this.pFirst = this.pFirst.getpNext();
        this.size--;
        
        if (this.isEmpty()) {
            this.pLast = null;
        }
        return dato;
    }

    public boolean isEmpty() {
        return this.pFirst == null;
    }
    
    public int getSize() {
        return this.size;
    }
}
