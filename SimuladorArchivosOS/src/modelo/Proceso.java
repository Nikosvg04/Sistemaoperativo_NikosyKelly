package modelo;

public class Proceso {
    
    // 1. Definimos los estados exactos que exige el requerimiento
    public enum Estado {
        NUEVO, LISTO, EJECUTANDO, BLOQUEADO, TERMINADO
    }

    // 2. Definimos las operaciones CRUD posibles
    public enum Operacion {
        CREATE, READ, UPDATE, DELETE
    }

    // Variable estática para que los PIDs (Process ID) se generen solos: 1, 2, 3...
    private static int contadorPid = 1; 

    private int pid;
    private Estado estado;
    private Operacion operacion;
    private String nombreArchivo; // El archivo sobre el que va a operar
    private int tamañoRequerido;  // Solo útil si la operación es CREATE (tamaño en bloques)
    private int bloqueObjetivo;   // El bloque específico del disco al que necesita acceder (vital para los algoritmos SSTF, SCAN, etc.)

    // Constructor del proceso
    public Proceso(Operacion operacion, String nombreArchivo, int tamañoRequerido) {
        this.pid = contadorPid++;
        this.estado = Estado.NUEVO; // Todo proceso nace en estado NUEVO
        this.operacion = operacion;
        this.nombreArchivo = nombreArchivo;
        this.tamañoRequerido = tamañoRequerido;
        this.bloqueObjetivo = -1; // Se asignará luego cuando el sistema busque en la FAT
    }

    // --- Getters y Setters ---
    public int getPid() { return pid; }
    
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    
    public Operacion getOperacion() { return operacion; }
    
    public String getNombreArchivo() { return nombreArchivo; }
    
    public int getTamañoRequerido() { return tamañoRequerido; }

    public int getBloqueObjetivo() { return bloqueObjetivo; }
    public void setBloqueObjetivo(int bloqueObjetivo) { this.bloqueObjetivo = bloqueObjetivo; }
    
    // Este método es útil para imprimir la cola de procesos en la interfaz gráfica
    @Override
    public String toString() {
        return "PID: " + pid + " | Op: " + operacion + " | Archivo: " + nombreArchivo + " | Estado: " + estado;
    }
}