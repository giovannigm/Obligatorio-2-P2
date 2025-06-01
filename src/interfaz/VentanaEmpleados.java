import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

  public VentanaEmpleados(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    this.datos = DatosSistema.getInstancia();
    initComponents();
    actualizarTabla();
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    // Panel de formulario
    JPanel panelFormulario = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Campos del formulario
    gbc.gridx = 0;
    gbc.gridy = 0;
    panelFormulario.add(new JLabel("Nombre:"), gbc);
    gbc.gridx = 1;
    txtNombre = new JTextField(20);
    panelFormulario.add(txtNombre, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panelFormulario.add(new JLabel("Cédula:"), gbc);
    gbc.gridx = 1;
    txtCedula = new JTextField(20);
    panelFormulario.add(txtCedula, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panelFormulario.add(new JLabel("Dirección:"), gbc);
    gbc.gridx = 1;
    txtDireccion = new JTextField(20);
    panelFormulario.add(txtDireccion, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    panelFormulario.add(new JLabel("Número Empleado:"), gbc);
    gbc.gridx = 1;
    txtNumeroEmpleado = new JTextField(20);
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
    String[] columnas = { "Nombre", "Cédula", "Dirección", "Número Empleado" };
    modelo = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tablaEmpleados = new JTable(modelo);
    JScrollPane scrollPane = new JScrollPane(tablaEmpleados);

    // Listener para selección en la tabla
    tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        int fila = tablaEmpleados.getSelectedRow();
        if (fila != -1) {
          cargarEmpleadoSeleccionado(fila);
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

    aplicarEstilos();
  }

  private void aplicarEstilos() {
    Color fondo = modoOscuro ? Color.DARK_GRAY : Color.WHITE;
    Color texto = modoOscuro ? Color.WHITE : Color.BLACK;

    setBackground(fondo);
    for (Component c : getComponents()) {
      if (c instanceof JPanel) {
        c.setBackground(fondo);
        for (Component child : ((JPanel) c).getComponents()) {
          child.setBackground(fondo);
          if (child instanceof JLabel) {
            child.setForeground(texto);
          }
        }
      }
    }
    tablaEmpleados.setBackground(fondo);
    tablaEmpleados.setForeground(texto);
    tablaEmpleados.getTableHeader().setBackground(fondo);
    tablaEmpleados.getTableHeader().setForeground(texto);
    lblEstado.setForeground(texto);
  }

  private void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<Empleado> empleados = datos.getEmpleados();
    for (Empleado empleado : empleados) {
      Object[] fila = {
          empleado.getNombre(),
          empleado.getCedula(),
          empleado.getDireccion(),
          empleado.getNumeroEmpleado()
      };
      modelo.addRow(fila);
    }
  }

  private void agregarEmpleado() {
    try {
      String nombre = txtNombre.getText().trim();
      String cedula = txtCedula.getText().trim();
      String direccion = txtDireccion.getText().trim();
      int numeroEmpleado = Integer.parseInt(txtNumeroEmpleado.getText().trim());

      if (nombre.isEmpty() || cedula.isEmpty() || direccion.isEmpty()) {
        mostrarError("Todos los campos son obligatorios");
        return;
      }

      if (datos.buscarEmpleado(cedula) != null) {
        mostrarError("Ya existe un empleado con esa cédula");
        return;
      }

      Empleado nuevoEmpleado = new Empleado(nombre, cedula, direccion, numeroEmpleado);
      datos.agregarEmpleado(nuevoEmpleado);
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Empleado agregado exitosamente");
    } catch (NumberFormatException e) {
      mostrarError("El número de empleado debe ser un número válido");
    }
  }

  private void eliminarEmpleado() {
    int fila = tablaEmpleados.getSelectedRow();
    if (fila == -1) {
      mostrarError("Debe seleccionar un empleado para eliminar");
      return;
    }

    String cedula = (String) tablaEmpleados.getValueAt(fila, 1);
    Empleado empleado = datos.buscarEmpleado(cedula);
    if (empleado != null) {
      datos.getEmpleados().remove(empleado);
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Empleado eliminado exitosamente");
    }
  }

  private void limpiarCampos() {
    txtNombre.setText("");
    txtCedula.setText("");
    txtDireccion.setText("");
    txtNumeroEmpleado.setText("");
    tablaEmpleados.clearSelection();
    lblEstado.setText(" ");
  }

  private void cargarEmpleadoSeleccionado(int fila) {
    txtNombre.setText((String) tablaEmpleados.getValueAt(fila, 0));
    txtCedula.setText((String) tablaEmpleados.getValueAt(fila, 1));
    txtDireccion.setText((String) tablaEmpleados.getValueAt(fila, 2));
    txtNumeroEmpleado.setText(tablaEmpleados.getValueAt(fila, 3).toString());
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
    this.modoOscuro = modoOscuro;
    aplicarEstilos();
  }
}
