/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simuladorarchivosos;

/**
 *
 * @author nikos
 */
public class SimuladorArchivosOS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      // Arrancar la ventana principal
        java.awt.EventQueue.invokeLater(() -> {
            new gui.VentanaPrincipal().setVisible(true);
        });  
    }
    
}
