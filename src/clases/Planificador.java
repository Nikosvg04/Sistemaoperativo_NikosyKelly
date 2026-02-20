package clases;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Planificador {

    // Método EDF: Ordena los procesos por su fecha límite (Deadline)
    public void planificarEDF(List<Proceso> listaDeProcesos) {
        
        Collections.sort(listaDeProcesos, new Comparator<Proceso>() {
            @Override
            public int compare(Proceso p1, Proceso p2) {
                // Compara el deadline de p1 con el de p2
                // El que tenga el número menor (más cercano) va primero
                return Integer.compare(p1.getDeadline(), p2.getDeadline());
            }
        });

        System.out.println("--- Lista planificada por EDF ---");
        for (Proceso p : listaDeProcesos) {
            System.out.println("Proceso: " + p.getNombre() + " | Deadline: " + p.getDeadline());
        }
    }
}