# Event Ticketing Platform

A full-stack event ticketing application built with Spring Boot, React, PostgreSQL, and Keycloak.

The application supports three user flows:

- **Organizers** can create and manage events and ticket types.
- **Attendees** can browse published events, purchase tickets, and access QR-code tickets.
- **Staff** can validate tickets using QR codes or manual ticket IDs.

## Features

### Organizer

- Create, view, update, and delete events
- Configure event details such as dates, venue, sales period, and status
- Create multiple ticket types with different prices and availability
- Publish events for attendees
- Manage event and ticket information through protected organizer endpoints

### Attendee

- Browse published events
- Search published events
- View event and ticket type details
- Purchase available tickets
- View purchased tickets
- Open individual ticket details
- Access a generated QR code for each ticket

### Staff

- Validate tickets by scanning a QR code
- Validate tickets manually using the ticket ID
- Prevent duplicate entry by marking later validation attempts as invalid
- Access validation functions through staff-only endpoints

## Authentication and Authorization

Authentication is handled by Keycloak using OAuth 2.0 and OpenID Connect.

The React application authenticates users with Keycloak and sends JWT access tokens to the Spring Boot API. Spring Security validates the tokens and uses Keycloak realm roles to control access to protected endpoints.

The application uses the following roles:

- `ORGANIZER`
- `ATTENDEE`
- `STAFF`

A user record is created in PostgreSQL the first time an authenticated Keycloak user accesses the backend.

## Ticket Purchase and QR Codes

When an attendee purchases a ticket, the backend:

1. Loads the requested ticket type.
2. Checks the remaining ticket inventory.
3. Creates the ticket for the authenticated user.
4. Generates a unique QR code for the ticket.
5. Stores the ticket and QR-code information in PostgreSQL.

Ticket purchases use a pessimistic database lock on the selected ticket type. This prevents concurrent purchases from overselling the available inventory.

QR codes are generated with ZXing and are used by staff during ticket validation.

## Ticket Validation

Tickets can be validated in two ways:

- QR-code scan
- Manual ticket ID entry

A successful first validation returns a valid result. If the same ticket is validated again, the application returns an invalid result so that the same ticket cannot be used twice.

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Hibernate
- Maven
- MapStruct
- Lombok
- ZXing

### Frontend

- React
- TypeScript
- Vite

### Data and Authentication

- PostgreSQL
- Keycloak
- OAuth 2.0
- OpenID Connect
- JWT

### Development

- Docker
- Docker Compose
- Adminer
- GitHub Actions

## Architecture

```text
                    +------------------+
                    |      React       |
                    |   Vite Frontend  |
                    +---------+--------+
                              |
                              | REST API + JWT
                              |
                    +---------v--------+
                    |   Spring Boot    |
                    |       API        |
                    +----+---------+---+
                         |         |
                         |         | JWT validation
                         |         |
                +--------v---+  +--v----------+
                | PostgreSQL |  |  Keycloak   |
                +------------+  +-------------+
```

The frontend runs separately from the backend and communicates with it through REST APIs. PostgreSQL stores application data, while Keycloak manages identity and authentication.

## Main Domain Model

The main entities are:

- `User`
- `Event`
- `TicketType`
- `Ticket`
- `QrCode`
- `TicketValidation`

An event belongs to an organizer and can contain multiple ticket types. Purchased tickets belong to attendees and reference a ticket type. Each ticket has a QR code and can have ticket validation records.

## API Overview

### Events

```http
POST   /api/v1/events
GET    /api/v1/events
GET    /api/v1/events/{eventId}
PUT    /api/v1/events/{eventId}
DELETE /api/v1/events/{eventId}
```

Organizer event operations require authentication and the organizer role.

### Published Events

```http
GET /api/v1/published-events
GET /api/v1/published-events/{eventId}
```

Published events can be browsed by attendees. The listing endpoint also supports event search.

### Tickets

```http
POST /api/v1/published-events/{eventId}/ticket-types/{ticketTypeId}
GET  /api/v1/tickets
GET  /api/v1/tickets/{ticketId}
GET  /api/v1/tickets/{ticketId}/qr-codes
```

These endpoints cover ticket purchase, ticket history, ticket details, and QR-code retrieval.

### Ticket Validation

```http
POST /api/v1/ticket-validations
```

The validation endpoint supports both QR-code and manual validation and is restricted to staff users.

## Local Development

### Prerequisites

Install the following before running the project:

- Java 21
- Node.js and npm
- Docker
- Docker Compose

## Start PostgreSQL and Keycloak

The project uses Docker Compose for PostgreSQL, Adminer, and Keycloak.

```bash
docker compose up -d
```

The local services use these ports:

| Service | URL / Port |
| --- | --- |
| Spring Boot API | `http://localhost:8080` |
| React frontend | `http://localhost:5173` |
| PostgreSQL | `localhost:5432` |
| Keycloak | `http://localhost:9090` |
| Adminer | `http://localhost:8888` |

## Keycloak Setup

Create a Keycloak realm named:

```text
event-ticket-platform
```

Create a public client named:

```text
event-ticket-platform-app
```

For local development, configure the client with:

```text
Valid Redirect URIs:
http://localhost:5173/*

Web Origins:
http://localhost:5173
```

Create the application roles:

```text
ORGANIZER
ATTENDEE
STAFF
```

Assign the required role to each test user.

The Spring Boot resource server uses the following issuer:

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/event-ticket-platform
```

## Run the Backend

From the backend directory:

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### macOS or Linux

```bash
./mvnw spring-boot:run
```

The API starts on:

```text
http://localhost:8080
```

## Run the Frontend

From the frontend directory:

```bash
npm install
npm run dev
```

The frontend starts on:

```text
http://localhost:5173
```

## Database Configuration

The development database runs in PostgreSQL on port `5432`, but I mapped it to 5433 on my machine from the docker container as I had another instance of PostgreSQL running on my machine. Please change the port from `5433` to `5432` in `application.properties` file if you are planning to clone and run the application.

A typical local configuration is:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=changemeinprod!

spring.jpa.hibernate.ddl-auto=update
```

The credentials above are intended only for local development. Use environment variables or a secret-management solution outside local development.

## Build and Test

Run the backend test suite with:

### Windows

```powershell
.\mvnw.cmd clean verify
```

### macOS or Linux

```bash
./mvnw clean verify
```

The project also includes a GitHub Actions workflow that runs the Maven build and tests for repository changes.

## Project Structure

```text
.
├── backend/
│   ├── src/main/java/
│   │   └── ...
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── vite.config.ts
│
├── docker-compose.yml
└── README.md
```

## Development Notes

This project focuses on the full event ticket lifecycle:

1. An organizer creates an event and its ticket types.
2. The event is published.
3. Attendees browse events and purchase tickets.
4. A QR code is created for each purchased ticket.
5. Staff validate tickets at entry.
6. Duplicate validation attempts are rejected.

Payment processing is not part of the current implementation. A ticket purchase represents reservation and issuance of a ticket within the application.

## Future Improvements

Possible next steps include:

- Add a payment provider
- Add organizer sales and attendance reports
- Add email confirmations and ticket delivery
- Add database migrations with Flyway or Liquibase
- Add Testcontainers-based integration tests
- Add production deployment configuration
- Add monitoring and application metrics

## License

This project is intended as a personal software engineering project.
