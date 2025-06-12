import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaContratos extends JPanel {
  private JComboBox<ClienteMensual> cmbClientes;
  private JComboBox<Vehiculo> cmbVehiculos;
  private JComboBox<Empleado> cmbEmpleados;
  private JTextField txtValorMensual;
  private JTable tablaContratos;
  private DefaultTableModel modelo;
  private JLabel lblEstado;
  private DatosSistema datos;
  private boolean modoOscuro;
  private Contrato contratoSeleccionado;

  public VentanaContratos(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    this.datos = DatosSistema.getInstancia();
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
    JScrollPane scrollPane = new JScrollPane(tablaContratos);

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

    for (ClienteMensual cliente : datos.getClientes()) {
      cmbClientes.addItem(cliente);
    }
    for (Vehiculo vehiculo : datos.getVehiculos()) {
      cmbVehiculos.addItem(vehiculo);
    }
    for (Empleado empleado : datos.getEmpleados()) {
      cmbEmpleados.addItem(empleado);
    }
  }

  public void actualizarTabla() {
    modelo.setRowCount(0);
    ArrayList<Contrato> contratos = datos.getContratos();
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
      ClienteMensual cliente = (ClienteMensual) cmbClientes.getSelectedItem();
      Vehiculo vehiculo = (Vehiculo) cmbVehiculos.getSelectedItem();
      Empleado empleado = (Empleado) cmbEmpleados.getSelectedItem();
      double valorMensual = Double.parseDouble(txtValorMensual.getText().trim());

      if (cliente == null || vehiculo == null || empleado == null) {
        Estilos.mostrarError(lblEstado, "Todos los campos son obligatorios");
        return;
      }

      if (valorMensual <= 0) {
        Estilos.mostrarError(lblEstado, "El valor mensual debe ser mayor a 0");
        return;
      }

      // Verificar si el vehículo ya tiene un contrato activo
      for (Contrato contrato : datos.getContratos()) {
        if (contrato.getVehiculo().equals(vehiculo)) {
          Estilos.mostrarError(lblEstado, "El vehículo ya tiene un contrato activo");
          return;
        }
      }

      Contrato nuevoContrato = new Contrato(cliente, vehiculo, empleado, valorMensual);
      datos.agregarContrato(nuevoContrato);
      actualizarTabla();
      limpiarCampos();
      Estilos.mostrarExito(lblEstado, "Contrato agregado exitosamente");
    } catch (NumberFormatException e) {
      Estilos.mostrarError(lblEstado, "El valor mensual debe ser un número válido");
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
    for (Contrato contrato : datos.getContratos()) {
      if (contrato.getIdContrato() == idContrato) {
        contratoSeleccionado = contrato;
        cmbClientes.setSelectedItem(contrato.getCliente());
        cmbVehiculos.setSelectedItem(contrato.getVehiculo());
        cmbEmpleados.setSelectedItem(contrato.getEmpleado());
        txtValorMensual.setText(String.valueOf(contrato.getValorMensual()));
        break;
      }
    }
  }

  public void setModoOscuro(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    aplicarEstilos();
  }

  private void aplicarEstilos() {
    Estilos.aplicarEstilos(this, modoOscuro);
  }
}
