import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
    private boolean modoOscuro = false;
    private JMenuBar menuBar;
    private JPanel panelPrincipal;
    private JButton btnModo;
    private JMenu menuGestion, menuMov, menuVarios, menuTerminar;
    private JMenuItem itemGestionVehiculos, itemGestionClientes, itemGestionEmpleados, itemGestionContratos,
            itemMiniJuego, itemServiciosAdicionales, itemGrabarDatos, itemRecuperarDatos;
    private List<JFrame> ventanasSecundarias = new ArrayList<>();
    private ControladorSistema controlador;
    private List<ModoOscuroObserver> observadores = new ArrayList<>();

    public VentanaPrincipal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Crear una única instancia del controlador que se compartirá entre todas las
        // ventanas
        this.controlador = new ControladorSistema();

        setTitle("Obligatorio Prog 2 - Autor: Giovanni - 288127,  Nicolas - 258264");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearMenu();
        crearContenido();

        Estilos.aplicarEstilos(panelPrincipal, modoOscuro);
        setVisible(true);
    }

    // Métodos para Observer
    public void agregarObservador(ModoOscuroObserver obs) {
        observadores.add(obs);
    }

    public void eliminarObservador(ModoOscuroObserver obs) {
        observadores.remove(obs);
    }

    private void notificarObservadores() {
        for (ModoOscuroObserver obs : observadores) {
            obs.actualizarModoOscuro(modoOscuro);
        }
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
        itemGestionContratos = new JMenuItem("Gestión de Contratos");
        menuGestion.add(itemGestionContratos);

        // Menú Movimientos
        menuMov = new JMenu("Movimientos");
        menuMov.add(new JMenuItem("Entradas"));
        menuMov.add(new JMenuItem("Salidas"));
        itemServiciosAdicionales = new JMenuItem("Servicios Adicionales");
        menuMov.add(itemServiciosAdicionales);

        // Menú Varios
        menuVarios = new JMenu("Varios");
        menuVarios.add(new JMenuItem("Reportes"));
        itemRecuperarDatos = new JMenuItem("Recuperación de datos");
        menuVarios.add(itemRecuperarDatos);
        itemGrabarDatos = new JMenuItem("Grabación de datos");
        menuVarios.add(itemGrabarDatos);
        itemMiniJuego = new JMenuItem("MiniJuego");
        menuVarios.add(itemMiniJuego);
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
            VentanaClientes panel = new VentanaClientes(modoOscuro, controlador);
            agregarObservador(panel);
            frameClientes.add(panel, BorderLayout.CENTER);
            frameClientes.setVisible(true);
            ventanasSecundarias.add(frameClientes);
            frameClientes.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    eliminarObservador(panel);
                    ventanasSecundarias.remove(frameClientes);
                }
            });
        });

        // Listener para abrir VentanaVehiculos en una ventana aparte
        itemGestionVehiculos.addActionListener(e -> {
            JFrame frameVehiculos = new JFrame("Gestión de Vehículos");
            frameVehiculos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameVehiculos.setSize(600, 400);
            frameVehiculos.setLocationRelativeTo(this);
            frameVehiculos.setLayout(new BorderLayout());
            VentanaVehiculos panel = new VentanaVehiculos(modoOscuro, controlador);
            agregarObservador(panel);
            frameVehiculos.add(panel, BorderLayout.CENTER);
            frameVehiculos.setVisible(true);
            ventanasSecundarias.add(frameVehiculos);
            frameVehiculos.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    eliminarObservador(panel);
                    ventanasSecundarias.remove(frameVehiculos);
                }
            });
        });

        // Listener para abrir VentanaEmpleados en una ventana aparte
        itemGestionEmpleados.addActionListener(e -> {
            JFrame frameEmpleados = new JFrame("Gestión de Empleados");
            frameEmpleados.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameEmpleados.setSize(600, 400);
            frameEmpleados.setLocationRelativeTo(this);
            frameEmpleados.setLayout(new BorderLayout());
            VentanaEmpleados panel = new VentanaEmpleados(modoOscuro, controlador);
            agregarObservador(panel);
            frameEmpleados.add(panel, BorderLayout.CENTER);
            frameEmpleados.setVisible(true);
            ventanasSecundarias.add(frameEmpleados);
            frameEmpleados.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    eliminarObservador(panel);
                    ventanasSecundarias.remove(frameEmpleados);
                }
            });
        });

        // Listener para abrir VentanaContratos en una ventana aparte
        itemGestionContratos.addActionListener(e -> {
            JFrame frameContratos = new JFrame("Gestión de Contratos");
            frameContratos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameContratos.setSize(600, 400);
            frameContratos.setLocationRelativeTo(this);
            frameContratos.setLayout(new BorderLayout());
            VentanaContratos panel = new VentanaContratos(modoOscuro, controlador);
            agregarObservador(panel);
            frameContratos.add(panel, BorderLayout.CENTER);
            frameContratos.setVisible(true);
            ventanasSecundarias.add(frameContratos);
            frameContratos.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    eliminarObservador(panel);
                    ventanasSecundarias.remove(frameContratos);
                }
            });
        });

        // Listener para abrir VentanaServiciosAdicionales en una ventana aparte
        itemServiciosAdicionales.addActionListener(e -> {
            JFrame frameServicios = new JFrame("Gestión de Servicios Adicionales");
            frameServicios.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameServicios.setSize(700, 500);
            frameServicios.setLocationRelativeTo(this);
            frameServicios.setLayout(new BorderLayout());
            VentanaServiciosAdicionales panel = new VentanaServiciosAdicionales(modoOscuro, controlador);
            agregarObservador(panel);
            frameServicios.add(panel, BorderLayout.CENTER);
            frameServicios.setVisible(true);
            ventanasSecundarias.add(frameServicios);
            frameServicios.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    eliminarObservador(panel);
                    ventanasSecundarias.remove(frameServicios);
                }
            });
        });

        // Listener para abrir VentanaMiniJuego en una ventana aparte
        itemMiniJuego.addActionListener(e -> {
            VentanaMiniJuego frameMiniJuego = new VentanaMiniJuego();
            frameMiniJuego.setVisible(true);
            frameMiniJuego.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                }
            });
        });

        // Listener para grabar datos del sistema
        itemGrabarDatos.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea grabar los datos del sistema?",
                    "Confirmar grabación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    ControladorSistema.guardarDatos(controlador);
                    JOptionPane.showMessageDialog(
                            this,
                            "Los datos han sido guardados exitosamente.",
                            "Grabación exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error al guardar los datos: " + ex.getMessage(),
                            "Error de grabación",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener para recuperar datos del sistema
        itemRecuperarDatos.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea recuperar los datos del sistema?",
                    "Confirmar recuperación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    ControladorSistema controladorRecuperado = ControladorSistema.recuperarDatos();

                    // Reemplazar la instancia actual del controlador
                    this.controlador = controladorRecuperado;

                    // Actualizar todas las ventanas secundarias abiertas con el nuevo controlador
                    for (JFrame ventana : ventanasSecundarias) {
                        Component panel = ventana.getContentPane().getComponent(0);
                        if (panel instanceof VentanaClientes) {
                            ((VentanaClientes) panel).actualizarControlador(controlador);
                        } else if (panel instanceof VentanaVehiculos) {
                            ((VentanaVehiculos) panel).actualizarControlador(controlador);
                        } else if (panel instanceof VentanaEmpleados) {
                            ((VentanaEmpleados) panel).actualizarControlador(controlador);
                        } else if (panel instanceof VentanaContratos) {
                            ((VentanaContratos) panel).actualizarControlador(controlador);
                        } else if (panel instanceof VentanaServiciosAdicionales) {
                            ((VentanaServiciosAdicionales) panel).actualizarControlador(controlador);
                        }
                    }

                    JOptionPane.showMessageDialog(
                            this,
                            "Los datos han sido recuperados exitosamente.",
                            "Recuperación exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error al leer el archivo: " + ex.getMessage(),
                            "Error de recuperación",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error de compatibilidad de clases. El archivo puede estar corrupto o ser incompatible.",
                            "Error de compatibilidad",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
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
            Estilos.aplicarEstilos(panelPrincipal, modoOscuro);
            notificarObservadores();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(btnModo, gbc);
        add(panelPrincipal, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
