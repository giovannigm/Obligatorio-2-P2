package interfaz;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private boolean modoOscuro = false;
    private JPanel panel;

    public VentanaPrincipal() {
        setTitle("Obligatorio Prog 2 - Autor: Giovanni 288127, Nicolas 258264");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void cambiarModo() {
        if (modoOscuro) {
            panel.setBackground(Color.WHITE);
        } else {
            panel.setBackground(Color.DARK_GRAY);
        }
        modoOscuro = !modoOscuro;
    }

    private void initComponents() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Barra de pestañas arriba
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Gestión", new JPanel());
        pestañas.addTab("Movimientos", new JPanel());
        pestañas.addTab("Varios", new JPanel());
        pestañas.addTab("Terminal", new JPanel());
        panel.add(pestañas, BorderLayout.NORTH);

        // Botón Claro/Oscuro centrado abajo
        JButton btnModo = new JButton("Claro/Oscuro");
        btnModo.addActionListener(e -> cambiarModo());
        JPanel panelModo = new JPanel();
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
