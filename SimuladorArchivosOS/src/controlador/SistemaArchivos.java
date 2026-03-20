package controlador;

import modelo.DirectorioVirtual;
import modelo.ArchivoVirtual;
import modelo.Proceso;
import java.awt.Color;

public class SistemaArchivos {
    
    // Los 3 sub-sistemas que hemos creado
    private GestorDisco gestorDisco;
    private PlanificadorDisco planificador;
    private GestorLocks gestorLocks;
    
    // La carpeta principal donde nacerán todos los archivos
    private DirectorioVirtual carpetaRaiz;

    public SistemaArchivos() {
        // Inicializamos el disco con 200 bloques (puedes cambiar este número luego)
        this.gestorDisco = new GestorDisco(200);
        
        // Inicializamos el planificador poniendo el cabezal en la posición 0
        this.planificador = new PlanificadorDisco(0);
        
        // Inicializamos el gestor de concurrencia
        this.gestorLocks = new GestorLocks();
        
        // Creamos la carpeta principal del sistema
        this.carpetaRaiz = new DirectorioVirtual("Raíz", "Administrador");
        
        // ¡VITAL! Encendemos el Hilo del disco para que corra de fondo
        this.planificador.start(); 
    }

    // --- Getters para que la GUI pueda dibujar la información ---
    public GestorDisco getGestorDisco() { return gestorDisco; }
    
    public PlanificadorDisco getPlanificador() { return planificador; }
    
    public DirectorioVirtual getCarpetaRaiz() { return carpetaRaiz; }

    // --- MÉTODO PRINCIPAL: Solicitar creación de un archivo ---
    // La GUI llamará a este método cuando el usuario llene los datos y presione "Crear"
    public String solicitarCreacionArchivo(String nombre, int tamaño, String dueño, Color color) {
        
        // 1. Verificamos si hay espacio antes de molestar al disco
        if (!gestorDisco.hayEspacioSuficiente(tamaño)) {
            return "Error: No hay espacio suficiente en el disco para " + tamaño + " bloques.";
        }

        // 2. Creamos el archivo lógicamente usando los métodos seguros que acabamos de crear
        ArchivoVirtual nuevoArchivo = new ArchivoVirtual(nombre, dueño, tamaño, color);
        
        // ---> ¡ESTA ES LA LÍNEA QUE FALTABA! Le decimos al disco que ocupe los bloques <---
        gestorDisco.asignarEspacio(nuevoArchivo);
        
        // 3. Lo metemos en la carpeta Raíz (luego haremos que se pueda meter en subcarpetas)
        carpetaRaiz.agregarElemento(nuevoArchivo);
        
        // 4. Creamos el Proceso (PCB) para que el planificador haga el trabajo físico
        Proceso procesoCrear = new Proceso(Proceso.Operacion.CREATE, nombre, tamaño);
        
        // 5. Encolamos el proceso en el Hilo del planificador
        planificador.agregarProceso(procesoCrear);
        
        return "Solicitud enviada con éxito. Proceso PID: " + procesoCrear.getPid() + " en espera.";
    }

    // --- MÉTODO PARA ELIMINAR UN ARCHIVO ---
    public String solicitarEliminacionArchivo(String nombre) {
        
        // 1. Buscamos los bloques en el disco y los vaciamos (los volvemos a poner grises/libres)
        modelo.BloqueDisco[] disco = gestorDisco.getDisco();
        boolean archivoEncontrado = false;
        
        for (int i = 0; i < disco.length; i++) {
            if (disco[i].isOcupado() && disco[i].getNombreArchivo().equals(nombre)) {
                disco[i].setOcupado(false);
                disco[i].setNombreArchivo("");
                disco[i].setSiguienteBloque(-1); // -1 significa que no apunta a nada
                archivoEncontrado = true;
            }
        }
        
        if (!archivoEncontrado) {
            return "Error: No se encontró el archivo '" + nombre + "' en el disco.";
        }
        
        // 2. Lo quitamos del árbol de carpetas (JTree)
        estructuras.ListaEnlazada<modelo.NodoSistemaArchivos> elementos = carpetaRaiz.getContenido();
        for (int i = 0; i < elementos.tamaño(); i++) {
            if (elementos.obtener(i).getNombre().equals(nombre)) {
                elementos.eliminar(i); // NOTA: Si aquí te sale una línea roja, cámbialo por "remover(i)"
                break;
            }
        }
        
        // 3. Encolamos el proceso de eliminación para el hilo
        Proceso procesoEliminar = new Proceso(Proceso.Operacion.DELETE, nombre, 0);
        planificador.agregarProceso(procesoEliminar);
        
        return "El archivo '" + nombre + "' fue eliminado y sus bloques liberados con éxito.";
    }
    
    // --- MÉTODO PARA LEER UN ARCHIVO ---
    public String solicitarLecturaArchivo(String nombre) {
        
        // 1. Verificamos si el archivo realmente existe en el disco
        modelo.BloqueDisco[] disco = gestorDisco.getDisco();
        boolean archivoEncontrado = false;
        int bloquesLeidos = 0;
        
        for (int i = 0; i < disco.length; i++) {
            if (disco[i].isOcupado() && disco[i].getNombreArchivo().equals(nombre)) {
                archivoEncontrado = true;
                bloquesLeidos++; // Contamos cuántos bloques tiene para la simulación
            }
        }
        
        if (!archivoEncontrado) {
            return "Error: No se encontró el archivo '" + nombre + "' en el disco.";
        }
        
        // 2. Encolamos el proceso de lectura para que el brazo del disco se mueva
        Proceso procesoLeer = new Proceso(Proceso.Operacion.READ, nombre, bloquesLeidos);
        planificador.agregarProceso(procesoLeer);
        
        return "Leyendo archivo '" + nombre + "'.\nSe enviaron " + bloquesLeidos + " bloques a la cola de lectura del disco.";
    }

    // Método de limpieza al cerrar el programa
    public void apagarSistema() {
        planificador.apagarSistema();
    }
}