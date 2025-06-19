import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Entrada implements Serializable {
  private Vehiculo vehiculo;
  private LocalDate fecha;
  private LocalTime hora;
  private String notas;
  private Empleado empleadoRecibe;
  private boolean tieneContrato;

  // Constructor
  public Entrada(Vehiculo vehiculo, LocalDate fecha, LocalTime hora, String notas,
      Empleado empleadoRecibe, boolean tieneContrato) {
    this.vehiculo = vehiculo;
    this.fecha = fecha;
    this.hora = hora;
    this.notas = notas;
    this.empleadoRecibe = empleadoRecibe;
    this.tieneContrato = tieneContrato;
  }

  // Constructor vacío
  public Entrada() {
  }

  // Getters y Setters
  public Vehiculo getVehiculo() {
    return vehiculo;
  }

  public void setVehiculo(Vehiculo vehiculo) {
    this.vehiculo = vehiculo;
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

  public String getNotas() {
    return notas;
  }

  public void setNotas(String notas) {
    this.notas = notas;
  }

  public Empleado getEmpleadoRecibe() {
    return empleadoRecibe;
  }

  public void setEmpleadoRecibe(Empleado empleadoRecibe) {
    this.empleadoRecibe = empleadoRecibe;
  }

  public boolean isTieneContrato() {
    return tieneContrato;
  }

  public void setTieneContrato(boolean tieneContrato) {
    this.tieneContrato = tieneContrato;
  }

  @Override
  public String toString() {
    if (vehiculo != null) {
      return vehiculo.getMatricula() + " - " + vehiculo.getModelo();
    } else {
      return "Sin vehículo";
    }
  }

  public boolean esIgualA(Entrada otra) {
    if (this == otra) return true;
    if (otra == null) return false;
    // Compara los campos relevantes para considerar dos entradas como iguales
    return (vehiculo != null && vehiculo.equals(otra.vehiculo)) &&
           (fecha != null && fecha.equals(otra.fecha)) &&
           (hora != null && hora.equals(otra.hora));
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Entrada otra) {
      return this.esIgualA(otra);
    }
    return false;
  }

}
