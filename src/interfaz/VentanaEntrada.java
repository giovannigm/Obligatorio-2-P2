import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaEntrada extends JPanel implements ModoOscuroObserver {
    private JComboBox<String> comboVehiculos;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextArea txtNotas;
    private JComboBox<String> comboEmpleados;
    private JLabel lblContrato;
    private JButton btnRegistrar;
    private ControladorSistema controlador;
    private boolean modoOscuro;

    public VentanaEntrada(boolean modoOscuro, ControladorSistema controlador) {
        this.modoOscuro = modoOscuro;
        this.controlador = controlador;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Vehículos disponibles
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Vehículo:"), gbc);
        gbc.gridx = 1;
        comboVehiculos = new JComboBox<>();
        cargarVehiculos();
        panelForm.add(comboVehiculos, gbc);

        // Contrato info debajo de vehículo
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        lblContrato = new JLabel("Tiene contrato: ");
        panelForm.add(lblContrato, gbc);
        gbc.gridwidth = 1;

        // Mostrar info de contrato para el primer vehículo si existe
        SwingUtilities.invokeLater(this::mostrarInfoContrato);

        // Fecha
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(10);
        panelForm.add(txtFecha, gbc);

        // Hora
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Hora (HH:MM):"), gbc);
        gbc.gridx = 1;
        txtHora = new JTextField(5);
        panelForm.add(txtHora, gbc);

        // Notas
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Notas:"), gbc);
        gbc.gridx = 1;
        txtNotas = new JTextArea(3, 20);
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        panelForm.add(scrollNotas, gbc);

        // Empleado
        gbc.gridx = 0; gbc.gridy = 5;
        panelForm.add(new JLabel("Empleado:"), gbc);
        gbc.gridx = 1;
        comboEmpleados = new JComboBox<>();
        cargarEmpleados();
        panelForm.add(comboEmpleados, gbc);

        // Registrar botón
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        btnRegistrar = new JButton("Registrar Entrada");
        panelForm.add(btnRegistrar, gbc);
        gbc.gridwidth = 1;

        add(panelForm, BorderLayout.CENTER);

        comboVehiculos.addActionListener(e -> mostrarInfoContrato());
        btnRegistrar.addActionListener(e -> registrarEntrada());

        Estilos.aplicarEstilos(this, modoOscuro);
    }

    private void cargarVehiculos() {
        comboVehiculos.removeAllItems();
        List<Vehiculo> vehiculos = controlador.getVehiculosFueraParking();
        for (Vehiculo v : vehiculos) {
            comboVehiculos.addItem(v.getMatricula() + " - " + v.getModelo());
        }
    }

    private void cargarEmpleados() {
        comboEmpleados.removeAllItems();
        List<Empleado> empleados = controlador.getEmpleados();
        for (Empleado e : empleados) {
            comboEmpleados.addItem(e.getNombre());
        }
    }

    private void mostrarInfoContrato() {
        int idx = comboVehiculos.getSelectedIndex();
        if (idx == -1) {
            lblContrato.setText("Tiene contrato: ");
            return;
        }
        Vehiculo v = controlador.getVehiculosFueraParking().get(idx);
        boolean tieneContrato = controlador.tieneContrato(v);
        lblContrato.setText("Tiene contrato: " + (tieneContrato ? "Sí" : "No"));
        lblContrato.setForeground(tieneContrato ? Color.GREEN.darker() : Color.RED);
    }

    private void registrarEntrada() {
        int idxVehiculo = comboVehiculos.getSelectedIndex();
        int idxEmpleado = comboEmpleados.getSelectedIndex();
        if (idxVehiculo == -1 || idxEmpleado == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar vehículo y empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String fecha = txtFecha.getText().trim();
        String hora = txtHora.getText().trim();
        String notas = txtNotas.getText().trim();
        Vehiculo vehiculo = controlador.getVehiculosFueraParking().get(idxVehiculo);
        Empleado empleado = controlador.getEmpleados().get(idxEmpleado);
        try {
            controlador.registrarEntrada(vehiculo, fecha, hora, notas, empleado);
            JOptionPane.showMessageDialog(this, "Entrada registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarVehiculos();
            txtFecha.setText("");
            txtHora.setText("");
            txtNotas.setText("");
            lblContrato.setText(" ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setModoOscuro(boolean modoOscuro) {
        this.modoOscuro = modoOscuro;
        Estilos.aplicarEstilos(this, modoOscuro);
    }

    @Override
    public void actualizarModoOscuro(boolean modoOscuro) {
        setModoOscuro(modoOscuro);
    }

    public void actualizarControlador(ControladorSistema nuevoControlador) {
        this.controlador = nuevoControlador;
        cargarVehiculos();
        cargarEmpleados();
    }
}
