import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaClientes extends JPanel {
  private JTextField txtNombre;
  private JTextField txtCedula;
  private JTextField txtDireccion;
  private JTextField txtCelular;
  private JTextField txtAnioAlta;
  private JTable tablaClientes;
  private DefaultTableModel modelo;
  private JLabel lblEstado;
  private DatosSistema datos;
  private boolean modoOscuro;

  public VentanaClientes(boolean modoOscuro) {
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
    panelFormulario.add(new JLabel("Celular:"), gbc);
    gbc.gridx = 1;
    txtCelular = new JTextField(20);
    panelFormulario.add(txtCelular, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    panelFormulario.add(new JLabel("Año Alta:"), gbc);
    gbc.gridx = 1;
    txtAnioAlta = new JTextField(20);
    panelFormulario.add(txtAnioAlta, gbc);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton btnAgregar = new JButton("Agregar");
    JButton btnEliminar = new JButton("Eliminar");
    JButton btnLimpiar = new JButton("Limpiar");

    btnAgregar.addActionListener(e -> agregarCliente());
    btnEliminar.addActionListener(e -> eliminarCliente());
    btnLimpiar.addActionListener(e -> limpiarCampos());

    panelBotones.add(btnAgregar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnLimpiar);

    // Panel de estado
    lblEstado = new JLabel(" ");
    lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

    // Tabla de clientes
    String[] columnas = { "Nombre", "Cédula", "Dirección", "Celular", "Año Alta" };
    modelo = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tablaClientes = new JTable(modelo);
    JScrollPane scrollPane = new JScrollPane(tablaClientes);

    // Listener para selección en la tabla
    tablaClientes.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        int fila = tablaClientes.getSelectedRow();
        if (fila != -1) {
          cargarClienteSeleccionado(fila);
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
    tablaClientes.setBackground(fondo);
    tablaClientes.setForeground(texto);
    tablaClientes.getTableHeader().setBackground(fondo);
    tablaClientes.getTableHeader().setForeground(texto);
    lblEstado.setForeground(texto);
  }

  private void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<ClienteMensual> clientes = datos.getClientes();
    for (ClienteMensual cliente : clientes) {
      Object[] fila = {
          cliente.getNombre(),
          cliente.getCedula(),
          cliente.getDireccion(),
          cliente.getCelular(),
          cliente.getAnioAlta()
      };
      modelo.addRow(fila);
    }
  }

  private void agregarCliente() {
    try {
      String nombre = txtNombre.getText().trim();
      String cedula = txtCedula.getText().trim();
      String direccion = txtDireccion.getText().trim();
      String celular = txtCelular.getText().trim();
      int anioAlta = Integer.parseInt(txtAnioAlta.getText().trim());

      if (nombre.isEmpty() || cedula.isEmpty() || direccion.isEmpty() || celular.isEmpty()) {
        mostrarError("Todos los campos son obligatorios");
        return;
      }

      if (datos.buscarCliente(cedula) != null) {
        mostrarError("Ya existe un cliente con esa cédula");
        return;
      }

      ClienteMensual nuevoCliente = new ClienteMensual(nombre, cedula, direccion, celular, anioAlta);
      datos.agregarCliente(nuevoCliente);
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Cliente agregado exitosamente");
    } catch (NumberFormatException e) {
      mostrarError("El año de alta debe ser un número válido");
    }
  }

  private void eliminarCliente() {
    int fila = tablaClientes.getSelectedRow();
    if (fila == -1) {
      mostrarError("Debe seleccionar un cliente para eliminar");
      return;
    }

    String cedula = (String) tablaClientes.getValueAt(fila, 1);
    ClienteMensual cliente = datos.buscarCliente(cedula);
    if (cliente != null) {
      datos.getClientes().remove(cliente);
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Cliente eliminado exitosamente");
    }
  }

  private void limpiarCampos() {
    txtNombre.setText("");
    txtCedula.setText("");
    txtDireccion.setText("");
    txtCelular.setText("");
    txtAnioAlta.setText("");
    tablaClientes.clearSelection();
    lblEstado.setText(" ");
  }

  private void cargarClienteSeleccionado(int fila) {
    txtNombre.setText((String) tablaClientes.getValueAt(fila, 0));
    txtCedula.setText((String) tablaClientes.getValueAt(fila, 1));
    txtDireccion.setText((String) tablaClientes.getValueAt(fila, 2));
    txtCelular.setText((String) tablaClientes.getValueAt(fila, 3));
    txtAnioAlta.setText(tablaClientes.getValueAt(fila, 4).toString());
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
