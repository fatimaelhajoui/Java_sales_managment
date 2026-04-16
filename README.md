# 📊 Sales Management System

A Spring Boot web application for managing call center sales teams — built with role-based access control, file uploads, and real-time filtering.

---

## 🔐 Roles & Access

| Role | What they can do |
|---|---|
| **Admin** | Manage all teams, users, and sales across the entire system |
| **Manager** | View and approve/reject sales within their own team |
| **Agent** | Upload sales contracts (PDF) and view their own submission history |

---

## ✨ Features

- **Authentication** — Form-based login with BCrypt password hashing and session management
- **Role-based security** — URL-level access control via Spring Security
- **Sales submission** — Agents upload PDF contracts with a Contract ID; files are stored with UUID-prefixed names to prevent conflicts
- **Sales approval workflow** — Managers and Admins can approve or reject submitted sales
- **Advanced filtering** — Filter sales by status, date range, and keyword using JPA Specification pattern
- **User management** — Create, edit, deactivate users; one manager per team enforced at service level
- **Team management** — Full CRUD for teams (Admin only)
- **Pagination** — All list views are paginated with keyword search
- **Inactive user blocking** — Deactivated accounts cannot log in

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 4.x |
| Security | Spring Security (form login, BCrypt) |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL |
| Template Engine | Thymeleaf + Thymeleaf Layout Dialect |
| Frontend | HTML5, CSS3, Bootstrap 5 |
| Build Tool | Maven |

---

## 🗄️ Database Schema

```
Team (1) ──────── (N) AppUser (1) ──────── (N) Sale
```

**Team** — `id`, `name`, `note`

**AppUser** — `id`, `username`, `password` (hashed), `email`, `role` (ADMIN / MANAGER / AGENT), `status` (active/inactive), `team_id` (FK)

**Sale** — `id`, `contract_id`, `original_file`, `stored_file`, `content_type`, `size`, `status` (PENDING / APPROVED / REJECTED), `uploaded_at`, `agent_id` (FK)

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8+

### Run locally

```bash
git clone https://github.com/fatimaelhajoui/Java_sales_managment.git
cd Java_sales_managment/sales/sales
mvn spring-boot:run
```

The app starts at **http://localhost:8085**

### Database setup

MySQL database is created automatically on first run (`createDatabaseIfNotExist=true`).

Update credentials in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sales?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### Create your first admin account

Insert a user directly into the database to get started (password is BCrypt hash of `admin1234`):

```sql
INSERT INTO team (name, note) VALUES ('Admin Team', 'Default team');

INSERT INTO app_user (username, password, email, role, status, team_id)
VALUES (
  'admin',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9S',
  'admin@example.com',
  'ADMIN',
  true,
  1
);
```

---

## 📁 Project Structure

```
src/main/java/net/elhajoui/sales/
├── config/          # Spring Security + password encoder config
├── controllers/     # MVC controllers (Auth, Sale, Team, User)
├── dto/             # Data Transfer Objects
├── entities/        # JPA entities (AppUser, Sale, Team)
├── enums/           # SaleStatus enum (PENDING, APPROVED, REJECTED)
├── repositories/    # Spring Data JPA repositories
├── services/        # Service interfaces + implementations
└── specification/   # JPA Specification for dynamic sale filtering
```

---

## 🔑 Key Design Decisions

- **Service interfaces** (`SaleService`, `UserService`, `TeamService`) decouple the controller from the implementation, making the code testable and extensible
- **JPA Specification pattern** enables composable, type-safe dynamic queries without writing JPQL strings
- **CustomUserDetails** stores the user's database ID so controllers can identify the logged-in user without an extra DB call
- **Role enforcement at service layer** — business rules like "one manager per team" are checked in the service, not just the UI
- **UUID file naming** prevents filename collisions and avoids exposing original filenames in storage

---

## 📌 Roadmap

- [ ] Unit and integration tests (JUnit 5 + Mockito)
- [ ] Global exception handler with user-friendly error pages
- [ ] Dashboard with sales statistics per team/agent
- [ ] Docker + docker-compose for one-command setup
- [ ] REST API endpoints alongside existing MVC views

---

## 👩‍💻 Author

**Fatima El Hajoui**
[GitHub](https://github.com/fatimaelhajoui) · [LinkedIn](#)
