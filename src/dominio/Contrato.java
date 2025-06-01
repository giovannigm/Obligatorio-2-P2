import java.io.Serializable;

public class Contrato implements Serializable {
  private static int contadorContratos = 0;
  private int idContrato;
  private ClienteMensual cliente;
  private Vehiculo vehiculo;
  private Empleado empleado;
  private double valorMensual;

  // Constructor
  public Contrato(ClienteMensual cliente, Vehiculo vehiculo, Empleado empleado, double valorMensual) {
    this.idContrato = ++contadorContratos;
    this.cliente = cliente;
    this.vehiculo = vehiculo;
    this.empleado = empleado;
    this.valorMensual = valorMensual;
  }

  // Constructor vacío
  public Contrato() {
    this.idContrato = ++contadorContratos;
  }

  // Getters y Setters
  public int getIdContrato() {
    return idContrato;
  }

  public ClienteMensual getCliente() {
    return cliente;
  }

  public void setCliente(ClienteMensual cliente) {
    this.cliente = cliente;
  }

  public Vehiculo getVehiculo() {
    return vehiculo;
  }

  public void setVehiculo(Vehiculo vehiculo) {
    this.vehiculo = vehiculo;
  }

  public Empleado getEmpleado() {
    return empleado;
  }

  public void setEmpleado(Empleado empleado) {
    this.empleado = empleado;
  }

  public double getValorMensual() {
    return valorMensual;
  }

  public void setValorMensual(double valorMensual) {
    this.valorMensual = valorMensual;
  }

  @Override
  public String toString() {
    return "Contrato{" + "idContrato=" + idContrato + ", cliente=" + cliente +
        ", vehiculo=" + vehiculo + ", empleado=" + empleado +
        ", valorMensual=" + valorMensual + '}';
  }
}
