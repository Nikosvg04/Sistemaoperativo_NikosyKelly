package clases;

public class Planificador {
    public void ordenarPorPrioridad(Cola<Proceso> cola) {
        int n = cola.getTamano(); // <--- Aquí estaba el error
        if (n <= 1) return;

        Proceso[] arr = new Proceso[n];
        for (int i = 0; i < n; i++) arr[i] = cola.desencolar();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getPrioridad() > arr[j + 1].getPrioridad()) {
                    Proceso temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (Proceso p : arr) cola.encolar(p);
    }
}