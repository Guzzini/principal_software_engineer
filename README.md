# Principal_Software_Engineer_Assignment - Spring Boot Application and Kafka Messaging

## Deploy a Java Spring Boot microservice using Docker and Docker Compose.

### Features
* **Java 17** , **Spring Boot 4** & **kafka** support
*  **Dockerfile** for image
*  **Docker Compose** integration for configuration
*  **Health checks via Spring Boot Actuator**

### Prerequisites
Ensure the following are installed on your machine
* [Java Development Kit(JDK) - Version 17 or higher]
* [Maven]
* [Docker/Docker Desktop]
* [PostgresSQL]
* [Kafka]

Create a Database name : **legal_documents** in the postgresSQL DB.

Execute queries: 
- SQL:

`CREATE DATABASE legal_documents;`

`CREATE TABLE document_state (
document_id VARCHAR(255) PRIMARY KEY,
title TEXT NOT NULL,
body TEXT NOT NULL,
last_sequence BIGINT NOT NULL,
updated_at TIMESTAMP NOT NULL);`

`CREATE TABLE processed_events (
event_id VARCHAR(255) PRIMARY KEY,
processed_at TIMESTAMP NOT NULL);`

`INSERT INTO document_state (document_id, title,body, last_sequence, updated_at)
VALUES ('1', 'LN', 'Document version', 1, CURRENT_TIMESTAMP);`

`INSERT INTO processed_events (event_id, processed_at)
VALUES ('1', CURRENT_TIMESTAMP);`

## Local Development 
[Note: We run bash]

Run the application on your local machine

### 1. Build the Artifact
`./mvnw clean install`

### 2. Run the App
`java -jar target/*.jar`

The application is configured to run on port : `8081` and context-root `/api/v1`
It will start locally on 'http://localhost:8081/api/v1/swagger-ui/index.html' 

*[Note]*: If application experience any issues with the database connection, please modify the connection string in the `application.yml` and `docker-compose.yml`.


## Running with Docker
### 1. Build the docker Image
Build the container image locally, execute this command from the root directory containing your `Dockerfile`:

`docker build -t ln-document-revision .`

### 2. Run the container standalone
Run the image and map your local machine's port `8081` to the container's internal port `8081`:

`docker run -p 8081:8081 --name ln-document-revision-app ln-document-revision:latest`

## Orchestration with Docker Compose
This repository includes a `docker-compose.yml` file to spin up both the Spring Boot service and its required relational database (PostgreSQL).

### Start All Services
Launch the stack in detached mode:

`docker compose up -d`

### Useful Docker Management Commands
* **View Logs**: `docker compose logs -f app`
* **Stop Container Stack**: `docker compose down`
* **Stop & Wipe Volumes (Fresh DB)**: `docker compose down -v`

## Environment Variables
The container may use environment profiles and variables, which can be modified inside the `docker-compose.yml`.

## Verification & Health Checks

Once your application container is fully initialized, you can verify its health state using the following endpoints:

* **Base URL**: `http://localhost:8081/api/v1/swagger-ui/index.html`
*  **Actuator URL**: `http://localhost:8081/api/v1/actuator/health` 

## Local Apache Kafka Development Environment
**Apache Kafka** cluster running in ZooKeeper less using **Docker Compose**.

## Prerequisites
Ensure you have the following installed on your host machine:
* [Docker Desktop - Version : v20.10.0 or higher]
* [Docker Compose V2]

### 1. Start the Cluster
Launch Kafka in detached mode by running:

`docker compose up -d`

### 2. Verify Running Containers
Check that the services are up:

`docker ps`

* **Kafka Broker** will be available externally on port `9092`.
Execute commands directly inside the running Kafka container using native shell scripts.

### Create a Topic
Create a new topic named `document-version` with 2 partitions and a replication factor of 1:

`docker exec -it kafka-broker kafka-topics.sh --create --topic document-version --partitions 2 --replication-factor 1 --bootstrap-server localhost:9092`

### List Topics
List all existing topics in the cluster:

`docker exec -it kafka-broker kafka-topics.sh   --list  --bootstrap-server localhost:9092`

### Produce Messages
Open a new terminal to start a console producer to publish live messages to your topic.

`docker exec -it kafka-broker kafka-console-producer.sh --topic document-version --bootstrap-server localhost:9092`

### Consume Messages
Open a new terminal window to stream messages from the beginning of your topic's history:

`docker exec -it kafka-broker kafka-console-consumer.sh --topic test-topic --from-beginning --bootstrap-server localhost:9092`

## Connect an Applications form External

`bootstrap.servers=localhost:9092`

Connect application running on the same Docker

`bootstrap.servers=kafka-broker:29092`

## You can shut down your container

`docker compose down`












