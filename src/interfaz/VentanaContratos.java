import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaContratos extends JPanel implements ModoOscuroObserver {
  private JComboBox<ClienteMensual> cmbClientes;
  private JComboBox<Vehiculo> cmbVehiculos;
  private JComboBox<Empleado> cmbEmpleados;
  private JTextField txtValorMensual;
  private JTable tablaContratos;
  private DefaultTableModel modelo;
  private JLabel lblEstado;
  private ControladorSistema controlador;
  private boolean modoOscuro;
  private Contrato contratoSeleccionado;

  public VentanaContratos(boolean modoOscuro, ControladorSistema controlador) {
    this.modoOscuro = modoOscuro;
    this.controlador = controlador;
    initComponents();
    actualizarTabla();
    actualizarCombos();
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
    panelFormulario.add(new JLabel("Cliente:"), gbc);
    gbc.gridx = 1;
    cmbClientes = new JComboBox<>();
    panelFormulario.add(cmbClientes, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panelFormulario.add(new JLabel("Vehículo:"), gbc);
    gbc.gridx = 1;
    cmbVehiculos = new JComboBox<>();
    panelFormulario.add(cmbVehiculos, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panelFormulario.add(new JLabel("Empleado:"), gbc);
    gbc.gridx = 1;
    cmbEmpleados = new JComboBox<>();
    panelFormulario.add(cmbEmpleados, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    panelFormulario.add(new JLabel("Valor Mensual:"), gbc);
    gbc.gridx = 1;
    txtValorMensual = new JTextField(20);
    panelFormulario.add(txtValorMensual, gbc);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton btnAgregar = new JButton("Agregar Contrato");
    JButton btnLimpiar = new JButton("Limpiar");

    btnAgregar.addActionListener(e -> agregarContrato());
    btnLimpiar.addActionListener(e -> limpiarCampos());

    panelBotones.add(btnAgregar);
    panelBotones.add(btnLimpiar);

    // Panel de estado
    lblEstado = new JLabel(" ");
    lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

    // Tabla de contratos
    String[] columnas = { "ID", "Cliente", "Vehículo", "Empleado", "Valor Mensual" };
    modelo = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    tablaContratos = new JTable(modelo);
    tablaContratos.setOpaque(false);
    tablaContratos.getTableHeader().setOpaque(false);
    JScrollPane scrollPane = new JScrollPane(tablaContratos);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);


    // Listener para selección en la tabla
    tablaContratos.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        int fila = tablaContratos.getSelectedRow();
        if (fila != -1) {
          cargarContratoSeleccionado(fila);
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

  public void actualizarCombos() {
    cmbClientes.removeAllItems();
    cmbVehiculos.removeAllItems();
    cmbEmpleados.removeAllItems();

    for (ClienteMensual cliente : controlador.getClientes()) {
      cmbClientes.addItem(cliente);
    }
    for (Vehiculo vehiculo : controlador.getVehiculos()) {
      cmbVehiculos.addItem(vehiculo);
    }
    for (Empleado empleado : controlador.getEmpleados()) {
      cmbEmpleados.addItem(empleado);
    }
  }

  public void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<Contrato> contratos = controlador.getContratos();
    for (Contrato contrato : contratos) {
      Object[] fila = {
          contrato.getIdContrato(),
          contrato.getCliente().getNombre(),
          contrato.getVehiculo().getMatricula(),
          contrato.getEmpleado().getNombre(),
          contrato.getValorMensual()
      };
      modelo.addRow(fila);
    }
  }

  // Método público para refrescar todos los datos
  public void refrescarDatos() {
    actualizarCombos();
    actualizarTabla();
  }

  private void agregarContrato() {
    try {
      // Capturar datos del formulario
      ClienteMensual cliente = (ClienteMensual) cmbClientes.getSelectedItem();
      Vehiculo vehiculo = (Vehiculo) cmbVehiculos.getSelectedItem();
      Empleado empleado = (Empleado) cmbEmpleados.getSelectedItem();
      String valorMensual = txtValorMensual.getText().trim();

      controlador.agregarContrato(cliente, vehiculo, empleado, valorMensual);

      // Actualizar interfaz
      actualizarTabla();
      limpiarCampos();
      mostrarExito("Contrato agregado exitosamente");
    } catch (IllegalArgumentException e) {
      mostrarError(e.getMessage());
    }
  }

  private void limpiarCampos() {
    cmbClientes.setSelectedIndex(-1);
    cmbVehiculos.setSelectedIndex(-1);
    cmbEmpleados.setSelectedIndex(-1);
    txtValorMensual.setText("");
    tablaContratos.clearSelection();
    contratoSeleccionado = null;
    lblEstado.setText(" ");
  }

  private void cargarContratoSeleccionado(int fila) {
    int idContrato = (int) tablaContratos.getValueAt(fila, 0);
    contratoSeleccionado = controlador.buscarContrato(idContrato);

    if (contratoSeleccionado != null) {
      cmbClientes.setSelectedItem(contratoSeleccionado.getCliente());
      cmbVehiculos.setSelectedItem(contratoSeleccionado.getVehiculo());
      cmbEmpleados.setSelectedItem(contratoSeleccionado.getEmpleado());
      txtValorMensual.setText(String.valueOf(contratoSeleccionado.getValorMensual()));
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

    tablaContratos.setBackground(fondo);
    tablaContratos.setForeground(texto);
    tablaContratos.getTableHeader().setBackground(fondo);
    tablaContratos.getTableHeader().setForeground(texto);
    lblEstado.setForeground(texto);
    cmbClientes.setBackground(fondo);
    cmbClientes.setForeground(texto);
    cmbVehiculos.setBackground(fondo);
    cmbVehiculos.setForeground(texto);
    cmbEmpleados.setBackground(fondo);
    cmbEmpleados.setForeground(texto);
  }

  public void setModoOscuro(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    aplicarEstilos();
  }

  @Override
  public void actualizarModoOscuro(boolean modoOscuro) {
    setModoOscuro(modoOscuro);
  }
}
