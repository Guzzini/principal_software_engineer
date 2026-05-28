package com.nl.principal.LN_Document_Revision_Service.repository;

import com.nl.principal.LN_Document_Revision_Service.domain.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Peter Maunatlala on 2026/05/25.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {
}
