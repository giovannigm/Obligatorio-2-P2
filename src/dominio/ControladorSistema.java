import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class ControladorSistema implements Serializable {
  private ArrayList<ClienteMensual> clientes;
  private ArrayList<Vehiculo> vehiculos;
  private ArrayList<Empleado> empleados;
  private ArrayList<Contrato> contratos;
  private ArrayList<Entrada> entradas;
  private ArrayList<Salida> salidas;
  private ArrayList<ServicioAdicional> servicios;

  public ControladorSistema() {
    this.clientes = new ArrayList<>();
    this.vehiculos = new ArrayList<>();
    this.empleados = new ArrayList<>();
    this.contratos = new ArrayList<>();
    this.entradas = new ArrayList<>();
    this.salidas = new ArrayList<>();
    this.servicios = new ArrayList<>();

    // Inicializar datos de ejemplo
    inicializarDatosEjemplo();
  }

  // ========== MÉTODOS DE VALIDACIÓN ==========

  public boolean validarClienteUnico(String cedula) {
    return buscarCliente(cedula) == null;
  }

  public boolean validarVehiculoUnico(String matricula) {
    return buscarVehiculo(matricula) == null;
  }

  public boolean validarEmpleadoUnico(String cedula) {
    return buscarEmpleado(cedula) == null;
  }

  public boolean validarCamposCliente(String nombre, String cedula, String direccion, String celular, String anioAlta) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre es obligatorio");
    }
    if (cedula == null || cedula.trim().isEmpty()) {
      throw new IllegalArgumentException("La cédula es obligatoria");
    }
    if (direccion == null || direccion.trim().isEmpty()) {
      throw new IllegalArgumentException("La dirección es obligatoria");
    }
    if (celular == null || celular.trim().isEmpty()) {
      throw new IllegalArgumentException("El celular es obligatorio");
    }
    if (anioAlta == null || anioAlta.trim().isEmpty()) {
      throw new IllegalArgumentException("El año de alta es obligatorio");
    }

    try {
      int anio = Integer.parseInt(anioAlta.trim());
      if (anio < 1900 || anio > 2100) {
        throw new IllegalArgumentException("El año de alta debe estar entre 1900 y 2100");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El año de alta debe ser un número válido");
    }

    return true;
  }

  public boolean validarCamposVehiculo(String matricula, String marca, String modelo, String estado) {
    if (matricula == null || matricula.trim().isEmpty()) {
      throw new IllegalArgumentException("La matrícula es obligatoria");
    }
    if (marca == null || marca.trim().isEmpty()) {
      throw new IllegalArgumentException("La marca es obligatoria");
    }
    if (modelo == null || modelo.trim().isEmpty()) {
      throw new IllegalArgumentException("El modelo es obligatorio");
    }
    if (estado == null || estado.trim().isEmpty()) {
      throw new IllegalArgumentException("El estado es obligatorio");
    }

    return true;
  }

  public boolean validarCamposEmpleado(String nombre, String cedula, String direccion) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre es obligatorio");
    }
    if (cedula == null || cedula.trim().isEmpty()) {
      throw new IllegalArgumentException("La cédula es obligatoria");
    }
    if (direccion == null || direccion.trim().isEmpty()) {
      throw new IllegalArgumentException("La dirección es obligatoria");
    }

    return true;
  }

  public boolean validarCamposContrato(ClienteMensual cliente, Vehiculo vehiculo, Empleado empleado,
      String valorMensual) {
    if (cliente == null) {
      throw new IllegalArgumentException("Debe seleccionar un cliente");
    }
    if (vehiculo == null) {
      throw new IllegalArgumentException("Debe seleccionar un vehículo");
    }
    if (empleado == null) {
      throw new IllegalArgumentException("Debe seleccionar un empleado");
    }
    if (valorMensual == null || valorMensual.trim().isEmpty()) {
      throw new IllegalArgumentException("El valor mensual es obligatorio");
    }

    try {
      double valor = Double.parseDouble(valorMensual.trim());
      if (valor <= 0) {
        throw new IllegalArgumentException("El valor mensual debe ser mayor a 0");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El valor mensual debe ser un número válido");
    }

    // Verificar si el vehículo ya tiene un contrato activo
    for (Contrato contrato : contratos) {
      if (contrato.getVehiculo().equals(vehiculo)) {
        throw new IllegalArgumentException("El vehículo ya tiene un contrato activo");
      }
    }

    return true;
  }

  public boolean validarCamposServicioAdicional(String tipo, String fecha, String hora, Vehiculo vehiculo,
      Empleado empleado, String costo) {
    if (tipo == null || tipo.trim().isEmpty()) {
      throw new IllegalArgumentException("Debe seleccionar un tipo de servicio");
    }
    if (fecha == null || fecha.trim().isEmpty()) {
      throw new IllegalArgumentException("La fecha es obligatoria");
    }
    if (hora == null || hora.trim().isEmpty()) {
      throw new IllegalArgumentException("La hora es obligatoria");
    }
    if (vehiculo == null) {
      throw new IllegalArgumentException("Debe seleccionar un vehículo");
    }
    if (empleado == null) {
      throw new IllegalArgumentException("Debe seleccionar un empleado");
    }
    if (costo == null || costo.trim().isEmpty()) {
      throw new IllegalArgumentException("El costo es obligatorio");
    }

    // Validar formato de fecha (dd/MM/yyyy)
    try {
      String[] partesFecha = fecha.trim().split("/");
      if (partesFecha.length != 3) {
        throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy");
      }
      int dia = Integer.parseInt(partesFecha[0]);
      int mes = Integer.parseInt(partesFecha[1]);
      int anio = Integer.parseInt(partesFecha[2]);

      if (dia < 1 || dia > 31 || mes < 1 || mes > 12 || anio < 1900 || anio > 2100) {
        throw new IllegalArgumentException("Fecha inválida");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy");
    }

    // Validar formato de hora (HH:mm)
    try {
      String[] partesHora = hora.trim().split(":");
      if (partesHora.length != 2) {
        throw new IllegalArgumentException("Formato de hora inválido. Use HH:mm");
      }
      int horas = Integer.parseInt(partesHora[0]);
      int minutos = Integer.parseInt(partesHora[1]);

      if (horas < 0 || horas > 23 || minutos < 0 || minutos > 59) {
        throw new IllegalArgumentException("Hora inválida");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Formato de hora inválido. Use HH:mm");
    }

    // Validar costo
    try {
      double valorCosto = Double.parseDouble(costo.trim());
      if (valorCosto < 0) {
        throw new IllegalArgumentException("El costo no puede ser negativo");
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El costo debe ser un número válido");
    }

    return true;
  }

  // ========== MÉTODOS DE GESTIÓN DE CLIENTES ==========

  public void agregarCliente(String nombre, String cedula, String direccion, String celular, String anioAlta) {
    // Validar campos
    validarCamposCliente(nombre, cedula, direccion, celular, anioAlta);

    // Validar unicidad
    if (!validarClienteUnico(cedula)) {
      throw new IllegalArgumentException("Ya existe un cliente con esa cédula");
    }

    // Crear y agregar cliente
    int anio = Integer.parseInt(anioAlta.trim());
    ClienteMensual nuevoCliente = new ClienteMensual(nombre.trim(), cedula.trim(),
        direccion.trim(), celular.trim(), anio);
    clientes.add(nuevoCliente);
  }

  public void eliminarCliente(String cedula) {
    ClienteMensual cliente = buscarCliente(cedula);
    if (cliente == null) {
      throw new IllegalArgumentException("No se encontró el cliente con cédula: " + cedula);
    }

    // Eliminar contratos asociados al cliente
    contratos.removeIf(contrato -> contrato.getCliente().equals(cliente));

    // Eliminar el cliente
    clientes.remove(cliente);
  }

  public ClienteMensual buscarCliente(String cedula) {
    for (ClienteMensual cliente : clientes) {
      if (cliente.esIgual(cedula)) {
        return cliente;
      }
    }
    return null;
  }

  public ArrayList<ClienteMensual> getClientes() {
    return new ArrayList<>(clientes);
  }

  // ========== MÉTODOS DE GESTIÓN DE VEHÍCULOS ==========

  public void agregarVehiculo(String matricula, String marca, String modelo, String estado) {
    // Validar campos
    validarCamposVehiculo(matricula, marca, modelo, estado);

    // Validar unicidad
    if (!validarVehiculoUnico(matricula)) {
      throw new IllegalArgumentException("Ya existe un vehículo con esa matrícula");
    }

    // Crear y agregar vehículo
    Vehiculo nuevoVehiculo = new Vehiculo(matricula.trim(), marca.trim(),
        modelo.trim(), estado.trim());
    vehiculos.add(nuevoVehiculo);
  }

  public void eliminarVehiculo(String matricula) {
    Vehiculo vehiculo = buscarVehiculo(matricula);
    if (vehiculo == null) {
      throw new IllegalArgumentException("No se encontró el vehículo con matrícula: " + matricula);
    }

    // Verificar si el vehículo tiene contratos activos
    for (Contrato contrato : contratos) {
      if (contrato.getVehiculo().equals(vehiculo)) {
        throw new IllegalArgumentException("No se puede eliminar un vehículo con contratos activos");
      }
    }

    // Eliminar el vehículo
    vehiculos.remove(vehiculo);
  }

  public Vehiculo buscarVehiculo(String matricula) {
    for (Vehiculo vehiculo : vehiculos) {
      if (vehiculo.esIgual(matricula)) {
        return vehiculo;
      }
    }
    return null;
  }

  public ArrayList<Vehiculo> getVehiculos() {
    return new ArrayList<>(vehiculos);
  }

  // ========== MÉTODOS DE GESTIÓN DE EMPLEADOS ==========

  public void agregarEmpleado(String nombre, String cedula, String direccion) {
    // Validar campos
    validarCamposEmpleado(nombre, cedula, direccion);

    // Validar unicidad
    if (!validarEmpleadoUnico(cedula)) {
      throw new IllegalArgumentException("Ya existe un empleado con esa cédula");
    }

    int numero = empleados.size() + 1;

    Empleado nuevoEmpleado = new Empleado(nombre.trim(), cedula.trim(), direccion.trim(), numero);
    empleados.add(nuevoEmpleado);
  }

  public void eliminarEmpleado(String cedula) {
    Empleado empleado = buscarEmpleado(cedula);
    if (empleado == null) {
      throw new IllegalArgumentException("No se encontró el empleado con cédula: " + cedula);
    }

    // Verificar si el empleado tiene contratos activos
    for (Contrato contrato : contratos) {
      if (contrato.getEmpleado().equals(empleado)) {
        throw new IllegalArgumentException("No se puede eliminar un empleado con contratos activos");
      }
    }

    // Eliminar el empleado
    empleados.remove(empleado);
  }

  public Empleado buscarEmpleado(String cedula) {
    for (Empleado empleado : empleados) {
      if (empleado.esIgual(cedula)) {
        return empleado;
      }
    }
    return null;
  }

  public ArrayList<Empleado> getEmpleados() {
    return new ArrayList<>(empleados);
  }

  // ========== MÉTODOS DE GESTIÓN DE CONTRATOS ==========

  public void agregarContrato(ClienteMensual cliente, Vehiculo vehiculo, Empleado empleado, String valorMensual) {
    // Validar campos
    validarCamposContrato(cliente, vehiculo, empleado, valorMensual);

    // Crear y agregar contrato
    double valor = Double.parseDouble(valorMensual.trim());
    Contrato nuevoContrato = new Contrato(cliente, vehiculo, empleado, valor);
    contratos.add(nuevoContrato);
  }

  public ArrayList<Contrato> getContratos() {
    return new ArrayList<>(contratos);
  }

  public Contrato buscarContrato(int idContrato) {
    for (Contrato contrato : contratos) {
      if (contrato.getIdContrato() == idContrato) {
        return contrato;
      }
    }
    return null;
  }

  // ========== MÉTODOS DE GESTIÓN DE SERVICIOS ADICIONALES ==========

  public void agregarServicioAdicional(String tipo, String fecha, String hora, Vehiculo vehiculo, Empleado empleado,
      String costo) {
    // Validar campos
    validarCamposServicioAdicional(tipo, fecha, hora, vehiculo, empleado, costo);

    // Parsear fecha y hora
    String[] partesFecha = fecha.trim().split("/");
    int dia = Integer.parseInt(partesFecha[0]);
    int mes = Integer.parseInt(partesFecha[1]);
    int anio = Integer.parseInt(partesFecha[2]);

    String[] partesHora = hora.trim().split(":");
    int horas = Integer.parseInt(partesHora[0]);
    int minutos = Integer.parseInt(partesHora[1]);

    // Crear LocalDate y LocalTime
    LocalDate fechaServicio = LocalDate.of(anio, mes, dia);
    LocalTime horaServicio = LocalTime.of(horas, minutos);

    // Crear y agregar servicio
    double valorCosto = Double.parseDouble(costo.trim());
    ServicioAdicional nuevoServicio = new ServicioAdicional(tipo.trim(), fechaServicio, horaServicio, vehiculo,
        empleado, valorCosto);
    servicios.add(nuevoServicio);
  }

  public ArrayList<ServicioAdicional> getServiciosAdicionales() {
    return new ArrayList<>(servicios);
  }

  // ========== MÉTODO DE INICIALIZACIÓN ==========

  private void inicializarDatosEjemplo() {
    // Agregar clientes de ejemplo
    try {
      agregarCliente("Juan Pérez", "12345678", "Av. 18 de Julio 1234", "099123456", "2020");
      agregarCliente("María García", "87654321", "Bulevar Artigas 567", "098765432", "2021");
      agregarCliente("Carlos López", "11223344", "Rambla República 890", "097112233", "2019");
      agregarCliente("Ana Rodríguez", "55667788", "Av. Italia 2345", "096556677", "2022");
      agregarCliente("Luis Martínez", "99887766", "Av. Garzón 678", "095998877", "2023");
    } catch (Exception e) {
      // Ignorar errores en datos de ejemplo
    }

    // Agregar vehículos de ejemplo
    try {
      agregarVehiculo("ABC1234", "Toyota", "Corolla", "Bueno");
      agregarVehiculo("XYZ5678", "Honda", "Civic", "Bueno");
      agregarVehiculo("DEF9012", "Ford", "Focus", "Bueno");
      agregarVehiculo("GHI3456", "Chevrolet", "Cruze", "Bueno");
      agregarVehiculo("JKL7890", "Volkswagen", "Golf", "Bueno");
      agregarVehiculo("MNO1234", "Nissan", "Sentra", "Bueno");
      agregarVehiculo("PQR5678", "Hyundai", "Elantra", "Bueno");
      agregarVehiculo("STU9012", "Kia", "Rio", "Bueno");
    } catch (Exception e) {
      // Ignorar errores en datos de ejemplo
    }

    // Agregar empleados de ejemplo
    try {
      agregarEmpleado("Roberto Silva", "11111111", "Av. Centenario 123");
      agregarEmpleado("Carmen Fernández", "22222222", "Bulevar España 456");
      agregarEmpleado("Miguel Torres", "33333333", "Av. Rivera 789");
      agregarEmpleado("Elena Morales", "44444444", "Rambla Wilson 012");
      agregarEmpleado("Diego Herrera", "55555555", "Av. Agraciada 345");
    } catch (Exception e) {
      // Ignorar errores en datos de ejemplo
    }
  }

  // ========== MÉTODOS PARA OTRAS ENTIDADES ==========

  public void agregarEntrada(Entrada entrada) {
    entradas.add(entrada);
  }

  public void agregarSalida(Salida salida) {
    salidas.add(salida);
  }

  public ArrayList<Entrada> getEntradas() {
    return new ArrayList<>(entradas);
  }

  public ArrayList<Salida> getSalidas() {
    return new ArrayList<>(salidas);
  }

  // ========== MÉTODOS DE PERSISTENCIA DE DATOS ==========

  /**
   * Guarda los datos del sistema en un archivo serializado
   * 
   * @param controlador La instancia del controlador a guardar
   * @throws IOException si ocurre un error al guardar los datos
   */
  public static void guardarDatos(ControladorSistema controlador) throws IOException {
    File archivo = new File("DATOS.ser");

    try (FileOutputStream fileOut = new FileOutputStream(archivo);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

      // Guardar la instancia del controlador
      objectOut.writeObject(controlador);

      System.out.println("Datos guardados exitosamente en: " + archivo.getAbsolutePath());

    } catch (IOException e) {
      System.err.println("Error al guardar los datos: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Recupera los datos del sistema desde un archivo serializado
   * 
   * @return La instancia del controlador recuperada, o una nueva instancia si no
   *         existe el archivo
   * @throws IOException            si ocurre un error al leer el archivo
   * @throws ClassNotFoundException si hay problemas de compatibilidad de clases
   */
  public static ControladorSistema recuperarDatos() throws IOException, ClassNotFoundException {
    File archivo = new File("DATOS.ser");

    // Verificar si el archivo existe
    if (!archivo.exists()) {
      System.out.println("No se encontró el archivo DATOS.ser. Se creará una nueva instancia vacía.");
      return new ControladorSistema();
    }

    try (FileInputStream fileIn = new FileInputStream(archivo);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

      // Leer la instancia del controlador
      ControladorSistema controladorRecuperado = (ControladorSistema) objectIn.readObject();

      // Verificar que la instancia no sea null
      if (controladorRecuperado == null) {
        throw new IOException("El archivo contiene datos nulos");
      }

      System.out.println("Datos recuperados exitosamente desde: " + archivo.getAbsolutePath());

      return controladorRecuperado;

    } catch (IOException e) {
      System.err.println("Error al leer el archivo: " + e.getMessage());
      e.printStackTrace();
      throw e;

    } catch (ClassNotFoundException e) {
      System.err.println("Error de compatibilidad de clases: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
