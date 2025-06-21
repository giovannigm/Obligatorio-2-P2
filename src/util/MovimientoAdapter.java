// Autores: Giovanni - 288127,  Nicolas - 258264

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 import dominio.Entrada;
 import dominio.Salida;
 import dominio.ServicioAdicional;
 
  - Esto da Error si esta descomentado.
*/

/**
 * Clase adaptadora que unifica los diferentes tipos de movimientos
 * (Entrada, Salida, ServicioAdicional) para facilitar su visualización
 * en la tabla de historial por vehículo.
 */
public class MovimientoAdapter {
  private String tipo;
  private LocalDateTime fechaHora;
  private String empleado;
  private String detalles;
  private String costoDuracion;

  // Constructor para Entrada
  public MovimientoAdapter(Entrada entrada) {
    this.tipo = "Entrada";
    this.fechaHora = LocalDateTime.of(entrada.getFecha(), entrada.getHora());
    this.empleado = entrada.getEmpleadoRecibe().getNombre();
    this.detalles = entrada.getNotas() != null ? entrada.getNotas() : "";
    this.costoDuracion = entrada.isTieneContrato() ? "Con contrato" : "Sin contrato";
  }

  // Constructor para Salida
  public MovimientoAdapter(Salida salida) {
    this.tipo = "Salida";
    this.fechaHora = LocalDateTime.of(salida.getFecha(), salida.getHora());
    this.empleado = salida.getEmpleadoEntrega().getNombre();
    this.detalles = salida.getComentarioEstado() != null ? salida.getComentarioEstado() : "";

    // Calcular duración si es posible
    if (salida.getEntrada() != null) {
      LocalDateTime entradaHora = LocalDateTime.of(salida.getEntrada().getFecha(), salida.getEntrada().getHora());
      long duracionHoras = java.time.Duration.between(entradaHora, this.fechaHora).toHours();
      long duracionMinutos = java.time.Duration.between(entradaHora, this.fechaHora).toMinutes() % 60;
      this.costoDuracion = duracionHoras + "h " + duracionMinutos + "m";
    } else {
      this.costoDuracion = "N/A";
    }
  }

  // Constructor para ServicioAdicional
  public MovimientoAdapter(ServicioAdicional servicio) {
    this.tipo = "Servicio Adicional";
    this.fechaHora = LocalDateTime.of(servicio.getFecha(), servicio.getHora());
    this.empleado = servicio.getEmpleado().getNombre();
    this.detalles = servicio.getTipo();
    this.costoDuracion = "$" + String.format("%.2f", servicio.getCosto());
  }

  // Getters
  public String getTipo() {
    return tipo;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public String getFecha() {
    return fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  public String getHora() {
    return fechaHora.format(DateTimeFormatter.ofPattern("HH:mm"));
  }

  public String getEmpleado() {
    return empleado;
  }

  public String getDetalles() {
    return detalles;
  }

  public String getCostoDuracion() {
    return costoDuracion;
  }

  @Override
  public String toString() {
    return tipo + " - " + getFecha() + " " + getHora() + " - " + empleado;
  }
}
