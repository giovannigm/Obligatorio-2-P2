import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
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
  private JLabel lblMatricula;
  private JLabel lblMarca;
  private JLabel lblModelo;
  private JLabel lblEstadoVehiculo;
  private DatosSistema datos;
  private Vehiculo vehiculoSeleccionado;

  public VentanaVehiculos(boolean modoOscuro) {
    this.datos = DatosSistema.getInstancia();
    initComponents(modoOscuro);
    actualizarTabla();
  }

  private void initComponents(boolean modoOscuro) {
    setLayout(new BorderLayout());

    // Panel de formulario
    JPanel panelFormulario = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Campos del formulario
    gbc.gridx = 0;
    gbc.gridy = 0;
    lblMatricula = new JLabel("Matrícula:");
    panelFormulario.add(lblMatricula, gbc);
    gbc.gridx = 1;
    txtMatricula = new JTextField(20);
    // Limitar a 7 caracteres
    ((AbstractDocument) txtMatricula.getDocument()).setDocumentFilter(new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
          throws BadLocationException {
        if (fb.getDocument().getLength() + string.length() <= 7) {
          super.insertString(fb, offset, string, attr);
        }
      }

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        if (fb.getDocument().getLength() - length + (text != null ? text.length() : 0) <= 7) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    });
    panelFormulario.add(txtMatricula, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    lblMarca = new JLabel("Marca:");
    panelFormulario.add(lblMarca, gbc);
    gbc.gridx = 1;
    txtMarca = new JTextField(20);
    panelFormulario.add(txtMarca, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    lblModelo = new JLabel("Modelo:");
    panelFormulario.add(lblModelo, gbc);
    gbc.gridx = 1;
    txtModelo = new JTextField(20);
    panelFormulario.add(txtModelo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    lblEstadoVehiculo = new JLabel("Estado:");
    panelFormulario.add(lblEstadoVehiculo, gbc);
    gbc.gridx = 1;
    String[] estados = { "Bueno", "Taller", "Averiado", "En servicio" };
    cmbEstado = new JComboBox<>(estados);
    panelFormulario.add(cmbEstado, gbc);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton btnAgregar = new JButton("Agregar");
    JButton btnEliminar = new JButton("Eliminar");
    JButton btnLimpiar = new JButton("Limpiar");

    btnAgregar.addActionListener(e -> agregarVehiculo());
    btnEliminar.addActionListener(e -> eliminarVehiculo());
    btnLimpiar.addActionListener(e -> limpiarCampos());

    btnEliminar.setEnabled(false); // Inicialmente deshabilitado

    panelBotones.add(btnAgregar);
    panelBotones.add(btnEliminar);
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
    tablaVehiculos.setOpaque(false);
    tablaVehiculos.getTableHeader().setOpaque(false);
    JScrollPane scrollPane = new JScrollPane(tablaVehiculos);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

    // Listener para selección en la tabla
    tablaVehiculos.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila != -1) {
          cargarVehiculoSeleccionado(fila);
          btnEliminar.setEnabled(true);
        } else {
          btnEliminar.setEnabled(false);
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

    aplicarEstilos(modoOscuro);
  }

  private void aplicarEstilos(boolean modoOscuro) {
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
    // Seteamos explícitamente el color de los labels del formulario
    lblMatricula.setForeground(texto);
    lblMarca.setForeground(texto);
    lblModelo.setForeground(texto);
    lblEstadoVehiculo.setForeground(texto);

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

  // Elimina el vehículo seleccionado de la tabla y la lista de vehículos.
  // Si no hay selección, muestra un error. Si hay, pide confirmación y elimina.
  private void eliminarVehiculo() {
    if (vehiculoSeleccionado == null) {
      mostrarError("Debe seleccionar un vehículo para eliminar");
      return;
    }
    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el vehículo?",
        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      datos.eliminarVehiculo(vehiculoSeleccionado.getMatricula());
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Vehículo eliminado exitosamente");
    }
  }

  // Limpia todos los campos del formulario y el estado.
  private void limpiarCampos() {
    txtMatricula.setText("");
    txtMarca.setText("");
    txtModelo.setText("");
    cmbEstado.setSelectedIndex(0);
    tablaVehiculos.clearSelection();
    vehiculoSeleccionado = null;
    lblEstado.setText(" ");
  }

  // Carga los datos del vehículo seleccionado en los campos del formulario.
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
    aplicarEstilos(modoOscuro);
  }
}
