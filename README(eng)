# PoolManager
## Description
REST API for managing a swimming pools operations (client registration, visit scheduling).

## Project Structure
### The project consists of:

REST API controllers for client and schedule management
Service layer with business logic
Data model with JPA entities
MySQL database (configured via Docker)

### Prerequisites
Docker and Docker Compose installed
Java 17 or higher
Maven

## Setup and Running
Start the database:

''' bash
docker-compose up -d  
Build and run the Spring Boot application:
'''
''' bash
mvn spring-boot:run 
'''
The application will be available at: http://localhost:8080


## API Documentation
A Postman collection is provided in postman_collection.json with examples of all available API endpoints.

### Client Management (/api/v0/pool)
Method	Endpoint	Description
GET	/client/all	Get all clients
GET	/client/get/{id}	Get a specific client by ID
POST	/client/add	Create a new client
POST	/client/update	Update client information

### Schedule Management (/api/v0/timetable)
Method	Endpoint	Description
POST	/day/add	Add a new day with time slots
POST	/reserve	Create a reservation
GET	/all	Get all time slots with occupancy count for a day
GET	/available	Get available time slots for a date
POST	/cancel	Cancel a reservation
GET	/reserveByFullName	Get reservations by clients full name
GET	/reserveByDate	Get all reservations for a specific date

## Data Model
Key entities:

Client: Stores client information
Day: Represents a day with working hours and capacity
VisitSlot: Time slots for reservations within a day
Reservation: Links clients to visit slots
