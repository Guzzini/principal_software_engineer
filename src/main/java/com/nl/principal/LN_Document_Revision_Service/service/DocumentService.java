package com.nl.principal.LN_Document_Revision_Service.service;

import com.nl.principal.LN_Document_Revision_Service.domain.DocumentEntity;
import com.nl.principal.LN_Document_Revision_Service.domain.Event;
import com.nl.principal.LN_Document_Revision_Service.domain.ProcessedEventEntity;
import com.nl.principal.LN_Document_Revision_Service.repository.DocumentRepository;
import com.nl.principal.LN_Document_Revision_Service.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Created by Peter Maunatlala on 2026/05/25.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProcessedEventRepository processedEventRepository;

    public DocumentService(
            DocumentRepository documentRepository,
            ProcessedEventRepository processedEventRepository
    ) {
        this.documentRepository = documentRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public void apply(Event event) {

        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        //Retrieve the document by event document by Id
        Optional<DocumentEntity> existing = documentRepository.findById(event.documentId());

        DocumentEntity document;

        //Create a new document if event document Id does not exist and default last sequence to zero
        if (existing.isEmpty()) {
            document = new DocumentEntity();
            document.setDocumentId(event.documentId());
            document.setLastSequence(0);
        } else {
            document = existing.get();
        }

        //If sequence is equal or less than stored last sequence ignore it
        if (event.sequence() <= document.getLastSequence()) {
            return;
        }

        //Else proceed setting the document values from event and persist the document and the event
        document.setTitle(event.payload().title());
        document.setBody(event.payload().body());
        document.setLastSequence(event.sequence());
        document.setUpdatedAt(Instant.now());

        documentRepository.save(document);

        processedEventRepository.save(
                new ProcessedEventEntity(event.eventId(), Instant.now())
        );
    }

    //Retrieve document by document id
    public Optional<DocumentEntity> get(String documentId) {
        return documentRepository.findById(documentId);
    }

}
