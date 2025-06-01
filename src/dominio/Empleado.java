import java.io.Serializable;

public class Empleado implements Serializable {
  private String nombre;
  private String cedula;
  private String direccion;
  private int numeroEmpleado;

  // Constructor
  public Empleado(String nombre, String cedula, String direccion, int numeroEmpleado) {
    this.nombre = nombre;
    this.cedula = cedula;
    this.direccion = direccion;
    this.numeroEmpleado = numeroEmpleado;
  }

  // Constructor vacío
  public Empleado() {
  }

  // Getters y Setters
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCedula() {
    return cedula;
  }

  public void setCedula(String cedula) {
    this.cedula = cedula;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public int getNumeroEmpleado() {
    return numeroEmpleado;
  }

  public void setNumeroEmpleado(int numeroEmpleado) {
    this.numeroEmpleado = numeroEmpleado;
  }

  // Método para comparar por cédula
  public boolean esIgual(String cedula) {
    return this.cedula.equals(cedula);
  }

  @Override
  public String toString() {
    return "Empleado{" + "nombre=" + nombre + ", cedula=" + cedula +
        ", direccion=" + direccion + ", numeroEmpleado=" + numeroEmpleado + '}';
  }
}
