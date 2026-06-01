# AUTOdeal Frontend

This folder contains the separate client application for the AUTOdeal system.

For Assignment 4, the frontend is separated from the backend services and communicates with them through exposed APIs.

The frontend is responsible for:
- displaying available cars
- displaying filtering and sorting options
- allowing users to log in and register
- allowing users to create, update and delete car posts
- supporting internationalization with English and Romanian
- providing a real-time chat interface

The backend functionality is split into:
- `car-microservice`, running on port 8081
- `user-microservice`, running on port 8082

The frontend communicates with the backend through REST APIs and WebSocket communication.