package clases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorArchivos {
    public Cola<Proceso> leerCSV(String ruta) {
        Cola<Proceso> colaResultado = new Cola<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d.length >= 5) {
                    colaResultado.encolar(new Proceso(
                        d[0].trim(), d[1].trim(), 
                        Integer.parseInt(d[2].trim()), 
                        Integer.parseInt(d[3].trim()), 
                        Integer.parseInt(d[4].trim())
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return colaResultado;
    }
}