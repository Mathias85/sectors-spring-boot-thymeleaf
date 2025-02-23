# Sector

A Spring Boot application that allows users to select and save their business sectors of operation. Users can enter their name, select multiple sectors from a hierarchical list (e.g., Manufacturing > Furniture > Kitchen), and agree to terms before saving their selections.

## Technologies Used

- Java 23
- Spring Boot 3.4.3
- Spring Data JPA
- Thymeleaf
- Flyway for database migrations
- H2 Database (for development)
- Lombok for reducing boilerplate code
- Gradle for build management
- Tailwind CSS for styling
- Docker for containerization

## Prerequisites

- Java Development Kit (JDK) 23 or later
- Gradle (included via wrapper)
- Docker and Docker Compose (optional, for containerized deployment)

1. Configure the Database:

   - By default, the application uses H2 in-memory database for development
   - Database configuration can be found in `src/main/resources/application.yml`
   - To use a different database:
     - Update the database configuration in `application.yml`:
       ```yaml
       spring:
         jpa:
           database-platform: org.hibernate.dialect.PostgreSQLDialect
         datasource:
           url: jdbc:postgresql://localhost:5432/sector
           username: postgres
           password: postgres
           driverClassName: org.postgresql.Driver
       ```

2. Build the project:

```bash
./gradlew clean build
```

4. Run the application:

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`

## Running with Docker

You can run the application using Docker:

1. Build the Docker image:

```bash
docker build -t sector .
```

2. Run with Docker Compose (includes PostgreSQL database):

```bash
docker-compose up
```

The application will be available at `http://localhost:8080`

## API Documentation

The application provides a RESTful API documented using OpenAPI/Swagger.

You can access the API documentation at:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Project Structure

```
sector/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── app/mathias/sector/
│   │   │       ├── controller/    # Web controllers
│   │   │       ├── dto/          # Data Transfer Objects
│   │   │       ├── entity/       # JPA entities
│   │   │       ├── model/        # Domain models
│   │   │       ├── repository/   # Data access layer
│   │   │       ├── service/      # Business logic
│   │   │       └── mapper/       # Object mappers
│   │   └── resources/
│   │       ├── db/migration/     # Flyway migrations
│   │       ├── templates/        # Thymeleaf templates
│   │       └── application.yml
│   │       └── application.properties
│   └── test/                     # Test files
├── build.gradle                  # Gradle build configuration
├── settings.gradle              # Gradle settings
└── README.md                    # This file
```

## Database

- `users` - Stores user information
- `sectors` - Stores the hierarchical sector data
- `users_sectors` - Junction table for user-sector relationships

### H2 Console (Development)

When running in development mode with the H2 database, you can access the H2 console at:

```
http://localhost:8080/h2-console
```

### Database Migrations

Database migrations are managed by Flyway and can be found in `src/main/resources/db/migration/`.

New migrations can be added in `src/main/resources/db/migration/` following the naming convention `V{version}__{description}.sql`.
