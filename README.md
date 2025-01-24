# Proyecto Equipos de Fútbol

# Requisitos
Docker -
Java 17 - 
Maven

# Docker
Crear jar: " mvn clean package "

Construir imagen Docker:
" docker build -t app-image:1.0 . "

Ejecutar contenedor:
" docker run -p8088:8088 --name app-equipos app-image:1.0 "

# Swagger
URL: http://localhost:8088/swagger-ui/index.html
