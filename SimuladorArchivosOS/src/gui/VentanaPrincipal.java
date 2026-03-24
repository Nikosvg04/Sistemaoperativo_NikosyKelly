package gui;

import controlador.SistemaArchivos;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaPrincipal extends javax.swing.JFrame {

    private SistemaArchivos sistema;
    private JTree arbolArchivos;
    private JButton btnActualizar, btnCrear, btnEliminar, btnLeer;
    private JPanel panelDiscoVisual;
    private JTable tablaFAT;
    private JTextArea areaLog;

    public VentanaPrincipal() {
        this.sistema = new SistemaArchivos();
        initComponents();
        actualizarTodo();
        escribirLog(">>> SISTEMA OPERATIVO INICIADO - MÓDULO FAT200 <<<");
        escribirLog("Estado: Listo para asignar bloques.");

        // --- ACCIÓN: ELIMINAR ---
        btnEliminar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del archivo a eliminar:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                String res = sistema.solicitarEliminacionArchivo(nombre);
                actualizarTodo();
                escribirLog("ACCION [ELIMINAR]: " + nombre + " | Resultado: " + res);
                JOptionPane.showMessageDialog(this, res);
            }
        });

        // --- ACCIÓN: LEER ---
        btnLeer.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del archivo a leer:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                String res = sistema.solicitarLecturaArchivo(nombre);
                escribirLog("ACCION [LECTURA]: Accediendo a punteros de '" + nombre + "'.");
                JOptionPane.showMessageDialog(this, res);
            }
        });

<<<<<<< HEAD
        // --- ACCIÓN: BUSCAR/RESALTAR ---
        btnActualizar.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del archivo a buscar:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                resaltarArchivoEnDisco(nombre);
            } else {
                refrescarDiscoVisual();
            }
        });
