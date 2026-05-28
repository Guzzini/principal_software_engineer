# Step 1: Use an official JDK runtime as a parent image
FROM eclipse-temurin:17-jre-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file from your host to the container
# For Maven: target/*.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Principal_Software_Engineer_Assignment.jar

# Step 4: Expose the port your Spring Boot app runs on (8081)
EXPOSE 8081

# Step 5: Run the JAR file
ENTRYPOINT ["java", "-jar", "Principal_Software_Engineer_Assignment.jar"]