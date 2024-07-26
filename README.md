Sistema de Gestión de Cuentas

Descripción

El Sistema de Gestión de Cuentas es una aplicación diseñada para administrar cuentas de clientes, incluyendo la creación, actualización y eliminación de cuentas, así como la gestión de información de clientes. Este sistema utiliza una arquitectura basada en RESTful APIs y está construido con Spring Boot.

Características

Creación, actualización y eliminación de cuentas de clientes.
Gestión de información personal de clientes.
Validación de datos de entrada.
Soporte para pruebas unitarias e integrales.
Documentación de API.

Requisitos

Java 11 o superior
Maven 3.6.0 o superior
H2 Database (para el entorno de desarrollo)
Spring Boot 2.5.4

Instalación

1. Clonar el repositorio

    git clone https://git@github.com:agomezpe/SistemaCuentas.git
    cd tu_repositorio

2. Compilar y empaquetar el proyecto

    mvn clean package

3. Ejecutar la aplicación
   
   java -jar target/sistema-cuentas-1.0.0.jar

   O bien, puedes ejecutar la aplicación directamente con Maven: mvn spring-boot:run

Uso

La aplicación estará disponible en http://localhost:8080.
La API ofrece varios endpoints para la gestión de clientes y cuentas. Puedes acceder a estos endpoints utilizando herramientas como Postman o cURL.

Endpoints principales

Clientes

GET /clientes: Lista todos los clientes.

GET /clientes/{id}: Trae un cliente por id.

POST /clientes: Crea un nuevo cliente.

PUT /clientes/{id}: Actualiza un cliente existente.

DELETE /clientes/{id}: Elimina un cliente.

Cuentas

GET /cuentas: Lista todas las cuentas.

GET /cuentas/{id}: Trae un cuenta por id.

POST /cuentas: Crea una nueva cuenta.

PUT /cuentas/{id}: Actualiza una cuenta existente.

DELETE /cuentas/{id}: Elimina una cuenta.

Movimientos

GET /movimientos: Lista todos los movimientos.

GET /movimientos/{id}: Trae un movimiento por id.

POST /movimientos: Crea un nuevo movimiento.

Reportes

GET /reportes/{clienteId, fechaInicio, fechaFin}: Genera un reporte de las transacciones de un cliente.


Estructura del Proyecto

config: Configuraciones de la aplicación, incluyendo ModelMapper y otras configuraciones de Spring.

controllers: Controladores REST que gestionan las solicitudes HTTP.

dto: Clases de Transferencia de Datos (DTO) utilizadas para la comunicación entre la aplicación y el cliente.

entities: Entidades JPA que representan la estructura de la base de datos.

repositories: Interfaces de JPA para el acceso a datos.

services: Implementaciones de lógica de negocio y servicios de la aplicación.

exception: Clases de excepción personalizadas para el manejo de errores.

test: Contiene pruebas unitarias e integrales.

Pruebas

Para ejecutar las pruebas unitarias e integrales, usa el siguiente comando: mvn test

Pruebas de integración

Las pruebas de integración están configuradas para verificar el correcto funcionamiento de los endpoints REST y la interacción con la base de datos.

Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue o un pull request para discutir cualquier cambio importante.
