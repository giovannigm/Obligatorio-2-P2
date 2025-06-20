// Autores: Giovanni - 288127,  Nicolas - 258264

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class VentanaReportes extends JFrame implements ModoOscuroObserver, ReportesObserver {
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
    this.controlador.addReportesObserver(this);
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
    JPanel panelHistorial = crearPanelHistorialVehiculo();
    tabbedPane.addTab("Historial", panelHistorial);

    JPanel panelMovimiento = new JPanel();
    panelMovimiento.add(new JLabel("Panel de Movimiento (en desarrollo)"));
    tabbedPane.addTab("Movimiento", panelMovimiento);

    JPanel panelEstadisticas = crearPanelEstadisticasGenerales();
    tabbedPane.addTab("Estadísticas Generales", panelEstadisticas);
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

    // Obtener movimientos del controlador
    ArrayList<MovimientoAdapter> movimientos = controlador.obtenerMovimientosVehiculo(vehiculoSeleccionado, filtroTipo);

    // Ordenar movimientos usando el controlador
    movimientos = controlador.ordenarMovimientos(movimientos, ordenDescendente);

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

  private void exportarATxt() {
    Vehiculo vehiculoSeleccionado = (Vehiculo) comboVehiculos.getSelectedItem();
    if (vehiculoSeleccionado == null) {
      JOptionPane.showMessageDialog(this,
          "Debe seleccionar un vehículo antes de exportar",
          "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    String nombreArchivo = vehiculoSeleccionado.getMatricula() + ".txt";
    String filtroTipo = (String) comboFiltroTipo.getSelectedItem();

    try {
      ArrayList<MovimientoAdapter> movimientos = controlador.obtenerMovimientosVehiculo(vehiculoSeleccionado,
          filtroTipo);
      boolean ordenDescendente = checkOrdenDescendente.isSelected();
      movimientos = controlador.ordenarMovimientos(movimientos, ordenDescendente);

      controlador.exportarMovimientosATxt(vehiculoSeleccionado, movimientos, nombreArchivo);

      JOptionPane.showMessageDialog(this,
          "Archivo exportado exitosamente: " + nombreArchivo,
          "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (IOException e) {
      JOptionPane.showMessageDialog(this,
          "Error al exportar el archivo: " + e.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
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

  @Override
  public void reportesActualizados() {
    SwingUtilities.invokeLater(() -> {
      int idx = tabbedPane.indexOfTab("Estadísticas Generales");
      if (idx != -1) {
        tabbedPane.setComponentAt(idx, crearPanelEstadisticasGenerales());
      }
    });
  }

  private JPanel crearPanelEstadisticasGenerales() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margen general

    // Panel superior con tres tablas en una sola fila y margen entre ellas
    JPanel panelFila1 = new JPanel(new GridLayout(1, 3, 20, 0)); // Espacio horizontal entre columnas
    panelFila1.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Margen inferior

    // Servicios adicionales más utilizados
    JPanel panelServicios = new JPanel(new BorderLayout());
    panelServicios.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Margen derecho
    JLabel lblServicios = new JLabel("Servicios adicionales más utilizados:", SwingConstants.CENTER);
    JTable tablaServicios = crearTablaServiciosMasUtilizados();
    panelServicios.add(lblServicios, BorderLayout.NORTH);
    panelServicios.add(new JScrollPane(tablaServicios), BorderLayout.CENTER);
    panelFila1.add(panelServicios);

    // Empleados con menor cantidad de movimientos
    JPanel panelEmpleados = new JPanel(new BorderLayout());
    panelEmpleados.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Margen a ambos lados
    JLabel lblEmpleados = new JLabel("Empleados con menor cantidad de movimientos:", SwingConstants.CENTER);
    JTable tablaEmpleados = crearTablaEmpleadosMenosMovimientos();
    panelEmpleados.add(lblEmpleados, BorderLayout.NORTH);
    panelEmpleados.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);
    panelFila1.add(panelEmpleados);

    // Clientes con mayor cantidad de vehículos
    JPanel panelClientes = new JPanel(new BorderLayout());
    panelClientes.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Margen izquierdo
    JLabel lblClientes = new JLabel("Clientes con mayor cantidad de vehículos:", SwingConstants.CENTER);
    JTable tablaClientes = crearTablaClientesMasVehiculos();
    panelClientes.add(lblClientes, BorderLayout.NORTH);
    panelClientes.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
    panelFila1.add(panelClientes);

    // Panel inferior con estadías más largas
    JPanel panelFila2 = new JPanel(new BorderLayout());
    JLabel lblEstadias = new JLabel("Estadías más largas:", SwingConstants.CENTER);
    JTable tablaEstadias = crearTablaEstadiasMasLargas();
    panelFila2.add(lblEstadias, BorderLayout.NORTH);
    panelFila2.add(new JScrollPane(tablaEstadias), BorderLayout.CENTER);

    // Agregar ambos paneles al panel principal
    panel.add(panelFila1, BorderLayout.NORTH);
    panel.add(panelFila2, BorderLayout.CENTER);

    return panel;
  }

  private JTable crearTablaServiciosMasUtilizados() {
    ArrayList<Object[]> datos = controlador.obtenerServiciosMasUtilizados();
    String[] columnas = { "Tipo de Servicio", "Cantidad" };
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    for (Object[] fila : datos) {
      modelo.addRow(fila);
    }

    return new JTable(modelo);
  }

  private JTable crearTablaEstadiasMasLargas() {
    ArrayList<Object[]> datos = controlador.obtenerEstadiasMasLargas();
    String[] columnas = { "Vehículo", "Duración (min)" };
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    for (Object[] fila : datos) {
      modelo.addRow(fila);
    }

    return new JTable(modelo);
  }

  private JTable crearTablaEmpleadosMenosMovimientos() {
    ArrayList<Object[]> datos = controlador.obtenerEmpleadosMenosMovimientos();
    String[] columnas = { "Empleado (Nombre - Cédula)", "Movimientos" };
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    for (Object[] fila : datos) {
      modelo.addRow(fila);
    }

    return new JTable(modelo);
  }

  private JTable crearTablaClientesMasVehiculos() {
    ArrayList<Object[]> datos = controlador.obtenerClientesMasVehiculos();
    String[] columnas = { "Cliente", "Cantidad de Vehículos" };
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    for (Object[] fila : datos) {
      modelo.addRow(fila);
    }

    return new JTable(modelo);
  }
}
