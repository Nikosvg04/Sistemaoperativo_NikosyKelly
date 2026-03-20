package gui;

import controlador.SistemaArchivos;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.Color;

public class VentanaPrincipal extends javax.swing.JFrame {

    private SistemaArchivos sistema;    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    public VentanaPrincipal() {
        initComponents();
        this.sistema = new SistemaArchivos();
        
        // ¡MAGIA! Cargamos todo apenas se abre la ventana
        refrescarArbol(sistema.getCarpetaRaiz()); 
        refrescarTablaFAT();
        refrescarDiscoVisual(); // Agregado para los cuadritos de colores

        // --- CONECTAMOS EL BOTÓN ELIMINAR MANUALMENTE (Bypass de NetBeans) ---
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Pedimos el nombre del archivo a borrar
                String nombre = JOptionPane.showInputDialog(VentanaPrincipal.this, "Ingrese el nombre exacto del archivo a eliminar (Ej. reporte.txt):");
                
                if (nombre == null || nombre.trim().isEmpty()) {
                    return; // Si el usuario cancela, no hacemos nada
                }

                // Le decimos al backend que lo elimine
                String mensaje = sistema.solicitarEliminacionArchivo(nombre);
                
                // ¡ACTUALIZAMOS TODO VISUALMENTE!
                refrescarArbol(sistema.getCarpetaRaiz());
                refrescarTablaFAT(); 
                refrescarDiscoVisual();
                
                // Mostramos el resultado
                JOptionPane.showMessageDialog(VentanaPrincipal.this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // --- CONECTAMOS EL BOTÓN LEER MANUALMENTE ---
        btnLeer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Pedimos el nombre del archivo a leer
                String nombre = JOptionPane.showInputDialog(VentanaPrincipal.this, "Ingrese el nombre del archivo a leer (Ej. foto.png):");
                
                if (nombre == null || nombre.trim().isEmpty()) {
                    return; // Si cancela, no hacemos nada
                }

                // Le pedimos al backend que simule la lectura
                String mensaje = sistema.solicitarLecturaArchivo(nombre);
                
                // Mostramos el aviso de que se está leyendo
                JOptionPane.showMessageDialog(VentanaPrincipal.this, mensaje, "Operación de Lectura", JOptionPane.INFORMATION_MESSAGE);
            }
        });
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
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panelDiscoVisualLayout.setVerticalGroup(
            panelDiscoVisualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        tablaFAT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(panelDiscoVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelDiscoVisual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>                        

    // --- ACCIÓN DEL BOTÓN CREAR ARCHIVO ---
    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo (Ej. reporte.txt):");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return; 
        }

        String tamañoStr = JOptionPane.showInputDialog(this, "Ingrese el tamaño en bloques (Ej. 5):");
        if (tamañoStr == null || tamañoStr.trim().isEmpty()) {
            return;
        }

        try {
            int tamaño = Integer.parseInt(tamañoStr); 
            Color colorAleatorio = new Color((int)(Math.random() * 0x1000000));
            
            // Llamamos al cerebro (Backend)
            String mensaje = sistema.solicitarCreacionArchivo(nombre, tamaño, "Usuario", colorAleatorio);
            
            // Mostramos resultado
            JOptionPane.showMessageDialog(this, mensaje, "Resultado de la Operación", JOptionPane.INFORMATION_MESSAGE);
            
            // ¡ACTUALIZAMOS EL ÁRBOL, LA TABLA Y EL DISCO VISUALMENTE!
            refrescarArbol(sistema.getCarpetaRaiz());
            refrescarTablaFAT(); 
            refrescarDiscoVisual();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El tamaño debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }                                        

    // --- MÉTODOS DEL JTREE ---
    public void refrescarArbol(modelo.DirectorioVirtual carpetaRaiz) {
        DefaultMutableTreeNode nodoRaiz = construirArbol(carpetaRaiz);
        DefaultTreeModel modeloArbol = new DefaultTreeModel(nodoRaiz);
        arbolArchivos.setModel(modeloArbol);
        
        for (int i = 0; i < arbolArchivos.getRowCount(); i++) {
            arbolArchivos.expandRow(i);
        }
    }

    private DefaultMutableTreeNode construirArbol(modelo.DirectorioVirtual directorioActual) {
        String etiquetaDir = directorioActual.getNombre();
        DefaultMutableTreeNode nodoDir = new DefaultMutableTreeNode(etiquetaDir);

        estructuras.ListaEnlazada<modelo.NodoSistemaArchivos> elementos = directorioActual.getContenido();
        
        for (int i = 0; i < elementos.tamaño(); i++) {
            modelo.NodoSistemaArchivos elementoActual = elementos.obtener(i);
            
            if (elementoActual instanceof modelo.ArchivoVirtual) {
                modelo.ArchivoVirtual archivo = (modelo.ArchivoVirtual) elementoActual;
                String etiquetaArchivo = archivo.getNombre() + " | " + archivo.getTamañoEnBloques() + " bloques";
                nodoDir.add(new DefaultMutableTreeNode(etiquetaArchivo));
            } 
            else if (elementoActual instanceof modelo.DirectorioVirtual) {
                modelo.DirectorioVirtual subCarpeta = (modelo.DirectorioVirtual) elementoActual;
                nodoDir.add(construirArbol(subCarpeta)); 
            }
        }
        return nodoDir;
    }

    // --- MÉTODO PARA ACTUALIZAR LA TABLA FAT ---
    public void refrescarTablaFAT() {
        javax.swing.table.DefaultTableModel modeloTabla = (javax.swing.table.DefaultTableModel) tablaFAT.getModel();
        modeloTabla.setColumnIdentifiers(new String[]{"Bloque ID", "Estado / Archivo", "Siguiente Bloque"});
        modeloTabla.setRowCount(0); 

        modelo.BloqueDisco[] disco = sistema.getGestorDisco().getDisco();
        
        for (int i = 0; i < disco.length; i++) {
            modelo.BloqueDisco bloque = disco[i];
            String estadoArchivo = bloque.isOcupado() ? bloque.getNombreArchivo() : "Libre";
            String siguiente = bloque.getSiguienteBloque() == -1 ? "-1 (EOF)" : String.valueOf(bloque.getSiguienteBloque());
            
            modeloTabla.addRow(new Object[]{
                bloque.getId(), 
                estadoArchivo, 
                bloque.isOcupado() ? siguiente : "-"
            });
        }
    }

    // --- MÉTODO PARA DIBUJAR LOS CUADRITOS DEL DISCO (SD) ---
    public void refrescarDiscoVisual() {
        panelDiscoVisual.removeAll();
        // Configuramos una cuadrícula de 10 filas por 20 columnas (200 bloques)
        panelDiscoVisual.setLayout(new java.awt.GridLayout(10, 20, 2, 2)); 
        
        modelo.BloqueDisco[] disco = sistema.getGestorDisco().getDisco();
        
        for (int i = 0; i < disco.length; i++) {
            javax.swing.JPanel cuadrito = new javax.swing.JPanel();
            cuadrito.setBorder(javax.swing.BorderFactory.createLineBorder(Color.DARK_GRAY));
            
            if (disco[i].isOcupado()) {
                // Si está ocupado, lo pintamos de un color y le ponemos texto al pasar el mouse
                cuadrito.setBackground(new Color(100, 150, 255)); // Azul claro
                cuadrito.setToolTipText("Bloque " + i + " - Archivo: " + disco[i].getNombreArchivo());
            } else {
                // Si está libre, lo pintamos gris
                cuadrito.setBackground(Color.LIGHT_GRAY);
                cuadrito.setToolTipText("Bloque " + i + " - Libre");
            }
            panelDiscoVisual.add(cuadrito);
        }
        
        // Recargamos el panel visualmente
        panelDiscoVisual.revalidate();
        panelDiscoVisual.repaint();
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