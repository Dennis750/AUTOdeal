# AUTOdeal Frontend

This folder represents the frontend layer of the AUTOdeal microservices architecture.

For the current Assignment 3 implementation, the web interface is still served through the `car-microservice` using Thymeleaf templates.

The frontend layer is responsible for:
- displaying available cars
- displaying filters and sorting options
- providing buttons for JSON, CSV and XML export
- allowing authenticated users to create, update and delete car posts

The backend functionality is split into:
- `car-microservice`, running on port 8081
- `user-microservice`, running on port 8082

The car microservice communicates with the user microservice through REST.