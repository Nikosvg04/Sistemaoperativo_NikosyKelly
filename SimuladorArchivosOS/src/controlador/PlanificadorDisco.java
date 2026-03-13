package controlador;

import estructuras.Cola;
import modelo.Proceso;

public class PlanificadorDisco extends Thread {
    
    private Cola<Proceso> colaProcesos;
    private int posicionCabezalActual;
    private boolean sistemaEncendido;
    private String politicaActual; // Puede ser "FIFO", "SSTF", "SCAN" o "C-SCAN"

    public PlanificadorDisco(int cabezalInicial) {
        this.colaProcesos = new Cola<>();
        this.posicionCabezalActual = cabezalInicial;
        this.sistemaEncendido = true;
        this.politicaActual = "FIFO"; // Política por defecto
    }

    // --- Métodos de configuración ---
    public void setPolitica(String politica) {
        this.politicaActual = politica;
    }
    
    public int getPosicionCabezalActual() {
        return posicionCabezalActual;
    }

    public void apagarSistema() {
        this.sistemaEncendido = false;
    }

    // Método que llamará la GUI cuando el usuario le dé al botón "Crear", "Leer", etc.
    public void agregarProceso(Proceso proceso) {
        proceso.setEstado(Proceso.Estado.LISTO);
        colaProcesos.encolar(proceso);
        System.out.println("Proceso encolado: " + proceso.getPid()); // Para ver en consola que funciona
    }

    // --- EL MOTOR DEL HILO (El método run se ejecuta infinitamente en el fondo) ---
    @Override
    public void run() {
        while (sistemaEncendido) {
            if (!colaProcesos.estaVacia()) {
                
                // Aquí es donde en el futuro meteremos los ifs para SSTF, SCAN, etc.
                // Por ahora, aplicamos la política básica: FIFO (El primero que llega es el primero en salir)
                Proceso procesoAtendido = colaProcesos.desencolar();
                
                // 1. Cambiamos estado a EJECUTANDO
                procesoAtendido.setEstado(Proceso.Estado.EJECUTANDO);
                
                // 2. Simulamos el tiempo que tarda el disco mecánicamente en moverse (1 segundo)
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    System.out.println("El hilo del disco fue interrumpido.");
                }
                
                // 3. Movemos el cabezal del disco a donde nos pidió el proceso
                if(procesoAtendido.getBloqueObjetivo() != -1){
                    this.posicionCabezalActual = procesoAtendido.getBloqueObjetivo();
                }

                // 4. Terminamos la operación de Entrada/Salida
                procesoAtendido.setEstado(Proceso.Estado.TERMINADO);
                System.out.println("Proceso Terminado: " + procesoAtendido.getPid() + " | Cabezal ahora en: " + posicionCabezalActual);
                
            } else {
                // Si la cola está vacía, hacemos que el hilo "duerma" un poco 
                // para no consumir el 100% de la CPU de tu computadora.
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // Ignorar
                }
            }
        }
    }
}