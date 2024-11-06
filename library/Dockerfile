# Use GraalVM 17 as the base image
FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22.3.0
ADD target/library-0.0.1-SNAPSHOT.jar app.jar
# Set the working directory in the container
#WORKDIR /app
EXPOSE 8080
## Copy the JAR file into the container
#COPY target/*.jar app.jar

# Set environment variables for GraalVM optimization
ENV JAVA_OPTS="Xms1024m -Xmx1024m -XX:+UseG1GC"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]