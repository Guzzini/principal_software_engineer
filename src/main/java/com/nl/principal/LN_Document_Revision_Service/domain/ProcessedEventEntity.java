package com.nl.principal.LN_Document_Revision_Service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Created by Peter Maunatlala on 2026/05/21.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
@Entity
@Table(name = "processed_events")
public class ProcessedEventEntity {

    @Id
    private String eventId;

    private Instant processedAt;

    public ProcessedEventEntity(String s, Instant now) {
    }

    public ProcessedEventEntity() {

    }

    String getEventId() {
        return eventId;
    }

    void setEventId(String eventId) {
        this.eventId = eventId;
    }

    Instant getProcessedAt() {
        return processedAt;
    }

    void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }
}
