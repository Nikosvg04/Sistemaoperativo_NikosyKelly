/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import modelo.Proceso;
import estructuras.ListaEnlazada; // Usando la lista de tu compañero

public class GestorJournaling {
    private ListaEnlazada registros; // Aquí guardaremos las operaciones pendientes

    public GestorJournaling() {
        this.registros = new ListaEnlazada();
    }

    // Paso 1: Antes de crear/borrar, anotamos
    public void registrarInicio(String descripcion) {
        System.out.println("JOURNAL: Operación [" + descripcion + "] iniciada. Estado: PENDIENTE");
        // Aquí podrías agregar un nodo a tu lista con el texto "PENDIENTE"
    }

    // Paso 2: Si todo salió bien, confirmamos
    public void confirmar() {
        System.out.println("JOURNAL: Operación completada. Estado: COMMIT/EXITOSO");
        // Aquí limpias el registro o lo marcas como completado
    }

    // Paso 3: Lo más importante, la recuperación
    public void recuperarSistema() {
        System.out.println("RECOVERY: Revisando registros pendientes para hacer UNDO...");
        // Si hay algo pendiente, aquí es donde mandas a llamar al GestorDisco de tu compañero 
        // para liberar los bloques que se habían quedado a medias.
    }
}