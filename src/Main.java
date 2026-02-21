import clases.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("*********************************");
        System.out.println(" HOLA! EL PROGRAMA ESTA CORRIENDO");
        System.out.println("*********************************");

        // 1. Instanciamos el lector y el planificador
        LectorArchivos lector = new LectorArchivos();
        Planificador planificador = new Planificador();

        // 2. Leemos el archivo (Asegúrate de que el nombre coincida con tu .csv)
        // Si tu archivo se llama diferente, cambia "procesos.csv" por el tuyo
        Cola<Proceso> colaProcesos = lector.leerCSV("procesos.csv");

        if (colaProcesos.estaVacia()) {
            System.out.println("Error: No se cargaron procesos. Revisa el archivo CSV.");
        } else {
            System.out.println("Procesos cargados: " + colaProcesos.getTamano());
            
            // 3. Ordenamos
            planificador.ordenarPorPrioridad(colaProcesos);
            System.out.println("¡Procesos ordenados por prioridad con éxito!");
        }
    }
}