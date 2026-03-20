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
        
        // 3. Lo metemos en la carpeta Raíz (luego haremos que se pueda meter en subcarpetas)
        carpetaRaiz.agregarElemento(nuevoArchivo);
        
        // 4. Creamos el Proceso (PCB) para que el planificador haga el trabajo físico
        Proceso procesoCrear = new Proceso(Proceso.Operacion.CREATE, nombre, tamaño);
        
        // 5. Encolamos el proceso en el Hilo del planificador
        planificador.agregarProceso(procesoCrear);
        
        return "Solicitud enviada con éxito. Proceso PID: " + procesoCrear.getPid() + " en espera.";
    }
    
    // Método de limpieza al cerrar el programa
    public void apagarSistema() {
        planificador.apagarSistema();
    }
}