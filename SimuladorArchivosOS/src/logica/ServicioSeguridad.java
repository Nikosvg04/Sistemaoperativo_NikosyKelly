/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package logica;

import modelo.NodoSistemaArchivos;

public class ServicioSeguridad {

    public boolean tienePermiso(String usuario, String rol, NodoSistemaArchivos nodo, String operacion) {
        // El Administrador siempre puede hacer todo
        if (rol != null && rol.equalsIgnoreCase("Administrador")) {
            return true;
        }

        // Si es un usuario normal y quiere Crear o Leer, lo dejamos (regla básica)
        if (operacion.equals("Crear") || operacion.equals("Leer")) {
            return true;
        }

        // Para Editar o Eliminar, verificamos si el usuario es el mismo que creó el archivo
        if (nodo != null) {
            return nodo.getDueño().equals(usuario);
        }

        return false;
    }
}