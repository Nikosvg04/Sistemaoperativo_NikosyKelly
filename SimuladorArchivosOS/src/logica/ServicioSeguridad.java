/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import modelo.ArchivoVirtual;

public class ServicioSeguridad {
    
    // Definimos los tipos de usuario como constantes para evitar errores
    public static final String MODO_ADMIN = "Administrador";
    public static final String MODO_USUARIO = "Usuario";

    /**
     * Valida si el usuario actual puede realizar una operación CRUD
     * @param usuarioActual El nombre del usuario que está usando el sistema
     * @param rolActual El rol (Administrador o Usuario)
     * @param archivo El archivo sobre el que se quiere actuar
     * @param operacion La operación (Crear, Eliminar, Editar)
     * @return true si tiene permiso, false si no
     */
    public boolean tienePermiso(String usuarioActual, String rolActual, ArchivoVirtual archivo, String operacion) {
        
        // REGLA 1: El Administrador siempre tiene permiso para todo
        if (rolActual.equalsIgnoreCase(MODO_ADMIN)) {
            return true;
        }

        // REGLA 2: Si es un "Usuario" normal...
        if (rolActual.equalsIgnoreCase(MODO_USUARIO)) {
            // Solo puede modificar/eliminar si es el dueño del archivo
            if (operacion.equals("Eliminar") || operacion.equals("Editar")) {
                return archivo.getDueño().equalsIgnoreCase(usuarioActual);
            }
            
            // Si la operación es "Leer", permitimos a todos (o podrías restringirlo)
            if (operacion.equals("Leer")) {
                return true;
            }
        }

        return false; // Por defecto, si no cumple nada, no hay permiso
    }
}