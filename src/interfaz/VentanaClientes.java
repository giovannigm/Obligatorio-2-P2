import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
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
  private JLabel lblNombre;
  private JLabel lblCedula;
  private JLabel lblDireccion;
  private JLabel lblCelular;
  private JLabel lblAnioAlta;

  public VentanaClientes(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    this.datos = DatosSistema.getInstancia();
    initComponents();
    actualizarTabla();
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    // Filtro para solo 4 números (Año Alta)
    DocumentFilter soloCuatroNumerosFilter = new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
          throws BadLocationException {
        String nuevoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (nuevoTexto.length() <= 4 && string.matches("\\d*")) {
          super.insertString(fb, offset, string, attr);
        }
      }

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder sb = new StringBuilder(actual);
        sb.replace(offset, offset + length, text);
        if (sb.length() <= 4 && text.matches("\\d*")) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    };

    // Filtro para solo 9 números (Cédula y Celular)
    DocumentFilter soloNueveNumerosFilter = new DocumentFilter() {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
          throws BadLocationException {
        String nuevoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        if (nuevoTexto.length() <= 9 && string.matches("\\d*")) {
          super.insertString(fb, offset, string, attr);
        }
      }

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder sb = new StringBuilder(actual);
        sb.replace(offset, offset + length, text);
        if (sb.length() <= 9 && text.matches("\\d*")) {
          super.replace(fb, offset, length, text, attrs);
        }
      }
    };

    // Panel de formulario
    JPanel panelFormulario = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Campos del formulario
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
    lblCelular = new JLabel("Celular:");
    panelFormulario.add(lblCelular, gbc);
    gbc.gridx = 1;
    txtCelular = new JTextField(20);
    ((AbstractDocument) txtCelular.getDocument()).setDocumentFilter(soloNueveNumerosFilter);
    panelFormulario.add(txtCelular, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    lblAnioAlta = new JLabel("Año Alta:");
    panelFormulario.add(lblAnioAlta, gbc);
    gbc.gridx = 1;
    txtAnioAlta = new JTextField(20);
    ((AbstractDocument) txtAnioAlta.getDocument()).setDocumentFilter(soloCuatroNumerosFilter);
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
    tablaClientes.setOpaque(false);
    tablaClientes.getTableHeader().setOpaque(false);
    JScrollPane scrollPane = new JScrollPane(tablaClientes);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);

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
    lblNombre.setForeground(texto);
    lblCedula.setForeground(texto);
    lblDireccion.setForeground(texto);
    lblCelular.setForeground(texto);
    lblAnioAlta.setForeground(texto);

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
