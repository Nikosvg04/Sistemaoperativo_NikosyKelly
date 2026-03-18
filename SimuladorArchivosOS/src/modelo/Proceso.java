package modelo;

public class Proceso {
    public enum Operacion { CREATE, READ, UPDATE, DELETE }
    public enum Estado { NUEVO, LISTO, EJECUTANDO, TERMINADO }

    private int pid;
    private static int contadorPid = 1;
    private Estado estado;
    private Operacion operacion;
    private String nombreArchivo;
    private int tamañoRequerido;
    private int bloqueObjetivo; // Esta es la variable importante

    public Proceso(Operacion operacion, String nombreArchivo, int tamañoRequerido) {
        this.pid = contadorPid++;
        this.estado = Estado.NUEVO;
        this.operacion = operacion;
        this.nombreArchivo = nombreArchivo;
        this.tamañoRequerido = tamañoRequerido;
        this.bloqueObjetivo = -1;
    }

    // Getters y Setters
    public int getPid() { return pid; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Operacion getOperacion() { return operacion; }
    public String getNombreArchivo() { return nombreArchivo; }
    public int getTamañoRequerido() { return tamañoRequerido; }
    public int getBloqueObjetivo() { return bloqueObjetivo; }
    public void setBloqueObjetivo(int bloqueObjetivo) { this.bloqueObjetivo = bloqueObjetivo; }

    @Override
    public String toString() {
        return "PID: " + pid + " | Op: " + operacion + " | Archivo: " + nombreArchivo + " | Estado: " + estado;
    }
}