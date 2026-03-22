package controlador;

import estructuras.ListaEnlazada;

public class GestorJournal {
    
    public static class Transaccion {
        public String operacion; 
        public String nombreArchivo;
        public String estado;    
        
        public Transaccion(String operacion, String nombreArchivo, String estado) {
            this.operacion = operacion;
            this.nombreArchivo = nombreArchivo;
            this.estado = estado;
        }
    }

    private ListaEnlazada<Transaccion> bitacora;

    public GestorJournal() {
        this.bitacora = new ListaEnlazada<>();
    }

    // Registra que una operación empezó a tocar el disco
    public void registrarPendiente(String operacion, String nombreArchivo) {
        bitacora.agregar(new Transaccion(operacion, nombreArchivo, "PENDIENTE"));
        System.out.println("[JOURNAL] Anotado en bitácora: " + operacion + " sobre " + nombreArchivo + " (PENDIENTE)");
    }

    // Cambia el estado a confirmada cuando el disco termina con éxito
    public void confirmarTransaccion(String nombreArchivo) {
        for (int i = 0; i < bitacora.tamaño(); i++) {
            Transaccion t = bitacora.obtener(i);
            if (t.nombreArchivo.equals(nombreArchivo) && t.estado.equals("PENDIENTE")) {
                t.estado = "CONFIRMADA";
                System.out.println("[JOURNAL] Operación sobre " + nombreArchivo + " marcada como CONFIRMADA.");
                break; 
            }
        }
    }

    public ListaEnlazada<Transaccion> getBitacora() { return bitacora; }
}