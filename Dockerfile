FROM maven:4.0.0-rc-5-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app

EXPOSE 8080
COPY --from=build /app/target/*.jar filmographie.jar

ENTRYPOINT ["java", "-jar", "filmographie.jar"]