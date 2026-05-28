package com.nl.principal.LN_Document_Revision_Service.consumer;

import com.nl.principal.LN_Document_Revision_Service.domain.Event;
import com.nl.principal.LN_Document_Revision_Service.service.SequenceService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by Peter Maunatlala on 2026/05/21.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
@Component
public class EventConsumer {

    private final SequenceService sequencingService;

    public EventConsumer(SequenceService sequencingService) {
        this.sequencingService = sequencingService;
    }

    @KafkaListener(
            topics = "document-versions",
            groupId = "version-service"
    )
    public void consume(Event event) {
        sequencingService.handle(event);
    }

}
