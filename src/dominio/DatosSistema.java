import java.io.Serializable;
import java.util.ArrayList;

public class DatosSistema implements Serializable {
  private static DatosSistema instancia;
  private ArrayList<ClienteMensual> clientes;
  private ArrayList<Vehiculo> vehiculos;
  private ArrayList<Empleado> empleados;
  private ArrayList<Contrato> contratos;
  private ArrayList<Entrada> entradas;
  private ArrayList<Salida> salidas;
  private ArrayList<ServicioAdicional> servicios;

  private DatosSistema() {
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

  public static DatosSistema getInstancia() {
    if (instancia == null) {
      instancia = new DatosSistema();
    }
    return instancia;
  }

  // Getters y Setters
  public ArrayList<ClienteMensual> getClientes() {
    return clientes;
  }

  public void setClientes(ArrayList<ClienteMensual> clientes) {
    this.clientes = clientes;
  }

  public ArrayList<Vehiculo> getVehiculos() {
    return vehiculos;
  }

  public void setVehiculos(ArrayList<Vehiculo> vehiculos) {
    this.vehiculos = vehiculos;
  }

  public ArrayList<Empleado> getEmpleados() {
    return empleados;
  }

  public void setEmpleados(ArrayList<Empleado> empleados) {
    this.empleados = empleados;
  }

  public ArrayList<Contrato> getContratos() {
    return contratos;
  }

  public void setContratos(ArrayList<Contrato> contratos) {
    this.contratos = contratos;
  }

  public ArrayList<Entrada> getEntradas() {
    return entradas;
  }

  public void setEntradas(ArrayList<Entrada> entradas) {
    this.entradas = entradas;
  }

  public ArrayList<Salida> getSalidas() {
    return salidas;
  }

  public void setSalidas(ArrayList<Salida> salidas) {
    this.salidas = salidas;
  }

  public ArrayList<ServicioAdicional> getServicios() {
    return servicios;
  }

  public void setServicios(ArrayList<ServicioAdicional> servicios) {
    this.servicios = servicios;
  }

  // Métodos para agregar elementos
  public void agregarCliente(ClienteMensual cliente) {
    this.clientes.add(cliente);
  }

  public void agregarVehiculo(Vehiculo vehiculo) {
    this.vehiculos.add(vehiculo);
  }

  public void agregarEmpleado(Empleado empleado) {
    this.empleados.add(empleado);
  }

  public void agregarContrato(Contrato contrato) {
    this.contratos.add(contrato);
  }

  public void agregarEntrada(Entrada entrada) {
    this.entradas.add(entrada);
  }

  public void agregarSalida(Salida salida) {
    this.salidas.add(salida);
  }

  public void agregarServicio(ServicioAdicional servicio) {
    this.servicios.add(servicio);
  }

  // Métodos para buscar elementos
  public ClienteMensual buscarCliente(String cedula) {
    for (ClienteMensual cliente : clientes) {
      if (cliente.esIgual(cedula)) {
        return cliente;
      }
    }
    return null;
  }

  public Vehiculo buscarVehiculo(String matricula) {
    for (Vehiculo vehiculo : vehiculos) {
      if (vehiculo.esIgual(matricula)) {
        return vehiculo;
      }
    }
    return null;
  }

  public Empleado buscarEmpleado(String cedula) {
    for (Empleado empleado : empleados) {
      if (empleado.esIgual(cedula)) {
        return empleado;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "DatosSistema{" + "clientes=" + clientes.size() +
        ", vehiculos=" + vehiculos.size() +
        ", empleados=" + empleados.size() +
        ", contratos=" + contratos.size() +
        ", entradas=" + entradas.size() +
        ", salidas=" + salidas.size() +
        ", servicios=" + servicios.size() + '}';
  }

  // Método para inicializar datos de ejemplo
  private void inicializarDatosEjemplo() {
    // Agregar clientes de ejemplo
    agregarCliente(new ClienteMensual("Juan Pérez", "12345678", "Av. 18 de Julio 1234", "099123456", 2020));
    agregarCliente(new ClienteMensual("María García", "87654321", "Bulevar Artigas 567", "098765432", 2021));
    agregarCliente(new ClienteMensual("Carlos López", "11223344", "Rambla República 890", "097112233", 2019));
    agregarCliente(new ClienteMensual("Ana Rodríguez", "55667788", "Av. Italia 2345", "096556677", 2022));
    agregarCliente(new ClienteMensual("Luis Martínez", "99887766", "Av. Garzón 678", "095998877", 2023));

    // Agregar vehículos de ejemplo
    agregarVehiculo(new Vehiculo("ABC1234", "Toyota", "Corolla", "Bueno"));
    agregarVehiculo(new Vehiculo("XYZ5678", "Honda", "Civic", "Bueno"));
    agregarVehiculo(new Vehiculo("DEF9012", "Ford", "Focus", "Bueno"));
    agregarVehiculo(new Vehiculo("GHI3456", "Chevrolet", "Cruze", "Bueno"));
    agregarVehiculo(new Vehiculo("JKL7890", "Volkswagen", "Golf", "Bueno"));
    agregarVehiculo(new Vehiculo("MNO1234", "Nissan", "Sentra", "Bueno"));
    agregarVehiculo(new Vehiculo("PQR5678", "Hyundai", "Elantra", "Bueno"));
    agregarVehiculo(new Vehiculo("STU9012", "Kia", "Rio", "Bueno"));

    // Agregar empleados de ejemplo
    agregarEmpleado(new Empleado("Roberto Silva", "11111111", "Av. Centenario 123", 1001));
    agregarEmpleado(new Empleado("Carmen Fernández", "22222222", "Bulevar España 456", 1002));
    agregarEmpleado(new Empleado("Miguel Torres", "33333333", "Av. Rivera 789", 1003));
    agregarEmpleado(new Empleado("Elena Morales", "44444444", "Rambla Wilson 012", 1004));
    agregarEmpleado(new Empleado("Diego Herrera", "55555555", "Av. Agraciada 345", 1005));
  }
}
