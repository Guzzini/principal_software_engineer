package com.nl.principal.LN_Document_Revision_Service.api;

import com.nl.principal.LN_Document_Revision_Service.domain.DocumentEntity;
import com.nl.principal.LN_Document_Revision_Service.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Peter Maunatlala on 2026/05/26.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> get(
            @PathVariable String id
    ) {
        return documentService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  }
