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
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.portfolio.loanaccount.service.LoanAssembler;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallment;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepaymentScheduleInstallmentRepository;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.apache.fineract.portfolio.client.domain.Client;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.useradministration.domain.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.apache.fineract.portfolio.client.domain.ClientRepositoryWrapper;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepositoryWrapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


@Service
public class CS_LoanWritePlatformServiceJpaRepositoryImpl implements CS_LoanWritePlatformService {

    private final static Logger logger = LoggerFactory.getLogger(CS_LoanWritePlatformServiceJpaRepositoryImpl.class);

    private final CS_PlatformSecurityContext context;
    private final LoanAssembler loanAssembler;
    private final ClientRepositoryWrapper clientRepository;
    private final LoanRepositoryWrapper loanRepositoryWrapper;
    private final LoanRepaymentScheduleInstallmentRepository repaymentScheduleInstallmentRepository;

    @Autowired
    public CS_LoanWritePlatformServiceJpaRepositoryImpl(final CS_PlatformSecurityContext context,
            final LoanAssembler loanAssembler,
            final ClientRepositoryWrapper clientRepository,
            final LoanRepositoryWrapper loanRepositoryWrapper,
            final LoanRepaymentScheduleInstallmentRepository repaymentScheduleInstallmentRepository) {
        this.context = context;
        this.loanAssembler = loanAssembler;
        this.clientRepository = clientRepository;
        this.repaymentScheduleInstallmentRepository = repaymentScheduleInstallmentRepository;
        this.loanRepositoryWrapper = loanRepositoryWrapper;
    }

    private AppUser getAppUserIfPresent() {
        AppUser user = null;
        if (this.context != null) {
            user = this.context.getAuthenticatedUserIfPresent();
        }
        return user;
    }

    private void saveAndFlushLoanWithDataIntegrityViolationChecks(final Loan loan) {
        try {
            List<LoanRepaymentScheduleInstallment> installments = loan.getRepaymentScheduleInstallments();
            for (LoanRepaymentScheduleInstallment installment : installments) {
                if (installment.getId() == null) {
                    this.repaymentScheduleInstallmentRepository.save(installment);
                }
            }
            this.loanRepositoryWrapper.saveAndFlush(loan);
        } catch (final DataIntegrityViolationException e) {
            final Throwable realCause = e.getCause();
            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("loan.transaction");
            if (realCause.getMessage().toLowerCase().contains("external_id_unique")) {
                baseDataValidator.reset().parameter("externalId").failWithCode("value.must.be.unique");
            }
            if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                    "Validation errors exist.", dataValidationErrors); }
        }
    }

    @Override
    @Transactional
    public CommandProcessingResult transferLoan(CS_JsonCommand command) {
        final AppUser currentUser = getAppUserIfPresent();

        final long loanId = command.longValueOfParameterNamed("loanId");
        final Loan loan = this.loanAssembler.assembleFrom(loanId);
        final long clientId = command.longValueOfParameterNamed("clientId");
        final Client toClient = this.clientRepository.findOneWithNotFoundDetection(clientId);
        loan.updateClient(toClient);

        saveAndFlushLoanWithDataIntegrityViolationChecks(loan);

        return new CommandProcessingResultBuilder() //
                .withCommandId(command.commandId()) //
                .withEntityId(loan.getId()) //
                .withOfficeId(loan.getOfficeId()) //
                .withClientId(loan.getClientId()) //
                .withGroupId(loan.getGroupId()) //
                .withLoanId(loanId)
                .build();
    }
}
