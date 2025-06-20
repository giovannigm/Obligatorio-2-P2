// Autores: Giovanni - 288127,  Nicolas - 258264

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Salida implements Serializable {
  private Entrada entrada;
  private Empleado empleadoEntrega;
  private LocalDate fecha;
  private LocalTime hora;
  private String comentarioEstado;

  // Constructor
  public Salida(Entrada entrada, Empleado empleadoEntrega, LocalDate fecha,
      LocalTime hora, String comentarioEstado) {
    this.entrada = entrada;
    this.empleadoEntrega = empleadoEntrega;
    this.fecha = fecha;
    this.hora = hora;
    this.comentarioEstado = comentarioEstado;
  }

  // Constructor vacío
  public Salida() {
  }

  // Getters y Setters
  public Entrada getEntrada() {
    return entrada;
  }

  public void setEntrada(Entrada entrada) {
    this.entrada = entrada;
  }

  public Empleado getEmpleadoEntrega() {
    return empleadoEntrega;
  }

  public void setEmpleadoEntrega(Empleado empleadoEntrega) {
    this.empleadoEntrega = empleadoEntrega;
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

  public String getComentarioEstado() {
    return comentarioEstado;
  }

  public void setComentarioEstado(String comentarioEstado) {
    this.comentarioEstado = comentarioEstado;
  }

  @Override
  public String toString() {
    return "Salida{" + "entrada=" + entrada + ", empleadoEntrega=" + empleadoEntrega +
        ", fecha=" + fecha + ", hora=" + hora +
        ", comentarioEstado=" + comentarioEstado + '}';
  }
}
