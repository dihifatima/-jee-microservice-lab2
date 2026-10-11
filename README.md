# Room Management (JPA / Hibernate)

A small Java project that demonstrates JPA with Hibernate: managing **rooms** and **users**, where each user can be assigned to a room.

## Tech stack

- Java 21
- Maven
- JPA 2.2 (`javax.persistence`)
- Hibernate 5.6.15
- H2 (in-memory database)
- JUnit 4

## Project structure

```
src/
├── main/
│   ├── java/com/example/jpa/
│   │   ├── entities/   Room, User
│   │   ├── dao/        GenericDao, RoomDao, UserDao
│   │   ├── service/    ManagementService
│   │   └── App.java
│   └── resources/META-INF/persistence.xml
└── test/java/com/example/jpa/test/
    └── ManagementServiceTest.java
```

## Data model

| Entity | Fields |
|--------|--------|
| `Room` | `id`, `name` (unique), `capacity` |
| `User` | `id`, `name`, `email` (unique), `room` (many-to-one, optional) |

## Features

- Create and list rooms and users
- Assign a user to a room
- Find rooms with a minimum capacity (`>=`) or a capacity strictly greater than a value (`>`)
- Find users by room name
- Search users by name (case-insensitive, partial match)

## Example JPQL queries

```java
// Rooms larger than a given capacity
em.createQuery("SELECT r FROM Room r WHERE r.capacity > :minCapacity", Room.class)
  .setParameter("minCapacity", 20)
  .getResultList();

// Users whose name contains a string
em.createQuery("SELECT u FROM User u WHERE LOWER(u.name) LIKE :pattern", User.class)
  .setParameter("pattern", "%jean%")
  .getResultList();
```

Named parameters keep queries readable and protect against injection, and typed queries return `List<Room>` / `List<User>` without casts.

## Getting started

### Prerequisites

- JDK 21
- Maven 3.8+

### Clone and run the tests

```bash
git clone https://github.com/<your-username>/gestion-salles.git
cd gestion-salles
mvn clean test
```

### Configuration

The persistence unit `managementPU` is defined in `src/main/resources/META-INF/persistence.xml`. By default it uses an in-memory H2 database with `hibernate.hbm2ddl.auto=create-drop`, so no database setup is needed.

## Author

<your name>