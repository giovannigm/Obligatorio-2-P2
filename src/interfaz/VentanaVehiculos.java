import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaVehiculos extends JPanel implements ModoOscuroObserver {
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
  private ControladorSistema controlador;
  private boolean modoOscuro;
  private Vehiculo vehiculoSeleccionado;

  public VentanaVehiculos(boolean modoOscuro, ControladorSistema controlador) {
    this.modoOscuro = modoOscuro;
    this.controlador = controlador;
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

    Estilos.aplicarEstilos(this, modoOscuro);
  }

  private void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<Vehiculo> vehiculos = controlador.getVehiculos();
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
    try {
      // Capturar datos del formulario
      String matricula = txtMatricula.getText().trim();
      String marca = txtMarca.getText().trim();
      String modelo = txtModelo.getText().trim();
      String estado = (String) cmbEstado.getSelectedItem();

      // Delegar la lógica al controlador
      controlador.agregarVehiculo(matricula, marca, modelo, estado);

      // Actualizar interfaz
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Vehículo agregado exitosamente");
    } catch (IllegalArgumentException e) {
      mostrarError(e.getMessage());
    }
  }

  private void eliminarVehiculo() {
    int fila = tablaVehiculos.getSelectedRow();
    if (fila == -1) {
      mostrarError("Debe seleccionar un vehículo para eliminar");
      return;
    }

    String matricula = (String) tablaVehiculos.getValueAt(fila, 0);
    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el vehículo?",
        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        // Delegar la lógica al controlador
        controlador.eliminarVehiculo(matricula);

        // Actualizar interfaz
        actualizarTabla();
        limpiarCampos();
        mostrarExito("Vehículo eliminado exitosamente");
      } catch (IllegalArgumentException e) {
        mostrarError(e.getMessage());
      }
    }
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
    vehiculoSeleccionado = controlador.buscarVehiculo(matricula);

    if (vehiculoSeleccionado != null) {
      txtMatricula.setText(vehiculoSeleccionado.getMatricula());
      txtMarca.setText(vehiculoSeleccionado.getMarca());
      txtModelo.setText(vehiculoSeleccionado.getModelo());

      // Seleccionar el estado en el combo
      String estado = vehiculoSeleccionado.getEstado();
      for (int i = 0; i < cmbEstado.getItemCount(); i++) {
        if (cmbEstado.getItemAt(i).equals(estado)) {
          cmbEstado.setSelectedIndex(i);
          break;
        }
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
    this.modoOscuro = modoOscuro;
    Estilos.aplicarEstilos(this, modoOscuro);
  }

  @Override
  public void actualizarModoOscuro(boolean modoOscuro) {
    setModoOscuro(modoOscuro);
  }
}
