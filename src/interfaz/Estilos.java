import javax.swing.*;
import java.awt.*;

public class Estilos {
  public static void aplicarEstilos(Component component, boolean modoOscuro) {
    Color fondo = modoOscuro ? Color.DARK_GRAY : Color.WHITE;
    Color texto = modoOscuro ? Color.WHITE : Color.BLACK;

    if (component instanceof JPanel) {
      component.setBackground(fondo);
      for (Component child : ((JPanel) component).getComponents()) {
        aplicarEstilos(child, modoOscuro);
      }
    } else if (component instanceof JLabel) {
      component.setForeground(texto);
    } else if (component instanceof JTable) {
      JTable tabla = (JTable) component;
      tabla.setBackground(fondo);
      tabla.setForeground(texto);
      tabla.getTableHeader().setBackground(fondo);
      tabla.getTableHeader().setForeground(texto);
    } else if (component instanceof JComboBox) {
      JComboBox<?> combo = (JComboBox<?>) component;
      combo.setBackground(fondo);
      combo.setForeground(texto);
    } else if (component instanceof JTextField) {
      JTextField textField = (JTextField) component;
      textField.setBackground(fondo);
      textField.setForeground(texto);
      textField.setCaretColor(texto);
    } else if (component instanceof JButton) {
      component.setBackground(fondo);
      component.setForeground(texto);
    }
  }

  public static void mostrarError(JLabel label, String mensaje) {
    label.setText("Error: " + mensaje);
    label.setForeground(Color.RED);
  }

  public static void mostrarExito(JLabel label, String mensaje) {
    label.setText(mensaje);
    label.setForeground(Color.GREEN);
  }

  // Estilos específicos para reportes
  public static class EstilosReportes {
    public static void aplicarEstilosReporte(Component comp, boolean modoOscuro) {
      Color fondo = modoOscuro ? Color.DARK_GRAY : Color.WHITE;
      Color texto = modoOscuro ? Color.WHITE : Color.BLACK;
      if (comp instanceof JPanel || comp instanceof JTabbedPane) {
        comp.setBackground(fondo);
      }
      if (comp instanceof JLabel || comp instanceof JButton || comp instanceof JCheckBox || comp instanceof JComboBox) {
        comp.setForeground(texto);
        comp.setBackground(fondo);
      }
      if (comp instanceof JTable) {
        JTable table = (JTable) comp;
        table.setBackground(fondo);
        table.setForeground(texto);
        table.getTableHeader().setBackground(fondo);
        table.getTableHeader().setForeground(texto);
      }
      if (comp instanceof JScrollPane) {
        comp.setBackground(fondo);
        Component view = ((JScrollPane) comp).getViewport().getView();
        if (view != null) aplicarEstilosReporte(view, modoOscuro);
      }
      if (comp instanceof Container) {
        for (Component child : ((Container) comp).getComponents()) {
          aplicarEstilosReporte(child, modoOscuro);
        }
      }
    }
  }
}
