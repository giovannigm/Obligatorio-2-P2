import javax.swing.*;
import java.awt.*;

public class VentanaReportes extends JPanel {
    private JTabbedPane tabbedPane;
    public VentanaReportes(boolean modoOscuro) {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Paneles de ejemplo para los reportes 1, 2 y 3
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Reporte 1"));
        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Reporte 2"));
        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("Reporte 3"));

        tabbedPane.addTab("Reporte 1", panel1);
        tabbedPane.addTab("Reporte 2", panel2);
        tabbedPane.addTab("Reporte 3", panel3);

        add(tabbedPane, BorderLayout.CENTER);
        aplicarModoOscuro(modoOscuro);
    }

    public void setModoOscuro(boolean modoOscuro) {
        aplicarModoOscuro(modoOscuro);
    }

    private void aplicarModoOscuro(boolean oscuro) {
        Color fondo, texto, fondoTabs;
        if (oscuro) {
            fondo = Color.BLACK;
            texto = Color.WHITE;
            fondoTabs = new Color(30, 30, 30);
        } else {
            fondo = Color.WHITE;
            texto = Color.BLACK;
            fondoTabs = new Color(240, 240, 240);
        }
        setBackground(fondo);
        tabbedPane.setBackground(fondoTabs);
        tabbedPane.setForeground(texto);
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component c = tabbedPane.getComponentAt(i);
            if (c instanceof JPanel) {
                c.setBackground(fondo);
                for (Component child : ((JPanel) c).getComponents()) {
                    child.setForeground(texto);
                    child.setBackground(fondo);
                }
            }
        }
    }
}
