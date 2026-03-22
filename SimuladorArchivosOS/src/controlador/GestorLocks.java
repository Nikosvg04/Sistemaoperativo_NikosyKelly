package controlador;

import estructuras.ListaEnlazada;

public class GestorLocks {

    // Clase interna para llevar la cuenta de cada archivo
    private class RegistroLock {
        String nombreArchivo;
        int lectoresActivos;
        boolean escritorActivo;

        RegistroLock(String nombreArchivo) {
            this.nombreArchivo = nombreArchivo;
            this.lectoresActivos = 0;
            this.escritorActivo = false;
        }
    }

    private ListaEnlazada<RegistroLock> listaLocks;

    public GestorLocks() {
        this.listaLocks = new ListaEnlazada<>();
    }

    // Busca si el archivo ya tiene un candado creado, si no, lo crea
    private RegistroLock obtenerOcrearLock(String nombreArchivo) {
        for (int i = 0; i < listaLocks.tamaño(); i++) {
            RegistroLock lock = listaLocks.obtener(i);
            if (lock.nombreArchivo.equals(nombreArchivo)) {
                return lock;
            }
        }
        RegistroLock nuevoLock = new RegistroLock(nombreArchivo);
        listaLocks.agregar(nuevoLock);
        return nuevoLock;
    }

    // --- LOCK COMPARTIDO (Múltiples lecturas permitidas) ---
    public boolean adquirirLockLectura(String nombreArchivo) {
        RegistroLock lock = obtenerOcrearLock(nombreArchivo);
        if (lock.escritorActivo) {
            return false; // Alguien está escribiendo, acceso denegado
        }
        lock.lectoresActivos++;
        System.out.println("   [LOCK] Lock de LECTURA concedido para: " + nombreArchivo);
        return true;
    }

    // --- LOCK EXCLUSIVO (Solo uno puede escribir, nadie puede leer) ---
    public boolean adquirirLockEscritura(String nombreArchivo) {
        RegistroLock lock = obtenerOcrearLock(nombreArchivo);
        if (lock.escritorActivo || lock.lectoresActivos > 0) {
            return false; // Alguien está leyendo o escribiendo, acceso denegado
        }
        lock.escritorActivo = true;
        System.out.println("   [LOCK] Lock de ESCRITURA concedido para: " + nombreArchivo);
        return true;
    }

    // --- LIBERAR CANDADOS ---
    public void liberarLockLectura(String nombreArchivo) {
        RegistroLock lock = obtenerOcrearLock(nombreArchivo);
        if (lock.lectoresActivos > 0) {
            lock.lectoresActivos--;
            System.out.println("   [UNLOCK] Lock de LECTURA liberado en: " + nombreArchivo);
        }
    }

    public void liberarLockEscritura(String nombreArchivo) {
        RegistroLock lock = obtenerOcrearLock(nombreArchivo);
        lock.escritorActivo = false;
        System.out.println("   [UNLOCK] Lock de ESCRITURA liberado en: " + nombreArchivo);
    }
}