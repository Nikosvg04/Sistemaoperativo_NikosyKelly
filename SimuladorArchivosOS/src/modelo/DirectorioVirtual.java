package modelo;

import estructuras.ListaEnlazada;

public class DirectorioVirtual extends NodoSistemaArchivos {
    
    private ListaEnlazada<NodoSistemaArchivos> contenido;

    public DirectorioVirtual(String nombre, String dueño) {
        // true indica que es un directorio
        super(nombre, dueño, true); 
        this.contenido = new ListaEnlazada<>();
    }

    public ListaEnlazada<NodoSistemaArchivos> getContenido() {
        return contenido;
    }

    public void agregarElemento(NodoSistemaArchivos elemento) {
        // Usamos insertarFinal que es el método común en listas enlazadas
        this.contenido.insertarFinal(elemento);
    }

    // Este método es el que SistemaArchivos necesita para borrar
    public void eliminarElemento(String nombre) {
        for (int i = 0; i < contenido.tamaño(); i++) {
            NodoSistemaArchivos actual = contenido.obtener(i);
            if (actual.getNombre().equals(nombre)) {
                // IMPORTANTE: Si tu ListaEnlazada usa otro nombre (como 'remover'), cámbialo aquí
                contenido.eliminar(i); 
                return;
            }
        }
    }
}