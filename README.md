# AUTOdeal - Assignment 3 Microservices

AUTOdeal is a car marketplace application developed for the Software Design assignments.

For Assignment 3, the original monolithic Spring Boot application was refactored into a microservices-based architecture.

The project is split into two main backend microservices:

- `car-microservice`
- `user-microservice`

Each microservice has its own responsibility and its own PostgreSQL database.

---

## Project Structure

```text
AUTOdeal
│
├── car-microservice
│   └── handles car posts, brands, models, filtering, sorting, export and car-related operations
│
├── user-microservice
│   └── handles users, roles, login/register and user-related REST APIs
│
├── frontend
│   └── reserved for a future separated frontend layer
│
└── README.md