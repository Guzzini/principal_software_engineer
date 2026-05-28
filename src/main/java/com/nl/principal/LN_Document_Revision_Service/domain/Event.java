package com.nl.principal.LN_Document_Revision_Service.domain;



import java.time.Instant;

/**
 * Created by Peter Maunatlala on 2026/05/21.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
public record Event(
        String eventId,
        String documentId,
        long sequence,
        Instant timestamp,
        Payload payload,
        EventType type) {
}
