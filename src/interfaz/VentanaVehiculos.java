import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaVehiculos extends JPanel {
  private JTextField txtMatricula;
  private JTextField txtMarca;
  private JTextField txtModelo;
  private JComboBox<String> cmbEstado;
  private JTable tablaVehiculos;
  private DefaultTableModel modelo;
  private JLabel lblEstado;
  private DatosSistema datos;
  private boolean modoOscuro;
  private Vehiculo vehiculoSeleccionado;

  public VentanaVehiculos(boolean modoOscuro) {
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
    panelFormulario.add(new JLabel("Matrícula:"), gbc);
    gbc.gridx = 1;
    txtMatricula = new JTextField(20);
    panelFormulario.add(txtMatricula, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panelFormulario.add(new JLabel("Marca:"), gbc);
    gbc.gridx = 1;
    txtMarca = new JTextField(20);
    panelFormulario.add(txtMarca, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panelFormulario.add(new JLabel("Modelo:"), gbc);
    gbc.gridx = 1;
    txtModelo = new JTextField(20);
    panelFormulario.add(txtModelo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    panelFormulario.add(new JLabel("Estado:"), gbc);
    gbc.gridx = 1;
    String[] estados = { "Bueno", "Taller", "Averiado", "En servicio" };
    cmbEstado = new JComboBox<>(estados);
    panelFormulario.add(cmbEstado, gbc);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton btnAgregar = new JButton("Agregar");
    JButton btnActualizar = new JButton("Actualizar Estado");
    JButton btnLimpiar = new JButton("Limpiar");

    btnAgregar.addActionListener(e -> agregarVehiculo());
    btnActualizar.addActionListener(e -> actualizarEstado());
    btnLimpiar.addActionListener(e -> limpiarCampos());

    btnActualizar.setEnabled(false); // Inicialmente deshabilitado

    panelBotones.add(btnAgregar);
    panelBotones.add(btnActualizar);
    panelBotones.add(btnLimpiar);

    // Panel de estado
    lblEstado = new JLabel(" ");
    lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

    // Tabla de vehículos
    String[] columnas = { "Matrícula", "Marca", "Modelo", "Estado" };
    modelo = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tablaVehiculos = new JTable(modelo);
    JScrollPane scrollPane = new JScrollPane(tablaVehiculos);

    // Listener para selección en la tabla
    tablaVehiculos.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila != -1) {
          cargarVehiculoSeleccionado(fila);
          btnActualizar.setEnabled(true);
        } else {
          btnActualizar.setEnabled(false);
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
    tablaVehiculos.setBackground(fondo);
    tablaVehiculos.setForeground(texto);
    tablaVehiculos.getTableHeader().setBackground(fondo);
    tablaVehiculos.getTableHeader().setForeground(texto);
    lblEstado.setForeground(texto);
    cmbEstado.setBackground(fondo);
    cmbEstado.setForeground(texto);
  }

  private void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<Vehiculo> vehiculos = datos.getVehiculos();
    for (Vehiculo vehiculo : vehiculos) {
      Object[] fila = {
          vehiculo.getMatricula(),
          vehiculo.getMarca(),
          vehiculo.getModelo(),
          vehiculo.getEstado()
      };
      modelo.addRow(fila);
    }
  }

  private void agregarVehiculo() {
    String matricula = txtMatricula.getText().trim();
    String marca = txtMarca.getText().trim();
    String modelo = txtModelo.getText().trim();
    String estado = (String) cmbEstado.getSelectedItem();

    if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
      mostrarError("Todos los campos son obligatorios");
      return;
    }

    if (datos.buscarVehiculo(matricula) != null) {
      mostrarError("Ya existe un vehículo con esa matrícula");
      return;
    }

    Vehiculo nuevoVehiculo = new Vehiculo(matricula, marca, modelo, estado);
    datos.agregarVehiculo(nuevoVehiculo);
    actualizarTabla();
    limpiarCampos();
    mostrarExito("Vehículo agregado exitosamente");
  }

  private void actualizarEstado() {
    if (vehiculoSeleccionado == null) {
      mostrarError("Debe seleccionar un vehículo para actualizar");
      return;
    }

    String nuevoEstado = (String) cmbEstado.getSelectedItem();
    vehiculoSeleccionado.setEstado(nuevoEstado);
    actualizarTabla();
    mostrarExito("Estado actualizado exitosamente");
  }

  private void limpiarCampos() {
    txtMatricula.setText("");
    txtMarca.setText("");
    txtModelo.setText("");
    cmbEstado.setSelectedIndex(0);
    tablaVehiculos.clearSelection();
    vehiculoSeleccionado = null;
    lblEstado.setText(" ");
  }

  private void cargarVehiculoSeleccionado(int fila) {
    String matricula = (String) tablaVehiculos.getValueAt(fila, 0);
    vehiculoSeleccionado = datos.buscarVehiculo(matricula);

    if (vehiculoSeleccionado != null) {
      txtMatricula.setText(vehiculoSeleccionado.getMatricula());
      txtMarca.setText(vehiculoSeleccionado.getMarca());
      txtModelo.setText(vehiculoSeleccionado.getModelo());
      cmbEstado.setSelectedItem(vehiculoSeleccionado.getEstado());
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
    this.modoOscuro = modoOscuro;
    aplicarEstilos();
  }
}
