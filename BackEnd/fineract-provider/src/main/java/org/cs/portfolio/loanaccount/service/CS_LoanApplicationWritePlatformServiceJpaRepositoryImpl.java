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
import java.util.*;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.fineract.infrastructure.accountnumberformat.domain.AccountNumberFormat;
import org.apache.fineract.infrastructure.accountnumberformat.domain.AccountNumberFormatRepositoryWrapper;
import org.apache.fineract.infrastructure.accountnumberformat.domain.EntityAccountType;
import org.apache.fineract.infrastructure.configuration.domain.ConfigurationDomainService;
import org.apache.fineract.infrastructure.configuration.domain.GlobalConfigurationProperty;
import org.apache.fineract.infrastructure.configuration.domain.GlobalConfigurationRepositoryWrapper;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.GeneralPlatformDomainRuleException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.infrastructure.dataqueries.data.EntityTables;
import org.apache.fineract.infrastructure.dataqueries.data.StatusEnum;
import org.apache.fineract.infrastructure.dataqueries.service.EntityDatatableChecksWritePlatformService;
import org.apache.fineract.infrastructure.entityaccess.FineractEntityAccessConstants;
import org.apache.fineract.infrastructure.entityaccess.domain.*;
import org.apache.fineract.infrastructure.entityaccess.exception.NotOfficeSpecificProductException;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.account.domain.AccountAssociationType;
import org.apache.fineract.portfolio.account.domain.AccountAssociations;
import org.apache.fineract.portfolio.account.domain.AccountAssociationsRepository;
import org.apache.fineract.portfolio.accountdetails.domain.AccountType;
import org.apache.fineract.portfolio.calendar.domain.*;
import org.apache.fineract.portfolio.calendar.domain.Calendar;
import org.apache.fineract.portfolio.calendar.exception.CalendarNotFoundException;
import org.apache.fineract.portfolio.charge.domain.Charge;
import org.apache.fineract.portfolio.client.domain.AccountNumberGenerator;
import org.apache.fineract.portfolio.client.domain.Client;
import org.apache.fineract.portfolio.client.domain.ClientRepositoryWrapper;
import org.apache.fineract.portfolio.common.BusinessEventNotificationConstants.BUSINESS_ENTITY;
import org.apache.fineract.portfolio.common.BusinessEventNotificationConstants.BUSINESS_EVENTS;
import org.apache.fineract.portfolio.common.domain.PeriodFrequencyType;
import org.apache.fineract.portfolio.common.service.BusinessEventNotifierService;
import org.apache.fineract.portfolio.group.domain.Group;
import org.apache.fineract.portfolio.group.domain.GroupRepositoryWrapper;
import org.apache.fineract.portfolio.loanaccount.api.LoanApiConstants;
import org.apache.fineract.portfolio.loanaccount.domain.*;
import org.apache.fineract.portfolio.loanaccount.exception.LoanApplicationDateException;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.LoanApplicationTerms;
import org.apache.fineract.portfolio.loanaccount.loanschedule.service.LoanScheduleAssembler;
import org.apache.fineract.portfolio.loanaccount.serialization.LoanApplicationCommandFromApiJsonHelper;
import org.apache.fineract.portfolio.loanproduct.data.LoanProductData;
import org.apache.fineract.portfolio.loanproduct.domain.*;
import org.apache.fineract.portfolio.loanproduct.service.LoanProductReadPlatformService;
import org.apache.fineract.portfolio.loanproduct.exception.LoanProductNotFoundException;
import org.apache.fineract.portfolio.loanproduct.serialization.LoanProductDataValidator;
import org.apache.fineract.portfolio.note.domain.Note;
import org.apache.fineract.portfolio.note.domain.NoteRepository;
import org.apache.fineract.portfolio.savings.domain.SavingsAccount;
import org.apache.fineract.portfolio.savings.domain.SavingsAccountAssembler;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import org.apache.fineract.portfolio.loanaccount.service.LoanAssembler;
import org.apache.fineract.portfolio.loanaccount.service.LoanReadPlatformService;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.cs.portfolio.client.service.CS_KYCWritePlatformService;
import org.cs.portfolio.loanaccount.service.CS_CoMakerWritePlatformService;
import org.cs.portfolio.loanaccount.domain.CS_CoMaker;
import org.cs.portfolio.client.domain.CS_KycInfo;


@Service
public class CS_LoanApplicationWritePlatformServiceJpaRepositoryImpl implements CS_LoanApplicationWritePlatformService {

    private final static Logger logger = LoggerFactory.getLogger(CS_LoanApplicationWritePlatformServiceJpaRepositoryImpl.class);

