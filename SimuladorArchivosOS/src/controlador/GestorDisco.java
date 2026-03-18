package controlador;

import modelo.ArchivoVirtual;
import modelo.BloqueDisco;

public class GestorDisco {
    private BloqueDisco[] disco;
    private int capacidadTotal;
    private int bloquesLibres;

    public GestorDisco(int capacidad) {
        this.capacidadTotal = capacidad;
        this.bloquesLibres = capacidad;
        this.disco = new BloqueDisco[capacidad];
        for (int i = 0; i < capacidad; i++) {
            disco[i] = new BloqueDisco(i);
        }
    }

    public boolean asignarEspacio(ArchivoVirtual archivo) {
        int bloquesNecesarios = archivo.getTamaño(); // Cambia a getTamañoEnBloques() si así se llama en tu clase
        
        if (bloquesLibres < bloquesNecesarios) return false;

        int mejorInicio = -1;
        int menorTamañoHueco = capacidadTotal + 1;
        int inicioActual = -1;
        int tamañoActual = 0;

        // Lógica Best-Fit
        for (int i = 0; i < capacidadTotal; i++) {
            if (disco[i].isLibre()) {
                if (inicioActual == -1) inicioActual = i;
                tamañoActual++;
            } else {
                if (tamañoActual >= bloquesNecesarios && tamañoActual < menorTamañoHueco) {
                    menorTamañoHueco = tamañoActual;
                    mejorInicio = inicioActual;
                }
                inicioActual = -1;
                tamañoActual = 0;
            }
        }
        if (tamañoActual >= bloquesNecesarios && tamañoActual < menorTamañoHueco) {
            mejorInicio = inicioActual;
        }

        if (mejorInicio != -1) {
            int anterior = -1;
            int cont = 0;
            for (int i = mejorInicio; cont < bloquesNecesarios; i++) {
                if (disco[i].isLibre()) {
                    disco[i].setLibre(false);
                    disco[i].setArchivoDueño(archivo.getNombre());
                    disco[i].setColor(archivo.getColor());
                    
                    if (cont == 0) archivo.setIdPrimerBloque(i);
                    else disco[anterior].setSiguienteBloque(i);
                    
                    anterior = i;
                    cont++;
                }
            }
            disco[anterior].setSiguienteBloque(-1);
            bloquesLibres -= bloquesNecesarios;
            return true;
        }
        return false;
    }

    public void liberarEspacio(ArchivoVirtual archivo) {
        int actual = archivo.getIdPrimerBloque();
        while (actual != -1) {
            int siguiente = disco[actual].getSiguienteBloque();
            disco[actual].setLibre(true);
            disco[actual].setArchivoDueño(null);
            disco[actual].setSiguienteBloque(-1);
            disco[actual].setColor(null);
            bloquesLibres++;
            actual = siguiente;
        }
    }

    public BloqueDisco[] getDisco() { return disco; }
    public int getBloquesLibres() { return bloquesLibres; }
}