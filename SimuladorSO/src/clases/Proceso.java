package clases;

public class Proceso {
    // Identificadores
    private String id;           // Ej: "P001"
    private String nombre;       // Ej: "Transmisión Datos"
    
    // Estado del proceso
    // Estados: "Nuevo", "Listo", "Ejecucion", "Bloqueado", "Suspendido", "Terminado"
    private String estado;       
    
    // Registros de la CPU (Simulados)
    private int programCounter;  // PC: Próxima instrucción a ejecutar
    private int MAR;             // MAR: Dirección de memoria actual
    
    // Planificación
    private int prioridad;       // 1 (Alta) a 3 (Baja) - Según tu lógica
    private int instruccionesTotales; // Longitud del proceso
    private int instruccionesEjecutadas; // Contador de progreso
    
    // TIEMPO REAL (Crítico para el proyecto)
    private int deadline;        // Ciclos máximos permitidos antes de fallar
    private int llegada;         // Ciclo en el que llegó al sistema (para métricas)
    
    // Constructor
    public Proceso(String id, String nombre, int instrucciones, int prioridad, int deadline, int cicloLlegada) {
        this.id = id;
        this.nombre = nombre;
        this.instruccionesTotales = instrucciones;
        this.prioridad = prioridad;
        this.deadline = deadline;
        this.llegada = cicloLlegada;
        
        // Valores iniciales por defecto
        this.estado = "Nuevo";
        this.programCounter = 0;
        this.MAR = 0;
        this.instruccionesEjecutadas = 0;
    }

    // --- MÉTODOS ÚTILES ---
    
    // Simula ejecutar una instrucción (avanza PC y MAR)
    public void ejecutarInstruccion() {
        this.programCounter++;
        this.MAR++;
        this.instruccionesEjecutadas++;
    }
    
    public boolean termino() {
        return instruccionesEjecutadas >= instruccionesTotales;
    }
    
    // Para imprimir bonito en la consola/interfaz
    @Override
    public String toString() {
        return id + " | " + nombre + " | Estado: " + estado;
    }

    // --- GETTERS Y SETTERS (Necesarios para leer/escribir datos) ---
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getProgramCounter() { return programCounter; }
    public void setProgramCounter(int programCounter) { this.programCounter = programCounter; }

    public int getMAR() { return MAR; }
    public void setMAR(int MAR) { this.MAR = MAR; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    public int getInstruccionesTotales() { return instruccionesTotales; }
    public void setInstruccionesTotales(int instruccionesTotales) { this.instruccionesTotales = instruccionesTotales; }

    public int getInstruccionesEjecutadas() { return instruccionesEjecutadas; }
    public void setInstruccionesEjecutadas(int instruccionesEjecutadas) { this.instruccionesEjecutadas = instruccionesEjecutadas; }

    public int getDeadline() { return deadline; }
    public void setDeadline(int deadline) { this.deadline = deadline; }
    
    public int getLlegada() { return llegada; }
    public void setLlegada(int llegada) { this.llegada = llegada; }
}