    private final PlatformSecurityContext context;
    private final FromJsonHelper fromJsonHelper;
    private final LoanProductDataValidator loanProductCommandFromApiJsonDeserializer;
    private final LoanApplicationCommandFromApiJsonHelper fromApiJsonDeserializer;
    private final LoanRepositoryWrapper loanRepositoryWrapper;
    private final NoteRepository noteRepository;
    private final LoanAssembler loanAssembler;
    private final ClientRepositoryWrapper clientRepository;
    private final LoanProductRepository loanProductRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final GroupRepositoryWrapper groupRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarInstanceRepository calendarInstanceRepository;
    private final SavingsAccountAssembler savingsAccountAssembler;
    private final AccountAssociationsRepository accountAssociationsRepository;
    private final LoanReadPlatformService loanReadPlatformService;
    private final AccountNumberFormatRepositoryWrapper accountNumberFormatRepository;
    private final BusinessEventNotifierService businessEventNotifierService;
    private final ConfigurationDomainService configurationDomainService;
    private final LoanScheduleAssembler loanScheduleAssembler;
    private final EntityDatatableChecksWritePlatformService entityDatatableChecksWritePlatformService;
    private final GlobalConfigurationRepositoryWrapper globalConfigurationRepository;
    private final FineractEntityToEntityMappingRepository repository;
    private final FineractEntityRelationRepository fineractEntityRelationRepository;
    private final LoanProductReadPlatformService loanProductReadPlatformService;
    private final CS_KYCWritePlatformService kycPlatformService;
    private final CS_CoMakerWritePlatformService coMakerPlatformService;

    @Autowired
    public CS_LoanApplicationWritePlatformServiceJpaRepositoryImpl(final PlatformSecurityContext context, final FromJsonHelper fromJsonHelper,
            final LoanApplicationCommandFromApiJsonHelper fromApiJsonDeserializer,
            final LoanProductDataValidator loanProductCommandFromApiJsonDeserializer,
            final LoanAssembler loanAssembler,
            final LoanRepositoryWrapper loanRepositoryWrapper,
            final NoteRepository noteRepository,
            final ClientRepositoryWrapper clientRepository,
            final LoanProductRepository loanProductRepository, final AccountNumberGenerator accountNumberGenerator,
            final GroupRepositoryWrapper groupRepository,
            final CalendarRepository calendarRepository, final CalendarInstanceRepository calendarInstanceRepository,
            final SavingsAccountAssembler savingsAccountAssembler, final AccountAssociationsRepository accountAssociationsRepository,
            final LoanReadPlatformService loanReadPlatformService,
            final AccountNumberFormatRepositoryWrapper accountNumberFormatRepository,
            final BusinessEventNotifierService businessEventNotifierService, final ConfigurationDomainService configurationDomainService,
            final LoanScheduleAssembler loanScheduleAssembler,
            final GlobalConfigurationRepositoryWrapper globalConfigurationRepository,
            final FineractEntityToEntityMappingRepository repository, final FineractEntityRelationRepository fineractEntityRelationRepository,
            final EntityDatatableChecksWritePlatformService entityDatatableChecksWritePlatformService,
            final LoanProductReadPlatformService loanProductReadPlatformService,
            final CS_KYCWritePlatformService kycPlatformService,
            final CS_CoMakerWritePlatformService coMakerPlatformService) {
        this.context = context;
        this.fromJsonHelper = fromJsonHelper;
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;
        this.loanProductCommandFromApiJsonDeserializer = loanProductCommandFromApiJsonDeserializer;
        this.loanAssembler = loanAssembler;
        this.noteRepository = noteRepository;
        this.clientRepository = clientRepository;
        this.loanProductRepository = loanProductRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.groupRepository = groupRepository;
        this.calendarRepository = calendarRepository;
        this.calendarInstanceRepository = calendarInstanceRepository;
        this.savingsAccountAssembler = savingsAccountAssembler;
        this.accountAssociationsRepository = accountAssociationsRepository;
        this.loanReadPlatformService = loanReadPlatformService;
        this.accountNumberFormatRepository = accountNumberFormatRepository;
        this.businessEventNotifierService = businessEventNotifierService;
        this.configurationDomainService = configurationDomainService;
        this.loanScheduleAssembler = loanScheduleAssembler;
        this.entityDatatableChecksWritePlatformService = entityDatatableChecksWritePlatformService;
        this.globalConfigurationRepository = globalConfigurationRepository;
        this.repository = repository;
        this.fineractEntityRelationRepository = fineractEntityRelationRepository;
        this.loanRepositoryWrapper = loanRepositoryWrapper;
        this.loanProductReadPlatformService = loanProductReadPlatformService;
        this.kycPlatformService = kycPlatformService;
        this.coMakerPlatformService = coMakerPlatformService;
    }

