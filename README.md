Sure, here's the translation of the README to English:

---

# Account Management System

## Description

The Account Management System is an application designed to manage customer accounts, including the creation, update, and deletion of accounts, as well as managing customer information. This system uses a RESTful API architecture and is built with Spring Boot.

## Features

- Creation, update, and deletion of customer accounts.
- Management of personal customer information.
- Input data validation.
- Support for unit and integration testing.
- API documentation.

## Requirements

- Java 11 or higher
- Maven 3.6.0 or higher
- H2 Database (for development environment)
- Spring Boot 2.5.4

## Installation

### Clone the repository

```bash
git clone https://git@github.com:agomezpe/SistemaCuentas.git
cd your_repository
```

### Build and package the project

```bash
mvn clean package
```

### Run the application

```bash
java -jar target/sistema-cuentas-1.0.0.jar
```

Or, you can run the application directly with Maven:

```bash
mvn spring-boot:run
```

## Usage

The application will be available at `http://localhost:8080`. The API provides several endpoints for managing customers and accounts. You can access these endpoints using tools like Postman or cURL.

## Main Endpoints

### Customers

- `GET /clientes`: Lists all customers.
- `GET /clientes/{id}`: Retrieves a customer by id.
- `POST /clientes`: Creates a new customer.
- `PUT /clientes/{id}`: Updates an existing customer.
- `DELETE /clientes/{id}`: Deletes a customer.

### Accounts

- `GET /cuentas`: Lists all accounts.
- `GET /cuentas/{id}`: Retrieves an account by id.
- `POST /cuentas`: Creates a new account.
- `PUT /cuentas/{id}`: Updates an existing account.
- `DELETE /cuentas/{id}`: Deletes an account.

### Transactions

- `GET /movimientos`: Lists all transactions.
- `GET /movimientos/{id}`: Retrieves a transaction by id.
- `POST /movimientos`: Creates a new transaction.

### Reports

- `GET /reportes/{clienteId, fechaInicio, fechaFin}`: Generates a report of a customer's transactions.

## Project Structure

- `config`: Application configurations, including ModelMapper and other Spring configurations.
- `controllers`: REST controllers handling HTTP requests.
- `dto`: Data Transfer Object (DTO) classes used for communication between the application and the client.
- `entities`: JPA entities representing the database structure.
- `repositories`: JPA interfaces for data access.
- `services`: Implementations of business logic and application services.
- `exception`: Custom exception classes for error handling.
- `test`: Contains unit and integration tests.

## Testing

To run unit and integration tests, use the following command:

```bash
mvn test
```

### Integration Testing

Integration tests are configured to verify the correct functioning of the REST endpoints and interaction with the database.

## Contributions

Contributions are welcome. Please open an issue or pull request to discuss any major changes.

---
