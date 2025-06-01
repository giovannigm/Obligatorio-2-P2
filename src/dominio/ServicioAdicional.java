package dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class ServicioAdicional implements Serializable {
  private String tipo;
  private LocalDate fecha;
  private LocalTime hora;
  private Vehiculo vehiculo;
  private Empleado empleado;
  private double costo;

  // Constructor
  public ServicioAdicional(String tipo, LocalDate fecha, LocalTime hora,
      Vehiculo vehiculo, Empleado empleado, double costo) {
    this.tipo = tipo;
    this.fecha = fecha;
    this.hora = hora;
    this.vehiculo = vehiculo;
    this.empleado = empleado;
    this.costo = costo;
  }

  // Constructor vacío
  public ServicioAdicional() {
  }

  // Getters y Setters
  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
  }

  public LocalTime getHora() {
    return hora;
  }

  public void setHora(LocalTime hora) {
    this.hora = hora;
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

  public double getCosto() {
    return costo;
  }

  public void setCosto(double costo) {
    this.costo = costo;
  }

  @Override
  public String toString() {
    return "ServicioAdicional{" + "tipo=" + tipo + ", fecha=" + fecha +
        ", hora=" + hora + ", vehiculo=" + vehiculo +
        ", empleado=" + empleado + ", costo=" + costo + '}';
  }
}