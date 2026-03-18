package controlador;

import modelo.Proceso;
import estructuras.Cola; // Asumiendo que tienes una clase Cola, si no, usamos ListaEnlazada
import estructuras.ListaEnlazada;

public class PlanificadorDisco extends Thread {
    private ListaEnlazada<Proceso> colaProcesos;
    private boolean activo;
    private int posicionCabezal; // Simula la ubicación física en el disco

    public PlanificadorDisco(int posicionInicial) {
        this.colaProcesos = new ListaEnlazada<>();
        this.activo = true;
        this.posicionCabezal = posicionInicial;
    }

    public void agregarProceso(Proceso p) {
        synchronized (colaProcesos) {
            colaProcesos.insertarFinal(p);
            System.out.println("Planificador: Nuevo proceso en cola -> " + p.getNombreArchivo());
        }
    }

    @Override
    public void run() {
        while (activo) {
            Proceso procesoActual = null;

            synchronized (colaProcesos) {
                if (colaProcesos.tamaño() > 0) {
                    procesoActual = colaProcesos.obtener(0);
                    colaProcesos.eliminar(0);
                }
            }

            if (procesoActual != null) {
                simularMovimientoCabezal(procesoActual);
            } else {
                try {
                    Thread.sleep(500); // Espera un poco si no hay procesos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void simularMovimientoCabezal(Proceso p) {
        p.setEstado(Proceso.Estado.EJECUTANDO);
        int destino = p.getBloqueObjetivo();
        
        System.out.println("Cabezal moviéndose de " + posicionCabezal + " a " + destino);
        
        // Simulamos el tiempo de búsqueda (Seek Time)
        try {
            int distancia = Math.abs(posicionCabezal - destino);
            Thread.sleep(distancia * 50); // 50ms por bloque
            posicionCabezal = destino;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        p.setEstado(Proceso.Estado.TERMINADO);
        System.out.println("Proceso finalizado: " + p.getOperacion() + " en " + p.getNombreArchivo());
    }

    public void apagarSistema() {
        this.activo = false;
    }
}