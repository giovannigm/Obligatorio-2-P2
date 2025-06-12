import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {
    private boolean modoOscuro = false;
    private JMenuBar menuBar;
    private JPanel panelPrincipal;
    private JButton btnModo;
    private JMenu menuGestion, menuMov, menuVarios, menuTerminar;
    private JMenuItem itemGestionVehiculos, itemGestionClientes, itemGestionEmpleados, itemMiniJuego;
    private List<JFrame> ventanasSecundarias = new ArrayList<>();

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
            VentanaClientes panel = new VentanaClientes(modoOscuro);
            frameClientes.add(panel, BorderLayout.CENTER);
            frameClientes.setVisible(true);
            ventanasSecundarias.add(frameClientes);
            frameClientes.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
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
            VentanaVehiculos panel = new VentanaVehiculos(modoOscuro);
            frameVehiculos.add(panel, BorderLayout.CENTER);
            frameVehiculos.setVisible(true);
            ventanasSecundarias.add(frameVehiculos);
            frameVehiculos.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
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
            VentanaEmpleados panel = new VentanaEmpleados(modoOscuro);
            frameEmpleados.add(panel, BorderLayout.CENTER);
            frameEmpleados.setVisible(true);
            ventanasSecundarias.add(frameEmpleados);
            frameEmpleados.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    ventanasSecundarias.remove(frameEmpleados);
                }
            });
        });

        // Listener para abrir VentanaMiniJuego en una ventana aparte
        itemMiniJuego.addActionListener(e -> {
            VentanaMiniJuego frameMiniJuego = new VentanaMiniJuego();
            frameMiniJuego.setVisible(true);
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
            // Cuando se cambia el modo, también se actualizan las ventanas secundarias abiertas.
            // Esto se hace recorriendo la lista y llamando setModoOscuro en cada panel secundario.
            // Así, el usuario ve el cambio de color en todas las ventanas, no solo en la principal.
            for (JFrame ventana : ventanasSecundarias) {
                Component panel = ventana.getContentPane().getComponent(0);
                if (panel instanceof VentanaClientes) {
                    ((VentanaClientes) panel).setModoOscuro(modoOscuro);
                } else if (panel instanceof VentanaVehiculos) {
                    ((VentanaVehiculos) panel).setModoOscuro(modoOscuro);
                } else if (panel instanceof VentanaEmpleados) {
                    ((VentanaEmpleados) panel).setModoOscuro(modoOscuro);
                }
            }
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

        getContentPane().setBackground(fondo);
    }

    // --- MODO CLARO/OSCURO EN TODAS LAS VENTANAS ---
//
// 1. Se crea una lista 'ventanasSecundarias' para guardar todas las ventanas secundarias abiertas (Clientes, Vehículos, Empleados).
//
// 2. Cuando se abre una ventana secundaria, se agrega a la lista y se le agrega un WindowListener para eliminarla de la lista al cerrarse.
//
// 3. Cuando el usuario cambia el modo (claro/oscuro) con el botón, además de cambiar el modo en la ventana principal,
//    se recorre la lista de ventanas secundarias y se llama al método setModoOscuro(modoOscuro) de cada panel secundario.
//    Así, todas las ventanas abiertas cambian de color automáticamente.
//
// 4. Si se abre una nueva ventana, toma el modo actual automáticamente porque se le pasa el valor de 'modoOscuro' al constructor.
//
// 5. Este patrón funciona porque VentanaClientes, VentanaVehiculos y VentanaEmpleados implementan el método setModoOscuro(boolean).
//
// Ejemplo de uso en el código:
//
// btnModo.addActionListener(e -> {
//     modoOscuro = !modoOscuro;
//     aplicarModo();
//     for (JFrame ventana : ventanasSecundarias) {
//         Component panel = ventana.getContentPane().getComponent(0);
//         if (panel instanceof VentanaClientes) {
//             ((VentanaClientes) panel).setModoOscuro(modoOscuro);
//         } else if (panel instanceof VentanaVehiculos) {
//             ((VentanaVehiculos) panel).setModoOscuro(modoOscuro);
//         } else if (panel instanceof VentanaEmpleados) {
//             ((VentanaEmpleados) panel).setModoOscuro(modoOscuro);
//         }
//     }
// });
//
// Así, el cambio de modo se refleja en todas las ventanas abiertas.
// --- FIN EXPLICACIÓN ---

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
