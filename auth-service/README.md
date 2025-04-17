
# Auth Service - TaskFlow

This is the authentication microservice for the **TaskFlow** project. It is built with Java and Spring Boot and provides user authentication using JWT tokens.

## 🛠️ Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (JSON Web Token)
- JPA / Hibernate
- H2 In-memory database

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven

### Steps
1. Clone the repository or extract the project folder.
2. Open a terminal in the `auth-service` directory.
3. Run the application with:
```bash
mvn spring-boot:run
```
4. Access the H2 console at [http://localhost:8081/h2-console](http://localhost:8081/h2-console) (JDBC URL: `jdbc:h2:mem:authdb`, user: `sa`, password: empty)

## 📝 Next Steps

This is just the basic structure. Future enhancements will include:
- User registration and login endpoints
- JWT generation and validation
- Secure protected endpoints

## 📁 Project Structure

```
auth-service/
├── src/
│   └── main/
│       ├── java/com/taskflow/auth/         # Application package
│       └── resources/
│           ├── application.yml             # App configuration
├── pom.xml                                 # Maven dependencies
└── README.md
```

## 📣 Author

Evandro Coelho – [LinkedIn](https://www.linkedin.com/in/evandrocoelho)

---
