
# Principal_Software_Engineer_Assignment - Spring Boot Implementation

## Overview

The design is chosen to intentionally avoids distributed locking. This will prevent performance bottleneck, network dipping and corrupting the data.

This implementation provides production-ready Spring Boot service for consuming legal document revision events from an at-least-once broker.  

This will maintain the core idea of;
- Hot documents isolation
- Document pre-ordering
- Horizontal scalability
- Throughput under burst load

# High-Level Architecture Component Models

                +----------------------+
                |  Message Broker      |
                | (Kafka / PubSub)     |
                +----------+-----------+
                           |
                           V
                +----------------------+
                | Spring Boot Consumer |
                +----------+-----------+
                           |
            +--------------+--------------+
            |                             |
            V                             V
    +-------------------+   +---------------------------+
    | Processed Events  |   | Sequencing Buffer per Doc |
    +-------------------+   +---------------------------+
                                            |
                                            V
                            +------------------------------------+
                            | Document State Store(current_doc)  |
                            +------------------------------------+
                                            |
                                            V
                            +--------------------------------------+
                            |        REST Query(get()) API         |
                            +--------------------------------------+
# Favoured Tech-Stack
- JAVA-17
- Spring Boot-4
- Kafka - (Messaging)
- PostgresSQL - (DB)
- Spring Data JPA - (ORM)
- Jackson - (Serializer)
- Maven Build
- Docker Container

# Kafka core benefits:
- Partitioning by ordering/document_id; ensures events for the same document are processed by one consumer instance.
- Better horizontal scaling; high availability.
- Consumer grouping; allows multiple consumer instances to work together. 
- Replayability; fault tolerance - reprocessing of failed messages.
- Backpressure support; prevents crashes and resource management.

# Design Decisions
## 1. Partition by document_id
Key: record.documentId()

Satisfactions rules :
- Same document always delivered to the same partition
- One consumer processes one document stream
- No distributed locking

## 2. Maintain last applied sequence
Each document stores: last_applied_sequence

Satisfactions rules:
- s == last_applied_sequence + 1 | apply
- s <= last_applied_sequence     | ignore
- s >  last_applied_sequence +1  | temporarily buffer

## 3. Reordering Buffer
The requirements state delayed events arrive within seconds; You need to maintain an in-memory buffer.

Satisfaction rules:

Sent : 1,3,2,4

Process behavior:

- apply 1
- buffer 3
- receive 2
- apply 2
- flush buffered 3
- apply 4

## Idempotency
Every processed event_id is persisted. 
Idempotency is mandatory in at-least-once systems.

Database: legal_documents

Table: 
processed_events(event_id PRIMARY KEY)

Duplicate delivery becomes harmless.
The concept is heavily applied across different fields to build reliable, error-proof systems.

# Project Structure
    src/main/java/com/nl/principal/LN_Document_Revision_Service
    |____ LnDocumentRevisionServiceApplication.java
    |____ api
    |       |____ DocumentController.java
    |____ consumer
    |       |____ EventConsumer.java
    |____ domain
    |       |____ Event.java
    |       |____ DocumentEntity.java
    |       |____ ProcessedEventEntity.java
    |       |____ Payload.java
    |       |____ EventType.java
    |____ repository
    |       |____ DocumentRepository.java
    |       |____ ProcessedEventRepository.java
    |____ service
    |       |____ DocumentService.java
    |       |____ SequenceService.java
    |       |____ BufferService.java
    |____ config
    |       |____ KafkaConfig.java
    |       |____ SwaggerConfig.java
    |____ dto
    |       |____ DocumentResponse.java

# Database Schema: `legal_documents`

- Table name: `document_state` 

- Columns and DataTypes:
    - **[document_id]** VARCHAR(255) PRIMARY KEY
    - **[title]** TEXT NOT NULL
    - **[body]** TEXT NOT NULL 
    - **[last_sequence]** BIGINT NOT NULL
    - **[updated_at]** TIMESTAMP NOT NULL
  

- SQL


    CREATE TABLE document_state (
      document_id VARCHAR(255) PRIMARY KEY,
      title TEXT NOT NULL,
      body TEXT NOT NULL,
      last_sequence BIGINT NOT NULL,
      updated_at TIMESTAMP NOT NULL);
    

- Table name: `processed_events`
- Columns and DataTypes
  - **[event_id]** VARCHAR(255) PRIMARY KEY
  - **[processed_at]** TIMESTAMP NOT NULL
  

- SQL


    CREATE TABLE processed_events (
    event_id VARCHAR(255) PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL);


# Handling Hot Documents
Only the partition containing the hot document becomes busy. Other partitions will continue processing normally.
Activity on one document does not slow down a job on others.

# Current implementation optimizes:
- Simplicity
- Throughput
- Operational clarity
- Current-state queries
