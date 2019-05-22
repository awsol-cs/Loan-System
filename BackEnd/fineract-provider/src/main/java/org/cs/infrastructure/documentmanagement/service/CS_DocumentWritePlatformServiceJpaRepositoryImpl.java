/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.cs.infrastructure.documentmanagement.service;

import java.io.InputStream;

import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.domain.Base64EncodedImage;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.infrastructure.documentmanagement.command.DocumentCommand;
import org.apache.fineract.infrastructure.documentmanagement.command.DocumentCommandValidator;
import org.apache.fineract.infrastructure.documentmanagement.contentrepository.ContentRepository;
import org.apache.fineract.infrastructure.documentmanagement.contentrepository.ContentRepositoryFactory;
import org.apache.fineract.infrastructure.documentmanagement.domain.Document;
import org.apache.fineract.infrastructure.documentmanagement.domain.DocumentRepository;
import org.apache.fineract.infrastructure.documentmanagement.domain.StorageType;
import org.apache.fineract.infrastructure.documentmanagement.exception.ContentManagementException;
import org.apache.fineract.infrastructure.documentmanagement.exception.DocumentNotFoundException;
import org.apache.fineract.infrastructure.documentmanagement.exception.InvalidEntityTypeForDocumentManagementException;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.cs.infrastructure.documentmanagement.contentrepository.CS_ContentRepository;
import org.cs.infrastructure.documentmanagement.contentrepository.CS_ContentRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CS_DocumentWritePlatformServiceJpaRepositoryImpl implements CS_DocumentWritePlatformService {

    private final static Logger logger = LoggerFactory.getLogger(CS_DocumentWritePlatformServiceJpaRepositoryImpl.class);

    private final PlatformSecurityContext context;
    private final DocumentRepository documentRepository;
    private final CS_ContentRepositoryFactory contentRepositoryFactory;

    @Autowired
    public CS_DocumentWritePlatformServiceJpaRepositoryImpl(final PlatformSecurityContext context,
            final DocumentRepository documentRepository, final CS_ContentRepositoryFactory documentStoreFactory) {
        this.context = context;
        this.documentRepository = documentRepository;
        this.contentRepositoryFactory = documentStoreFactory;
    }

    @Transactional
    @Override
    public Long createDocument(final DocumentCommand documentCommand, final Base64EncodedImage inputStream) {
        try {
            this.context.authenticatedUser();

            final DocumentCommandValidator validator = new DocumentCommandValidator(documentCommand);

            validateParentEntityType(documentCommand);

            validator.validateForCreate();

            final CS_ContentRepository contentRepository = this.contentRepositoryFactory.getRepository();

            final String fileLocation = contentRepository.saveImage(inputStream, documentCommand);

            final Document document = Document.createNew(documentCommand.getParentEntityType(), documentCommand.getParentEntityId(),
                    documentCommand.getName(), documentCommand.getFileName(), documentCommand.getSize(), documentCommand.getType(),
                    documentCommand.getDescription(), fileLocation, contentRepository.getStorageType());

            this.documentRepository.save(document);

            return document.getId();
        } catch (final DataIntegrityViolationException dve) {
            logger.error(dve.getMessage(), dve);
            throw new PlatformDataIntegrityException("error.msg.document.unknown.data.integrity.issue",
                    "Unknown data integrity issue with resource.");
        }
    }


    private void validateParentEntityType(final DocumentCommand documentCommand) {
        if (!checkValidEntityType(documentCommand.getParentEntityType())) { throw new InvalidEntityTypeForDocumentManagementException(
                documentCommand.getParentEntityType()); }
    }

    private static boolean checkValidEntityType(final String entityType) {
        for (final DOCUMENT_MANAGEMENT_ENTITY entities : DOCUMENT_MANAGEMENT_ENTITY.values()) {
            if (entities.name().equalsIgnoreCase(entityType)) { return true; }
        }
        return false;
    }

    /*** Entities for document Management **/
    public static enum DOCUMENT_MANAGEMENT_ENTITY {
        CLIENTS, CLIENT_IDENTIFIERS, STAFF, LOANS, SAVINGS, GROUPS,IMPORT;

        @Override
        public String toString() {
            return name().toString().toLowerCase();
        }
    }
}