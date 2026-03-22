package controlador;

import modelo.*;
import java.awt.Color;
import logica.ServicioSeguridad;
import logica.GestorLocks; // <-- ESTA ES LA LÍNEA MÁGICA QUE FALTABA
import estructuras.ListaEnlazada;

public class SistemaArchivos {
    
    private GestorDisco gestorDisco;
    private GestorJournal gestorJournal; 
    private PlanificadorDisco planificador;
    private GestorLocks gestorLocks;
    private DirectorioVirtual carpetaRaiz;

    public SistemaArchivos() {
        this.gestorDisco = new GestorDisco(200);
        
        // IMPORTANTE: Le pasamos 'this' al planificador para que él tenga permiso de tocar el disco
        this.planificador = new PlanificadorDisco(0, this);
        
        this.gestorLocks = new GestorLocks();
        this.gestorJournal = new GestorJournal(); 
        this.carpetaRaiz = new DirectorioVirtual("Raíz", "Administrador");
        
        this.planificador.start(); // Encendemos el hilo del disco
        
        // --- CARGA INICIAL DESDE JSON ---
        estructuras.ListaEnlazada<GestorJSON.ArchivoCargado> archivosIniciales = GestorJSON.leerEstadoInicial("estado_archivos.json");
        for (int i = 0; i < archivosIniciales.tamaño(); i++) {
            GestorJSON.ArchivoCargado arch = archivosIniciales.obtener(i);
            Color colorRandom = new Color((int)(Math.random()*0x1000000));
            // Durante la carga inicial asumimos rol de Administrador para que el JSON no sea bloqueado
            solicitarCreacionArchivo(arch.nombre, arch.tamaño, arch.dueño, "Administrador", colorRandom);
        }
    }

    public GestorDisco getGestorDisco() { return gestorDisco; }
    public PlanificadorDisco getPlanificador() { return planificador; }
    public GestorLocks getGestorLocks() { return gestorLocks; }
    public DirectorioVirtual getCarpetaRaiz() { return carpetaRaiz; }
    public GestorJournal getGestorJournal() { return gestorJournal; }

    // --- AHORA LAS ACCIONES VERIFICAN SEGURIDAD (KELLY) Y ENCOLAN PROCESOS (NIKOS) ---
    
    public String solicitarCreacionArchivo(String nombre, int tamaño, String dueño, String rol, Color color) {
        ServicioSeguridad seguridad = new ServicioSeguridad();
        
        // 1. Verificación de Seguridad de Kelly
        if (!seguridad.tienePermiso(dueño, rol, null, "Crear")) {
            return "Acceso denegado: Solo los administradores pueden crear archivos.";
        }

        // 2. Encolado en el Planificador de Nikos
        Proceso procesoCrear = new Proceso(Proceso.Operacion.CREATE, nombre, tamaño);
        planificador.agregarProceso(procesoCrear);
        return "Proceso encolado: Petición para CREAR '" + nombre + "' añadida a la cola.";
    }

    public String solicitarEliminacionArchivo(String nombre, String usuario, String rol) {
        ArchivoVirtual archivo = buscarArchivo(nombre);
        if (archivo == null) return "Error: No existe el archivo.";

        ServicioSeguridad seguridad = new ServicioSeguridad();
        
        // 1. Verificación de Seguridad de Kelly
        if (!seguridad.tienePermiso(usuario, rol, archivo, "Eliminar")) {
            return "Acceso denegado: No tienes permiso para eliminar este archivo.";
        }

        // 2. Encolado en el Planificador de Nikos
        Proceso procesoEliminar = new Proceso(Proceso.Operacion.DELETE, nombre, 0);
        planificador.agregarProceso(procesoEliminar);
        return "Proceso encolado: Petición para ELIMINAR '" + nombre + "' añadida a la cola.";
    }
    
    public String solicitarLecturaArchivo(String nombre, String usuario, String rol) {
        ArchivoVirtual archivo = buscarArchivo(nombre);
        if (archivo == null) return "Error: No existe el archivo.";

        ServicioSeguridad seguridad = new ServicioSeguridad();
        
        // 1. Verificación de Seguridad de Kelly
        if (!seguridad.tienePermiso(usuario, rol, archivo, "Leer")) {
            return "Acceso denegado: No tienes permiso para leer este archivo.";
        }

        // 2. Encolado en el Planificador de Nikos
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

    // --- RUTINA DE CRASH RECOVERY (SIMULADOR DE FALLOS) ---
    public void simularFalloYRecuperar() {
        System.out.println("\n💥 ¡CRASH SIMULADO! El sistema se ha caído (Simulando pérdida de RAM)...");
        
        // 1. "Borramos" la memoria reconstruyendo el disco y la raíz desde cero
        this.gestorDisco = new GestorDisco(200); 
        this.carpetaRaiz = new modelo.DirectorioVirtual("Raíz", "Administrador");
        
        System.out.println("🔧 Iniciando modo de recuperación (Leyendo Journal)...");
        
        // 2. Leemos la bitácora paso a paso para restaurar la consistencia
        estructuras.ListaEnlazada<GestorJournal.Transaccion> log = gestorJournal.getBitacora();
        
        for (int i = 0; i < log.tamaño(); i++) {
            GestorJournal.Transaccion t = log.obtener(i);
            
            // ¡Solo recuperamos lo que se guardó completamente antes del fallo!
            if (t.estado.equals("CONFIRMADA")) {
                
                if (t.operacion.equals("CREATE")) {
                    // Recreamos el archivo instantáneamente sin pasar por el hilo del disco
                    Color colorRecuperado = new Color((int)(Math.random()*0x1000000));
                    ArchivoVirtual archivoRecuperado = new ArchivoVirtual(t.nombreArchivo, "Recuperado", t.tamaño, colorRecuperado);
                    
                    if (gestorDisco.hayEspacioSuficiente(t.tamaño)) {
                        gestorDisco.asignarEspacio(archivoRecuperado);
                        carpetaRaiz.agregarElemento(archivoRecuperado);
                        System.out.println("✅ Archivo restaurado con éxito: " + t.nombreArchivo);
                    }
                } 
                else if (t.operacion.equals("DELETE")) {
                    // Si confirmamos un DELETE antes del fallo, nos aseguramos de que no exista
                    ArchivoVirtual arch = buscarArchivo(t.nombreArchivo);
                    if (arch != null) {
                        gestorDisco.liberarEspacio(arch);
                        System.out.println("🗑️ Archivo eliminado según Journal: " + t.nombreArchivo);
                    }
                }
            } else {
                // El PDF exige "deshacer (undo) operaciones pendientes no confirmadas"
                System.out.println("⚠️ Deshaciendo (UNDO) operación por corrupción (Se quedó PENDIENTE): " + t.operacion + " sobre " + t.nombreArchivo);
            }
        }
        
        System.out.println("🚀 ¡Recuperación completada! El disco vuelve a estar en línea.\n");
    }

    // --- MÉTODO PARA EXPORTAR EL SISTEMA A JSON ---
    public void guardarSistemaEnJSON() {
        System.out.println("\n📦 Preparando exportación del sistema a JSON...");
        GestorJSON.guardarEstado(this.carpetaRaiz, "estado_archivos.json");
    }
}