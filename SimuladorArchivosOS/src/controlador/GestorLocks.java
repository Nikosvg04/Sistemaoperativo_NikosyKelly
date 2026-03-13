package controlador;

import estructuras.ListaEnlazada;
import java.util.concurrent.Semaphore;

public class GestorLocks {

    // Clase interna para llevar el control de cada archivo sin usar HashMap de Java
    private class LockArchivo {
        String nombreArchivo;
        int lectoresActivos;
        Semaphore mutexLectores;    // Semáforo para proteger la variable 'lectoresActivos'
        Semaphore semaforoEscritor; // Semáforo para bloquear/desbloquear la escritura

        public LockArchivo(String nombre) {
            this.nombreArchivo = nombre;
            this.lectoresActivos = 0;
            this.mutexLectores = new Semaphore(1); // 1 = Mutex (Mutual Exclusion)
            this.semaforoEscritor = new Semaphore(1);
        }
    }

    // Nuestra estructura propia para guardar los locks de todos los archivos
    private ListaEnlazada<LockArchivo> listaLocks;
    
    // Semáforo extra para que dos procesos no intenten crear el lock del mismo archivo a la vez
    private Semaphore mutexLista; 

    public GestorLocks() {
        this.listaLocks = new ListaEnlazada<>();
        this.mutexLista = new Semaphore(1);
    }

    // Método que busca el lock de un archivo. Si el archivo es nuevo y no tiene lock, lo crea.
    private LockArchivo obtenerOCrearLock(String nombreArchivo) {
        try {
            mutexLista.acquire(); // Bloqueamos la lista para buscar seguros
            
            // Buscamos en nuestra lista enlazada usando el método que creamos en el Día 1
            for (int i = 0; i < listaLocks.tamaño(); i++) {
                LockArchivo lock = listaLocks.obtener(i);
                if (lock.nombreArchivo.equals(nombreArchivo)) {
                    mutexLista.release(); // Liberamos la lista
                    return lock;
                }
            }
            
            // Si llegamos aquí, el lock no existía. Lo creamos.
            LockArchivo nuevoLock = new LockArchivo(nombreArchivo);
            listaLocks.agregar(nuevoLock);
            mutexLista.release(); // Liberamos la lista
            return nuevoLock;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    // --- OPERACIONES DE LECTURA (Lock Compartido) ---
    public void adquirirLockLectura(String nombreArchivo) {
        LockArchivo lock = obtenerOCrearLock(nombreArchivo);
        try {
            lock.mutexLectores.acquire(); // Pedimos permiso para modificar el contador
            lock.lectoresActivos++;
            
            if (lock.lectoresActivos == 1) {
                // Si soy el PRIMER lector, tranco la puerta a los escritores
                lock.semaforoEscritor.acquire();
            }
            lock.mutexLectores.release(); // Soltamos el contador para que otros lectores puedan entrar
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void liberarLockLectura(String nombreArchivo) {
        LockArchivo lock = obtenerOCrearLock(nombreArchivo);
        try {
            lock.mutexLectores.acquire();
            lock.lectoresActivos--;
            
            if (lock.lectoresActivos == 0) {
                // Si soy el ÚLTIMO lector en salir, abro la puerta para los escritores
                lock.semaforoEscritor.release();
            }
            lock.mutexLectores.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // --- OPERACIONES DE ESCRITURA (Lock Exclusivo) ---
    public void adquirirLockEscritura(String nombreArchivo) {
        LockArchivo lock = obtenerOCrearLock(nombreArchivo);
        try {
            // El escritor pide el semáforo principal. 
            // Si hay algún lector o escritor adentro, se quedará aquí bloqueado (dormido)
            lock.semaforoEscritor.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void liberarLockEscritura(String nombreArchivo) {
        LockArchivo lock = obtenerOCrearLock(nombreArchivo);
        // El escritor terminó, libera el semáforo para el siguiente proceso
        lock.semaforoEscritor.release();
    }
}