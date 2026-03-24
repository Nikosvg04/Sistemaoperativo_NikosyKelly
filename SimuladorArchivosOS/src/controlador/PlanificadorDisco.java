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
    
    // Variables nuevas para SCAN y C-SCAN
    private boolean subiendo; // Dirección del cabezal
    private final int MAX_BLOQUE = 199; // Tamaño del disco - 1

    public PlanificadorDisco(int cabezalInicial, SistemaArchivos sistema) {
        this.colaProcesos = new Cola<>();
        this.posicionCabezalActual = cabezalInicial;
        this.sistemaEncendido = true;
        this.politicaActual = "FIFO"; // Empezamos con FIFO
        this.sistema = sistema;
        this.subiendo = true; // Por defecto el brazo sube
    }

    public void setPolitica(String politica) { this.politicaActual = politica; }
    public String getPolitica() { return politicaActual; }
    public int getPosicionCabezalActual() { return posicionCabezalActual; }
    public void apagarSistema() { this.sistemaEncendido = false; }

    public void agregarProceso(Proceso proceso) {
        proceso.setEstado(Proceso.Estado.LISTO);
        colaProcesos.encolar(proceso);
    }

    @Override
    public void run() {
        while (sistemaEncendido) {
            if (!colaProcesos.estaVacia()) {
                
                // 1. EXTRAER SEGÚN LA POLÍTICA ELEGIDA
                Proceso p = extraerProcesoSegunPolitica();
                if (p == null) continue;
                
                // --- NUEVO: INTENTAR ADQUIRIR LOCK (CANDADO) ---
                boolean lockAdquirido = true;
                if (p.getOperacion() == Proceso.Operacion.READ) {
                    lockAdquirido = sistema.getGestorLocks().adquirirLockLectura(p.getNombreArchivo());
                } else {
                    // CREATE, DELETE o UPDATE requieren Lock Exclusivo
                    lockAdquirido = sistema.getGestorLocks().adquirirLockEscritura(p.getNombreArchivo());
                }

                if (!lockAdquirido) {
                    System.out.println("   [ACCESO DENEGADO] Archivo '" + p.getNombreArchivo() + "' ocupado. Proceso " + p.getPid() + " BLOQUEADO.");
                    p.setEstado(Proceso.Estado.BLOQUEADO);
                    colaProcesos.encolar(p); // Lo mandamos al final de la cola para que espere su turno
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                    continue; // Saltamos a la siguiente vuelta del disco
                }
                
                // Si logramos adquirir el lock, procedemos normalmente...
                p.setEstado(Proceso.Estado.EJECUTANDO);
                System.out.println("\n>> PLANIFICADOR (" + politicaActual + "): Atendiendo Proceso " + p.getPid());
                System.out.println("   Moviendo cabezal de " + posicionCabezalActual + " -> " + p.getBloqueObjetivo());
                
                // --- JOURNAL: ANOTAR INICIO ---
                sistema.getGestorJournal().registrarPendiente(p.getOperacion().toString(), p.getNombreArchivo(), p.getTamañoRequerido());
                
                // 2. TIEMPO MECÁNICO
                try { Thread.sleep(1500); } catch (InterruptedException e) {}

                // 3. EJECUTAR FÍSICAMENTE
                ejecutarOperacionFisica(p);

                // --- JOURNAL: CONFIRMAR ÉXITO ---
                sistema.getGestorJournal().confirmarTransaccion(p.getNombreArchivo());

                // 4. TERMINAR Y LIBERAR EL LOCK
                p.setEstado(Proceso.Estado.TERMINADO);
                System.out.println("   Completado. Cabezal quedó en: " + posicionCabezalActual);
                
                if (p.getOperacion() == Proceso.Operacion.READ) {
                    sistema.getGestorLocks().liberarLockLectura(p.getNombreArchivo());
                } else {
                    sistema.getGestorLocks().liberarLockEscritura(p.getNombreArchivo());
                }
                
            } else {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }
    }

    // --- EL CEREBRO DE LAS POLÍTICAS ---
    private Proceso extraerProcesoSegunPolitica() {
        // Primero, calculamos los bloques de destino de todos los procesos en cola
        precalcularDestinos();
        
        switch (politicaActual) {
            case "FIFO": return extraerProcesoFIFO();
            case "SSTF": return extraerProcesoSSTF();
            case "SCAN": return extraerProcesoSCAN();
            case "C-SCAN": return extraerProcesoCSCAN();
            default: return extraerProcesoFIFO();
        }
    }

    // Calcula a qué bloque necesita ir cada proceso antes de elegir
    private void precalcularDestinos() {
        Cola<Proceso> temp = new Cola<>();
        int tam = colaProcesos.tamaño();
        for (int i = 0; i < tam; i++) {
            Proceso p = colaProcesos.desencolar();
            int destino = posicionCabezalActual; // Default
            if (p.getOperacion() == Proceso.Operacion.READ || p.getOperacion() == Proceso.Operacion.DELETE) {
                ArchivoVirtual arch = sistema.buscarArchivo(p.getNombreArchivo());
                if (arch != null) destino = arch.getIdPrimerBloque();
            }
            p.setBloqueObjetivo(destino);
            temp.encolar(p);
        }
        colaProcesos = temp;
    }

    // 1. FIFO: El primero que llegó es el primero en salir
    private Proceso extraerProcesoFIFO() {
        return colaProcesos.desencolar();
    }

    // 2. SSTF: El más cercano al cabezal actual
    private Proceso extraerProcesoSSTF() {
        Cola<Proceso> temp = new Cola<>();
        Proceso seleccionado = null;
        int menorDistancia = Integer.MAX_VALUE;
        int tam = colaProcesos.tamaño();
        
        for (int i = 0; i < tam; i++) {
            Proceso p = colaProcesos.desencolar();
            int distancia = Math.abs(p.getBloqueObjetivo() - posicionCabezalActual);
            
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                seleccionado = p;
            }
            temp.encolar(p);
        }
        return limpiarColaYDevolver(temp, seleccionado);
    }

    // 3. SCAN (Algoritmo del Ascensor): Sube hasta el final y luego baja
    private Proceso extraerProcesoSCAN() {
        Cola<Proceso> temp = new Cola<>();
        Proceso seleccionado = null;
        int tam = colaProcesos.tamaño();
        
        // Intentar encontrar el más cercano en la dirección actual
        int menorDistancia = Integer.MAX_VALUE;
        for (int i = 0; i < tam; i++) {
            Proceso p = colaProcesos.desencolar();
            int destino = p.getBloqueObjetivo();
            
            boolean mismaDireccion = (subiendo && destino >= posicionCabezalActual) || (!subiendo && destino <= posicionCabezalActual);
            
            if (mismaDireccion) {
                int distancia = Math.abs(destino - posicionCabezalActual);
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    seleccionado = p;
                }
            }
            temp.encolar(p);
        }
        
        // Si no hay ninguno en esta dirección, cambiamos de dirección y buscamos de nuevo
        if (seleccionado == null) {
            subiendo = !subiendo; 
            System.out.println("   [SCAN] Cabezal llegó al extremo. Cambiando dirección a: " + (subiendo ? "ARRIBA" : "ABAJO"));
            
            // Repetimos la búsqueda con la nueva dirección
            menorDistancia = Integer.MAX_VALUE;
            Cola<Proceso> temp2 = new Cola<>();
            for (int i = 0; i < tam; i++) {
                Proceso p = temp.desencolar();
                int distancia = Math.abs(p.getBloqueObjetivo() - posicionCabezalActual);
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    seleccionado = p;
                }
                temp2.encolar(p);
            }
            temp = temp2;
        }
        
        return limpiarColaYDevolver(temp, seleccionado);
    }

    // 4. C-SCAN (Ascensor Circular): Sube hasta el final, salta al inicio y sigue subiendo
    private Proceso extraerProcesoCSCAN() {
        Cola<Proceso> temp = new Cola<>();
        Proceso seleccionado = null;
        int tam = colaProcesos.tamaño();
        
        // C-SCAN siempre asume que va en una sola dirección (hacia arriba)
        int menorDistancia = Integer.MAX_VALUE;
        for (int i = 0; i < tam; i++) {
            Proceso p = colaProcesos.desencolar();
            int destino = p.getBloqueObjetivo();
            
            if (destino >= posicionCabezalActual) {
                int distancia = destino - posicionCabezalActual;
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    seleccionado = p;
                }
            }
            temp.encolar(p);
        }
        
        // Si no hay ninguno más arriba, el brazo da la vuelta al bloque 0
        if (seleccionado == null) {
            System.out.println("   [C-SCAN] Cabezal llegó al tope. Reiniciando al bloque 0.");
            posicionCabezalActual = 0; 
            
            menorDistancia = Integer.MAX_VALUE;
            Cola<Proceso> temp2 = new Cola<>();
            for (int i = 0; i < tam; i++) {
                Proceso p = temp.desencolar();
                int distancia = p.getBloqueObjetivo() - posicionCabezalActual;
                if (distancia < menorDistancia && distancia >= 0) {
                    menorDistancia = distancia;
                    seleccionado = p;
                }
                temp2.encolar(p);
            }
            temp = temp2;
        }
        
        return limpiarColaYDevolver(temp, seleccionado);
    }

    // Método utilitario para devolver a la cola todos los procesos EXCEPTO el que vamos a ejecutar
    private Proceso limpiarColaYDevolver(Cola<Proceso> temp, Proceso seleccionado) {
        int tam = temp.tamaño();
        for (int i = 0; i < tam; i++) {
            Proceso p = temp.desencolar();
            if (p != seleccionado) {
                colaProcesos.encolar(p);
            }
        }
        return seleccionado;
    }

    private void ejecutarOperacionFisica(Proceso p) {
        switch (p.getOperacion()) {
            case CREATE:
                if (sistema.getGestorDisco().hayEspacioSuficiente(p.getTamañoRequerido())) {
                    ArchivoVirtual nuevoArch = new ArchivoVirtual(p.getNombreArchivo(), "User", p.getTamañoRequerido(), new Color(100, 150, 255));
                    sistema.getGestorDisco().asignarEspacio(nuevoArch);
                    sistema.getCarpetaRaiz().agregarElemento(nuevoArch);
                    this.posicionCabezalActual = nuevoArch.getIdPrimerBloque(); 
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
            default: break;
        }
    }
}