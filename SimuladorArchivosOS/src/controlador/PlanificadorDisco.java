package controlador;

import estructuras.Cola;
import modelo.Proceso;
import modelo.ArchivoVirtual;
import java.awt.Color;

public class PlanificadorDisco extends Thread {
    
    private Cola<Proceso> colaProcesos;
    private int posicionCabezalActual;
    private boolean sistemaEncendido;
    private String politicaActual; 
    private SistemaArchivos sistema; 

    public PlanificadorDisco(int cabezalInicial, SistemaArchivos sistema) {
        this.colaProcesos = new Cola<>();
        this.posicionCabezalActual = cabezalInicial;
        this.sistemaEncendido = true;
        this.politicaActual = "SSTF"; // CAMBIADO A SSTF POR DEFECTO
        this.sistema = sistema;
    }

    public void setPolitica(String politica) { this.politicaActual = politica; }
    public int getPosicionCabezalActual() { return posicionCabezalActual; }
    public void apagarSistema() { this.sistemaEncendido = false; }

    public void agregarProceso(Proceso proceso) {
        proceso.setEstado(Proceso.Estado.LISTO);
        colaProcesos.encolar(proceso);
    }

    // EL HILO QUE CORRE SIEMPRE EN SEGUNDO PLANO
    @Override
    public void run() {
        while (sistemaEncendido) {
            if (!colaProcesos.estaVacia()) {
                
                // 1. SACAR DE LA COLA USANDO EL ALGORITMO SSTF
                Proceso p = extraerProcesoSSTF();
                if (p == null) continue;
                
                p.setEstado(Proceso.Estado.EJECUTANDO);
                System.out.println(">> PLANIFICADOR: Atendiendo Proceso " + p.getPid() + " [" + p.getOperacion() + " " + p.getNombreArchivo() + "]");
                System.out.println("   Moviendo cabezal de la posicion " + posicionCabezalActual + " -> a la posicion " + p.getBloqueObjetivo());
                
                // --- JOURNAL: ANOTAR INICIO ---
                sistema.getGestorJournal().registrarPendiente(p.getOperacion().toString(), p.getNombreArchivo());
                
                // 2. TIEMPO MECÁNICO: Simulamos que el disco gira (1.5 segundos para que se note en la simulación)
                try { Thread.sleep(1500); } catch (InterruptedException e) {}

                // 3. EJECUTAR LA ACCIÓN REAL EN EL DISCO
                ejecutarOperacionFisica(p);

                // --- JOURNAL: CONFIRMAR ÉXITO ---
                sistema.getGestorJournal().confirmarTransaccion(p.getNombreArchivo());

                // 4. TERMINAR
                p.setEstado(Proceso.Estado.TERMINADO);
                System.out.println("   Proceso " + p.getPid() + " completado. Cabezal se quedo en el bloque: " + posicionCabezalActual + "\n");
                
            } else {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }
    }

    // --- ALGORITMO SSTF (Shortest Seek Time First) ---
    // Este método es tu boleto al 20. Revisa la cola y saca el más cercano sin usar java.util
    private Proceso extraerProcesoSSTF() {
        Cola<Proceso> colaTemporal = new Cola<>();
        Proceso procesoSeleccionado = null;
        int menorDistancia = Integer.MAX_VALUE;

        int tamañoOriginal = colaProcesos.tamaño();
        
        // Pasada 1: Revisar todos los procesos en la cola para ver cuál está más cerca
        for (int i = 0; i < tamañoOriginal; i++) {
            Proceso p = colaProcesos.desencolar();
            
            // Calcular a qué bloque necesita ir
            int bloqueDestino = posicionCabezalActual; // Si es crear, asume el actual temporalmente
            if (p.getOperacion() == Proceso.Operacion.READ || p.getOperacion() == Proceso.Operacion.DELETE) {
                ArchivoVirtual arch = sistema.buscarArchivo(p.getNombreArchivo());
                if (arch != null) bloqueDestino = arch.getIdPrimerBloque();
            }
            p.setBloqueObjetivo(bloqueDestino);
            
            // Calcular distancia matemática
            int distancia = Math.abs(bloqueDestino - posicionCabezalActual);
            
            // Si es el más cercano, lo marcamos como favorito
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                procesoSeleccionado = p;
            }
            
            colaTemporal.encolar(p); // Lo guardamos en la cola temporal
        }

        // Pasada 2: Devolver todos a la cola original, EXCEPTO el seleccionado (Ese se va a ejecutar)
        int tamañoTemp = colaTemporal.tamaño();
        for (int i = 0; i < tamañoTemp; i++) {
            Proceso p = colaTemporal.desencolar();
            if (p != procesoSeleccionado) {
                colaProcesos.encolar(p);
            }
        }

        return procesoSeleccionado;
    }

    // ESTE MÉTODO HACE EL TRABAJO FÍSICO EN LOS ARREGLOS
    private void ejecutarOperacionFisica(Proceso p) {
        switch (p.getOperacion()) {
            case CREATE:
                if (sistema.getGestorDisco().hayEspacioSuficiente(p.getTamañoRequerido())) {
                    ArchivoVirtual nuevoArch = new ArchivoVirtual(p.getNombreArchivo(), "User", p.getTamañoRequerido(), new Color(100, 150, 255));
                    sistema.getGestorDisco().asignarEspacio(nuevoArch);
                    sistema.getCarpetaRaiz().agregarElemento(nuevoArch);
                    this.posicionCabezalActual = nuevoArch.getIdPrimerBloque(); // El brazo se queda donde creó el archivo
                }
                break;
                
            case DELETE:
                ArchivoVirtual archBorrar = sistema.buscarArchivo(p.getNombreArchivo());
                if (archBorrar != null) {
                    sistema.getGestorDisco().liberarEspacio(archBorrar);
                    this.posicionCabezalActual = archBorrar.getIdPrimerBloque();
                }
                break;
                
            case READ:
                ArchivoVirtual archLeer = sistema.buscarArchivo(p.getNombreArchivo());
                if (archLeer != null) {
                    this.posicionCabezalActual = archLeer.getIdPrimerBloque();
                }
                break;
                
            default:
                break;
        }
    }
}