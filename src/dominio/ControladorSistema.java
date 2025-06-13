import java.util.ArrayList;

public class ControladorSistema {
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

  // ========== MÉTODOS PARA OTRAS ENTIDADES ==========

  public void agregarEntrada(Entrada entrada) {
    entradas.add(entrada);
  }

  public void agregarSalida(Salida salida) {
    salidas.add(salida);
  }

  public void agregarServicio(ServicioAdicional servicio) {
    servicios.add(servicio);
  }

  public ArrayList<Entrada> getEntradas() {
    return new ArrayList<>(entradas);
  }

  public ArrayList<Salida> getSalidas() {
    return new ArrayList<>(salidas);
  }

  public ArrayList<ServicioAdicional> getServicios() {
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
}
