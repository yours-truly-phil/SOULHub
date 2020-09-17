.\mvnw.cmd spring-boot:build-image -Pproduction && docker-compose build && docker-compose up -d && echo "visit http://localhost:8080" || echo "error"
