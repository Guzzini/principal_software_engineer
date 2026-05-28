package com.nl.principal.LN_Document_Revision_Service.service;

import com.nl.principal.LN_Document_Revision_Service.domain.DocumentEntity;
import com.nl.principal.LN_Document_Revision_Service.domain.Event;
import com.nl.principal.LN_Document_Revision_Service.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Peter Maunatlala on 2026/05/25.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
@Service
public class SequenceService {

    private final DocumentRepository documentRepository;
    private final DocumentService documentService;
    private final BufferService bufferService;

    public SequenceService(
            DocumentRepository documentRepository,
            DocumentService documentService,
            BufferService bufferService
    ) {
        this.documentRepository = documentRepository;
        this.documentService = documentService;
        this.bufferService = bufferService;
    }

    public void handle(Event event) {

        //Retrieve the document by event document by Id
        Optional<DocumentEntity> existing = documentRepository.findById(event.documentId());

        //Extract the last sequence value from the document
        long currentSequence = existing.map(DocumentEntity::getLastSequence)
                .orElse(0L);

        long expected = currentSequence + 1;

        //If sequence is equals to expected sequence value push and apply event
        if (event.sequence() == expected) {
            applyAndDrain(event);
            return;
        }

        //If sequence is greater than expected sequence temporarily buffer event sequence
        if (event.sequence() > expected) {
            bufferService.buffer(event);
        }
    }

    private void applyAndDrain(Event event) {

        //Flush buffered event sequence
        documentService.apply(event);

        //Retrieve the next expected sequence
        long nextExpected = event.sequence() + 1;

        //Continue checking if there is available next sequence in the buffer and apply the event and increment the sequence or break the loop
        while (true) {
            Event next = bufferService.getNext(event.documentId(), nextExpected);

            if (next == null) {
                return;
            }

            documentService.apply(next);
            nextExpected++;
        }
    }

}
