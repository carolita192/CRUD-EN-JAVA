package com.transporte.vista;

import com.transporte.conexion.Conexion;
import com.transporte.dao.CamionDAO;
import com.transporte.dao.CamioneroDAO;
import com.transporte.dao.PaqueteDAO;
import com.transporte.dao.CamioneroCamionDAO;
import com.transporte.modelo.Camion;
import com.transporte.modelo.Camionero;
import com.transporte.modelo.Paquete;
import com.transporte.modelo.CamioneroCamion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final CamionDAO camionDAO = new CamionDAO();
    private final CamioneroDAO camioneroDAO = new CamioneroDAO();
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();
    private final CamioneroCamionDAO camioneroCamionDAO = new CamioneroCamionDAO();

    private JTabbedPane tabbedPane;

    // Campos formulario Camion
    private JTextField txtCamionMatricula;
    private JTextField txtCamionModelo;
    private JComboBox<String> cbCamionTipo;
    private JTextField txtCamionPotencia;
    private JTable tablaCamiones;
    private DefaultTableModel modeloTablaCamiones;
    private JButton btnCamionGuardar;
    private JButton btnCamionActualizar;
    private JButton btnCamionEliminar;
    private JButton btnCamionLimpiar;

    // Campos formulario Camionero
    private JTextField txtCamioneroDni;
    private JTextField txtCamioneroNombre;
    private JTextField txtCamioneroTelefono;
    private JTextField txtCamioneroDireccion;
    private JTextField txtCamioneroSalario;
    private JTextField txtCamioneroPoblacion;
    private JTable tablaCamioneros;
    private DefaultTableModel modeloTablaCamioneros;
    private JButton btnCamioneroGuardar;
    private JButton btnCamioneroActualizar;
    private JButton btnCamioneroEliminar;
    private JButton btnCamioneroLimpiar;

    // Campos formulario Paquete
    private JTextField txtPaqueteCodigo;
    private JTextField txtPaqueteDescripcion;
    private JTextField txtPaqueteDestinatario;
    private JTextField txtPaqueteDireccion;
    private JComboBox<Camionero> cbPaqueteCamionero;
    private JTable tablaPaquetes;
    private DefaultTableModel modeloTablaPaquetes;
    private JButton btnPaqueteGuardar;
    private JButton btnPaqueteActualizar;
    private JButton btnPaqueteEliminar;
    private JButton btnPaqueteLimpiar;

    // Campos formulario Camionero - Camion
    private JTextField txtAsignacionId;
    private JComboBox<Camionero> cbAsignacionCamionero;
    private JComboBox<Camion> cbAsignacionCamion;
    private JTextField txtAsignacionFecha;
    private JTable tablaAsignaciones;
    private DefaultTableModel modeloTablaAsignaciones;
    private JButton btnAsignacionGuardar;
    private JButton btnAsignacionActualizar;
    private JButton btnAsignacionEliminar;
    private JButton btnAsignacionLimpiar;

    public VentanaPrincipal() {
        super("Sistema de Transporte y Paqueteria - Base de Datos db_paqueteria");
        initUI();
        cargarTodosLosDatos();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearMenu();

        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(new Color(30, 50, 90));
        panelEncabezado.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTION DE TRANSPORTE Y PAQUETERIA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Modulo de Mantenimiento CRUD - db_paqueteria");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(220, 230, 250));

        panelEncabezado.add(lblTitulo, BorderLayout.NORTH);
        panelEncabezado.add(lblSubtitulo, BorderLayout.SOUTH);
        add(panelEncabezado, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabbedPane.addTab("Camiones", crearPanelCamion());
        tabbedPane.addTab("Camioneros", crearPanelCamionero());
        tabbedPane.addTab("Paquetes", crearPanelPaquete());
        tabbedPane.addTab("Asignaciones (Camionero - Camion)", crearPanelAsignacion());

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 2 || selectedIndex == 3) {
                refrescarCombosRelacionales();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStatus.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblStatus = new JLabel("Estado: Conectado a db_paqueteria (MySQL).");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelStatus.add(lblStatus);
        add(panelStatus, BorderLayout.SOUTH);
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuBD = new JMenu("Conexion");
        JMenuItem itemConfig = new JMenuItem("Configurar parametros de conexion...");
        itemConfig.addActionListener(e -> mostrarDialogoConfiguracionBD());
        JMenuItem itemTest = new JMenuItem("Probar conexion");
        itemTest.addActionListener(e -> probarConexionBD());
        menuBD.add(itemConfig);
        menuBD.add(itemTest);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemInfo = new JMenuItem("Acerca de");
        itemInfo.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistema CRUD para caso de Transporte y Paqueteria\n" +
                "Base de datos: db_paqueteria\n" +
                "Tablas: camion, camionero, paquete y camionero_camion",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemInfo);

        menuBar.add(menuBD);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);
    }

    // =========================================================================
    // 1. PANEL CAMIONES
    // =========================================================================
    private JPanel crearPanelCamion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Camion", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(30, 50, 90)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtCamionMatricula = new JTextField(15);
        txtCamionModelo = new JTextField(15);
        cbCamionTipo = new JComboBox<>(new String[]{"Trailer Frigorifico", "Cisterna", "Plataforma", "Furgon Cerrado", "Volquete", "Portacontenedores"});
        txtCamionPotencia = new JTextField(15);

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Matricula:"), g);
        g.gridx = 1; g.gridy = 0; form.add(txtCamionMatricula, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Modelo:"), g);
        g.gridx = 1; g.gridy = 1; form.add(txtCamionModelo, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Tipo:"), g);
        g.gridx = 1; g.gridy = 2; form.add(cbCamionTipo, g);

        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Potencia (CV):"), g);
        g.gridx = 1; g.gridy = 3; form.add(txtCamionPotencia, g);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnCamionGuardar = new JButton("Guardar");
        btnCamionActualizar = new JButton("Actualizar");
        btnCamionEliminar = new JButton("Eliminar");
        btnCamionLimpiar = new JButton("Limpiar");

        btnCamionActualizar.setEnabled(false);
        btnCamionEliminar.setEnabled(false);

        pnlBotones.add(btnCamionGuardar);
        pnlBotones.add(btnCamionActualizar);
        pnlBotones.add(btnCamionEliminar);
        pnlBotones.add(btnCamionLimpiar);

        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        form.add(pnlBotones, g);

        modeloTablaCamiones = new DefaultTableModel(new String[]{"Matricula", "Modelo", "Tipo", "Potencia"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCamiones = new JTable(modeloTablaCamiones);
        tablaCamiones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCamiones.getTableHeader().setReorderingAllowed(false);

        tablaCamiones.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaCamiones.getSelectedRow();
            if (fila >= 0) {
                txtCamionMatricula.setText(modeloTablaCamiones.getValueAt(fila, 0).toString());
                txtCamionMatricula.setEditable(false);
                txtCamionModelo.setText(modeloTablaCamiones.getValueAt(fila, 1).toString());
                cbCamionTipo.setSelectedItem(modeloTablaCamiones.getValueAt(fila, 2).toString());
                txtCamionPotencia.setText(modeloTablaCamiones.getValueAt(fila, 3).toString());

                btnCamionGuardar.setEnabled(false);
                btnCamionActualizar.setEnabled(true);
                btnCamionEliminar.setEnabled(true);
            }
        });

        btnCamionGuardar.addActionListener(e -> guardarCamion());
        btnCamionActualizar.addActionListener(e -> actualizarCamion());
        btnCamionEliminar.addActionListener(e -> eliminarCamion());
        btnCamionLimpiar.addActionListener(e -> limpiarCamionForm());

        panel.add(form, BorderLayout.WEST);
        panel.add(new JScrollPane(tablaCamiones), BorderLayout.CENTER);
        return panel;
    }

    private void guardarCamion() {
        String matricula = txtCamionMatricula.getText().trim();
        String modelo = txtCamionModelo.getText().trim();
        String tipo = (String) cbCamionTipo.getSelectedItem();
        String potenciaStr = txtCamionPotencia.getText().trim();

        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una matricula.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamionMatricula.requestFocus();
            return;
        }
        if (modelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El modelo del camion es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamionModelo.requestFocus();
            return;
        }
        int potencia;
        try {
            potencia = Integer.parseInt(potenciaStr);
            if (potencia <= 0) {
                JOptionPane.showMessageDialog(this, "La potencia debe ser mayor a 0.", "Validacion", JOptionPane.WARNING_MESSAGE);
                txtCamionPotencia.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La potencia debe ser un numero entero valido.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamionPotencia.requestFocus();
            return;
        }

        try {
            if (camionDAO.existe(matricula)) {
                JOptionPane.showMessageDialog(this, "Ya existe un camion registrado con la matricula: " + matricula, "Registro existente", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Camion c = new Camion(matricula, modelo, tipo, potencia);
            if (camionDAO.insertar(c)) {
                JOptionPane.showMessageDialog(this, "Camion registrado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamionForm();
                cargarCamiones();
                refrescarCombosRelacionales();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar camion: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCamion() {
        String matricula = txtCamionMatricula.getText().trim();
        String modelo = txtCamionModelo.getText().trim();
        String tipo = (String) cbCamionTipo.getSelectedItem();
        String potenciaStr = txtCamionPotencia.getText().trim();

        if (modelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El modelo del camion es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int potencia;
        try {
            potencia = Integer.parseInt(potenciaStr);
            if (potencia <= 0) {
                JOptionPane.showMessageDialog(this, "La potencia debe ser mayor a 0.", "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La potencia debe ser un numero entero valido.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Camion c = new Camion(matricula, modelo, tipo, potencia);
            if (camionDAO.actualizar(c)) {
                JOptionPane.showMessageDialog(this, "Camion actualizado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamionForm();
                cargarCamiones();
                refrescarCombosRelacionales();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar camion: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCamion() {
        String matricula = txtCamionMatricula.getText().trim();
        int opt = JOptionPane.showConfirmDialog(this, "Desea eliminar el camion con matricula " + matricula + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                if (camionDAO.eliminar(matricula)) {
                    JOptionPane.showMessageDialog(this, "Camion eliminado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCamionForm();
                    cargarCamiones();
                    refrescarCombosRelacionales();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar el camion porque tiene registros asociados en asignaciones de conduccion.\nDetalle: " + ex.getMessage(),
                        "Restriccion de clave foranea", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCamionForm() {
        txtCamionMatricula.setText("");
        txtCamionMatricula.setEditable(true);
        txtCamionModelo.setText("");
        cbCamionTipo.setSelectedIndex(0);
        txtCamionPotencia.setText("");
        tablaCamiones.clearSelection();

        btnCamionGuardar.setEnabled(true);
        btnCamionActualizar.setEnabled(false);
        btnCamionEliminar.setEnabled(false);
    }

    private void cargarCamiones() {
        modeloTablaCamiones.setRowCount(0);
        try {
            List<Camion> lista = camionDAO.listar();
            for (Camion c : lista) {
                modeloTablaCamiones.addRow(new Object[]{c.getMatricula(), c.getModelo(), c.getTipo(), c.getPotencia()});
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar camiones: " + ex.getMessage());
        }
    }

    // =========================================================================
    // 2. PANEL CAMIONEROS
    // =========================================================================
    private JPanel crearPanelCamionero() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Camionero", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(30, 50, 90)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtCamioneroDni = new JTextField(15);
        txtCamioneroNombre = new JTextField(15);
        txtCamioneroTelefono = new JTextField(15);
        txtCamioneroDireccion = new JTextField(15);
        txtCamioneroSalario = new JTextField(15);
        txtCamioneroPoblacion = new JTextField(15);

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("DNI:"), g);
        g.gridx = 1; g.gridy = 0; form.add(txtCamioneroDni, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Nombre:"), g);
        g.gridx = 1; g.gridy = 1; form.add(txtCamioneroNombre, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Telefono:"), g);
        g.gridx = 1; g.gridy = 2; form.add(txtCamioneroTelefono, g);

        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Direccion:"), g);
        g.gridx = 1; g.gridy = 3; form.add(txtCamioneroDireccion, g);

        g.gridx = 0; g.gridy = 4; form.add(new JLabel("Salario:"), g);
        g.gridx = 1; g.gridy = 4; form.add(txtCamioneroSalario, g);

        g.gridx = 0; g.gridy = 5; form.add(new JLabel("Poblacion:"), g);
        g.gridx = 1; g.gridy = 5; form.add(txtCamioneroPoblacion, g);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnCamioneroGuardar = new JButton("Guardar");
        btnCamioneroActualizar = new JButton("Actualizar");
        btnCamioneroEliminar = new JButton("Eliminar");
        btnCamioneroLimpiar = new JButton("Limpiar");

        btnCamioneroActualizar.setEnabled(false);
        btnCamioneroEliminar.setEnabled(false);

        pnlBotones.add(btnCamioneroGuardar);
        pnlBotones.add(btnCamioneroActualizar);
        pnlBotones.add(btnCamioneroEliminar);
        pnlBotones.add(btnCamioneroLimpiar);

        g.gridx = 0; g.gridy = 6; g.gridwidth = 2;
        form.add(pnlBotones, g);

        modeloTablaCamioneros = new DefaultTableModel(new String[]{"DNI", "Nombre", "Telefono", "Direccion", "Salario", "Poblacion"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCamioneros = new JTable(modeloTablaCamioneros);
        tablaCamioneros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCamioneros.getTableHeader().setReorderingAllowed(false);

        tablaCamioneros.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaCamioneros.getSelectedRow();
            if (fila >= 0) {
                txtCamioneroDni.setText(modeloTablaCamioneros.getValueAt(fila, 0).toString());
                txtCamioneroDni.setEditable(false);
                txtCamioneroNombre.setText(modeloTablaCamioneros.getValueAt(fila, 1).toString());
                txtCamioneroTelefono.setText(modeloTablaCamioneros.getValueAt(fila, 2).toString());
                txtCamioneroDireccion.setText(modeloTablaCamioneros.getValueAt(fila, 3).toString());
                txtCamioneroSalario.setText(modeloTablaCamioneros.getValueAt(fila, 4).toString());
                txtCamioneroPoblacion.setText(modeloTablaCamioneros.getValueAt(fila, 5).toString());

                btnCamioneroGuardar.setEnabled(false);
                btnCamioneroActualizar.setEnabled(true);
                btnCamioneroEliminar.setEnabled(true);
            }
        });

        btnCamioneroGuardar.addActionListener(e -> guardarCamionero());
        btnCamioneroActualizar.addActionListener(e -> actualizarCamionero());
        btnCamioneroEliminar.addActionListener(e -> eliminarCamionero());
        btnCamioneroLimpiar.addActionListener(e -> limpiarCamioneroForm());

        panel.add(form, BorderLayout.WEST);
        panel.add(new JScrollPane(tablaCamioneros), BorderLayout.CENTER);
        return panel;
    }

    private void guardarCamionero() {
        String dni = txtCamioneroDni.getText().trim();
        String nombre = txtCamioneroNombre.getText().trim();
        String telefono = txtCamioneroTelefono.getText().trim();
        String direccion = txtCamioneroDireccion.getText().trim();
        String salarioStr = txtCamioneroSalario.getText().trim();
        String poblacion = txtCamioneroPoblacion.getText().trim();

        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El DNI es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamioneroDni.requestFocus();
            return;
        }
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre completo es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamioneroNombre.requestFocus();
            return;
        }
        if (telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El telefono es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamioneroTelefono.requestFocus();
            return;
        }
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La direccion es obligatoria.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamioneroDireccion.requestFocus();
            return;
        }
        double salario;
        try {
            salario = Double.parseDouble(salarioStr);
            if (salario <= 0) {
                JOptionPane.showMessageDialog(this, "El salario debe ser mayor que 0.", "Validacion", JOptionPane.WARNING_MESSAGE);
                txtCamioneroSalario.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un numero valido.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamioneroSalario.requestFocus();
            return;
        }
        if (poblacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La poblacion es obligatoria.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCamioneroPoblacion.requestFocus();
            return;
        }

        try {
            if (camioneroDAO.existe(dni)) {
                JOptionPane.showMessageDialog(this, "Ya existe un camionero con el DNI: " + dni, "Registro existente", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Camionero c = new Camionero(dni, nombre, telefono, direccion, salario, poblacion);
            if (camioneroDAO.insertar(c)) {
                JOptionPane.showMessageDialog(this, "Camionero registrado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamioneroForm();
                cargarCamioneros();
                refrescarCombosRelacionales();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar camionero: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCamionero() {
        String dni = txtCamioneroDni.getText().trim();
        String nombre = txtCamioneroNombre.getText().trim();
        String telefono = txtCamioneroTelefono.getText().trim();
        String direccion = txtCamioneroDireccion.getText().trim();
        String salarioStr = txtCamioneroSalario.getText().trim();
        String poblacion = txtCamioneroPoblacion.getText().trim();

        if (nombre.isEmpty() || direccion.isEmpty() || poblacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double salario;
        try {
            salario = Double.parseDouble(salarioStr);
            if (salario <= 0) {
                JOptionPane.showMessageDialog(this, "El salario debe ser positivo.", "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un valor numerico.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Camionero c = new Camionero(dni, nombre, telefono, direccion, salario, poblacion);
            if (camioneroDAO.actualizar(c)) {
                JOptionPane.showMessageDialog(this, "Camionero actualizado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamioneroForm();
                cargarCamioneros();
                refrescarCombosRelacionales();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar camionero: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCamionero() {
        String dni = txtCamioneroDni.getText().trim();
        int opt = JOptionPane.showConfirmDialog(this, "Desea eliminar al camionero con DNI " + dni + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                if (camioneroDAO.eliminar(dni)) {
                    JOptionPane.showMessageDialog(this, "Camionero eliminado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCamioneroForm();
                    cargarCamioneros();
                    refrescarCombosRelacionales();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar el camionero porque tiene paquetes o asignaciones vinculadas.\nDetalle: " + ex.getMessage(),
                        "Restriccion de clave foranea", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCamioneroForm() {
        txtCamioneroDni.setText("");
        txtCamioneroDni.setEditable(true);
        txtCamioneroNombre.setText("");
        txtCamioneroTelefono.setText("");
        txtCamioneroDireccion.setText("");
        txtCamioneroSalario.setText("");
        txtCamioneroPoblacion.setText("");
        tablaCamioneros.clearSelection();

        btnCamioneroGuardar.setEnabled(true);
        btnCamioneroActualizar.setEnabled(false);
        btnCamioneroEliminar.setEnabled(false);
    }

    private void cargarCamioneros() {
        modeloTablaCamioneros.setRowCount(0);
        try {
            List<Camionero> lista = camioneroDAO.listar();
            for (Camionero c : lista) {
                modeloTablaCamioneros.addRow(new Object[]{
                        c.getDni(), c.getNombre(), c.getTelefono(), c.getDireccion(),
                        String.format("%.2f", c.getSalario()), c.getPoblacion()
                });
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar camioneros: " + ex.getMessage());
        }
    }

    // =========================================================================
    // 3. PANEL PAQUETES
    // =========================================================================
    private JPanel crearPanelPaquete() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Paquete", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(30, 50, 90)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtPaqueteCodigo = new JTextField(15);
        txtPaqueteCodigo.setEditable(false);
        txtPaqueteCodigo.setText("(Autogenerado)");
        txtPaqueteDescripcion = new JTextField(15);
        txtPaqueteDestinatario = new JTextField(15);
        txtPaqueteDireccion = new JTextField(15);
        cbPaqueteCamionero = new JComboBox<>();

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Codigo:"), g);
        g.gridx = 1; g.gridy = 0; form.add(txtPaqueteCodigo, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Descripcion:"), g);
        g.gridx = 1; g.gridy = 1; form.add(txtPaqueteDescripcion, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Destinatario:"), g);
        g.gridx = 1; g.gridy = 2; form.add(txtPaqueteDestinatario, g);

        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Direccion Destino:"), g);
        g.gridx = 1; g.gridy = 3; form.add(txtPaqueteDireccion, g);

        g.gridx = 0; g.gridy = 4; form.add(new JLabel("Camionero Asignado:"), g);
        g.gridx = 1; g.gridy = 4; form.add(cbPaqueteCamionero, g);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnPaqueteGuardar = new JButton("Guardar");
        btnPaqueteActualizar = new JButton("Actualizar");
        btnPaqueteEliminar = new JButton("Eliminar");
        btnPaqueteLimpiar = new JButton("Limpiar");

        btnPaqueteActualizar.setEnabled(false);
        btnPaqueteEliminar.setEnabled(false);

        pnlBotones.add(btnPaqueteGuardar);
        pnlBotones.add(btnPaqueteActualizar);
        pnlBotones.add(btnPaqueteEliminar);
        pnlBotones.add(btnPaqueteLimpiar);

        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        form.add(pnlBotones, g);

        modeloTablaPaquetes = new DefaultTableModel(new String[]{"Codigo", "Descripcion", "Destinatario", "Direccion", "DNI Camionero", "Camionero"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaPaquetes = new JTable(modeloTablaPaquetes);
        tablaPaquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPaquetes.getTableHeader().setReorderingAllowed(false);

        tablaPaquetes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPaquetes.getSelectedRow();
            if (fila >= 0) {
                txtPaqueteCodigo.setText(modeloTablaPaquetes.getValueAt(fila, 0).toString());
                txtPaqueteDescripcion.setText(modeloTablaPaquetes.getValueAt(fila, 1).toString());
                txtPaqueteDestinatario.setText(modeloTablaPaquetes.getValueAt(fila, 2).toString());
                txtPaqueteDireccion.setText(modeloTablaPaquetes.getValueAt(fila, 3).toString());

                String dniCamionero = modeloTablaPaquetes.getValueAt(fila, 4).toString();
                seleccionarCamioneroEnCombo(cbPaqueteCamionero, dniCamionero);

                btnPaqueteGuardar.setEnabled(false);
                btnPaqueteActualizar.setEnabled(true);
                btnPaqueteEliminar.setEnabled(true);
            }
        });

        btnPaqueteGuardar.addActionListener(e -> guardarPaquete());
        btnPaqueteActualizar.addActionListener(e -> actualizarPaquete());
        btnPaqueteEliminar.addActionListener(e -> eliminarPaquete());
        btnPaqueteLimpiar.addActionListener(e -> limpiarPaqueteForm());

        panel.add(form, BorderLayout.WEST);
        panel.add(new JScrollPane(tablaPaquetes), BorderLayout.CENTER);
        return panel;
    }

    private void guardarPaquete() {
        String descripcion = txtPaqueteDescripcion.getText().trim();
        String destinatario = txtPaqueteDestinatario.getText().trim();
        String direccion = txtPaqueteDireccion.getText().trim();
        Camionero camioneroSel = (Camionero) cbPaqueteCamionero.getSelectedItem();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripcion del paquete es obligatoria.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtPaqueteDescripcion.requestFocus();
            return;
        }
        if (destinatario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El destinatario es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtPaqueteDestinatario.requestFocus();
            return;
        }
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La direccion del destinatario es obligatoria.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtPaqueteDireccion.requestFocus();
            return;
        }
        if (camioneroSel == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un camionero.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Paquete p = new Paquete(0, descripcion, destinatario, direccion, camioneroSel.getDni());
            if (paqueteDAO.insertar(p)) {
                JOptionPane.showMessageDialog(this, "Paquete registrado con codigo #" + p.getCodigo(), "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarPaqueteForm();
                cargarPaquetes();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar paquete: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPaquete() {
        int codigo = Integer.parseInt(txtPaqueteCodigo.getText().trim());
        String descripcion = txtPaqueteDescripcion.getText().trim();
        String destinatario = txtPaqueteDestinatario.getText().trim();
        String direccion = txtPaqueteDireccion.getText().trim();
        Camionero camioneroSel = (Camionero) cbPaqueteCamionero.getSelectedItem();

        if (descripcion.isEmpty() || destinatario.isEmpty() || direccion.isEmpty() || camioneroSel == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Paquete p = new Paquete(codigo, descripcion, destinatario, direccion, camioneroSel.getDni());
            if (paqueteDAO.actualizar(p)) {
                JOptionPane.showMessageDialog(this, "Paquete #" + codigo + " actualizado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarPaqueteForm();
                cargarPaquetes();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar paquete: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPaquete() {
        int codigo = Integer.parseInt(txtPaqueteCodigo.getText().trim());
        int opt = JOptionPane.showConfirmDialog(this, "Desea eliminar el paquete #" + codigo + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                if (paqueteDAO.eliminar(codigo)) {
                    JOptionPane.showMessageDialog(this, "Paquete #" + codigo + " eliminado correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                    limpiarPaqueteForm();
                    cargarPaquetes();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar paquete: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarPaqueteForm() {
        txtPaqueteCodigo.setText("(Autogenerado)");
        txtPaqueteDescripcion.setText("");
        txtPaqueteDestinatario.setText("");
        txtPaqueteDireccion.setText("");
        if (cbPaqueteCamionero.getItemCount() > 0) cbPaqueteCamionero.setSelectedIndex(0);
        tablaPaquetes.clearSelection();

        btnPaqueteGuardar.setEnabled(true);
        btnPaqueteActualizar.setEnabled(false);
        btnPaqueteEliminar.setEnabled(false);
    }

    private void cargarPaquetes() {
        modeloTablaPaquetes.setRowCount(0);
        try {
            List<Paquete> lista = paqueteDAO.listar();
            for (Paquete p : lista) {
                modeloTablaPaquetes.addRow(new Object[]{
                        p.getCodigo(), p.getDescripcion(), p.getDestinatario(),
                        p.getDireccionDestinatario(), p.getDniCamionero(), p.getNombreCamionero()
                });
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar paquetes: " + ex.getMessage());
        }
    }

    // =========================================================================
    // 4. PANEL ASIGNACIONES (CAMIONERO - CAMION)
    // =========================================================================
    private JPanel crearPanelAsignacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos de Asignacion", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(30, 50, 90)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtAsignacionId = new JTextField(15);
        txtAsignacionId.setEditable(false);
        txtAsignacionId.setText("(Autogenerado)");
        cbAsignacionCamionero = new JComboBox<>();
        cbAsignacionCamion = new JComboBox<>();
        txtAsignacionFecha = new JTextField(LocalDate.now().toString(), 15);

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("ID Asignacion:"), g);
        g.gridx = 1; g.gridy = 0; form.add(txtAsignacionId, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Camionero:"), g);
        g.gridx = 1; g.gridy = 1; form.add(cbAsignacionCamionero, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Camion:"), g);
        g.gridx = 1; g.gridy = 2; form.add(cbAsignacionCamion, g);

        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Fecha (AAAA-MM-DD):"), g);
        g.gridx = 1; g.gridy = 3; form.add(txtAsignacionFecha, g);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnAsignacionGuardar = new JButton("Guardar");
        btnAsignacionActualizar = new JButton("Actualizar");
        btnAsignacionEliminar = new JButton("Eliminar");
        btnAsignacionLimpiar = new JButton("Limpiar");

        btnAsignacionActualizar.setEnabled(false);
        btnAsignacionEliminar.setEnabled(false);

        pnlBotones.add(btnAsignacionGuardar);
        pnlBotones.add(btnAsignacionActualizar);
        pnlBotones.add(btnAsignacionEliminar);
        pnlBotones.add(btnAsignacionLimpiar);

        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        form.add(pnlBotones, g);

        modeloTablaAsignaciones = new DefaultTableModel(new String[]{"ID", "DNI Camionero", "Camionero", "Matricula", "Modelo Camion", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaAsignaciones = new JTable(modeloTablaAsignaciones);
        tablaAsignaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAsignaciones.getTableHeader().setReorderingAllowed(false);

        tablaAsignaciones.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaAsignaciones.getSelectedRow();
            if (fila >= 0) {
                txtAsignacionId.setText(modeloTablaAsignaciones.getValueAt(fila, 0).toString());
                String dniCamionero = modeloTablaAsignaciones.getValueAt(fila, 1).toString();
                String matriculaCamion = modeloTablaAsignaciones.getValueAt(fila, 3).toString();
                String fechaStr = modeloTablaAsignaciones.getValueAt(fila, 5).toString();

                seleccionarCamioneroEnCombo(cbAsignacionCamionero, dniCamionero);
                seleccionarCamionEnCombo(cbAsignacionCamion, matriculaCamion);
                txtAsignacionFecha.setText(fechaStr);

                btnAsignacionGuardar.setEnabled(false);
                btnAsignacionActualizar.setEnabled(true);
                btnAsignacionEliminar.setEnabled(true);
            }
        });

        btnAsignacionGuardar.addActionListener(e -> guardarAsignacion());
        btnAsignacionActualizar.addActionListener(e -> actualizarAsignacion());
        btnAsignacionEliminar.addActionListener(e -> eliminarAsignacion());
        btnAsignacionLimpiar.addActionListener(e -> limpiarAsignacionForm());

        panel.add(form, BorderLayout.WEST);
        panel.add(new JScrollPane(tablaAsignaciones), BorderLayout.CENTER);
        return panel;
    }

    private void guardarAsignacion() {
        Camionero camioneroSel = (Camionero) cbAsignacionCamionero.getSelectedItem();
        Camion camionSel = (Camion) cbAsignacionCamion.getSelectedItem();
        String fechaStr = txtAsignacionFecha.getText().trim();

        if (camioneroSel == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un camionero.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (camionSel == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un camion.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha invalido. Use AAAA-MM-DD.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtAsignacionFecha.requestFocus();
            return;
        }

        try {
            if (camioneroCamionDAO.existeAsignacion(camioneroSel.getDni(), camionSel.getMatricula(), fecha, 0)) {
                JOptionPane.showMessageDialog(this, "Ya existe una asignacion para este camionero y camion en la fecha " + fecha + ".", "Registro existente", JOptionPane.WARNING_MESSAGE);
                return;
            }
            CamioneroCamion cc = new CamioneroCamion(0, camioneroSel.getDni(), camionSel.getMatricula(), fecha);
            if (camioneroCamionDAO.insertar(cc)) {
                JOptionPane.showMessageDialog(this, "Asignacion registrada con ID #" + cc.getId(), "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarAsignacionForm();
                cargarAsignaciones();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar asignacion: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarAsignacion() {
        int id = Integer.parseInt(txtAsignacionId.getText().trim());
        Camionero camioneroSel = (Camionero) cbAsignacionCamionero.getSelectedItem();
        Camion camionSel = (Camion) cbAsignacionCamion.getSelectedItem();
        String fechaStr = txtAsignacionFecha.getText().trim();

        if (camioneroSel == null || camionSel == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar camionero y camion.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha invalido. Use AAAA-MM-DD.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (camioneroCamionDAO.existeAsignacion(camioneroSel.getDni(), camionSel.getMatricula(), fecha, id)) {
                JOptionPane.showMessageDialog(this, "Ya existe otra asignacion para ese camionero, camion y fecha.", "Registro existente", JOptionPane.WARNING_MESSAGE);
                return;
            }
            CamioneroCamion cc = new CamioneroCamion(id, camioneroSel.getDni(), camionSel.getMatricula(), fecha);
            if (camioneroCamionDAO.actualizar(cc)) {
                JOptionPane.showMessageDialog(this, "Asignacion #" + id + " actualizada correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarAsignacionForm();
                cargarAsignaciones();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar asignacion: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAsignacion() {
        int id = Integer.parseInt(txtAsignacionId.getText().trim());
        int opt = JOptionPane.showConfirmDialog(this, "Desea eliminar la asignacion #" + id + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                if (camioneroCamionDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Asignacion #" + id + " eliminada correctamente.", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                    limpiarAsignacionForm();
                    cargarAsignaciones();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar asignacion: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarAsignacionForm() {
        txtAsignacionId.setText("(Autogenerado)");
        if (cbAsignacionCamionero.getItemCount() > 0) cbAsignacionCamionero.setSelectedIndex(0);
        if (cbAsignacionCamion.getItemCount() > 0) cbAsignacionCamion.setSelectedIndex(0);
        txtAsignacionFecha.setText(LocalDate.now().toString());
        tablaAsignaciones.clearSelection();

        btnAsignacionGuardar.setEnabled(true);
        btnAsignacionActualizar.setEnabled(false);
        btnAsignacionEliminar.setEnabled(false);
    }

    private void cargarAsignaciones() {
        modeloTablaAsignaciones.setRowCount(0);
        try {
            List<CamioneroCamion> lista = camioneroCamionDAO.listar();
            for (CamioneroCamion cc : lista) {
                modeloTablaAsignaciones.addRow(new Object[]{
                        cc.getId(), cc.getDniCamionero(), cc.getNombreCamionero(),
                        cc.getMatriculaCamion(), cc.getModeloCamion(), cc.getFecha()
                });
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar asignaciones: " + ex.getMessage());
        }
    }

    // =========================================================================
    // METODOS AUXILIARES
    // =========================================================================
    private void cargarTodosLosDatos() {
        cargarCamiones();
        cargarCamioneros();
        cargarPaquetes();
        cargarAsignaciones();
        refrescarCombosRelacionales();
    }

    private void refrescarCombosRelacionales() {
        try {
            List<Camionero> camioneros = camioneroDAO.listar();
            cbPaqueteCamionero.removeAllItems();
            cbAsignacionCamionero.removeAllItems();
            for (Camionero c : camioneros) {
                cbPaqueteCamionero.addItem(c);
                cbAsignacionCamionero.addItem(c);
            }

            List<Camion> camiones = camionDAO.listar();
            cbAsignacionCamion.removeAllItems();
            for (Camion c : camiones) {
                cbAsignacionCamion.addItem(c);
            }
        } catch (SQLException ex) {
            System.err.println("Error al actualizar listas: " + ex.getMessage());
        }
    }

    private void seleccionarCamioneroEnCombo(JComboBox<Camionero> combo, String dni) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Camionero item = combo.getItemAt(i);
            if (item != null && item.getDni().equalsIgnoreCase(dni)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarCamionEnCombo(JComboBox<Camion> combo, String matricula) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Camion item = combo.getItemAt(i);
            if (item != null && item.getMatricula().equalsIgnoreCase(matricula)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void probarConexionBD() {
        try (Connection con = Conexion.getConexion()) {
            if (con != null && !con.isClosed()) {
                JOptionPane.showMessageDialog(this,
                        "Conexion exitosa a MySQL.\nBase de datos: " + Conexion.getBaseDatos() + "\nUsuario: " + Conexion.getUsuario(),
                        "Conexion", JOptionPane.INFORMATION_MESSAGE);
                cargarTodosLosDatos();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar a la base de datos MySQL.\nVerifica que MySQL este iniciado y que la base de datos 'db_paqueteria' exista.\n\nError: " + ex.getMessage(),
                    "Error de conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoConfiguracionBD() {
        JTextField txtHost = new JTextField(Conexion.getHost(), 12);
        JTextField txtPort = new JTextField(Conexion.getPuerto(), 6);
        JTextField txtBD = new JTextField(Conexion.getBaseDatos(), 12);
        JTextField txtUser = new JTextField(Conexion.getUsuario(), 10);
        JPasswordField txtPass = new JPasswordField(Conexion.getClave(), 10);

        JPanel panel = new JPanel(new GridLayout(5, 2, 6, 6));
        panel.add(new JLabel("Host:")); panel.add(txtHost);
        panel.add(new JLabel("Puerto:")); panel.add(txtPort);
        panel.add(new JLabel("Base de Datos:")); panel.add(txtBD);
        panel.add(new JLabel("Usuario MySQL:")); panel.add(txtUser);
        panel.add(new JLabel("Contrasena:")); panel.add(txtPass);

        int res = JOptionPane.showConfirmDialog(this, panel, "Parametros de Conexion MySQL", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            Conexion.configurar(
                    txtHost.getText().trim(),
                    txtPort.getText().trim(),
                    txtBD.getText().trim(),
                    txtUser.getText().trim(),
                    new String(txtPass.getPassword())
            );
            probarConexionBD();
        }
    }
}
