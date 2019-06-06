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
package org.cs.portfolio.loanaccount.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.useradministration.domain.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.cs.portfolio.loanaccount.domain.CS_CoMaker;
import org.cs.portfolio.client.domain.CS_KycInfo;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.cs.portfolio.loanaccount.domain.CS_CoMakerRepository;
import org.cs.portfolio.client.service.CS_KYCWritePlatformService;
import org.springframework.transaction.annotation.Transactional;

import org.cs.portfolio.loanaccount.exception.CS_CoMakerNotFoundException;



@Service
public class CS_CoMakerWritePlatformServiceImpl implements CS_CoMakerWritePlatformService {

    private final static Logger logger = LoggerFactory.getLogger(CS_CoMakerWritePlatformServiceImpl.class);

    private final CS_PlatformSecurityContext context;
    private final CS_CoMakerRepository comakerRepository;
    private final CodeValueRepositoryWrapper codeValueRepository;
    private final CS_KYCWritePlatformService kycRepository;

    @Autowired
    public CS_CoMakerWritePlatformServiceImpl(final CS_PlatformSecurityContext context,
            final CS_CoMakerRepository comakerRepository,
            final CodeValueRepositoryWrapper codeValueRepository,
            final CS_KYCWritePlatformService kycRepository) {
        this.context = context;
        this.comakerRepository = comakerRepository;
        this.codeValueRepository = codeValueRepository;
        this.kycRepository = kycRepository;
    }

    @Transactional
    @Override
    public CS_CoMaker CreateCoMaker(JsonCommand command, Loan loan, CS_KycInfo kycInfo){

        CodeValue gender = null;
        final Long genderId = command.longValueOfParameterNamed("genderId");
        if (genderId != null) {
            gender = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("gender", genderId);
        }

        CS_CoMaker comaker = CS_CoMaker.createNew(loan, kycInfo, gender, command);
        this.comakerRepository.save(comaker);
        return comaker;
    }

    @Transactional
    @Override
    public CommandProcessingResult UpdateCoMaker(CS_JsonCommand command){
        JsonCommand jCom = command.thisToJsonCommand();
        this.context.authenticatedUser();
        JsonCommand coMakerCommand = JsonCommand.fromExistingCommand(jCom, jCom.jsonElement("comaker"));
        JsonCommand coMakerInfoCommand = JsonCommand.fromExistingCommand(coMakerCommand, coMakerCommand.jsonElement("info"));
        JsonCommand coMakerKycCommand = JsonCommand.fromExistingCommand(coMakerCommand, coMakerCommand.jsonElement("kyc"));
        Long coMakerId = coMakerInfoCommand.longValueOfParameterNamed("id");
        Long kycId = coMakerKycCommand.longValueOfParameterNamed("id");

        CS_CoMaker comaker = this.comakerRepository.findOne(coMakerId);
        if (comaker == null) { throw new CS_CoMakerNotFoundException(coMakerId); }
        CS_KycInfo kyc = this.kycRepository.updateKYC(kycId, coMakerKycCommand);

        final Map<String, Object> changes = comaker.update(coMakerInfoCommand);


        if (changes.containsKey("gender")) {

            CodeValue gender = null;
            final Long genderId = command.longValueOfParameterNamed("genderId");
            if (genderId != null) {
                gender = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection("gender", genderId);
            }
            comaker.updateGender(gender);
        }

        if (!changes.isEmpty()) {
            this.comakerRepository.saveAndFlush(comaker);
        }

        return new CommandProcessingResultBuilder().withEntityId(comaker.getId()).build();

    }
}
