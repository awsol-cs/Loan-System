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
package org.cs.infrastructure.documentmanagement.contentrepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.fineract.infrastructure.core.domain.Base64EncodedImage;
import org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil;
import org.apache.fineract.infrastructure.documentmanagement.command.DocumentCommand;
import org.apache.fineract.infrastructure.documentmanagement.contentrepository.ContentRepositoryUtils;
import org.apache.fineract.infrastructure.documentmanagement.domain.StorageType;
import org.apache.fineract.infrastructure.documentmanagement.exception.ContentManagementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.pdf.codec.Base64;

public class CS_FileSystemContentRepository implements CS_ContentRepository {

    private final static Logger logger = LoggerFactory.getLogger(CS_FileSystemContentRepository.class);

    public static final String FINERACT_BASE_DIR = System.getProperty("user.home") + File.separator + ".fineract";


    @Override
    public String saveImage(final Base64EncodedImage uploadedInputStream, final DocumentCommand documentCommand) {
        final String fileName = documentCommand.getFileName();
        final String uploadDocumentLocation = generateFileParentDirectory(documentCommand.getParentEntityType(),
                documentCommand.getParentEntityId());

        makeDirectories(uploadDocumentLocation);

        final String fileLocation = uploadDocumentLocation + File.separator + fileName;

        try {
            final OutputStream out = new FileOutputStream(new File(fileLocation));
            final byte[] imgBytes = Base64.decode(uploadedInputStream.getBase64EncodedString());
            out.write(imgBytes);
            out.flush();
            out.close();
        } catch (final IOException ioe) {
            throw new ContentManagementException(fileName, ioe.getMessage());
        }
        return fileLocation;
    }
    
    @Override
    public StorageType getStorageType() {
        return StorageType.FILE_SYSTEM;
    }

    /**
     * Generate the directory path for storing the new document
     * 
     * @param entityType
     * @param entityId
     * @return
     */
    private String generateFileParentDirectory(final String entityType, final Long entityId) {
        return CS_FileSystemContentRepository.FINERACT_BASE_DIR + File.separator
                + ThreadLocalContextUtil.getTenant().getName().replaceAll(" ", "").trim() + File.separator + "documents" + File.separator
                + entityType + File.separator + entityId + File.separator + ContentRepositoryUtils.generateRandomString();
    }

    /**
     * Recursively create the directory if it does not exist *
     */
    private void makeDirectories(final String uploadDocumentLocation) {
        if (!new File(uploadDocumentLocation).isDirectory()) {
            new File(uploadDocumentLocation).mkdirs();
        }
    }

}