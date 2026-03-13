package modelo;

import java.awt.Color;

public abstract class NodoSistemaArchivos {
    protected String nombre;
    protected String dueño; // "Administrador" o "Usuario"
    protected boolean esDirectorio;
    
    // Variables nuevas para la simulación visual en el disco
    protected int tamaño = 0;
    protected Color color = Color.BLUE; // Color por defecto

    public NodoSistemaArchivos(String nombre, String dueño, boolean esDirectorio) {
        this.nombre = nombre;
        this.dueño = dueño;
        this.esDirectorio = esDirectorio;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; } // Solo el admin usará esto

    public String getDueño() { return dueño; }
    
    public boolean isDirectorio() { return esDirectorio; }
    
    // --- Métodos nuevos que pedía la ventana gráfica ---
    public int getTamaño() { 
        return tamaño; 
    }
    
    public void setTamaño(int tamaño) { 
        this.tamaño = tamaño; 
    }
    
    public Color getColor() { 
        return color; 
    }
    
    public void setColor(Color color) { 
        this.color = color; 
    }
    // ---------------------------------------------------
    
    // Este método es obligatorio para que el JTree (la interfaz visual) muestre el nombre correcto
    @Override
    public String toString() {
        return this.nombre;
    }
}