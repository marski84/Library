## Use GraalVM 17 as the base image
#FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22.3.0
#
## Set the working directory in the container
#WORKDIR /app
#
## Copy the JAR file into the container
#ADD target/library-0.0.1-SNAPSHOT.jar app.jar
#
## Expose port 8080
#EXPOSE 8080
#
## Run the application with JVM arguments directly
#ENTRYPOINT ["java", "-Xms1024m", "-Xmx1024m", "-XX:+UseG1GC", "-jar", "app.jar"]
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY target/library-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Używamy shell form dla ENTRYPOINT
ENTRYPOINT exec java -XX:+UseG1GC -jar app.jar