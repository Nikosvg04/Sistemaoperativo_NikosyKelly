package controlador;

import modelo.*;
import java.awt.Color;
import logica.ServicioSeguridad;
import logica.GestorJournaling;
import logica.GestorLocks; // ESTA ES LA LÍNEA QUE FALTABA
import estructuras.ListaEnlazada;

public class SistemaArchivos {
    private GestorDisco gestorDisco;
    private PlanificadorDisco planificador;
    private GestorLocks gestorLocks; 
    private DirectorioVirtual carpetaRaiz;

    public SistemaArchivos() {
        this.gestorDisco = new GestorDisco(100);
        this.planificador = new PlanificadorDisco(0);
        this.gestorLocks = new GestorLocks();
        this.carpetaRaiz = new DirectorioVirtual("Raíz", "Administrador");
        try {
            this.planificador.start();
        } catch (Exception e) {
            System.err.println("Error al iniciar el planificador");
        }
    }

    // --- PUNTO 4: OPERACIONES CON LOCKS ---

    public String solicitarLecturaArchivo(String nombre, String usuario, String rol) {
        ArchivoVirtual archivo = buscarArchivo(nombre);
        if (archivo == null) return "Error: No existe el archivo.";

        ServicioSeguridad seguridad = new ServicioSeguridad();
        if (!seguridad.tienePermiso(usuario, rol, archivo, "Leer")) return "Acceso denegado.";

        // Bloqueamos para lectura
        gestorLocks.adquirirLockLectura(nombre);
        
        Proceso p = new Proceso(Proceso.Operacion.READ, nombre, 0);
        p.setBloqueObjetivo(archivo.getIdPrimerBloque());
        planificador.agregarProceso(p);
        
        gestorLocks.liberarLockLectura(nombre);
        return "Leyendo archivo (Sincronizado)...";
    }

    public String solicitarEliminacionArchivo(String nombre, String usuario, String rol) {
        ArchivoVirtual archivo = buscarArchivo(nombre);
        if (archivo == null) return "Error: No existe.";

        ServicioSeguridad seguridad = new ServicioSeguridad();
        if (!seguridad.tienePermiso(usuario, rol, archivo, "Eliminar")) return "No tienes permiso.";

        // Bloqueo exclusivo (nadie más entra mientras borramos)
        gestorLocks.adquirirLockEscritura(nombre);
        
        gestorDisco.liberarEspacio(archivo);
        carpetaRaiz.eliminarElemento(nombre);
        
        gestorLocks.liberarLockEscritura(nombre);
        return "Archivo eliminado con éxito.";
    }

    public String solicitarCreacionArchivo(String nombre, int tamaño, String dueño, String rol, Color color) {
        ServicioSeguridad seguridad = new ServicioSeguridad();
        if (!seguridad.tienePermiso(dueño, rol, null, "Crear")) return "Acceso denegado.";

        ArchivoVirtual nuevo = new ArchivoVirtual(nombre, dueño, tamaño, color);
        if (gestorDisco.asignarEspacio(nuevo)) {
            carpetaRaiz.agregarElemento(nuevo);
            
            Proceso p = new Proceso(Proceso.Operacion.CREATE, nombre, tamaño);
            p.setBloqueObjetivo(nuevo.getIdPrimerBloque());
            planificador.agregarProceso(p);
            
            return "Archivo creado.";
        }
        return "Sin espacio en disco.";
    }

    private ArchivoVirtual buscarArchivo(String nombre) {
        ListaEnlazada<NodoSistemaArchivos> lista = carpetaRaiz.getContenido();
        for (int i = 0; i < lista.tamaño(); i++) {
            NodoSistemaArchivos nodo = lista.obtener(i);
            if (!nodo.isDirectorio() && nodo.getNombre().equals(nombre)) {
                return (ArchivoVirtual) nodo;
            }
        }
        return null;
    }

    public DirectorioVirtual getCarpetaRaiz() { return carpetaRaiz; }
    public GestorDisco getGestorDisco() { return gestorDisco; }
    public PlanificadorDisco getPlanificador() { return planificador; }
}