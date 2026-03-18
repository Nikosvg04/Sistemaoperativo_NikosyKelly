package gui;

import controlador.SistemaArchivos;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import modelo.NodoSistemaArchivos;

public class VentanaPrincipal extends javax.swing.JFrame {

    private SistemaArchivos sistema;    
    private javax.swing.JPanel[] bloquesVisuales; // Array para los cuadritos de colores del disco
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    public VentanaPrincipal() {
        initComponents();
        this.sistema = new SistemaArchivos();
        
        inicializarDiscoVisual(); // Dibujamos la cuadrícula de 100 bloques vacíos
        actualizarArbolVisual();  // Dibujamos el árbol a la izquierda
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        arbolArchivos = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        panelDiscoVisual = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaFAT = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        comboModoUsuario = new javax.swing.JComboBox<>();
        comboPoliticas = new javax.swing.JComboBox<>();
        btnCrear = new javax.swing.JButton();
        btnLeer = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(arbolArchivos);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        panelDiscoVisual.setBorder(javax.swing.BorderFactory.createTitledBorder("Simulación de Disco (SD)"));

        javax.swing.GroupLayout panelDiscoVisualLayout = new javax.swing.GroupLayout(panelDiscoVisual);
        panelDiscoVisual.setLayout(panelDiscoVisualLayout);
        panelDiscoVisualLayout.setHorizontalGroup(
            panelDiscoVisualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE) // Expandí un poco el panel visualmente
        );
        panelDiscoVisualLayout.setVerticalGroup(
            panelDiscoVisualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        tablaFAT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}, {null, null, null, null},
                {null, null, null, null}, {null, null, null, null}
            },
            new String [] { "Title 1", "Title 2", "Title 3", "Title 4" }
        ));
        jScrollPane2.setViewportView(tablaFAT);

        comboModoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboPoliticas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        btnLeer.setText("Leer");
        btnActualizar.setText("Actualizar");
        btnEliminar.setText("Eliminar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboModoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboPoliticas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCrear)
                    .addComponent(btnLeer)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(comboModoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboPoliticas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCrear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLeer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelDiscoVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelDiscoVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>                        

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo (Ej. reporte.txt):");
        if (nombre == null || nombre.trim().isEmpty()) { return; }

        String tamañoStr = JOptionPane.showInputDialog(this, "Ingrese el tamaño en bloques (Ej. 5):");
        if (tamañoStr == null || tamañoStr.trim().isEmpty()) { return; }

        try {
            int tamaño = Integer.parseInt(tamañoStr);
            Color colorAleatorio = new Color((int)(Math.random() * 0x1000000));
String mensaje = sistema.solicitarCreacionArchivo(nombre, tamaño, "Usuario1", "Administrador", colorAleatorio);
            
            JOptionPane.showMessageDialog(this, mensaje, "Resultado de la Operación", JOptionPane.INFORMATION_MESSAGE);
            
            // ¡Magia doble! Refrescamos el árbol y también el disco
            actualizarArbolVisual();
            actualizarDiscoVisual();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El tamaño debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }                                        

    // Método para crear la cuadrícula de 10x10 bloques (100 cuadritos)
    private void inicializarDiscoVisual() {
        int totalBloques = 100;
        panelDiscoVisual.setLayout(new java.awt.GridLayout(10, 10, 2, 2)); // Cuadrícula de 10x10 con 2px de separación
        bloquesVisuales = new javax.swing.JPanel[totalBloques];
        
        for (int i = 0; i < totalBloques; i++) {
            bloquesVisuales[i] = new javax.swing.JPanel();
            bloquesVisuales[i].setBackground(Color.WHITE); // Blanco significa vacío
            bloquesVisuales[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            bloquesVisuales[i].setToolTipText("Bloque " + i + " (Libre)");
            panelDiscoVisual.add(bloquesVisuales[i]);
        }
    }

    // Método para colorear los cuadritos según los archivos creados
    private void actualizarDiscoVisual() {
        // 1. Limpiamos todo poniéndolo blanco primero
        for (int i = 0; i < bloquesVisuales.length; i++) {
            bloquesVisuales[i].setBackground(Color.WHITE);
            bloquesVisuales[i].setToolTipText("Bloque " + i + " (Libre)");
        }

        // 2. Obtenemos tus archivos y los pintamos
        estructuras.ListaEnlazada<NodoSistemaArchivos> contenido = sistema.getCarpetaRaiz().getContenido();
        int bloqueActual = 0; // Puntero para ir pintando casilla por casilla
        
        for (int i = 0; i < contenido.tamaño(); i++) {
            NodoSistemaArchivos nodo = contenido.obtener(i);
            
            // OJO: Asumimos que NodoSistemaArchivos tiene getTamaño() y getColor()
            int tamañoArchivo = nodo.getTamaño(); 
            Color colorArchivo = nodo.getColor(); 
            
            // Pintamos tantos cuadritos como tamaño tenga el archivo
            for(int j = 0; j < tamañoArchivo; j++) {
                if (bloqueActual < bloquesVisuales.length) {
                    bloquesVisuales[bloqueActual].setBackground(colorArchivo != null ? colorArchivo : Color.BLUE);
                    bloquesVisuales[bloqueActual].setToolTipText("Bloque " + bloqueActual + " - " + nodo.getNombre());
                    bloqueActual++;
                }
            }
        }
    }

    private void actualizarArbolVisual() {
        modelo.DirectorioVirtual raizLogica = sistema.getCarpetaRaiz();
        DefaultMutableTreeNode raizVisual = new DefaultMutableTreeNode(raizLogica.getNombre());
        estructuras.ListaEnlazada<NodoSistemaArchivos> contenido = raizLogica.getContenido();
        
        for (int i = 0; i < contenido.tamaño(); i++) {
            NodoSistemaArchivos archivo = contenido.obtener(i);
            DefaultMutableTreeNode nodoHijo = new DefaultMutableTreeNode(archivo.getNombre());
            raizVisual.add(nodoHijo);
        }
        
        DefaultTreeModel modeloArbol = new DefaultTreeModel(raizVisual);
        arbolArchivos.setModel(modeloArbol);
        
        for (int i = 0; i < arbolArchivos.getRowCount(); i++) {
            arbolArchivos.expandRow(i);
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTree arbolArchivos;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLeer;
    private javax.swing.JComboBox<String> comboModoUsuario;
    private javax.swing.JComboBox<String> comboPoliticas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelDiscoVisual;
    private javax.swing.JTable tablaFAT;
    // End of variables declaration                   
}