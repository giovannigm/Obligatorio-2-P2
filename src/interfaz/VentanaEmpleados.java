import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaEmpleados extends JPanel {
  private JTextField txtNombre;
  private JTextField txtCedula;
  private JTextField txtDireccion;
  private JTextField txtNumeroEmpleado;
  private JTable tablaEmpleados;
  private DefaultTableModel modelo;
  private JLabel lblEstado;
  private DatosSistema datos;
  private boolean modoOscuro;
  private JLabel lblNombre;
  private JLabel lblCedula;
  private JLabel lblDireccion;
  private JLabel lblNumeroEmpleado;

  public VentanaEmpleados(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    this.datos = DatosSistema.getInstancia();
    initComponents();
    aplicarEstilos();
    actualizarTabla();
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    // Filtro para solo 9 números (Cédula)
    DocumentFilter soloNueveNumerosFilter = new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if ((fb.getDocument().getLength() + string.length() <= 9) && string.matches("\\d*")) {
          super.insertString(fb, offset, string, attr);
        }
      }
      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if ((fb.getDocument().getLength() - length + (text != null ? text.length() : 0) <= 9) && (text == null || text.matches("\\d*"))) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    };

    // Filtro para solo números (Número de empleado)
    DocumentFilter soloNumerosFilter = new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string.matches("\\d*")) {
          super.insertString(fb, offset, string, attr);
        }
      }
      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null || text.matches("\\d*")) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    };

    // Panel de formulario
    JPanel panelFormulario = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    lblNombre = new JLabel("Nombre:");
    panelFormulario.add(lblNombre, gbc);
    gbc.gridx = 1;
    txtNombre = new JTextField(20);
    panelFormulario.add(txtNombre, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    lblCedula = new JLabel("Cédula:");
    panelFormulario.add(lblCedula, gbc);
    gbc.gridx = 1;
    txtCedula = new JTextField(20);
    ((AbstractDocument) txtCedula.getDocument()).setDocumentFilter(soloNueveNumerosFilter);
    panelFormulario.add(txtCedula, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    lblDireccion = new JLabel("Dirección:");
    panelFormulario.add(lblDireccion, gbc);
    gbc.gridx = 1;
    txtDireccion = new JTextField(20);
    panelFormulario.add(txtDireccion, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    lblNumeroEmpleado = new JLabel("Número Empleado:");
    panelFormulario.add(lblNumeroEmpleado, gbc);
    gbc.gridx = 1;
    txtNumeroEmpleado = new JTextField(20);
    ((AbstractDocument) txtNumeroEmpleado.getDocument()).setDocumentFilter(soloNumerosFilter);
    panelFormulario.add(txtNumeroEmpleado, gbc);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton btnAgregar = new JButton("Agregar");
    JButton btnEliminar = new JButton("Eliminar");
    JButton btnLimpiar = new JButton("Limpiar");
    btnAgregar.addActionListener(e -> agregarEmpleado());
    btnEliminar.addActionListener(e -> eliminarEmpleado());
    btnLimpiar.addActionListener(e -> limpiarCampos());
    panelBotones.add(btnAgregar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnLimpiar);

    // Panel de estado
    lblEstado = new JLabel(" ");
    lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

    // Tabla de empleados
    modelo = new DefaultTableModel(new Object[]{"Nombre", "Cédula", "Dirección", "Nro. Empleado"}, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tablaEmpleados = new JTable(modelo);
    tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting() && tablaEmpleados.getSelectedRow() != -1) {
        cargarEmpleadoSeleccionado(tablaEmpleados.getSelectedRow());
      }
    });
    JScrollPane scrollTabla = new JScrollPane(tablaEmpleados);

    // Panel superior (formulario + botones)
    JPanel panelSuperior = new JPanel(new BorderLayout());
    panelSuperior.add(panelFormulario, BorderLayout.CENTER);
    panelSuperior.add(panelBotones, BorderLayout.SOUTH);

    add(panelSuperior, BorderLayout.NORTH);
    add(scrollTabla, BorderLayout.CENTER);
    add(lblEstado, BorderLayout.SOUTH);

    aplicarEstilos();
  }

  private void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<Empleado> empleados = datos.getEmpleados();
    for (Empleado emp : empleados) {
      modelo.addRow(new Object[]{emp.getNombre(), emp.getCedula(), emp.getDireccion(), emp.getNumeroEmpleado()});
    }
  }

  private void agregarEmpleado() {
    String nombre = txtNombre.getText().trim();
    String cedula = txtCedula.getText().trim();
    String direccion = txtDireccion.getText().trim();
    String nroEmpStr = txtNumeroEmpleado.getText().trim();
    if (nombre.isEmpty() || cedula.isEmpty() || direccion.isEmpty() || nroEmpStr.isEmpty()) {
      mostrarError("Todos los campos son obligatorios.");
      return;
    }
    // Validar cédula única
    for (Empleado emp : datos.getEmpleados()) {
      if (emp.getCedula().equals(cedula)) {
        mostrarError("Ya existe un empleado con esa cédula.");
        return;
      }
    }
    int nroEmpleado;
    try {
      nroEmpleado = Integer.parseInt(nroEmpStr);
    } catch (NumberFormatException ex) {
      mostrarError("Número de empleado inválido.");
      return;
    }
    Empleado nuevo = new Empleado(nombre, cedula, direccion, nroEmpleado);
    datos.agregarEmpleado(nuevo);
    actualizarTabla();
    limpiarCampos();
    mostrarExito("Empleado agregado correctamente.");
  }

  private void eliminarEmpleado() {
    int fila = tablaEmpleados.getSelectedRow();
    if (fila == -1) {
      mostrarError("Seleccione un empleado para eliminar.");
      return;
    }
    String cedula = (String) modelo.getValueAt(fila, 1);
    ArrayList<Empleado> empleados = datos.getEmpleados();
    empleados.removeIf(emp -> emp.getCedula().equals(cedula));
    actualizarTabla();
    limpiarCampos();
    mostrarExito("Empleado eliminado.");
  }

  private void limpiarCampos() {
    txtNombre.setText("");
    txtCedula.setText("");
    txtDireccion.setText("");
    txtNumeroEmpleado.setText("");
    tablaEmpleados.clearSelection();
  }

  private void cargarEmpleadoSeleccionado(int fila) {
    txtNombre.setText((String) modelo.getValueAt(fila, 0));
    txtCedula.setText((String) modelo.getValueAt(fila, 1));
    txtDireccion.setText((String) modelo.getValueAt(fila, 2));
    txtNumeroEmpleado.setText(modelo.getValueAt(fila, 3).toString());
  }

  private void mostrarError(String mensaje) {
    lblEstado.setForeground(Color.RED);
    lblEstado.setText(mensaje);
  }

  private void mostrarExito(String mensaje) {
    lblEstado.setForeground(new Color(0, 128, 0));
    lblEstado.setText(mensaje);
  }

  private void aplicarEstilos() {
    Color fondo = modoOscuro ? Color.BLACK : Color.WHITE;
    Color texto = modoOscuro ? Color.WHITE : Color.BLACK;
    setBackground(fondo);
    for (Component c : getComponents()) {
      if (c instanceof JPanel) {
        c.setBackground(fondo);
        for (Component child : ((JPanel) c).getComponents()) {
          child.setBackground(fondo);
        }
      }
    }
    if (lblNombre != null) lblNombre.setForeground(texto);
    if (lblCedula != null) lblCedula.setForeground(texto);
    if (lblDireccion != null) lblDireccion.setForeground(texto);
    if (lblNumeroEmpleado != null) lblNumeroEmpleado.setForeground(texto);
    if (tablaEmpleados != null) {
      tablaEmpleados.setBackground(fondo);
      tablaEmpleados.setForeground(texto);
      tablaEmpleados.getTableHeader().setBackground(fondo);
      tablaEmpleados.getTableHeader().setForeground(texto);
    }
    if (lblEstado != null) lblEstado.setForeground(texto);
  }

  public void setModoOscuro(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    aplicarEstilos();
  }
}
