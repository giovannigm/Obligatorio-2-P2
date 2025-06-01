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
}
