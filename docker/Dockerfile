# Frontend
FROM node:20.18.0 AS frontend

WORKDIR /frontend

# Copy build and config files
COPY /frontend/package.json /frontend/package-lock.json /frontend/angular.json /frontend/tsconfig.app.json /frontend/tsconfig.json /frontend/tsconfig.spec.json /frontend/

RUN npm install

# Copy src files
COPY frontend/src /frontend/src

RUN npx ng build --configuration production


# Maven (frontend to backend)
FROM maven:3.8.6-openjdk-18 AS build

WORKDIR /app

COPY backend/pom.xml /app/

COPY backend/src/ /app/src

# Copy production files from Angular to backend-public
COPY --from=frontend /frontend/dist/frontend/browser /app/src/main/resources/static

RUN mvn clean install -DskipTests

# Java
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]