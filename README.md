# Social Media Spring Boot MVC Application

![Java](https://img.shields.io/badge/Java-17-orange.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Blue.svg) ![Spring Security](https://img.shields.io/badge/Spring%20Security-Green.svg)

##  Overview
A robust, server-side rendered social media web application built with **Java 17** and **Spring Boot 3.2.0**. This project demonstrates core backend engineering principles, including secure user authentication, relational data modeling, and clean N-tier architecture. The view layer is handled via **Thymeleaf**, providing a seamless, server-rendered MVC experience.

##  Architecture & Design Patterns
This application enforces a strict **Controller-Service-Repository** design pattern to ensure separation of concerns and maintainability:

* **Presentation Layer (`/controller`)**: Handles incoming HTTP requests and routing. Separated into logical domains (`AuthController`, `PostController`, `FriendController`).
* **View Layer**: Utilizes Spring Boot Thymeleaf for dynamic server-side HTML rendering.
* **Data Transfer Objects (`/dto`)**: Implements the DTO pattern (`UserDto`, `PostDto`, `FriendRequestDto`) to encapsulate payload data, prevent over-posting vulnerabilities, and decouple internal database entities from the view/API layer.
* **Persistence Layer (`/repository` & `/entity`)**: Utilizes Spring Data JPA and Hibernate for ORM. Entities (`User`, `Post`, `Like`, `FriendRequest`) are mapped to a **PostgreSQL** database.
* **Security (`/config`)**: Centralized security configurations managing authentication, route authorization, and password encoding.

##  Tech Stack
* **Core**: Java 17, Spring Boot 3.2.0
* **Data Access**: Spring Data JPA, Hibernate, PostgreSQL
* **Security**: Spring Security (Role-based access control, secure session management)
* **View/Templating**: Thymeleaf
* **Tooling**: Maven, Lombok (Boilerplate reduction), Hibernate Validator (Input sanitization)
* **Testing**: Spring Boot Starter Test, Spring Security Test

##  Core Features
1. **Identity & Access Management**: 
   * User registration and login utilizing Spring Security.
   * Encrypted password storage and session validation.
2. **Social Graph Management**:
   * Send, accept, and decline friend requests (`FriendRequest` entity mapping).
   * Bidirectional friendship associations.
3. **Content Interaction**:
   * Create and manage posts.
   * Like system with relational mapping between Users and Posts (`Like` entity).
   * Aggregated home feed (`HomeController`).



##  Getting Started

### Prerequisites
* JDK 17
* Maven 3.8+
* PostgreSQL server running locally or via Docker.

### Local Setup
1. Clone the repository.
2. Update `src/main/resources/application.properties` with your PostgreSQL credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/social_media_db
   spring.datasource.username=your_user
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
