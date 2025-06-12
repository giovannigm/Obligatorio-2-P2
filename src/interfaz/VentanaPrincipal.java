import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private boolean modoOscuro = false;
    private JPanel panel;
    private JTabbedPane pestañas;
    private JPanel panelModo;
    private VentanaClientes ventanaClientes;
    private VentanaVehiculos ventanaVehiculos;
    private VentanaEmpleados ventanaEmpleados;
    private VentanaContratos ventanaContratos;
    private JMenuBar menuBar;
    private JPanel panelPrincipal;
    private JButton btnModo;
    private JMenu menuGestion, menuMov, menuVarios, menuTerminar;
    private JMenuItem itemGestionVehiculos, itemGestionClientes, itemGestionEmpleados;

    public VentanaPrincipal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Obligatorio Prog 2 - Autor: Giovanni - 288127,  Nicolas - 258264");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearMenu();
        crearContenido();

        aplicarModo();
        setVisible(true);
    }

    private void crearMenu() {
        menuBar = new JMenuBar();

        // Menú Gestión
        menuGestion = new JMenu("Gestión");
        itemGestionClientes = new JMenuItem("Gestión de Clientes");

        menuGestion.add(itemGestionClientes);
        itemGestionVehiculos = new JMenuItem("Gestión de Vehículos");

        menuGestion.add(itemGestionVehiculos);
        itemGestionEmpleados = new JMenuItem("Gestión de Empleados");
        menuGestion.add(itemGestionEmpleados);
        menuGestion.add(new JMenuItem("Gestión de Contratos"));

        // Menú Movimientos
        menuMov = new JMenu("Movimientos");
        menuMov.add(new JMenuItem("Entradas"));
        menuMov.add(new JMenuItem("Salidas"));
        menuMov.add(new JMenuItem("Servicios Adicionales"));

        // Menú Varios
        menuVarios = new JMenu("Varios");
        menuVarios.add(new JMenuItem("Reportes"));
        menuVarios.add(new JMenuItem("Recuperación de datos"));
        menuVarios.add(new JMenuItem("Grabación de datos"));
        menuVarios.add(new JMenuItem("MiniJuego"));
        menuVarios.add(new JMenuItem("Información de Autores"));

        // Menú Terminar
        menuTerminar = new JMenu("Terminar");
        JMenuItem itemSalir = new JMenuItem("Salir");
        menuTerminar.add(itemSalir);

        menuBar.add(menuGestion);
        menuBar.add(menuMov);
        menuBar.add(menuVarios);
        menuBar.add(menuTerminar);

        setJMenuBar(menuBar);

        // Listener para abrir VentanaClientes en una ventana aparte
        itemGestionClientes.addActionListener(e -> {
            JFrame frameClientes = new JFrame("Gestión de Clientes");
            frameClientes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameClientes.setSize(600, 400);
            frameClientes.setLocationRelativeTo(this);
            frameClientes.setLayout(new BorderLayout());
            frameClientes.add(new VentanaClientes(modoOscuro), BorderLayout.CENTER);
            frameClientes.setVisible(true);
        });

        // Listener para abrir VentanaVehiculos en una ventana aparte
        itemGestionVehiculos.addActionListener(e -> {
            JFrame frameVehiculos = new JFrame("Gestión de Vehículos");
            frameVehiculos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameVehiculos.setSize(600, 400);
            frameVehiculos.setLocationRelativeTo(this);
            frameVehiculos.setLayout(new BorderLayout());
            frameVehiculos.add(new VentanaVehiculos(modoOscuro), BorderLayout.CENTER);
            frameVehiculos.setVisible(true);
        });

        // Listener para abrir VentanaEmpleados en una ventana aparte
        itemGestionEmpleados.addActionListener(e -> {
            JFrame frameEmpleados = new JFrame("Gestión de Empleados");
            frameEmpleados.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameEmpleados.setSize(600, 400);
            frameEmpleados.setLocationRelativeTo(this);
            frameEmpleados.setLayout(new BorderLayout());
            frameEmpleados.add(new VentanaEmpleados(modoOscuro), BorderLayout.CENTER);
            frameEmpleados.setVisible(true);
        });

        // Listener para salir con confirmación
        itemSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir?", "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void crearContenido() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());

        btnModo = new JButton("Claro / Oscuro");
        btnModo.setPreferredSize(new Dimension(180, 40)); // Tamaño mediano
        btnModo.addActionListener(e -> {
            modoOscuro = !modoOscuro;
            aplicarModo();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(btnModo, gbc);
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void aplicarModo() {
        Color fondo, texto, fondoMenu;

        if (modoOscuro) {
            fondo = Color.BLACK;
            texto = Color.WHITE;
            fondoMenu = new Color(30, 30, 30);
        } else {
            fondo = Color.WHITE;
            texto = Color.BLACK;
            fondoMenu = new Color(240, 240, 240);
        }

        // Fondo y botón
        panelPrincipal.setBackground(fondo);
        btnModo.setBackground(fondoMenu);
        btnModo.setForeground(texto);

        // Menú principal
        menuBar.setBackground(fondoMenu);
        menuBar.setOpaque(true);

        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null) {
                menu.setBackground(fondoMenu);
                menu.setForeground(texto);
                menu.setOpaque(true);

                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setBackground(Color.WHITE); // 🔄 Siempre blanco
                        item.setForeground(Color.BLACK);
                        item.setOpaque(true);
                    }
                }
            }
        }
        if (ventanaClientes != null) {
            ventanaClientes.setModoOscuro(!modoOscuro);
        }
        if (ventanaVehiculos != null) {
            ventanaVehiculos.setModoOscuro(!modoOscuro);
        }
        if (ventanaEmpleados != null) {
            ventanaEmpleados.setModoOscuro(!modoOscuro);
        }
        if (ventanaContratos != null) {
            ventanaContratos.setModoOscuro(!modoOscuro);
        }
        modoOscuro = !modoOscuro;
    }

    private void initComponents() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Barra de pestañas arriba
        pestañas = new JTabbedPane();
        ventanaClientes = new VentanaClientes(modoOscuro);
        ventanaVehiculos = new VentanaVehiculos(modoOscuro);
        ventanaEmpleados = new VentanaEmpleados(modoOscuro);
        ventanaContratos = new VentanaContratos(modoOscuro);
        pestañas.addTab("Clientes", ventanaClientes);
        pestañas.addTab("Vehículos", ventanaVehiculos);
        pestañas.addTab("Empleados", ventanaEmpleados);
        pestañas.addTab("Contratos", ventanaContratos);
        pestañas.addTab("Movimientos", new JPanel());
        pestañas.addTab("Varios", new JPanel());
        pestañas.addTab("Terminal", new JPanel());

        // Agregar listener para actualizar comboboxes cuando se cambie a la pestaña
        // Contratos
        pestañas.addChangeListener(e -> {
            if (pestañas.getSelectedComponent() == ventanaContratos) {
                ventanaContratos.refrescarDatos();
            }
        });

        panel.add(pestañas, BorderLayout.CENTER);

        // Botón Claro/Oscuro centrado abajo
        JButton btnModo = new JButton("Claro/Oscuro");
        btnModo.addActionListener(e -> aplicarModo());
        panelModo = new JPanel();
        panelModo.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelModo.add(btnModo);
        panel.add(panelModo, BorderLayout.SOUTH);

        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
