import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VentanaReportes extends JFrame implements ModoOscuroObserver {
  private ControladorSistema controlador;
  private JTabbedPane tabbedPane;
  private boolean modoOscuro;

  // Componentes de la pestaña Historial por Vehículo
  private JComboBox<Vehiculo> comboVehiculos;
  private JComboBox<String> comboFiltroTipo;
  private JCheckBox checkOrdenDescendente;
  private JTable tablaMovimientos;
  private DefaultTableModel modeloTabla;
  private JButton btnExportar;
  private JLabel lblEstado;

  public VentanaReportes(ControladorSistema controlador, boolean modoOscuro) {
    this.controlador = controlador;
    this.modoOscuro = modoOscuro;

    configurarVentana();
    crearComponentes();
    configurarEventos();
    aplicarEstilos();
  }

  private void configurarVentana() {
    setTitle("Reportes del Sistema");
    setSize(900, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setResizable(true);
  }

  private void crearComponentes() {
    tabbedPane = new JTabbedPane();

    // Crear pestaña de Historial por Vehículo
    JPanel panelHistorial = crearPanelHistorialVehiculo();
    tabbedPane.addTab("Historial por Vehículo", panelHistorial);

    add(tabbedPane);
  }

  private JPanel crearPanelHistorialVehiculo() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Panel superior con controles
    JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

    // Combo de vehículos
    JLabel lblVehiculo = new JLabel("Vehículo:");
    comboVehiculos = new JComboBox<>();
    comboVehiculos.addItem(null); // Opción vacía
    cargarVehiculos();

    // Combo de filtro por tipo
    JLabel lblFiltro = new JLabel("Filtrar por:");
    comboFiltroTipo = new JComboBox<>(new String[] { "Todos", "Entrada", "Salida", "Servicio Adicional" });

    // Check para orden descendente
    checkOrdenDescendente = new JCheckBox("Orden descendente");

    // Botón exportar
    btnExportar = new JButton("Exportar a TXT");

    // Label de estado
    lblEstado = new JLabel("Seleccione un vehículo para ver su historial");

    panelControles.add(lblVehiculo);
    panelControles.add(comboVehiculos);
    panelControles.add(lblFiltro);
    panelControles.add(comboFiltroTipo);
    panelControles.add(checkOrdenDescendente);
    panelControles.add(btnExportar);
    panelControles.add(lblEstado);

    // Tabla de movimientos
    String[] columnas = { "Tipo", "Fecha", "Hora", "Empleado", "Detalles", "Costo/Duración" };
    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Tabla de solo lectura
      }
    };

    tablaMovimientos = new JTable(modeloTabla);
    JScrollPane scrollTabla = new JScrollPane(tablaMovimientos);

    panel.add(panelControles, BorderLayout.NORTH);
    panel.add(scrollTabla, BorderLayout.CENTER);

    return panel;
  }

  private void cargarVehiculos() {
    comboVehiculos.removeAllItems();
    comboVehiculos.addItem(null); // Opción vacía

    for (Vehiculo vehiculo : controlador.getVehiculos()) {
      comboVehiculos.addItem(vehiculo);
    }
  }

  private void configurarEventos() {
    // Evento de cambio de vehículo
    comboVehiculos.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        actualizarTabla();
      }
    });

    // Evento de cambio de filtro
    comboFiltroTipo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        actualizarTabla();
      }
    });

    // Evento de cambio de orden
    checkOrdenDescendente.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        actualizarTabla();
      }
    });

    // Evento de exportar
    btnExportar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        exportarATxt();
      }
    });
  }

  private void actualizarTabla() {
    modeloTabla.setRowCount(0);

    Vehiculo vehiculoSeleccionado = (Vehiculo) comboVehiculos.getSelectedItem();
    if (vehiculoSeleccionado == null) {
      lblEstado.setText("Seleccione un vehículo para ver su historial");
      return;
    }

    String filtroTipo = (String) comboFiltroTipo.getSelectedItem();
    boolean ordenDescendente = checkOrdenDescendente.isSelected();

    List<MovimientoAdapter> movimientos = obtenerMovimientosVehiculo(vehiculoSeleccionado, filtroTipo);

    // Ordenar movimientos
    if (ordenDescendente) {
      Collections.sort(movimientos, new Comparator<MovimientoAdapter>() {
        @Override
        public int compare(MovimientoAdapter m1, MovimientoAdapter m2) {
          return m2.getFechaHora().compareTo(m1.getFechaHora());
        }
      });
    } else {
      Collections.sort(movimientos, new Comparator<MovimientoAdapter>() {
        @Override
        public int compare(MovimientoAdapter m1, MovimientoAdapter m2) {
          return m1.getFechaHora().compareTo(m2.getFechaHora());
        }
      });
    }

    // Llenar tabla
    for (MovimientoAdapter movimiento : movimientos) {
      modeloTabla.addRow(new Object[] {
          movimiento.getTipo(),
          movimiento.getFecha(),
          movimiento.getHora(),
          movimiento.getEmpleado(),
          movimiento.getDetalles(),
          movimiento.getCostoDuracion()
      });
    }

    lblEstado.setText("Mostrando " + movimientos.size() + " movimientos para " + vehiculoSeleccionado.getMatricula());
  }

  private List<MovimientoAdapter> obtenerMovimientosVehiculo(Vehiculo vehiculo, String filtroTipo) {
    List<MovimientoAdapter> movimientos = new ArrayList<>();

    // Obtener entradas
    if (filtroTipo.equals("Todos") || filtroTipo.equals("Entrada")) {
      for (Entrada entrada : controlador.getEntradas()) {
        if (entrada.getVehiculo().equals(vehiculo)) {
          movimientos.add(new MovimientoAdapter(entrada));
        }
      }
    }

    // Obtener salidas
    if (filtroTipo.equals("Todos") || filtroTipo.equals("Salida")) {
      for (Salida salida : controlador.getSalidas()) {
        if (salida.getEntrada().getVehiculo().equals(vehiculo)) {
          movimientos.add(new MovimientoAdapter(salida));
        }
      }
    }

    // Obtener servicios adicionales
    if (filtroTipo.equals("Todos") || filtroTipo.equals("Servicio Adicional")) {
      for (ServicioAdicional servicio : controlador.getServiciosAdicionales()) {
        if (servicio.getVehiculo().equals(vehiculo)) {
          movimientos.add(new MovimientoAdapter(servicio));
        }
      }
    }

    return movimientos;
  }

  private void exportarATxt() {
    Vehiculo vehiculoSeleccionado = (Vehiculo) comboVehiculos.getSelectedItem();
    if (vehiculoSeleccionado == null) {
      JOptionPane.showMessageDialog(this,
          "Debe seleccionar un vehículo antes de exportar",
          "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    String nombreArchivo = vehiculoSeleccionado.getMatricula() + ".txt";

    try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
      writer.println("HISTORIAL DE MOVIMIENTOS - VEHÍCULO: " + vehiculoSeleccionado.getMatricula());
      writer.println(
          "Fecha de exportación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
      writer.println(crearLineaSeparadora(80));
      writer.println();

      // Escribir encabezados
      writer.printf("%-15s %-12s %-8s %-20s %-30s %-15s%n",
          "TIPO", "FECHA", "HORA", "EMPLEADO", "DETALLES", "COSTO/DURACIÓN");
      writer.println(crearLineaSeparadora(80));

      // Escribir datos de la tabla
      for (int i = 0; i < modeloTabla.getRowCount(); i++) {
        writer.printf("%-15s %-12s %-8s %-20s %-30s %-15s%n",
            modeloTabla.getValueAt(i, 0),
            modeloTabla.getValueAt(i, 1),
            modeloTabla.getValueAt(i, 2),
            modeloTabla.getValueAt(i, 3),
            modeloTabla.getValueAt(i, 4),
            modeloTabla.getValueAt(i, 5));
      }

      writer.println();
      writer.println("Total de movimientos: " + modeloTabla.getRowCount());

      JOptionPane.showMessageDialog(this,
          "Archivo exportado exitosamente: " + nombreArchivo,
          "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (IOException e) {
      JOptionPane.showMessageDialog(this,
          "Error al exportar el archivo: " + e.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private String crearLineaSeparadora(int longitud) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < longitud; i++) {
      sb.append("=");
    }
    return sb.toString();
  }

  private void aplicarEstilos() {
    Estilos.EstilosReportes.aplicarEstilosReporte(this, modoOscuro);
    repaint();
  }

  public void setModoOscuro(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    aplicarEstilos();
  }

  @Override
  public void actualizarModoOscuro(boolean modoOscuro) {
    setModoOscuro(modoOscuro);
  }
}
