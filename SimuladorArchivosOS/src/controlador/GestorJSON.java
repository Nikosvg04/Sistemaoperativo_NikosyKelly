package controlador;

import estructuras.ListaEnlazada;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GestorJSON {
    
    // Clase interna para devolver los datos que extraigamos del JSON
    public static class ArchivoCargado {
        public String nombre;
        public int tamaño;
        public String dueño;
        
        public ArchivoCargado(String nombre, int tamaño, String dueño) {
            this.nombre = nombre;
            this.tamaño = tamaño;
            this.dueño = dueño;
        }
    }

    // --- LEER JSON A MANO (Cero Librerías) ---
    public static ListaEnlazada<ArchivoCargado> leerEstadoInicial(String rutaArchivo) {
        ListaEnlazada<ArchivoCargado> lista = new ListaEnlazada<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            StringBuilder contenidoJSON = new StringBuilder();
            
            while ((linea = br.readLine()) != null) {
                contenidoJSON.append(linea.trim());
            }
            
            String jsonStr = contenidoJSON.toString();
            
            // Algoritmo de Parseo Rústico: Buscamos todo lo que esté entre llaves { }
            int indexInicio = jsonStr.indexOf("{");
            while (indexInicio != -1) {
                int indexFin = jsonStr.indexOf("}", indexInicio);
                if (indexFin == -1) break;
                
                String objetoStr = jsonStr.substring(indexInicio + 1, indexFin);
                
                // Extraer atributos cortando la cadena de texto
                String nombre = extraerValorString(objetoStr, "\"nombre\"");
                int tamaño = extraerValorInt(objetoStr, "\"tamaño\"");
                String dueño = extraerValorString(objetoStr, "\"dueño\"");
                
                if (nombre != null && tamaño > 0) {
                    if (dueño == null) dueño = "Administrador"; // Por si acaso
                    lista.agregar(new ArchivoCargado(nombre, tamaño, dueño));
                }
                
                // Buscar la siguiente llave {
                indexInicio = jsonStr.indexOf("{", indexFin + 1);
            }
            
        } catch (IOException e) {
            System.out.println("No se encontró el archivo '" + rutaArchivo + "'. El sistema iniciará vacío.");
        }
        
        return lista;
    }
    
    // Métodos auxiliares matemáticos para encontrar los valores dentro del texto
    private static String extraerValorString(String json, String clave) {
        int indexClave = json.indexOf(clave);
        if (indexClave == -1) return null;
        
        int indexDosPuntos = json.indexOf(":", indexClave);
        int indexComilla1 = json.indexOf("\"", indexDosPuntos);
        int indexComilla2 = json.indexOf("\"", indexComilla1 + 1);
        
        if (indexComilla1 != -1 && indexComilla2 != -1) {
            return json.substring(indexComilla1 + 1, indexComilla2);
        }
        return null;
    }
    
    private static int extraerValorInt(String json, String clave) {
        int indexClave = json.indexOf(clave);
        if (indexClave == -1) return 0;
        
        int indexDosPuntos = json.indexOf(":", indexClave);
        int indexComa = json.indexOf(",", indexDosPuntos);
        
        if (indexComa == -1) indexComa = json.length(); // Si es el último atributo
        
        String numeroStr = json.substring(indexDosPuntos + 1, indexComa).trim();
        try {
            return Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
// --- ESCRIBIR JSON A MANO (Exportar el estado actual) ---
    public static void guardarEstado(modelo.DirectorioVirtual carpetaRaiz, String rutaArchivo) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        estructuras.ListaEnlazada<modelo.NodoSistemaArchivos> contenido = carpetaRaiz.getContenido();
        boolean primero = true;
        
        for (int i = 0; i < contenido.tamaño(); i++) {
            modelo.NodoSistemaArchivos nodo = contenido.obtener(i);
            
            // Solo guardamos los archivos, no las carpetas (para mantenerlo simple como pide el PDF)
            if (!nodo.isDirectorio()) {
                modelo.ArchivoVirtual arch = (modelo.ArchivoVirtual) nodo;
                
                if (!primero) {
                    json.append(",\n"); // Coma para separar los objetos JSON
                }
                
                json.append("  {\n");
                json.append("    \"nombre\": \"").append(arch.getNombre()).append("\",\n");
                json.append("    \"tamaño\": ").append(arch.getTamaño()).append(",\n");
                json.append("    \"dueño\": \"").append(arch.getDueño()).append("\"\n");
                json.append("  }");
                
                primero = false;
            }
        }
        json.append("\n]");
        
        // Escribimos el texto armado en el archivo físico
        try (java.io.FileWriter fw = new java.io.FileWriter(rutaArchivo)) {
            fw.write(json.toString());
            System.out.println("💾 [EXITO] Estado del sistema guardado correctamente en: " + rutaArchivo);
        } catch (java.io.IOException e) {
            System.out.println("❌ [ERROR] No se pudo guardar el archivo JSON: " + e.getMessage());
        }
    }
}