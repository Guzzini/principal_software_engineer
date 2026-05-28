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
@Table(name = "document_state")
public class DocumentEntity {

    @Id
    private String documentId;

    private String title;

    private String body;

    private long lastSequence;

    private Instant updatedAt;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getLastSequence() {
        return lastSequence;
    }

    public void setLastSequence(long lastSequence) {
        this.lastSequence = lastSequence;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
