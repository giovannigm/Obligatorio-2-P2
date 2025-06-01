import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private boolean modoOscuro = false;
    private JMenuBar menuBar;
    private JPanel panelPrincipal;
    private JButton btnModo;
    private JMenu menuGestion, menuMov, menuVarios, menuTerminar;

    public VentanaPrincipal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Obligatorio Prog 2 - Autor: Estudiante - Número");
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
        menuGestion.add(new JMenuItem("Gestión de Clientes"));
        menuGestion.add(new JMenuItem("Gestión de Vehículos"));
        menuGestion.add(new JMenuItem("Gestión de Empleados"));
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
        menuTerminar.add(new JMenuItem("Salir"));

        menuBar.add(menuGestion);
        menuBar.add(menuMov);
        menuBar.add(menuVarios);
        menuBar.add(menuTerminar);

        setJMenuBar(menuBar);
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
                        item.setBackground(Color.WHITE);  // 🔄 Siempre blanco
                        item.setForeground(Color.BLACK);
                        item.setOpaque(true);
                    }
                }
            }
        }

        getContentPane().setBackground(fondo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
