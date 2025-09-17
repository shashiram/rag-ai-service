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
1. Open project in Idea.
2. run below cmd on root folder
      docker compose -f docker-compose.db.yml up --build
3. run below cmd on root folder
      docker compose -f docker-compose.pg.yml up --build

App: http://localhost:8080
   OpenAPI UI: http://localhost:8080/swagger-ui.html or /swagger-ui/index.html
   pgAdmin: http://localhost:5050 (shashiram01@gmail.com / password)
   prometheus: http://localhost:9090 (shashiram01@gmail.com / password)
   grafana: http://localhost:3000 (shashiram01@gmail.com / password)



