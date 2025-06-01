import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private boolean modoOscuro = false;
    private JPanel panel;
    private JTabbedPane pestañas;
    private JPanel panelModo;

    public VentanaPrincipal() {
        setTitle("Obligatorio Prog 2 - Autor: Giovanni 288127, Nicolas 258264");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void cambiarModo() {
        Color fondo = modoOscuro ? Color.WHITE : Color.DARK_GRAY;
        Color texto = modoOscuro ? Color.BLACK : Color.WHITE;
        panel.setBackground(fondo);
        pestañas.setBackground(fondo);
        pestañas.setForeground(texto);
        panelModo.setBackground(fondo);
        for (int i = 0; i < pestañas.getTabCount(); i++) {
            Component c = pestañas.getComponentAt(i);
            if (c instanceof JPanel) {
                c.setBackground(fondo);
                c.setForeground(texto);
            }
        }
        modoOscuro = !modoOscuro;
    }

    private void initComponents() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Barra de pestañas arriba
        pestañas = new JTabbedPane();
        pestañas.addTab("Gestión", new JPanel());
        pestañas.addTab("Movimientos", new JPanel());
        pestañas.addTab("Varios", new JPanel());
        pestañas.addTab("Terminal", new JPanel());
        panel.add(pestañas, BorderLayout.NORTH);

        // Botón Claro/Oscuro centrado abajo
        JButton btnModo = new JButton("Claro/Oscuro");
        btnModo.addActionListener(e -> cambiarModo());
        panelModo = new JPanel();
        panelModo.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelModo.add(btnModo);
        panel.add(panelModo, BorderLayout.SOUTH);

        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
