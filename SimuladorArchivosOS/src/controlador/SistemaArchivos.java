package controlador;

import modelo.DirectorioVirtual;
import modelo.ArchivoVirtual;
import modelo.Proceso;
import java.awt.Color;

public class SistemaArchivos {
    
    private GestorDisco gestorDisco;
    private GestorJournal gestorJournal; // Ya estaba aquí
    private PlanificadorDisco planificador;
    private GestorLocks gestorLocks;
    private DirectorioVirtual carpetaRaiz;

    public SistemaArchivos() {
        this.gestorDisco = new GestorDisco(200);
        
        // IMPORTANTE: Le pasamos 'this' al planificador para que él tenga permiso de tocar el disco
        this.planificador = new PlanificadorDisco(0, this);
        
        this.gestorLocks = new GestorLocks();
        this.gestorJournal = new GestorJournal(); // <-- INICIALIZACIÓN DEL JOURNAL AÑADIDA
        this.carpetaRaiz = new DirectorioVirtual("Raíz", "Administrador");
        
        this.planificador.start(); // Encendemos el hilo del disco
        
        // --- CARGA INICIAL DESDE JSON (Vuelto a agregar para que funcione lo de antes) ---
        estructuras.ListaEnlazada<GestorJSON.ArchivoCargado> archivosIniciales = GestorJSON.leerEstadoInicial("estado_archivos.json");
        for (int i = 0; i < archivosIniciales.tamaño(); i++) {
            GestorJSON.ArchivoCargado arch = archivosIniciales.obtener(i);
            Color colorRandom = new Color((int)(Math.random()*0x1000000));
            solicitarCreacionArchivo(arch.nombre, arch.tamaño, arch.dueño, colorRandom);
        }
    }

    public GestorDisco getGestorDisco() { return gestorDisco; }
    public PlanificadorDisco getPlanificador() { return planificador; }
    public GestorLocks getGestorLocks() { return gestorLocks; }
    public DirectorioVirtual getCarpetaRaiz() { return carpetaRaiz; }
    public GestorJournal getGestorJournal() { return gestorJournal; } // <-- GETTER AÑADIDO

    // --- AHORA LAS ACCIONES SOLO ENCOLAN PROCESOS ---
    
    public String solicitarCreacionArchivo(String nombre, int tamaño, String dueño, Color color) {
        Proceso procesoCrear = new Proceso(Proceso.Operacion.CREATE, nombre, tamaño);
        planificador.agregarProceso(procesoCrear);
        return "Proceso encolado: Petición para CREAR '" + nombre + "' añadida a la cola.";
    }

    public String solicitarEliminacionArchivo(String nombre) {
        Proceso procesoEliminar = new Proceso(Proceso.Operacion.DELETE, nombre, 0);
        planificador.agregarProceso(procesoEliminar);
        return "Proceso encolado: Petición para ELIMINAR '" + nombre + "' añadida a la cola.";
    }
    
    public String solicitarLecturaArchivo(String nombre) {
        Proceso procesoLeer = new Proceso(Proceso.Operacion.READ, nombre, 0);
        planificador.agregarProceso(procesoLeer);
        return "Proceso encolado: Petición para LEER '" + nombre + "' añadida a la cola.";
    }
    
    // Método útil para que el Planificador busque archivos
    public ArchivoVirtual buscarArchivo(String nombre) {
        for (int i = 0; i < carpetaRaiz.getContenido().tamaño(); i++) {
            modelo.NodoSistemaArchivos nodo = carpetaRaiz.getContenido().obtener(i);
            if (!nodo.isDirectorio() && nodo.getNombre().equalsIgnoreCase(nombre)) {
                return (ArchivoVirtual) nodo;
            }
        }
        return null;
    }
}