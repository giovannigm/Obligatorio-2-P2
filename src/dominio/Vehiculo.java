import java.io.Serializable;

public class Vehiculo implements Serializable {
  private String matricula;
  private String marca;
  private String modelo;
  private String estado;

  // Indica si el vehículo está dentro del parking
  private boolean dentroParking = false;

  // Constructor
  public Vehiculo(String matricula, String marca, String modelo, String estado) {
    this.matricula = matricula;
    this.marca = marca;
    this.modelo = modelo;
    this.estado = estado;
  }

  // Constructor vacío
  public Vehiculo() {
  }

  // Getters y Setters
  public String getMatricula() {
    return matricula;
  }

  public void setMatricula(String matricula) {
    this.matricula = matricula;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public boolean isDentroParking() {
    return dentroParking;
  }

  public void setDentroParking(boolean dentro) {
    this.dentroParking = dentro;
  }

  // Método para comparar por matrícula
  public boolean esIgual(String matricula) {
    return this.matricula.equals(matricula);
  }

  @Override
  public String toString() {
    return marca + " " + modelo + " (" + matricula + ")";
  }
}