=======
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
>>>>>>> 7ef52eb22dc78fa57936b285e89a64f385a434a8
        
        btnCrear.addActionListener(evt -> btnCrearActionPerformed(evt));

        // --- MOTOR VISUAL AUTOMÁTICO ---
        // Este timer actualiza la pantalla cada medio segundo para que veas al disco trabajar en vivo
        javax.swing.Timer motorVisual = new javax.swing.Timer(500, e -> {
            actualizarTodo();
        });
        motorVisual.start();
    }

    private void escribirLog(String mensaje) {
        String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
        areaLog.append("[" + hora + "] " + mensaje + "\n");
        // Auto-scroll hacia abajo
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void actualizarTodo() {
        refrescarArbol(sistema.getCarpetaRaiz());
        refrescarTablaFAT();
        refrescarDiscoVisual();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador OS - Nikos & Kelly | Gestión FAT");
        setPreferredSize(new Dimension(1250, 750));
        getContentPane().setLayout(new BorderLayout(10, 10));

        // PANEL NORTE: BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(45, 45, 45));
        
        btnCrear = new JButton("NUEVO ARCHIVO");
        btnLeer = new JButton("LEER (PUNTEROS)");
        btnActualizar = new JButton("BUSCAR (VISUAL)");
        btnEliminar = new JButton("FORMATEAR/ELIMINAR");
        
        panelBotones.add(btnCrear); panelBotones.add(btnLeer); 
        panelBotones.add(btnActualizar); panelBotones.add(btnEliminar);
        getContentPane().add(panelBotones, BorderLayout.NORTH);

        // PANEL OESTE: ARBOL
        arbolArchivos = new JTree();
        JScrollPane scrollArbol = new JScrollPane(arbolArchivos);
        scrollArbol.setPreferredSize(new Dimension(250, 0));
        scrollArbol.setBorder(BorderFactory.createTitledBorder("Directorio Raíz"));
        getContentPane().add(scrollArbol, BorderLayout.WEST);

        // PANEL CENTRAL: DISCO Y TABLA
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 15, 0));
        
        panelDiscoVisual = new JPanel(new GridLayout(10, 20, 3, 3));
        panelDiscoVisual.setBackground(Color.WHITE);
        JScrollPane scrollDisco = new JScrollPane(panelDiscoVisual);
        scrollDisco.setBorder(BorderFactory.createTitledBorder("Mapa de Bits / Sectores (200 Bloques)"));
        
        tablaFAT = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tablaFAT);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Estructura de la Tabla FAT"));
        
        panelCentro.add(scrollDisco);
        panelCentro.add(scrollTabla);
        getContentPane().add(panelCentro, BorderLayout.CENTER);

        // PANEL SUR: LOG
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setBackground(new Color(30, 30, 30));
        areaLog.setForeground(new Color(0, 255, 0)); // Verde consola
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setPreferredSize(new Dimension(0, 180));
        scrollLog.setBorder(BorderFactory.createTitledBorder(null, "CONSOLA DEL SISTEMA", 0, 0, null, Color.WHITE));
        getContentPane().add(scrollLog, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {
        String n = JOptionPane.showInputDialog(this, "Nombre del archivo:");
        String t = JOptionPane.showInputDialog(this, "Cantidad de bloques (Tamaño):");
        if (n != null && t != null) {
            try {
                int tam = Integer.parseInt(t);
                Color colorArc = new Color((int)(Math.random()*0x1000000));
                
                String res = sistema.solicitarCreacionArchivo(n, tam, "User", colorArc);
                actualizarTodo();
                
                // Log detallado de bloques asignados
                escribirLog("ACCION [CREAR]: Petición encolada para '" + n + "' con " + tam + " bloques.");
                
            } catch (NumberFormatException e) {
                escribirLog("ERROR: El tamaño debe ser un valor numérico.");
            }
        }
    }

    public void resaltarArchivoEnDisco(String nombre) {
        panelDiscoVisual.removeAll();
        panelDiscoVisual.setLayout(new GridLayout(10, 20, 3, 3));
        modelo.BloqueDisco[] disco = sistema.getGestorDisco().getDisco();
        boolean encontrado = false;
        StringBuilder bloques = new StringBuilder();
        
        for (int i = 0; i < disco.length; i++) {
            JPanel cuadrito = new JPanel();
            cuadrito.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            
            if (disco[i].isOcupado() && disco[i].getNombreArchivo().equalsIgnoreCase(nombre)) {
                cuadrito.setBackground(Color.YELLOW);
                cuadrito.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                encontrado = true;
                bloques.append(i).append(" ");
            } else {
                cuadrito.setBackground(disco[i].isOcupado() ? new Color(100, 150, 255) : new Color(230, 230, 230));
            }
            panelDiscoVisual.add(cuadrito);
        }
        
        panelDiscoVisual.revalidate(); panelDiscoVisual.repaint();
        
        if (encontrado) {
            escribirLog("BUSQUEDA: '" + nombre + "' encontrado en bloques: [ " + bloques + "]");
        } else {
            escribirLog("BUSQUEDA: El archivo '" + nombre + "' no existe en el índice.");
            JOptionPane.showMessageDialog(this, "Archivo no localizado.");
            refrescarDiscoVisual();
        }
    }

    public void refrescarArbol(modelo.DirectorioVirtual raiz) {
        DefaultMutableTreeNode nRaiz = new DefaultMutableTreeNode("Root (C:/)");
        llenarNodo(nRaiz, raiz);
        arbolArchivos.setModel(new DefaultTreeModel(nRaiz));
        for (int i = 0; i < arbolArchivos.getRowCount(); i++) arbolArchivos.expandRow(i);
    }

    private void llenarNodo(DefaultMutableTreeNode nodo, modelo.DirectorioVirtual dir) {
        for (int i = 0; i < dir.getContenido().tamaño(); i++) {
            Object el = dir.getContenido().obtener(i);
            if (el instanceof modelo.ArchivoVirtual) {
                modelo.ArchivoVirtual arc = (modelo.ArchivoVirtual)el;
                nodo.add(new DefaultMutableTreeNode(arc.getNombre() + " (" + arc.getTamañoEnBloques() + " blq)"));
            } else {
                modelo.DirectorioVirtual subD = (modelo.DirectorioVirtual)el;
                DefaultMutableTreeNode sub = new DefaultMutableTreeNode(subD.getNombre());
                nodo.add(sub);
                llenarNodo(sub, subD);
            }
        }
    }

    public void refrescarTablaFAT() {
        String[] col = {"BLOQUE", "CONTENIDO (DATA)", "PUNTERO (SIG)"};
        Object[][] data = new Object[200][3];
        modelo.BloqueDisco[] d = sistema.getGestorDisco().getDisco();
        for (int i = 0; i < 200; i++) {
            data[i][0] = String.format("%03d", i);
            data[i][1] = d[i].isOcupado() ? "ID: " + d[i].getNombreArchivo() : "[ VACÍO ]";
            data[i][2] = d[i].getSiguienteBloque() == -1 ? "EOF" : d[i].getSiguienteBloque();
        }
        tablaFAT.setModel(new javax.swing.table.DefaultTableModel(data, col));
    }

    public void refrescarDiscoVisual() {
        panelDiscoVisual.removeAll();
        panelDiscoVisual.setLayout(new GridLayout(10, 20, 3, 3));
        modelo.BloqueDisco[] d = sistema.getGestorDisco().getDisco();
        for (int i = 0; i < 200; i++) {
            JPanel c = new JPanel();
            c.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            c.setBackground(d[i].isOcupado() ? new Color(100, 150, 255) : new Color(230, 230, 230));
            panelDiscoVisual.add(c);
        }
        panelDiscoVisual.revalidate(); panelDiscoVisual.repaint();
    }

    public static void main(String args[]) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}