package controlador;

import modelo.ArchivoVirtual;
import modelo.BloqueDisco;

public class GestorDisco {
    private BloqueDisco[] disco;    // El arreglo que simula todo el Disco Duro
    private int capacidadTotal;     // Cuántos bloques tiene el disco en total
    private int bloquesLibres;      // Cuántos bloques nos quedan disponibles

    public GestorDisco(int capacidad) {
        this.capacidadTotal = capacidad;
        this.bloquesLibres = capacidad;
        this.disco = new BloqueDisco[capacidad];
        
        // Inicializar cada bloque del disco (formateo inicial)
        for (int i = 0; i < capacidad; i++) {
            disco[i] = new BloqueDisco(i);
        }
    }

    public BloqueDisco[] getDisco() { return disco; }
    
    public int getBloquesLibres() { return bloquesLibres; }

    // Verifica si hay suficiente espacio. 
    // Como es Asignación Encadenada, solo importa que haya bloques libres en total, no importa si están separados.
    public boolean hayEspacioSuficiente(int bloquesRequeridos) {
        return bloquesLibres >= bloquesRequeridos;
    }

    // --- EL ALGORITMO ESTRELLA: Asignación Encadenada ---
    public boolean asignarEspacio(ArchivoVirtual archivo) {
        int bloquesNecesarios = archivo.getTamañoEnBloques();
        
        if (!hayEspacioSuficiente(bloquesNecesarios)) {
            return false; // Retorna falso si no hay espacio para que la GUI muestre un error
        }

        int bloquesAsignados = 0;
        int bloqueAnterior = -1;

        // Recorremos el disco buscando bloques libres
        for (int i = 0; i < capacidadTotal; i++) {
            if (!disco[i].isOcupado()) {
                
                // 1. Ocupamos el bloque libre encontrado
                disco[i].setOcupado(true);
                disco[i].setNombreArchivo(archivo.getNombre());
                bloquesLibres--;

                // 2. Lógica de los punteros (Encadenamiento)
                if (bloquesAsignados == 0) {
                    // Si es el PRIMER bloque que encontramos, el archivo debe recordar dónde empieza
                    archivo.setIdPrimerBloque(i);
                } else {
                    // Si NO es el primero, hacemos que el bloque anterior apunte a este nuevo bloque
                    disco[bloqueAnterior].setSiguienteBloque(i);
                }

                // 3. Nos preparamos para la siguiente vuelta del ciclo
                bloqueAnterior = i;
                bloquesAsignados++;

                // 4. ¿Ya terminamos de asignar todos los bloques que pedía el archivo?
                if (bloquesAsignados == bloquesNecesarios) {
                    disco[i].setSiguienteBloque(-1); // -1 significa "Fin del archivo" (EOF)
                    break; // Salimos del ciclo
                }
            }
        }
        return true; // Asignación exitosa
    }

    // --- LIBERAR ESPACIO (Para cuando se elimina un archivo) ---
    public void liberarEspacio(ArchivoVirtual archivo) {
        int bloqueActual = archivo.getIdPrimerBloque();
        
        // Seguimos la cadena de bloques hasta encontrar el final (-1)
        while (bloqueActual != -1) {
            int siguiente = disco[bloqueActual].getSiguienteBloque();
            disco[bloqueActual].liberar(); // Llama al método que creamos en BloqueDisco (limpia los datos)
            bloquesLibres++;
            bloqueActual = siguiente;      // Saltamos al siguiente eslabón de la cadena
        }
        
        // Le quitamos el bloque de inicio al archivo porque ya fue borrado del disco
        archivo.setIdPrimerBloque(-1);
    }
}