import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VentanaSalida extends JPanel implements ModoOscuroObserver {
    private JComboBox<Entrada> comboEntradas;
    private JComboBox<Empleado> comboEmpleados;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextArea txtComentario;
    private JButton btnRegistrar;
    private JLabel lblEstado;
    private ControladorSistema controlador;
    private boolean modoOscuro;

    public VentanaSalida(boolean modoOscuro, ControladorSistema controlador) {
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

        // Entradas sin salida
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Entrada sin salida:"), gbc);
        gbc.gridx = 1;
        comboEntradas = new JComboBox<>();
        cargarEntradasSinSalida();
        comboEntradas.setPreferredSize(new Dimension(200, 25));
        panelForm.add(comboEntradas, gbc);

        // Empleados
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Empleado que entrega:"), gbc);
        gbc.gridx = 1;
        comboEmpleados = new JComboBox<>();
        cargarEmpleados();
        comboEmpleados.setPreferredSize(new Dimension(200, 25));
        panelForm.add(comboEmpleados, gbc);

        // Fecha
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(LocalDate.now().toString(), 10);
        txtFecha.setPreferredSize(new Dimension(200, 25));
        panelForm.add(txtFecha, gbc);

        // Hora
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Hora (HH:MM, 24hs):"), gbc);
        gbc.gridx = 1;
        txtHora = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), 5);
        txtHora.setPreferredSize(new Dimension(200, 25));
        panelForm.add(txtHora, gbc);

        // Comentario
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("Comentario estado vehículo:"), gbc);
        gbc.gridx = 1;
        txtComentario = new JTextArea(3, 20);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);
        scrollComentario.setPreferredSize(new Dimension(200, 50));
        panelForm.add(scrollComentario, gbc);

        // Botón registrar
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        btnRegistrar = new JButton("Registrar Salida");
        panelForm.add(btnRegistrar, gbc);
        gbc.gridwidth = 1;

        add(panelForm, BorderLayout.CENTER);

        // Label de estado
        lblEstado = new JLabel(" ");
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblEstado, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarSalida());
        Estilos.aplicarEstilos(this, modoOscuro);
    }

    private void cargarEntradasSinSalida() {
        comboEntradas.removeAllItems();
        ArrayList<Entrada> entradas = controlador.getEntradas();
        ArrayList<Salida> salidas = controlador.getSalidas();
        boolean hayEntradas = false;
        for (Entrada entrada : entradas) {
            boolean tieneSalida = false;
            for (Salida salida : salidas) {
                if (salida.getEntrada() != null && salida.getEntrada().equals(entrada)) {
                    tieneSalida = true;
                    break;
                }
            }
            if (!tieneSalida) {
                comboEntradas.addItem(entrada);
                hayEntradas = true;
            }
        }
        if (!hayEntradas) {
            comboEntradas.addItem(new Entrada()); // Entrada vacía, toString muestra 'Sin vehículo'
        }
    }

    private void cargarEmpleados() {
        comboEmpleados.removeAllItems();
        for (Empleado emp : controlador.getEmpleados()) {
            comboEmpleados.addItem(emp);
        }
    }

    private void registrarSalida() {
        Entrada entrada = (Entrada) comboEntradas.getSelectedItem();
        Empleado empleado = (Empleado) comboEmpleados.getSelectedItem();
        String fechaStr = txtFecha.getText().trim();
        String horaStr = txtHora.getText().trim();
        String comentario = txtComentario.getText().trim();

        if (entrada == null || empleado == null || fechaStr.isEmpty() || horaStr.isEmpty()) {
            Estilos.mostrarError(lblEstado, "Complete todos los campos.");
            return;
        }
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            if (fecha.isBefore(entrada.getFecha())
                    || (fecha.isEqual(entrada.getFecha()) && hora.isBefore(entrada.getHora()))) {
                Estilos.mostrarError(lblEstado, "La salida debe ser posterior a la entrada.");
                return;
            }
            Salida salida = new Salida(entrada, empleado, fecha, hora, comentario);
            controlador.agregarSalida(salida);
            Duration duracion = Duration.between(entrada.getFecha().atTime(entrada.getHora()), fecha.atTime(hora));
            long horas = duracion.toHours();
            long minutos = duracion.toMinutes() % 60;
            String tieneContrato = entrada.isTieneContrato() ? "Sí" : "No";
            Estilos.mostrarExito(lblEstado,
                    "Tiempo: " + horas + "h " + minutos + "m. Tiene Contrato: " + tieneContrato);
            cargarEntradasSinSalida();
            txtComentario.setText("");
        } catch (Exception ex) {
            Estilos.mostrarError(lblEstado, "Error en formato de fecha/hora.");
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
        cargarEntradasSinSalida();
        cargarEmpleados();
    }
}
