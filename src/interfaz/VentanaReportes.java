// Autores: Giovanni - 288127,  Nicolas - 258264

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.time.ZoneId;

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

  // Componentes de la pestaña Grilla de Movimientos
  private JSpinner spinnerFecha;
  private JPanel panelMovimientos;
  private JButton[][] botonesMovimientos;
  private LocalDate fechaSeleccionada;
  private JPanel panelTitulosColumnas;
  private static final int ALTURA_MAX_TABLA_ESTADISTICA = 250;

  public VentanaReportes(ControladorSistema controlador, boolean modoOscuro) {
    this.controlador = controlador;
    this.modoOscuro = modoOscuro;
    this.fechaSeleccionada = LocalDate.now();
    this.controlador.addReportesObserver(this);
    configurarVentana();
    crearComponentes();
    configurarEventos();
    aplicarEstilos();

    // Actualizar la grilla después de que todos los componentes estén creados
    if (panelMovimientos != null) {
      actualizarGrillaMovimientos();
    }
  }

  private void configurarVentana() {
    setTitle("Reportes del Sistema");
    setSize(900, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setResizable(true);
  }

  private void crearComponentes() {
    tabbedPane = new JTabbedPane();
    JPanel panelHistorial = crearPanelHistorialVehiculo();
    tabbedPane.addTab("Historial", panelHistorial);

    JPanel panelGrillaMovimientos = crearPanelGrillaMovimientos();
    tabbedPane.addTab("Grilla de movimientos", panelGrillaMovimientos);

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

    // Evento de cambio de fecha en la grilla de movimientos
    spinnerFecha.addChangeListener(new javax.swing.event.ChangeListener() {
      @Override
      public void stateChanged(javax.swing.event.ChangeEvent e) {
        actualizarGrillaMovimientos();
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

    // Aplicar estilos específicos a la grilla de movimientos
    if (panelMovimientos != null) {
      Estilos.EstilosReportes.aplicarEstilosReporte(panelMovimientos, modoOscuro);
    }

    if (spinnerFecha != null) {
      Estilos.EstilosReportes.aplicarEstilosReporte(spinnerFecha, modoOscuro);
    }
  }

  public void setModoOscuro(boolean modoOscuro) {
    this.modoOscuro = modoOscuro;
    aplicarEstilos();
  }

  @Override
  public void actualizarModoOscuro(boolean modoOscuro) {
    setModoOscuro(modoOscuro);
    aplicarEstilos();

    // Actualizar la grilla de movimientos si existe
    if (panelMovimientos != null) {
      actualizarGrillaMovimientos();
    }
  }

  @Override
  public void reportesActualizados() {
    // Actualizar tabla de historial
    actualizarTabla();

    // Actualizar grilla de movimientos
    if (panelMovimientos != null) {
      actualizarGrillaMovimientos();
    }
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
    JScrollPane scrollServicios = new JScrollPane(tablaServicios);
    scrollServicios
        .setPreferredSize(new Dimension(scrollServicios.getPreferredSize().width, ALTURA_MAX_TABLA_ESTADISTICA));
    panelServicios.add(lblServicios, BorderLayout.NORTH);
    panelServicios.add(scrollServicios, BorderLayout.CENTER);
    panelFila1.add(panelServicios);

    // Empleados con menor cantidad de movimientos
    JPanel panelEmpleados = new JPanel(new BorderLayout());
    panelEmpleados.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Margen a ambos lados
    JLabel lblEmpleados = new JLabel("Empleados con menor cantidad de movimientos:", SwingConstants.CENTER);
    JTable tablaEmpleados = crearTablaEmpleadosMenosMovimientos();
    JScrollPane scrollEmpleados = new JScrollPane(tablaEmpleados);
    scrollEmpleados
        .setPreferredSize(new Dimension(scrollEmpleados.getPreferredSize().width, ALTURA_MAX_TABLA_ESTADISTICA));
    panelEmpleados.add(lblEmpleados, BorderLayout.NORTH);
    panelEmpleados.add(scrollEmpleados, BorderLayout.CENTER);
    panelFila1.add(panelEmpleados);

    // Clientes con mayor cantidad de vehículos
    JPanel panelClientes = new JPanel(new BorderLayout());
    panelClientes.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Margen izquierdo
    JLabel lblClientes = new JLabel("Clientes con mayor cantidad de vehículos:", SwingConstants.CENTER);
    JTable tablaClientes = crearTablaClientesMasVehiculos();
    JScrollPane scrollClientes = new JScrollPane(tablaClientes);
    scrollClientes
        .setPreferredSize(new Dimension(scrollClientes.getPreferredSize().width, ALTURA_MAX_TABLA_ESTADISTICA));
    panelClientes.add(lblClientes, BorderLayout.NORTH);
    panelClientes.add(scrollClientes, BorderLayout.CENTER);
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

    int limite = Math.min(datos.size(), 5);
    for (int i = 0; i < limite; i++) {
      modelo.addRow(datos.get(i));
    }

    return new JTable(modelo);
  }

  private JTable crearTablaEmpleadosMenosMovimientos() {
    ArrayList<Object[]> datos = controlador.obtenerEmpleadosMenosMovimientos();
    String[] columnas = { "Empleado (Nombre - Cédula)", "Movimientos" };
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    int limite = Math.min(datos.size(), 5);
    for (int i = 0; i < limite; i++) {
      modelo.addRow(datos.get(i));
    }

    return new JTable(modelo);
  }

  private JTable crearTablaClientesMasVehiculos() {
    ArrayList<Object[]> datos = controlador.obtenerClientesMasVehiculos();
    String[] columnas = { "Cliente", "Cantidad de Vehículos" };
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    int limite = Math.min(datos.size(), 5);
    for (int i = 0; i < limite; i++) {
      modelo.addRow(datos.get(i));
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

  private JPanel crearPanelGrillaMovimientos() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Panel superior con selector de fecha
    JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    JLabel lblFecha = new JLabel("Fecha base:");

    // Crear spinner para la fecha
    SpinnerDateModel model = new SpinnerDateModel();
    spinnerFecha = new JSpinner(model);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy");
    spinnerFecha.setEditor(editor);
    // Inicializar con la fecha seleccionada
    Date initialDate = Date.from(fechaSeleccionada.atStartOfDay(ZoneId.systemDefault()).toInstant());
    spinnerFecha.setValue(initialDate);

    panelSuperior.add(lblFecha);
    panelSuperior.add(spinnerFecha);

    // Panel central con la grilla de movimientos (con títulos)
    JPanel panelGrillaCompleta = new JPanel(new BorderLayout(5, 5));
    panelGrillaCompleta.setBorder(BorderFactory.createTitledBorder("Movimientos por bloque horario"));

    // Panel de títulos de columnas (fechas)
    panelTitulosColumnas = new JPanel(new GridLayout(1, 3, 5, 5));
    panelTitulosColumnas.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));

    // Panel de títulos de filas (rangos horarios) y grilla
    JPanel panelFilaTitulos = new JPanel(new BorderLayout(5, 5));

    // Panel de títulos de filas
    JPanel panelTitulosFilas = new JPanel(new GridLayout(4, 1, 5, 5));
    String[] rangosHorarios = { "0:00-5:59", "6:00-11:59", "12:00-17:59", "18:00-23:59" };
    for (String rango : rangosHorarios) {
      JLabel lblRango = new JLabel(rango, SwingConstants.RIGHT);
      lblRango.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
      panelTitulosFilas.add(lblRango);
    }

    // Panel central con la grilla de movimientos
    panelMovimientos = new JPanel(new GridLayout(4, 3, 5, 5));

    // Inicializar array de botones
    botonesMovimientos = new JButton[4][3];

    // Crear botones dinámicamente
    for (int fila = 0; fila < 4; fila++) {
      for (int columna = 0; columna < 3; columna++) {
        JButton nuevo = new JButton(" ");
        nuevo.setMargin(new Insets(-5, -5, -5, -5));
        nuevo.setBackground(Color.BLACK);
        nuevo.setForeground(Color.WHITE);
        nuevo.setText("texto"); // completar luego
        nuevo.addActionListener(new MovListener(fila, columna));
        panelMovimientos.add(nuevo);
        botonesMovimientos[fila][columna] = nuevo;
      }
    }

    // Agregar componentes al panel de fila de títulos
    panelFilaTitulos.add(panelTitulosFilas, BorderLayout.WEST);
    panelFilaTitulos.add(panelMovimientos, BorderLayout.CENTER);

    // Agregar todo al panel de grilla completa
    panelGrillaCompleta.add(panelTitulosColumnas, BorderLayout.NORTH);
    panelGrillaCompleta.add(panelFilaTitulos, BorderLayout.CENTER);

    // Panel inferior con leyenda
    JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    JLabel lblLeyenda = new JLabel("Leyenda: Verde (< 5) | Amarillo (5-8) | Rojo (> 8)");
    panelLeyenda.add(lblLeyenda);

    panel.add(panelSuperior, BorderLayout.NORTH);
    panel.add(panelGrillaCompleta, BorderLayout.CENTER);
    panel.add(panelLeyenda, BorderLayout.SOUTH);

    return panel;
  }

  private void actualizarGrillaMovimientos() {
    // Obtener la fecha seleccionada del spinner
    Date fechaSpinner = (Date) spinnerFecha.getValue();
    fechaSeleccionada = fechaSpinner.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    // Actualizar títulos de columnas (fechas)
    actualizarTitulosColumnas();

    // Actualizar cada botón de la grilla
    for (int fila = 0; fila < 4; fila++) {
      for (int columna = 0; columna < 3; columna++) {
        LocalDate fechaBloque = fechaSeleccionada.plusDays(columna);
        int cantidadMovimientos = contarMovimientosEnBloque(fechaBloque, fila);

        JButton boton = botonesMovimientos[fila][columna];
        boton.setText(cantidadMovimientos + " movs");

        // Aplicar color según la cantidad
        if (cantidadMovimientos < 5) {
          boton.setBackground(Color.GREEN);
          boton.setForeground(Color.WHITE);
        } else if (cantidadMovimientos <= 8) {
          boton.setBackground(Color.YELLOW);
          boton.setForeground(Color.BLACK);
        } else {
          boton.setBackground(Color.RED);
          boton.setForeground(Color.WHITE);
        }
      }
    }
  }

  private void actualizarTitulosColumnas() {
    // Limpiar y agregar nuevos títulos
    panelTitulosColumnas.removeAll();

    String[] nombresDias = { "Hoy", "Mañana", "Pasado mañana" };
    for (int i = 0; i < 3; i++) {
      LocalDate fechaColumna = fechaSeleccionada.plusDays(i);
      String tituloColumna = nombresDias[i] + "\n" + fechaColumna.format(DateTimeFormatter.ofPattern("dd/MM"));

      JLabel lblFecha = new JLabel(tituloColumna, SwingConstants.CENTER);
      lblFecha.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      panelTitulosColumnas.add(lblFecha);
    }

    // Refrescar el panel
    panelTitulosColumnas.revalidate();
    panelTitulosColumnas.repaint();
  }

  private int contarMovimientosEnBloque(LocalDate fecha, int bloqueHorario) {
    // Arrays para rangos horarios (más simple que switch)
    LocalTime[] horasInicio = {
        LocalTime.of(0, 0), // 0:00-5:59
        LocalTime.of(6, 0), // 6:00-11:59
        LocalTime.of(12, 0), // 12:00-17:59
        LocalTime.of(18, 0) // 18:00-23:59
    };

    LocalTime[] horasFin = {
        LocalTime.of(5, 59), // 0:00-5:59
        LocalTime.of(11, 59), // 6:00-11:59
        LocalTime.of(17, 59), // 12:00-17:59
        LocalTime.of(23, 59) // 18:00-23:59
    };

    if (bloqueHorario < 0 || bloqueHorario >= 4) {
      return 0;
    }

    LocalTime horaInicio = horasInicio[bloqueHorario];
    LocalTime horaFin = horasFin[bloqueHorario];
    int contador = 0;

    // Contar entradas
    for (Entrada entrada : controlador.getEntradas()) {
      if (entrada.getFecha().equals(fecha) &&
          !entrada.getHora().isBefore(horaInicio) &&
          !entrada.getHora().isAfter(horaFin)) {
        contador++;
      }
    }

    // Contar salidas
    for (Salida salida : controlador.getSalidas()) {
      if (salida.getFecha().equals(fecha) &&
          !salida.getHora().isBefore(horaInicio) &&
          !salida.getHora().isAfter(horaFin)) {
        contador++;
      }
    }

    // Contar servicios adicionales
    for (ServicioAdicional servicio : controlador.getServiciosAdicionales()) {
      if (servicio.getFecha().equals(fecha) &&
          !servicio.getHora().isBefore(horaInicio) &&
          !servicio.getHora().isAfter(horaFin)) {
        contador++;
      }
    }

    return contador;
  }

  private String obtenerMovimientosEnBloque(LocalDate fecha, int bloqueHorario) {
    // Arrays para rangos horarios
    LocalTime[] horasInicio = {
        LocalTime.of(0, 0), // 0:00-5:59
        LocalTime.of(6, 0), // 6:00-11:59
        LocalTime.of(12, 0), // 12:00-17:59
        LocalTime.of(18, 0) // 18:00-23:59
    };

    LocalTime[] horasFin = {
        LocalTime.of(5, 59), // 0:00-5:59
        LocalTime.of(11, 59), // 6:00-11:59
        LocalTime.of(17, 59), // 12:00-17:59
        LocalTime.of(23, 59) // 18:00-23:59
    };

    if (bloqueHorario < 0 || bloqueHorario >= 4) {
      return "No hay movimientos registrados en este bloque horario.";
    }

    LocalTime horaInicio = horasInicio[bloqueHorario];
    LocalTime horaFin = horasFin[bloqueHorario];

    ArrayList<String> movimientos = new ArrayList<>();

    // Obtener entradas
    for (Entrada entrada : controlador.getEntradas()) {
      if (entrada.getFecha().equals(fecha) &&
          !entrada.getHora().isBefore(horaInicio) &&
          !entrada.getHora().isAfter(horaFin)) {
        String movimiento = String.format("%s %s - %s - Entrada",
            entrada.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            entrada.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
            entrada.getVehiculo().getMatricula());
        movimientos.add(movimiento);
      }
    }

    // Obtener salidas
    for (Salida salida : controlador.getSalidas()) {
      if (salida.getFecha().equals(fecha) &&
          !salida.getHora().isBefore(horaInicio) &&
          !salida.getHora().isAfter(horaFin)) {
        String movimiento = String.format("%s %s - %s - Salida",
            salida.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            salida.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
            salida.getEntrada().getVehiculo().getMatricula());
        movimientos.add(movimiento);
      }
    }

    // Obtener servicios adicionales
    for (ServicioAdicional servicio : controlador.getServiciosAdicionales()) {
      if (servicio.getFecha().equals(fecha) &&
          !servicio.getHora().isBefore(horaInicio) &&
          !servicio.getHora().isAfter(horaFin)) {
        String movimiento = String.format("%s %s - %s - Servicio: %s",
            servicio.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            servicio.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
            servicio.getVehiculo().getMatricula(),
            servicio.getTipo());
        movimientos.add(movimiento);
      }
    }

    // Ordenar movimientos (orden simple por fecha/hora)
    movimientos.sort((m1, m2) -> m1.compareTo(m2));

    // Construir resultado
    if (movimientos.isEmpty()) {
      return "No hay movimientos registrados en este bloque horario.";
    }

    StringBuilder resultado = new StringBuilder("Movimientos registrados:\n\n");
    for (String movimiento : movimientos) {
      resultado.append(movimiento).append("\n");
    }

    return resultado.toString();
  }

  private class MovListener implements ActionListener {
    private int fila;
    private int columna;

    public MovListener(int fila, int columna) {
      this.fila = fila;
      this.columna = columna;
    }

    public void actionPerformed(ActionEvent e) {
      // Calcular la fecha del bloque
      LocalDate fechaBloque = fechaSeleccionada.plusDays(columna);

      // Obtener movimientos del bloque
      String movimientos = obtenerMovimientosEnBloque(fechaBloque, fila);

      // Mostrar ventana emergente (más simple con JOptionPane)
      String[] rangosHorarios = { "0:00-5:59", "6:00-11:59", "12:00-17:59", "18:00-23:59" };
      String rangoHorario = rangosHorarios[fila];
      String titulo = "Movimientos - " + fechaBloque.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " "
          + rangoHorario;

      JTextArea areaTexto = new JTextArea(movimientos);
      areaTexto.setEditable(false);
      areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));

      JScrollPane scrollPane = new JScrollPane(areaTexto);
      scrollPane.setPreferredSize(new Dimension(500, 300));

      JOptionPane.showMessageDialog(VentanaReportes.this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
  }
}
