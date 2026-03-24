package modelo;

import java.awt.Color;

public class BloqueDisco {
    private int id;
    private boolean libre;
    private String archivoDueño;
    private int siguienteBloque; // Para la asignación encadenada
    private Color color;         // Para que la interfaz sepa de qué color pintarlo

    public BloqueDisco(int id) {
        this.id = id;
        this.libre = true;
        this.archivoDueño = null;
        this.siguienteBloque = -1; // -1 significa que es el último bloque o está vacío
        this.color = Color.WHITE;
    }

    // Getters y Setters
    public int getId() { return id; }
    
    public boolean isLibre() { return libre; }
    public void setLibre(boolean libre) { this.libre = libre; }

    public String getArchivoDueño() { return archivoDueño; }
    public void setArchivoDueño(String archivoDueño) { this.archivoDueño = archivoDueño; }

    public int getSiguienteBloque() { return siguienteBloque; }
    public void setSiguienteBloque(int siguienteBloque) { this.siguienteBloque = siguienteBloque; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}