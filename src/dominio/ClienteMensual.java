import java.io.Serializable;

public class ClienteMensual implements Serializable {
  private String nombre;
  private String cedula;
  private String direccion;
  private String celular;
  private int anioAlta;

  // Constructor
  public ClienteMensual(String nombre, String cedula, String direccion, String celular, int anioAlta) {
    this.nombre = nombre;
    this.cedula = cedula;
    this.direccion = direccion;
    this.celular = celular;
    this.anioAlta = anioAlta;
  }

  // Constructor vacío
  public ClienteMensual() {
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

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }

  public int getAnioAlta() {
    return anioAlta;
  }

  public void setAnioAlta(int anioAlta) {
    this.anioAlta = anioAlta;
  }

  // Método para comparar por cédula
  public boolean esIgual(String cedula) {
    return this.cedula.equals(cedula);
  }

  @Override
  public String toString() {
    return "ClienteMensual{" + "nombre=" + nombre + ", cedula=" + cedula +
        ", direccion=" + direccion + ", celular=" + celular +
        ", anioAlta=" + anioAlta + '}';
  }
}
