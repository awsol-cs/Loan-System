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
package org.cs.portfolio.client.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import org.cs.portfolio.client.domain.CS_KycInfo;
import org.cs.portfolio.client.domain.CS_KycRepository;
import org.apache.fineract.portfolio.client.domain.Client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class CS_KYCWritePlatformServiceJpaRepositoryImpl implements CS_KYCWritePlatformService {

    private final static Logger logger = LoggerFactory.getLogger(CS_KYCWritePlatformServiceJpaRepositoryImpl.class);

    private final PlatformSecurityContext context;
    private final CS_KycRepository kycRepository;
    private final CodeValueRepositoryWrapper codeValueRepository;

    @Autowired
    public CS_KYCWritePlatformServiceJpaRepositoryImpl(final PlatformSecurityContext context,
            final CS_KycRepository kycRepository,
            final CodeValueRepositoryWrapper codeValueRepository){
        this.context = context;
        this.kycRepository = kycRepository;
        this.codeValueRepository = codeValueRepository;
    }

    @Override
    public CS_KycInfo addKYC(final JsonCommand command, Object object){

            CodeValue maritalStatus = null;
            final Long maritalStatusId = command.longValueOfParameterNamed("maritalStatusId");
            if (maritalStatusId != null) {
                maritalStatus = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("MARITAL STATUS", maritalStatusId);
            }
            CodeValue educationalAttainment = null;
            final Long educationalAttainmentId = command.longValueOfParameterNamed("educationalAttainmentId");
            if (educationalAttainmentId != null) {
                educationalAttainment = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("educationalAttainment", educationalAttainmentId);
            }
            CodeValue residenceOwnership = null;
            final Long residenceOwnershipId = command.longValueOfParameterNamed("residenceOwnershipId");
            if (residenceOwnershipId != null) {
                residenceOwnership = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("residenceOwnership", residenceOwnershipId);
            }
            CodeValue employment = null;
            final Long employmentId = command.longValueOfParameterNamed("employmentId");
            if (employmentId != null) {
                employment = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("employment", employmentId);
            }
            CodeValue selfEmployed = null;
            final Long selfEmployedId = command.longValueOfParameterNamed("selfEmployedId");
            if (selfEmployedId != null) {
                selfEmployed = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("selfEmployed", selfEmployedId);
            }
            CodeValue rank = null;
            final Long rankId = command.longValueOfParameterNamed("rankId");
            if (rankId != null) {
                rank = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("rank", rankId);
            }
            CodeValue relatedToOfficer = null;
            final Long relatedToOfficerId = command.longValueOfParameterNamed("relatedToOfficerId");
            if (relatedToOfficerId != null) {
                relatedToOfficer = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("YesNo", relatedToOfficerId);
            }
            CodeValue relationshipOfOfficer = null;
            final Long relationshipOfOfficerId = command.longValueOfParameterNamed("relationshipOfOfficerId");
            if (relationshipOfOfficerId != null) {
                relationshipOfOfficer = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("relationshipOfOfficer", relationshipOfOfficerId);
            }

            Client client = null;
            if(object != null && object instanceof Client){
                client = (Client)object;
            }
            
            final CS_KycInfo newKYC = CS_KycInfo.createNew(
                client, maritalStatus, educationalAttainment, residenceOwnership, employment, selfEmployed,
                rank, relatedToOfficer, relationshipOfOfficer, command);
            this.kycRepository.save(newKYC);
            return newKYC;
    }

    @Override
    public CS_KycInfo updateKYC(final Long kycId, final JsonCommand command){

            final CS_KycInfo kycForUpdate = this.kycRepository.findOne(kycId);

            final Map<String, Object> changes = kycForUpdate.update(command);


            if (changes.containsKey("maritalStatus")) {

                CodeValue maritalStatus = null;
                final Long maritalStatusId = command.longValueOfParameterNamed("maritalStatusId");
                if (maritalStatusId != null) {
                    maritalStatus = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("maritalStatus", maritalStatusId);
                }
                kycForUpdate.updateMaritalStatus(maritalStatus);
            }
            if (changes.containsKey("educationalAttainment")) {

                CodeValue educationalAttainment = null;
                final Long educationalAttainmentId = command.longValueOfParameterNamed("educationalAttainmentId");
                if (educationalAttainmentId != null) {
                    educationalAttainment = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("educationalAttainment", educationalAttainmentId);
                }
                kycForUpdate.updateEducationalAttainment(educationalAttainment);
            }
            if (changes.containsKey("residenceOwnership")) {

                CodeValue residenceOwnership = null;
                final Long residenceOwnershipId = command.longValueOfParameterNamed("residenceOwnershipId");
                if (residenceOwnershipId != null) {
                    residenceOwnership = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("residenceOwnership", residenceOwnershipId);
                }
                kycForUpdate.updateResidenceOwnership(residenceOwnership);
            }
            if (changes.containsKey("employment")) {

                CodeValue employment = null;
                final Long employmentId = command.longValueOfParameterNamed("employmentId");
                if (employmentId != null) {
                    employment = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("employment", employmentId);
                }
                kycForUpdate.updateEmployment(employment);
            }
            if (changes.containsKey("selfEmployed")) {

                CodeValue selfEmployed = null;
                final Long selfEmployedId = command.longValueOfParameterNamed("selfEmployedId");
                if (selfEmployedId != null) {
                    selfEmployed = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("selfEmployed", selfEmployedId);
                }
                kycForUpdate.updateSelfEmployed(selfEmployed);
            }
            if (changes.containsKey("rank")) {

                CodeValue rank = null;
                final Long rankId = command.longValueOfParameterNamed("rankId");
                if (rankId != null) {
                    rank = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("rank", rankId);
                }
                kycForUpdate.updateRank(rank);
            }
            if (changes.containsKey("relatedToOfficer")) {

                CodeValue relatedToOfficer = null;
                final Long relatedToOfficerId = command.longValueOfParameterNamed("relatedToOfficerId");
                if (relatedToOfficerId != null) {
                    relatedToOfficer = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("relatedToOfficer", relatedToOfficerId);
                }
                kycForUpdate.updateRelatedToOfficer(relatedToOfficer);
            }
            if (changes.containsKey("relationshipOfOfficer")) {

                CodeValue relationshipOfOfficer = null;
                final Long relationshipOfOfficerId = command.longValueOfParameterNamed("relationshipOfOfficerId");
                if (relationshipOfOfficerId != null) {
                    relationshipOfOfficer = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("relationshipOfOfficer", relationshipOfOfficerId);
                }
                kycForUpdate.updateRelationshipOfOfficer(relationshipOfOfficer);
            }

            if (!changes.isEmpty()) {
                this.kycRepository.saveAndFlush(kycForUpdate);
            }
            
            return kycForUpdate;
    }
}
