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
    return "Entrada{" + "vehiculo=" + vehiculo + ", fecha=" + fecha +
        ", hora=" + hora + ", notas=" + notas +
        ", empleadoRecibe=" + empleadoRecibe +
        ", tieneContrato=" + tieneContrato + '}';
  }
}
