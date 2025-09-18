# RAG Chat Storage Microservice (Spring Boot) - Starter (Updated)

## Overview
This is a starter Spring Boot project that implements a chat session and message storage microservice.
Enhancements in this version:
- Java version 21
- Swagger / OpenAPI via springdoc
- Per-API-key rate limiting using Resilience4j registry
- grafana prometheus observability
- Docker multi-stage build 

## Quickstart
1. Clone project from github.
2. Setup api-key and open-api-key.
3. run below cmd on root folder
      docker compose -f docker-compose.yml up --build


   App: http://localhost:8080
   RagChatAPI swagger UI: http://localhost:8080/swagger-ui.html or /swagger-ui/index.html
   pgAdmin: http://localhost:5050 (shashiram01@gmail.com / password)
   postgres db: http://localhost:5432 (admin / password)
   prometheus: http://localhost:9090 
   grafana: http://localhost:3000 (admin / password)



