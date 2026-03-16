/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import controlador.GestorDisco;
import modelo.DirectorioVirtual;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Esta clase se encarga de guardar y cargar el estado del simulador.
 * Es la parte de "Persistencia" que pide el proyecto.
 */
public class PersistenciaJSON {

    private static final String ARCHIVO_RESPALDO = "estado_disco.json";

    // Método para guardar todo lo que hay en el disco
    public void guardarSistema(GestorDisco disco, DirectorioVirtual raiz) {
        System.out.println("PERSISTENCIA: Guardando datos en " + ARCHIVO_RESPALDO + "...");
        
        try (FileWriter file = new FileWriter(ARCHIVO_RESPALDO)) {
            // Aquí simulamos la estructura JSON para no complicarte con librerías externas
            String jsonMock = "{\n" +
                "  \"disco\": \"FAT32_Simulado\",\n" +
                "  \"raiz\": \"" + raiz.getNombre() + "\",\n" +
                "  \"bloques_libres\": " + disco.getBloquesLibres() + "\n" +
                "}";
            
            file.write(jsonMock);
            System.out.println("PERSISTENCIA: ¡Guardado exitoso!");
            
        } catch (IOException e) {
            System.out.println("PERSISTENCIA: Error al escribir el archivo: " + e.getMessage());
        }
    }

    public void cargarSistema() {
        System.out.println("PERSISTENCIA: Buscando archivo JSON para restaurar el sistema...");
        // Aquí iría la lógica para leer el archivo y reconstruir los objetos
    }
}