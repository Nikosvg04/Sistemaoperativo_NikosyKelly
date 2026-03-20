package clases;

public class Proceso {
    private String nombre;
    private String tipo;
    private int tiempoTotal;
    private int prioridad;
    private int deadline;

    public Proceso(String nombre, String tipo, int tiempo, int prioridad, int deadline) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tiempoTotal = tiempo;
        this.prioridad = prioridad;
        this.deadline = deadline;
    }

    public String getNombre() { return nombre; }
    public int getPrioridad() { return prioridad; }
}