    @Transactional
    @Override
    public CommandProcessingResult submitApplication(final CS_JsonCommand command) {
        JsonCommand jCom = command.thisToJsonCommand();
        JsonCommand loanCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("loan"));
        JsonCommand coMakerCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("comaker"));
        JsonCommand coMakerInfoCommand = JsonCommand.fromExistingCommand(coMakerCommand, coMakerCommand.jsonElement("info"));
        JsonCommand coMakerKycCommand = JsonCommand.fromExistingCommand(coMakerCommand, coMakerCommand.jsonElement("kyc"));
        try {
            Loan newLoanApplication = addLoan(loanCommand);
            CS_KycInfo coMakerKyc = addKYC(coMakerKycCommand, null);
            CS_CoMaker coMakerInfo = addCoMaker(coMakerInfoCommand, newLoanApplication, coMakerKyc);

            return new CommandProcessingResultBuilder() //
                    .withCommandId(jCom.commandId()) //
                    .withEntityId(newLoanApplication.getId()) //
                    .withOfficeId(newLoanApplication.getOfficeId()) //
                    .withClientId(newLoanApplication.getClientId()) //
                    .withGroupId(newLoanApplication.getGroupId()) //
                    .withLoanId(newLoanApplication.getId()) //
                    .build();
        } catch (final DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(jCom, dve.getMostSpecificCause(), dve);
            return CommandProcessingResult.empty();
        }catch(final PersistenceException dve) {
        	Throwable throwable = ExceptionUtils.getRootCause(dve.getCause()) ;
            handleDataIntegrityIssues(jCom, throwable, dve);
         	return CommandProcessingResult.empty();
        }
    }

    @Override
    public Loan addLoan(JsonCommand command){

            final AppUser currentUser = getAppUserIfPresent();
            boolean isMeetingMandatoryForJLGLoans = configurationDomainService.isMeetingMandatoryForJLGLoans();
            final Long productId = this.fromJsonHelper.extractLongNamed("productId", command.parsedJson());
            final LoanProduct loanProduct = this.loanProductRepository.findOne(productId);
            if (loanProduct == null) { throw new LoanProductNotFoundException(productId); }

            final Long clientId = this.fromJsonHelper.extractLongNamed("clientId", command.parsedJson());
                        if(clientId !=null){
                        Client client= this.clientRepository.findOneWithNotFoundDetection(clientId);
                        officeSpecificLoanProductValidation( productId,client.getOffice().getId());
                        }
                        final Long groupId = this.fromJsonHelper.extractLongNamed("groupId", command.parsedJson());
                        if(groupId != null){
                            Group group= this.groupRepository.findOneWithNotFoundDetection(groupId);
                            officeSpecificLoanProductValidation( productId,group.getOffice().getId());
                        }
            
            this.fromApiJsonDeserializer.validateForCreate(command.json(), isMeetingMandatoryForJLGLoans, loanProduct);

            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("loan");

            if (loanProduct.useBorrowerCycle()) {
                Integer cycleNumber = 0;
                if (clientId != null) {
                    cycleNumber = this.loanReadPlatformService.retriveLoanCounter(clientId, loanProduct.getId());
                } else if (groupId != null) {
                    cycleNumber = this.loanReadPlatformService.retriveLoanCounter(groupId, AccountType.GROUP.getValue(),
                            loanProduct.getId());
                }
                this.loanProductCommandFromApiJsonDeserializer.validateMinMaxConstraints(command.parsedJson(), baseDataValidator,
                        loanProduct, cycleNumber);
            } else {
                this.loanProductCommandFromApiJsonDeserializer.validateMinMaxConstraints(command.parsedJson(), baseDataValidator,
                        loanProduct);
            }
            if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException(dataValidationErrors); }

            final Loan newLoanApplication = this.loanAssembler.assembleFrom(command, currentUser);

         checkForProductMixRestrictions(newLoanApplication);

            validateSubmittedOnDate(newLoanApplication);

            final LoanProductRelatedDetail productRelatedDetail = newLoanApplication.repaymentScheduleDetail();

            if (loanProduct.getLoanProductConfigurableAttributes() != null) {
                updateProductRelatedDetails(productRelatedDetail, newLoanApplication);
            }

            this.fromApiJsonDeserializer.validateLoanTermAndRepaidEveryValues(newLoanApplication.getTermFrequency(),
                    newLoanApplication.getTermPeriodFrequencyType(), productRelatedDetail.getNumberOfRepayments(),
                    productRelatedDetail.getRepayEvery(), productRelatedDetail.getRepaymentPeriodFrequencyType().getValue(),
                    newLoanApplication);

            if(loanProduct.canUseForTopup() && clientId != null){
                final Boolean isTopup = command.booleanObjectValueOfParameterNamed(LoanApiConstants.isTopup);
                if(null == isTopup){
                    newLoanApplication.setIsTopup(false);
                }else{
                    newLoanApplication.setIsTopup(isTopup);
                }

                if(newLoanApplication.isTopup()){
                    final Long loanIdToClose = command.longValueOfParameterNamed(LoanApiConstants.loanIdToClose);
                    final Loan loanToClose = this.loanRepositoryWrapper.findNonClosedLoanThatBelongsToClient(loanIdToClose, clientId);
                    if(loanToClose == null){
                        throw new GeneralPlatformDomainRuleException("error.msg.loan.loanIdToClose.no.active.loan.associated.to.client.found",
                                "loanIdToClose is invalid, No Active Loan associated with the given Client ID found.");
                    }
                    if(loanToClose.isMultiDisburmentLoan() && !loanToClose.isInterestRecalculationEnabledForProduct()){
                        throw new GeneralPlatformDomainRuleException(
                                "error.msg.loan.topup.on.multi.tranche.loan.without.interest.recalculation.not.supported",
                                "Topup on loan with multi-tranche disbursal and without interest recalculation is not supported.");
                    }
                    final LocalDate disbursalDateOfLoanToClose = loanToClose.getDisbursementDate();
                    if(!newLoanApplication.getSubmittedOnDate().isAfter(disbursalDateOfLoanToClose)){
                        throw new GeneralPlatformDomainRuleException(
                                "error.msg.loan.submitted.date.should.be.after.topup.loan.disbursal.date",
                                "Submitted date of this loan application "+newLoanApplication.getSubmittedOnDate()
                                        +" should be after the disbursed date of loan to be closed "+ disbursalDateOfLoanToClose);
                    }
                    if(!loanToClose.getCurrencyCode().equals(newLoanApplication.getCurrencyCode())){
                        throw new GeneralPlatformDomainRuleException("error.msg.loan.to.be.closed.has.different.currency",
                                "loanIdToClose is invalid, Currency code is different.");
                    }
                    final LocalDate lastUserTransactionOnLoanToClose = loanToClose.getLastUserTransactionDate();
                    if(newLoanApplication.getDisbursementDate().isBefore(lastUserTransactionOnLoanToClose)){
                        throw new GeneralPlatformDomainRuleException(
                                "error.msg.loan.disbursal.date.should.be.after.last.transaction.date.of.loan.to.be.closed",
                                "Disbursal date of this loan application "+newLoanApplication.getDisbursementDate()
                                        +" should be after last transaction date of loan to be closed "+ lastUserTransactionOnLoanToClose);
                    }
                    BigDecimal loanOutstanding = this.loanReadPlatformService.retrieveLoanPrePaymentTemplate(loanIdToClose,
                            newLoanApplication.getDisbursementDate()).getAmount();
                    final BigDecimal firstDisbursalAmount = newLoanApplication.getFirstDisbursalAmount();
                    if(loanOutstanding.compareTo(firstDisbursalAmount) > 0){
                        throw new GeneralPlatformDomainRuleException("error.msg.loan.amount.less.than.outstanding.of.loan.to.be.closed",
                                "Topup loan amount should be greater than outstanding amount of loan to be closed.");
                    }

                    final LoanTopupDetails topupDetails = new LoanTopupDetails(newLoanApplication, loanIdToClose);
                    newLoanApplication.setTopupLoanDetails(topupDetails);
                }
            }

            this.loanRepositoryWrapper.save(newLoanApplication);

            if (loanProduct.isInterestRecalculationEnabled()) {
                this.fromApiJsonDeserializer.validateLoanForInterestRecalculation(newLoanApplication);
                createAndPersistCalendarInstanceForInterestRecalculation(newLoanApplication);
            }

            if (newLoanApplication.isAccountNumberRequiresAutoGeneration()) {
                final AccountNumberFormat accountNumberFormat = this.accountNumberFormatRepository
                        .findByAccountType(EntityAccountType.LOAN);
                newLoanApplication.updateAccountNo(this.accountNumberGenerator.generate(newLoanApplication, accountNumberFormat));
                this.loanRepositoryWrapper.save(newLoanApplication);
            }

            final String submittedOnNote = command.stringValueOfParameterNamed("submittedOnNote");
            if (StringUtils.isNotBlank(submittedOnNote)) {
                final Note note = Note.loanNote(newLoanApplication, submittedOnNote);
                this.noteRepository.save(note);
            }

            // Save calendar instance
            final Long calendarId = command.longValueOfParameterNamed("calendarId");
            Calendar calendar = null;

            if (calendarId != null && calendarId != 0) {
                calendar = this.calendarRepository.findOne(calendarId);
                if (calendar == null) { throw new CalendarNotFoundException(calendarId); }

                final CalendarInstance calendarInstance = new CalendarInstance(calendar, newLoanApplication.getId(),
                        CalendarEntityType.LOANS.getValue());
                this.calendarInstanceRepository.save(calendarInstance);
            } else {
                final LoanApplicationTerms loanApplicationTerms = this.loanScheduleAssembler.assembleLoanTerms(command.parsedJson());
                final Integer repaymentFrequencyNthDayType = command.integerValueOfParameterNamed("repaymentFrequencyNthDayType");
                if (loanApplicationTerms.getRepaymentPeriodFrequencyType() == PeriodFrequencyType.MONTHS
                        && repaymentFrequencyNthDayType != null) {
                    final String title = "loan_schedule_" + newLoanApplication.getId();
                    LocalDate calendarStartDate = loanApplicationTerms.getRepaymentsStartingFromLocalDate();
                    if (calendarStartDate == null) calendarStartDate = loanApplicationTerms.getExpectedDisbursementDate();
                    final CalendarFrequencyType calendarFrequencyType = CalendarFrequencyType.MONTHLY;
                    final Integer frequency = loanApplicationTerms.getRepaymentEvery();
                    final Integer repeatsOnDay = loanApplicationTerms.getWeekDayType().getValue();
                    final Integer repeatsOnNthDayOfMonth = loanApplicationTerms.getNthDay();
                    final Integer calendarEntityType = CalendarEntityType.LOANS.getValue();
                    final Calendar loanCalendar = Calendar.createRepeatingCalendar(title, calendarStartDate,
                            CalendarType.COLLECTION.getValue(), calendarFrequencyType, frequency, repeatsOnDay, repeatsOnNthDayOfMonth);
                    this.calendarRepository.save(loanCalendar);
                    final CalendarInstance calendarInstance = CalendarInstance.from(loanCalendar, newLoanApplication.getId(),
                            calendarEntityType);
                    this.calendarInstanceRepository.save(calendarInstance);
                }
            }

            // Save linked account information
            final Long savingsAccountId = command.longValueOfParameterNamed("linkAccountId");
            if (savingsAccountId != null) {
                final SavingsAccount savingsAccount = this.savingsAccountAssembler.assembleFrom(savingsAccountId);
                this.fromApiJsonDeserializer.validatelinkedSavingsAccount(savingsAccount, newLoanApplication);
                boolean isActive = true;
                final AccountAssociations accountAssociations = AccountAssociations.associateSavingsAccount(newLoanApplication,
                        savingsAccount, AccountAssociationType.LINKED_ACCOUNT_ASSOCIATION.getValue(), isActive);
                this.accountAssociationsRepository.save(accountAssociations);
            }

            if(command.parameterExists(LoanApiConstants.datatables)){
                this.entityDatatableChecksWritePlatformService.saveDatatables(StatusEnum.CREATE.getCode().longValue(),
                        EntityTables.LOAN.getName(), newLoanApplication.getId(), newLoanApplication.productId(),
                        command.arrayOfParameterNamed(LoanApiConstants.datatables));
            }

            this.entityDatatableChecksWritePlatformService.runTheCheckForProduct(newLoanApplication.getId(),
                    EntityTables.LOAN.getName(), StatusEnum.CREATE.getCode().longValue(),
                    EntityTables.LOAN.getForeignKeyColumnNameOnDatatable(), newLoanApplication.productId());

            this.businessEventNotifierService.notifyBusinessEventWasExecuted(BUSINESS_EVENTS.LOAN_CREATE,
                    constructEntityMap(BUSINESS_ENTITY.LOAN, newLoanApplication));

            return newLoanApplication;
    }

    private CS_KycInfo addKYC(final JsonCommand command, Client client){
        return this.kycPlatformService.addKYC(command, client);
    }

    private CS_CoMaker addCoMaker(final JsonCommand command, Loan loan, CS_KycInfo kycInfo){
        return this.coMakerPlatformService.CreateCoMaker(command, loan, kycInfo);
    }


