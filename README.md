# Helping Hands - Donation Platform

A transparent donation platform built with React (frontend) and Spring Boot (backend). This platform allows users to donate to specific institutions or causes, with complete transparency where everyone can view all donations and statements.

## Features

### Guest Users (without login)
- Can donate to specific institutions or causes
- Gets a unique guest ID that can be reused to donate later
- Can link all donations using the guest ID
- Can later create a normal user account using the guest ID to preserve donation history
- Everything is transparent - everyone can view everybody's statements

### Admin Users
- Manage users (except delete)
- Manage institutions (create, update, activate/deactivate)
- Manage causes (create, update, activate/deactivate)
- Access to comprehensive dashboard with statistics

### Donator (Normal User)
- Can donate to specific institutions or causes
- Every statement is attached to a user ID
- View personal donation statements
- Add messages to donation statements
- Access to personal dashboard

### Dashboards
- **Public Dashboard**: Shows highest donators, highest donation receiving causes and institutions, donation messages in real-time
- **User Dashboard**: Personal donation statistics and history
- **Admin Dashboard**: Comprehensive platform statistics and management tools

## Tech Stack

### Backend
- Spring Boot 4.0.1
- Spring Security with JWT authentication
- Spring Data JPA
- MySQL 8.0
- Lombok
- ModelMapper
- Spring Boot Actuator (metrics)
- Micrometer (Prometheus metrics)
- Micrometer Tracing (distributed tracing)
- Loki Logback Appender (log aggregation)
- Zipkin Reporter (trace export)

### Frontend
- React 19.2.0
- TypeScript
- Vite 7.2.4
- React Router DOM
- TanStack Query
- React Hook Form
- Tailwind CSS
- Recharts (for charts)
- React Icons
- React Hot Toast
- Redux Toolkit
- Socket.io Client
- Zod (validation)
- Chart.js & React Chart.js 2

## Prerequisites

- Java 25 or higher
- Node.js 18+ and npm
- MySQL 8.0+
- Maven 3.6+
- Docker & Docker Compose (for containerized deployment)

## Setup Instructions

### 1. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE helpinghands;
```

Update the database credentials in `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/helpinghands
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Build and run the Spring Boot application:

```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Create a `.env` file in the frontend directory:

```env
VITE_API_URL=http://localhost:8080/api
```

Start the development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register

### Users
- `POST /api/users/guest` - Create guest user
- `GET /api/users/guest/{guestId}` - Get guest user by ID
- `POST /api/users/guest/{guestId}/convert` - Convert guest to user
- `GET /api/users` - Get all users (Admin only)
- `PUT /api/users/{id}` - Update user (Admin only)

### Institutions
- `GET /api/institutions` - Get all active institutions
- `GET /api/institutions/{id}` - Get institution by ID
- `POST /api/institutions` - Create institution (Admin only)
- `PUT /api/institutions/{id}` - Update institution (Admin only)

### Causes
- `GET /api/causes` - Get all active causes
- `GET /api/causes/{id}` - Get cause by ID
- `POST /api/causes` - Create cause (Admin only)
- `PUT /api/causes/{id}` - Update cause (Admin only)

### Donations
- `POST /api/donations` - Create donation (authenticated or guest)
- `GET /api/donations/my` - Get my donations (authenticated)
- `GET /api/donations` - Get all donations (public)

### Statements
- `POST /api/statements` - Create statement (authenticated)
- `GET /api/statements` - Get all statements (public)
- `GET /api/statements/donation/{donationId}` - Get statements for a donation

### Dashboard
- `GET /api/dashboard/stats` - Get dashboard statistics (public)

## Default Admin User

To create an admin user, you can either:
1. Manually insert into the database with role 'ADMIN'
2. Use the API to register and then update the role in the database

## Project Structure

```
helpinghands/
├── backend/
│   ├── src/main/java/com/helpinghands/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # JPA repositories
│   │   ├── security/         # Security configuration
│   │   ├── service/          # Business logic
│   │   └── util/            # Utility classes
│   └── src/main/resources/
│       └── application.properties
├── frontend/
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── context/          # React context providers
│   │   ├── layouts/          # Layout components
│   │   ├── pages/            # Page components
│   │   ├── services/         # API service functions
│   │   └── App.tsx           # Main app component
│   └── package.json
├── monitoring/
│   ├── docker/
│   │   ├── grafana/         # Grafana dashboards & provisioning
│   │   ├── prometheus/      # Prometheus configuration
│   │   ├── loki/            # Loki configuration
│   │   └── tempo/           # Tempo configuration
│   └── docker-compose.yml   # Monitoring stack compose file
├── k6/
│   └── scripts/             # Load testing scripts
├── docker-compose.yml       # Main application compose file
└── README.md
```

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control (RBAC)
- CORS configured for frontend
- Public endpoints for transparency (donations, statements, dashboard)
- Spring Security with custom filter chain

## Observability

The application implements comprehensive observability using the LGTM stack:

- **Metrics**: Collected via Micrometer and exported to Prometheus
  - JVM metrics
  - HTTP request metrics
  - Custom business metrics
  - DataSource metrics

- **Logs**: Aggregated by Loki via Loki Logback Appender
  - Structured logging
  - Trace ID correlation
  - Log level filtering

- **Tracing**: Distributed tracing via Micrometer Tracing and Tempo
  - Request tracing across services
  - Span propagation
  - Zipkin-compatible endpoint

- **Dashboards**: Pre-configured Grafana dashboards for monitoring
  - Application performance
  - System metrics
  - Custom business metrics

## Docker Deployment

### Quick Start with Docker Compose

The easiest way to run the entire application stack is using Docker Compose.

1. Create the observability network:
```bash
docker network create observability
```

2. Start the application:
```bash
docker-compose up -d
```

This will start:
- MySQL database on port 3307
- Backend API (proxied by NGINX) on port 8080
- Frontend on port 8081

3. View logs:
```bash
docker-compose logs -f
```

4. Stop the application:
```bash
docker-compose down
```

### Monitoring Stack

To start the monitoring and observability stack:

1. Navigate to the monitoring directory:
```bash
cd monitoring
```

2. Start the monitoring services:
```bash
docker-compose up -d
```

This will start:
- **Prometheus** on port 9090 - Metrics collection and storage
- **Grafana** on port 3000 - Visualization and dashboards (admin/password)
- **Loki** on port 3100 - Log aggregation
- **Tempo** on port 3200 - Distributed tracing (Zipkin compatible on 9411)
- **K6** - Load testing container

3. Access Grafana:
- URL: http://localhost:3000
- Default credentials: admin / password

4. Access Prometheus:
- URL: http://localhost:9090

The backend application is configured to export:
- Metrics to Prometheus via Actuator endpoint `/actuator/prometheus`
- Logs to Loki via Loki Logback Appender
- Traces to Tempo via Zipkin reporter

## Load Testing with K6

The project includes K6 load testing scripts in the `k6/scripts/` directory.

### Running Load Tests

1. Ensure the monitoring stack is running:
```bash
cd monitoring
docker-compose up -d
```

2. Run a load test:
```bash
docker-compose run --rm k6 run /scripts/auth-load-test.js
```

3. View the results in Grafana by importing the K6 dashboard or checking Prometheus metrics.

## Development

### Running Tests

Backend:
```bash
cd backend
mvn test
```

Frontend:
```bash
cd frontend
npm run lint
```

### Building for Production

Backend:
```bash
cd backend
mvn clean package
```

Frontend:
```bash
cd frontend
npm run build
```

## License

This project is open source and available for use.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
