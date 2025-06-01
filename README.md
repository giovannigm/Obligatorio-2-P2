# Parking247 - Sistema de Gestión de Parking

Este proyecto implementa un sistema de gestión para un parking 24/7, desarrollado en Java utilizando NetBeans IDE.

## Estructura del Proyecto

El proyecto está organizado de la siguiente manera:

- `src/dominio/`: Contiene todas las clases del modelo de dominio
  - `ClienteMensual.java`: Gestiona la información de clientes mensuales
  - `Vehiculo.java`: Representa los vehículos del parking
  - `Empleado.java`: Gestiona la información de empleados
  - `Contrato.java`: Maneja los contratos mensuales
  - `Entrada.java`: Registra las entradas de vehículos
  - `Salida.java`: Registra las salidas de vehículos
  - `ServicioAdicional.java`: Gestiona servicios adicionales
  - `DatosSistema.java`: Almacena y gestiona todos los datos del sistema

## Requisitos

- Java JDK 1.8 o superior
- NetBeans IDE 8.2 o superior
- Apache Ant (incluido con NetBeans)

## Configuración del Proyecto

1. Clonar o descargar el repositorio
2. Abrir NetBeans IDE
3. Seleccionar File -> Open Project
4. Navegar hasta la carpeta del proyecto y seleccionarla
5. El proyecto se abrirá como un proyecto Java con configuración ANT

## Compilación y Ejecución

### Usando NetBeans IDE:
1. Click derecho en el proyecto
2. Seleccionar "Clean and Build"
3. Para ejecutar, click derecho en el proyecto y seleccionar "Run"

### Usando Ant desde la línea de comandos:
1. Navegar hasta la carpeta del proyecto
2. Ejecutar `ant clean` para limpiar
3. Ejecutar `ant` para compilar y crear el JAR
4. El JAR se generará en la carpeta `dist`

## Características Implementadas

- Modelo de dominio completo con todas las clases requeridas
- Implementación de Serializable para persistencia
- Gestión de datos centralizada mediante DatosSistema
- Métodos de búsqueda por identificadores únicos
- Estructura lista para implementación de GUI

## Próximos Pasos

- Implementación de interfaz gráfica
- Persistencia de datos
- Validaciones de negocio
- Reportes y estadísticas

Letra Obligatorio: https://aulas.ort.edu.uy/pluginfile.php/952924/mod_label/intro/p22504o2%20OBLIG%20P2%20V6c.pdf?time=1747602334834


Rubrica:  https://aulas.ort.edu.uy/pluginfile.php/952924/mod_label/intro/Rubrica%202do%20Obligatorio%20Alumnos%20%281%29.pdf