public void checkForProductMixRestrictions(final Loan loan) {

        final List<Long> activeLoansLoanProductIds;
        final Long productId = loan.loanProduct().getId();

        if (loan.isGroupLoan()) {
            activeLoansLoanProductIds = this.loanRepositoryWrapper.findActiveLoansLoanProductIdsByGroup(loan.getGroupId(),
                    LoanStatus.ACTIVE.getValue());
        } else {
            activeLoansLoanProductIds = this.loanRepositoryWrapper.findActiveLoansLoanProductIdsByClient(loan.getClientId(),
                    LoanStatus.ACTIVE.getValue());
        }
        checkForProductMixRestrictions(activeLoansLoanProductIds, productId, loan.loanProduct().productName());
    }

    private void checkForProductMixRestrictions(final List<Long> activeLoansLoanProductIds, final Long productId, final String productName) {

        if (!CollectionUtils.isEmpty(activeLoansLoanProductIds)) {
            final Collection<LoanProductData> restrictedPrdouctsList = this.loanProductReadPlatformService
                    .retrieveRestrictedProductsForMix(productId);
            for (final LoanProductData restrictedProduct : restrictedPrdouctsList) {
                if (activeLoansLoanProductIds.contains(restrictedProduct.getId())) { throw new GeneralPlatformDomainRuleException("error.msg.loan.applied.or.to.be.disbursed.can.not.co-exist.with.the.loan.already.active.to.this.client", "This loan could not be applied/disbursed as the loan and `" + restrictedProduct + "` are not allowed to co-exist"); }
            }
        }
    }

    private void updateProductRelatedDetails(LoanProductRelatedDetail productRelatedDetail, Loan loan) {
        final Boolean amortization = loan.loanProduct().getLoanProductConfigurableAttributes().getAmortizationBoolean();
        final Boolean arrearsTolerance = loan.loanProduct().getLoanProductConfigurableAttributes().getArrearsToleranceBoolean();
        final Boolean graceOnArrearsAging = loan.loanProduct().getLoanProductConfigurableAttributes().getGraceOnArrearsAgingBoolean();
        final Boolean interestCalcPeriod = loan.loanProduct().getLoanProductConfigurableAttributes().getInterestCalcPeriodBoolean();
        final Boolean interestMethod = loan.loanProduct().getLoanProductConfigurableAttributes().getInterestMethodBoolean();
        final Boolean graceOnPrincipalAndInterestPayment = loan.loanProduct().getLoanProductConfigurableAttributes()
                .getGraceOnPrincipalAndInterestPaymentBoolean();
        final Boolean repaymentEvery = loan.loanProduct().getLoanProductConfigurableAttributes().getRepaymentEveryBoolean();
        final Boolean transactionProcessingStrategy = loan.loanProduct().getLoanProductConfigurableAttributes()
                .getTransactionProcessingStrategyBoolean();

        if (!amortization) {
            productRelatedDetail.setAmortizationMethod(loan.loanProduct().getLoanProductRelatedDetail().getAmortizationMethod());
        }
        if (!arrearsTolerance) {
            productRelatedDetail.setInArrearsTolerance(loan.loanProduct().getLoanProductRelatedDetail().getArrearsTolerance());
        }
        if (!graceOnArrearsAging) {
            productRelatedDetail.setGraceOnArrearsAgeing(loan.loanProduct().getLoanProductRelatedDetail().getGraceOnArrearsAgeing());
        }
        if (!interestCalcPeriod) {
            productRelatedDetail.setInterestCalculationPeriodMethod(loan.loanProduct().getLoanProductRelatedDetail()
                    .getInterestCalculationPeriodMethod());
        }
        if (!interestMethod) {
            productRelatedDetail.setInterestMethod(loan.loanProduct().getLoanProductRelatedDetail().getInterestMethod());
        }
        if (!graceOnPrincipalAndInterestPayment) {
            productRelatedDetail.setGraceOnInterestPayment(loan.loanProduct().getLoanProductRelatedDetail().getGraceOnInterestPayment());
            productRelatedDetail.setGraceOnPrincipalPayment(loan.loanProduct().getLoanProductRelatedDetail().getGraceOnPrincipalPayment());
        }
        if (!repaymentEvery) {
            productRelatedDetail.setRepayEvery(loan.loanProduct().getLoanProductRelatedDetail().getRepayEvery());
        }
        if (!transactionProcessingStrategy) {
            loan.updateTransactionProcessingStrategy(loan.loanProduct().getRepaymentStrategy());
        }
    }

    private void createAndPersistCalendarInstanceForInterestRecalculation(final Loan loan) {

        LocalDate calendarStartDate = loan.getExpectedDisbursedOnLocalDate();
        Integer repeatsOnDay = null;
        final RecalculationFrequencyType recalculationFrequencyType = loan.loanInterestRecalculationDetails().getRestFrequencyType();
        Integer recalculationFrequencyNthDay = loan.loanInterestRecalculationDetails().getRestFrequencyOnDay();
        if (recalculationFrequencyNthDay == null) {
            recalculationFrequencyNthDay = loan.loanInterestRecalculationDetails().getRestFrequencyNthDay();
            repeatsOnDay = loan.loanInterestRecalculationDetails().getRestFrequencyWeekday();
        }

        Integer frequency = loan.loanInterestRecalculationDetails().getRestInterval();
        CalendarEntityType calendarEntityType = CalendarEntityType.LOAN_RECALCULATION_REST_DETAIL;
        final String title = "loan_recalculation_detail_" + loan.loanInterestRecalculationDetails().getId();

        createCalendar(loan, calendarStartDate, recalculationFrequencyNthDay, repeatsOnDay, recalculationFrequencyType, frequency,
                calendarEntityType, title);

        if (loan.loanInterestRecalculationDetails().getInterestRecalculationCompoundingMethod().isCompoundingEnabled()) {
            LocalDate compoundingStartDate = loan.getExpectedDisbursedOnLocalDate();
            Integer compoundingRepeatsOnDay = null;
            final RecalculationFrequencyType recalculationCompoundingFrequencyType = loan.loanInterestRecalculationDetails()
                    .getCompoundingFrequencyType();
            Integer recalculationCompoundingFrequencyNthDay = loan.loanInterestRecalculationDetails().getCompoundingFrequencyOnDay();
            if (recalculationCompoundingFrequencyNthDay == null) {
                recalculationCompoundingFrequencyNthDay = loan.loanInterestRecalculationDetails().getCompoundingFrequencyNthDay();
                compoundingRepeatsOnDay = loan.loanInterestRecalculationDetails().getCompoundingFrequencyWeekday();
            }

            Integer compoundingFrequency = loan.loanInterestRecalculationDetails().getCompoundingInterval();
            CalendarEntityType compoundingCalendarEntityType = CalendarEntityType.LOAN_RECALCULATION_COMPOUNDING_DETAIL;
            final String compoundingCalendarTitle = "loan_recalculation_detail_compounding_frequency"
                    + loan.loanInterestRecalculationDetails().getId();

            createCalendar(loan, compoundingStartDate, recalculationCompoundingFrequencyNthDay, compoundingRepeatsOnDay,
                    recalculationCompoundingFrequencyType, compoundingFrequency, compoundingCalendarEntityType, compoundingCalendarTitle);
        }

    }

    private void createCalendar(final Loan loan, LocalDate calendarStartDate, Integer recalculationFrequencyNthDay,
            final Integer repeatsOnDay, final RecalculationFrequencyType recalculationFrequencyType, Integer frequency,
            CalendarEntityType calendarEntityType, final String title) {
        CalendarFrequencyType calendarFrequencyType = CalendarFrequencyType.INVALID;
        Integer updatedRepeatsOnDay = repeatsOnDay;
        switch (recalculationFrequencyType) {
            case DAILY:
                calendarFrequencyType = CalendarFrequencyType.DAILY;
            break;
            case MONTHLY:
                calendarFrequencyType = CalendarFrequencyType.MONTHLY;
            break;
            case SAME_AS_REPAYMENT_PERIOD:
                frequency = loan.repaymentScheduleDetail().getRepayEvery();
                calendarFrequencyType = CalendarFrequencyType.from(loan.repaymentScheduleDetail().getRepaymentPeriodFrequencyType());
                calendarStartDate = loan.getExpectedDisbursedOnLocalDate();
                if (updatedRepeatsOnDay == null) {
                    updatedRepeatsOnDay = calendarStartDate.getDayOfWeek();
                }
            break;
            case WEEKLY:
                calendarFrequencyType = CalendarFrequencyType.WEEKLY;
            break;
            default:
            break;
        }
         
        final Calendar calendar = Calendar.createRepeatingCalendar(title, calendarStartDate, CalendarType.COLLECTION.getValue(),
                calendarFrequencyType, frequency, updatedRepeatsOnDay, recalculationFrequencyNthDay);
        final CalendarInstance calendarInstance = CalendarInstance.from(calendar, loan.loanInterestRecalculationDetails().getId(),
                calendarEntityType.getValue());
        this.calendarInstanceRepository.save(calendarInstance);
    }

    /*
     * Guaranteed to throw an exception no matter what the data integrity issue
     * is.
     */
    private void handleDataIntegrityIssues(final JsonCommand command, final Throwable realCause, final Exception dve) {
    	
        if (realCause.getMessage().contains("loan_account_no_UNIQUE") || realCause.getCause().getMessage().contains("loan_account_no_UNIQUE")) {

            final String accountNo = command.stringValueOfParameterNamed("accountNo");
            throw new PlatformDataIntegrityException("error.msg.loan.duplicate.accountNo", "Loan with accountNo `" + accountNo
                    + "` already exists", "accountNo", accountNo);
        } else if (realCause.getMessage().contains("loan_externalid_UNIQUE") || realCause.getCause().getMessage().contains("loan_externalid_UNIQUE")) {
            final String externalId = command.stringValueOfParameterNamed("externalId");
            throw new PlatformDataIntegrityException("error.msg.loan.duplicate.externalId", "Loan with externalId `" + externalId
                    + "` already exists", "externalId", externalId);
        }

        logAsErrorUnexpectedDataIntegrityException(dve);
        throw new PlatformDataIntegrityException("error.msg.unknown.data.integrity.issue", "Unknown data integrity issue with resource.");
    }

    private void logAsErrorUnexpectedDataIntegrityException(final Exception dve) {
        logger.error(dve.getMessage(), dve);
    }

    private void validateSubmittedOnDate(final Loan loan) {
        final LocalDate startDate = loan.loanProduct().getStartDate();
        final LocalDate closeDate = loan.loanProduct().getCloseDate();
        final LocalDate expectedFirstRepaymentOnDate = loan.getExpectedFirstRepaymentOnDate();
        final LocalDate submittedOnDate = loan.getSubmittedOnDate();

        String defaultUserMessage = "";
        if (startDate != null && submittedOnDate.isBefore(startDate)) {
            defaultUserMessage = "submittedOnDate cannot be before the loan product startDate.";
            throw new LoanApplicationDateException("submitted.on.date.cannot.be.before.the.loan.product.start.date", defaultUserMessage,
                    submittedOnDate.toString(), startDate.toString());
        }

        if (closeDate != null && submittedOnDate.isAfter(closeDate)) {
            defaultUserMessage = "submittedOnDate cannot be after the loan product closeDate.";
            throw new LoanApplicationDateException("submitted.on.date.cannot.be.after.the.loan.product.close.date", defaultUserMessage,
                    submittedOnDate.toString(), closeDate.toString());
        }

        if (expectedFirstRepaymentOnDate != null && submittedOnDate.isAfter(expectedFirstRepaymentOnDate)) {
            defaultUserMessage = "submittedOnDate cannot be after the loans  expectedFirstRepaymentOnDate.";
            throw new LoanApplicationDateException("submitted.on.date.cannot.be.after.the.loan.expected.first.repayment.date",
                    defaultUserMessage, submittedOnDate.toString(), expectedFirstRepaymentOnDate.toString());
        }
    }

    private AppUser getAppUserIfPresent() {
        AppUser user = null;
        if (this.context != null) {
            user = this.context.getAuthenticatedUserIfPresent();
        }
        return user;
    }

    private Map<BUSINESS_ENTITY, Object> constructEntityMap(final BUSINESS_ENTITY entityEvent, Object entity) {
        Map<BUSINESS_ENTITY, Object> map = new HashMap<>(1);
        map.put(entityEvent, entity);
        return map;
    }

    private void officeSpecificLoanProductValidation(final Long productId, final Long officeId) {
    			final GlobalConfigurationProperty restrictToUserOfficeProperty = this.globalConfigurationRepository
    					.findOneByNameWithNotFoundDetection(
    							FineractEntityAccessConstants.GLOBAL_CONFIG_FOR_OFFICE_SPECIFIC_PRODUCTS);
    			if (restrictToUserOfficeProperty.isEnabled()) {
    				FineractEntityRelation fineractEntityRelation = fineractEntityRelationRepository
    						                               .findOneByCodeName(FineractEntityAccessType.OFFICE_ACCESS_TO_LOAN_PRODUCTS.toStr());
    				FineractEntityToEntityMapping officeToLoanProductMappingList = this.repository.findListByProductId(fineractEntityRelation, productId,
    						officeId);
    				if (officeToLoanProductMappingList == null) {
    					throw new NotOfficeSpecificProductException(productId, officeId);
    				}
    
    			}
    		}
    
}
