package logica;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class GestorLocks {
    // Usamos un mapa para tener un semáforo distinto por cada archivo
    private HashMap<String, Semaphore> semaforosArchivos;
    // Llevamos la cuenta de cuántos usuarios están leyendo
    private HashMap<String, Integer> contadoresLectores;

    public GestorLocks() {
        this.semaforosArchivos = new HashMap<>();
        this.contadoresLectores = new HashMap<>();
    }

    // --- BLOQUEO PARA LECTURA ---
    // Muchos pueden leer al mismo tiempo, siempre que nadie esté escribiendo
    public synchronized void adquirirLockLectura(String nombreArchivo) {
        semaforosArchivos.putIfAbsent(nombreArchivo, new Semaphore(1));
        contadoresLectores.putIfAbsent(nombreArchivo, 0);

        try {
            // Si es el primer lector, debe pedir permiso al semáforo (bloquea escritores)
            if (contadoresLectores.get(nombreArchivo) == 0) {
                semaforosArchivos.get(nombreArchivo).acquire();
            }
            // Aumentamos el contador de lectores
            contadoresLectores.put(nombreArchivo, contadoresLectores.get(nombreArchivo) + 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void liberarLockLectura(String nombreArchivo) {
        int lectores = contadoresLectores.get(nombreArchivo) - 1;
        contadoresLectores.put(nombreArchivo, lectores);

        // Si ya no queda ningún lector, liberamos el semáforo para que entren escritores
        if (lectores == 0) {
            semaforosArchivos.get(nombreArchivo).release();
        }
    }

    // --- BLOQUEO PARA ESCRITURA (Exclusivo) ---
    // Solo uno puede escribir y nadie puede leer mientras tanto
    public void adquirirLockEscritura(String nombreArchivo) {
        semaforosArchivos.putIfAbsent(nombreArchivo, new Semaphore(1));
        try {
            semaforosArchivos.get(nombreArchivo).acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void liberarLockEscritura(String nombreArchivo) {
        if (semaforosArchivos.containsKey(nombreArchivo)) {
            semaforosArchivos.get(nombreArchivo).release();
        }
    }
}