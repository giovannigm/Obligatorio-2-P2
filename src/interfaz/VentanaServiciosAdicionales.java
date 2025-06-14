import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VentanaServiciosAdicionales extends JPanel implements ModoOscuroObserver {
  private JComboBox<String> cmbTipoServicio;
  private JTextField txtFecha;
  private JTextField txtHora;
  private JComboBox<Vehiculo> cmbVehiculos;
  private JComboBox<Empleado> cmbEmpleados;
  private JTextField txtCosto;
  private JTable tablaServicios;
  private DefaultTableModel modelo;
  private JLabel lblEstado;
  private ControladorSistema controlador;

  // Labels para estilos
  private JLabel lblTipoServicio;
  private JLabel lblFecha;
  private JLabel lblHora;
  private JLabel lblVehiculo;
  private JLabel lblEmpleado;
  private JLabel lblCosto;

  public VentanaServiciosAdicionales(boolean modoOscuro, ControladorSistema controlador) {
    this.controlador = controlador;
    initComponents();
    actualizarTabla();
    actualizarCombos();
    Estilos.aplicarEstilos(this, modoOscuro);
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    // Filtro para fecha (dd/MM/yyyy)
    DocumentFilter fechaFilter = new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
          throws BadLocationException {
        String nuevoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (nuevoTexto.length() <= 10 && string.matches("[0-9/]*")) {
          super.insertString(fb, offset, string, attr);
        }
      }

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder sb = new StringBuilder(actual);
        sb.replace(offset, offset + length, text);
        if (sb.length() <= 10 && text.matches("[0-9/]*")) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    };

    // Filtro para hora (HH:mm)
    DocumentFilter horaFilter = new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
          throws BadLocationException {
        String nuevoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (nuevoTexto.length() <= 5 && string.matches("[0-9:]*")) {
          super.insertString(fb, offset, string, attr);
        }
      }

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder sb = new StringBuilder(actual);
        sb.replace(offset, offset + length, text);
        if (sb.length() <= 5 && text.matches("[0-9:]*")) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    };

    // Filtro para costo (números y punto decimal)
    DocumentFilter costoFilter = new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
          throws BadLocationException {
        String nuevoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (nuevoTexto.matches("\\d*\\.?\\d*")) {
          super.insertString(fb, offset, string, attr);
        }
      }

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder sb = new StringBuilder(actual);
        sb.replace(offset, offset + length, text);
        if (sb.toString().matches("\\d*\\.?\\d*")) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    };

    // Panel de formulario
    JPanel panelFormulario = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Tipo de servicio
    gbc.gridx = 0;
    gbc.gridy = 0;
    lblTipoServicio = new JLabel("Tipo de Servicio:");
    panelFormulario.add(lblTipoServicio, gbc);
    gbc.gridx = 1;
    String[] tiposServicio = { "lavado", "cambio de rueda", "limpieza de tapizado", "cambio de luces", "otro" };
    cmbTipoServicio = new JComboBox<>(tiposServicio);
    panelFormulario.add(cmbTipoServicio, gbc);

    // Fecha
    gbc.gridx = 0;
    gbc.gridy = 1;
    lblFecha = new JLabel("Fecha (dd/MM/yyyy):");
    panelFormulario.add(lblFecha, gbc);
    gbc.gridx = 1;
    txtFecha = new JTextField(15);
    ((AbstractDocument) txtFecha.getDocument()).setDocumentFilter(fechaFilter);
    panelFormulario.add(txtFecha, gbc);

    // Hora
    gbc.gridx = 0;
    gbc.gridy = 2;
    lblHora = new JLabel("Hora (HH:mm):");
    panelFormulario.add(lblHora, gbc);
    gbc.gridx = 1;
    txtHora = new JTextField(15);
    ((AbstractDocument) txtHora.getDocument()).setDocumentFilter(horaFilter);
    panelFormulario.add(txtHora, gbc);

    // Vehículo
    gbc.gridx = 0;
    gbc.gridy = 3;
    lblVehiculo = new JLabel("Vehículo:");
    panelFormulario.add(lblVehiculo, gbc);
    gbc.gridx = 1;
    cmbVehiculos = new JComboBox<>();
    panelFormulario.add(cmbVehiculos, gbc);

    // Empleado
    gbc.gridx = 0;
    gbc.gridy = 4;
    lblEmpleado = new JLabel("Empleado:");
    panelFormulario.add(lblEmpleado, gbc);
    gbc.gridx = 1;
    cmbEmpleados = new JComboBox<>();
    panelFormulario.add(cmbEmpleados, gbc);

    // Costo
    gbc.gridx = 0;
    gbc.gridy = 5;
    lblCosto = new JLabel("Costo:");
    panelFormulario.add(lblCosto, gbc);
    gbc.gridx = 1;
    txtCosto = new JTextField(15);
    ((AbstractDocument) txtCosto.getDocument()).setDocumentFilter(costoFilter);
    panelFormulario.add(txtCosto, gbc);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton btnAgregar = new JButton("Agregar Servicio");
    JButton btnLimpiar = new JButton("Limpiar");

    btnAgregar.addActionListener(e -> agregarServicio());
    btnLimpiar.addActionListener(e -> limpiarCampos());

    panelBotones.add(btnAgregar);
    panelBotones.add(btnLimpiar);

    // Panel de estado
    lblEstado = new JLabel(" ");
    lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

    // Tabla de servicios
    String[] columnas = { "Tipo", "Fecha", "Hora", "Vehículo", "Empleado", "Costo" };
    modelo = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tablaServicios = new JTable(modelo);
    tablaServicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaServicios.setOpaque(false);
    tablaServicios.getTableHeader().setOpaque(false);
    JScrollPane scrollPane = new JScrollPane(tablaServicios);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    // Listener para selección en la tabla
    tablaServicios.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        int fila = tablaServicios.getSelectedRow();
        if (fila != -1) {
          cargarServicioSeleccionado(fila);
        }
      }
    });

    // Agregar componentes al panel principal
    JPanel panelSuperior = new JPanel(new BorderLayout());
    panelSuperior.add(panelFormulario, BorderLayout.CENTER);
    panelSuperior.add(panelBotones, BorderLayout.SOUTH);

    add(panelSuperior, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(lblEstado, BorderLayout.SOUTH);
  }

  private void actualizarCombos() {
    cmbVehiculos.removeAllItems();
    cmbEmpleados.removeAllItems();

    for (Vehiculo vehiculo : controlador.getVehiculos()) {
      cmbVehiculos.addItem(vehiculo);
    }
    for (Empleado empleado : controlador.getEmpleados()) {
      cmbEmpleados.addItem(empleado);
    }
  }

  private void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<ServicioAdicional> servicios = controlador.getServiciosAdicionales();
    DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

    for (ServicioAdicional servicio : servicios) {
      Object[] fila = {
          servicio.getTipo(),
          servicio.getFecha().format(formatterFecha),
          servicio.getHora().format(formatterHora),
          servicio.getVehiculo().getMatricula(),
          servicio.getEmpleado().getNombre(),
          String.format("%.2f", servicio.getCosto())
      };
      modelo.addRow(fila);
    }
  }

  private void agregarServicio() {
    try {
      // Capturar datos del formulario
      String tipo = (String) cmbTipoServicio.getSelectedItem();
      String fecha = txtFecha.getText().trim();
      String hora = txtHora.getText().trim();
      Vehiculo vehiculo = (Vehiculo) cmbVehiculos.getSelectedItem();
      Empleado empleado = (Empleado) cmbEmpleados.getSelectedItem();
      String costo = txtCosto.getText().trim();

      // Delegar la lógica al controlador
      controlador.agregarServicioAdicional(tipo, fecha, hora, vehiculo, empleado, costo);

      // Actualizar interfaz
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Servicio adicional agregado exitosamente");
    } catch (IllegalArgumentException e) {
      mostrarError(e.getMessage());
    }
  }

  private void limpiarCampos() {
    cmbTipoServicio.setSelectedIndex(0);
    txtFecha.setText("");
    txtHora.setText("");
    cmbVehiculos.setSelectedIndex(-1);
    cmbEmpleados.setSelectedIndex(-1);
    txtCosto.setText("");
    tablaServicios.clearSelection();
    lblEstado.setText(" ");
  }

  private void cargarServicioSeleccionado(int fila) {
    // Solo para visualización, no edición
    String tipo = (String) tablaServicios.getValueAt(fila, 0);
    String fecha = (String) tablaServicios.getValueAt(fila, 1);
    String hora = (String) tablaServicios.getValueAt(fila, 2);
    String matricula = (String) tablaServicios.getValueAt(fila, 3);
    String nombreEmpleado = (String) tablaServicios.getValueAt(fila, 4);
    String costo = (String) tablaServicios.getValueAt(fila, 5);

    // Cargar datos en los campos (solo lectura)
    cmbTipoServicio.setSelectedItem(tipo);
    txtFecha.setText(fecha);
    txtHora.setText(hora);
    txtCosto.setText(costo);

    // Seleccionar vehículo y empleado correspondientes
    for (int i = 0; i < cmbVehiculos.getItemCount(); i++) {
      Vehiculo v = cmbVehiculos.getItemAt(i);
      if (v.getMatricula().equals(matricula)) {
        cmbVehiculos.setSelectedIndex(i);
        break;
      }
    }

    for (int i = 0; i < cmbEmpleados.getItemCount(); i++) {
      Empleado e = cmbEmpleados.getItemAt(i);
      if (e.getNombre().equals(nombreEmpleado)) {
        cmbEmpleados.setSelectedIndex(i);
        break;
      }
    }
  }

  private void mostrarError(String mensaje) {
    lblEstado.setText("Error: " + mensaje);
    lblEstado.setForeground(Color.RED);
  }

  private void mostrarExito(String mensaje) {
    lblEstado.setText(mensaje);
    lblEstado.setForeground(Color.GREEN);
  }

  public void setModoOscuro(boolean modoOscuro) {
    Estilos.aplicarEstilos(this, modoOscuro);
  }

  // Método público para refrescar datos
  public void refrescarDatos() {
    actualizarCombos();
    actualizarTabla();
  }

  @Override
  public void actualizarModoOscuro(boolean modoOscuro) {
    setModoOscuro(modoOscuro);
  }
}
