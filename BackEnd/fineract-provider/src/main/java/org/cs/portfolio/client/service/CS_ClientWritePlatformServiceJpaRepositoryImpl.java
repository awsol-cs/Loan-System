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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandProcessingService;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.cs.commands.domain.CS_CommandWrapper;
import org.cs.commands.service.CS_CommandProcessingService;
import org.cs.commands.service.CS_CommandWrapperBuilder;
import org.apache.fineract.infrastructure.accountnumberformat.domain.AccountNumberFormat;
import org.apache.fineract.infrastructure.accountnumberformat.domain.AccountNumberFormatRepositoryWrapper;
import org.apache.fineract.infrastructure.accountnumberformat.domain.EntityAccountType;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.apache.fineract.infrastructure.configuration.data.GlobalConfigurationPropertyData;
import org.apache.fineract.infrastructure.configuration.service.ConfigurationReadPlatformService;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.infrastructure.dataqueries.data.EntityTables;
import org.apache.fineract.infrastructure.dataqueries.data.StatusEnum;
import org.apache.fineract.infrastructure.dataqueries.service.EntityDatatableChecksWritePlatformService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.organisation.office.domain.Office;
import org.apache.fineract.organisation.office.domain.OfficeRepositoryWrapper;
import org.apache.fineract.organisation.staff.domain.Staff;
import org.apache.fineract.organisation.staff.domain.StaffRepositoryWrapper;
import org.apache.fineract.portfolio.address.service.AddressWritePlatformService;
import org.apache.fineract.portfolio.client.api.ClientApiConstants;
import org.apache.fineract.portfolio.client.data.ClientDataValidator;
import org.apache.fineract.portfolio.client.domain.AccountNumberGenerator;
import org.apache.fineract.portfolio.client.domain.Client;
import org.apache.fineract.portfolio.client.domain.ClientRepositoryWrapper;
import org.apache.fineract.portfolio.client.domain.ClientStatus;
import org.apache.fineract.portfolio.client.domain.LegalForm;
import org.apache.fineract.portfolio.common.BusinessEventNotificationConstants.BUSINESS_ENTITY;
import org.apache.fineract.portfolio.common.BusinessEventNotificationConstants.BUSINESS_EVENTS;
import org.apache.fineract.portfolio.common.service.BusinessEventNotifierService;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepositoryWrapper;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.fineract.portfolio.client.service.ClientFamilyMembersWritePlatformService;
import org.cs.portfolio.client.domain.CS_KycInfo;
import org.cs.portfolio.client.domain.CS_KycRepository;
import org.cs.portfolio.loanaccount.service.CS_LoanApplicationWritePlatformService;
import org.cs.portfolio.client.service.CS_KYCWritePlatformService;
import org.cs.portfolio.loanaccount.service.CS_CoMakerWritePlatformService;
import org.cs.portfolio.loanaccount.domain.CS_CoMaker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class CS_ClientWritePlatformServiceJpaRepositoryImpl implements CS_ClientWritePlatformService {

    private final static Logger logger = LoggerFactory.getLogger(CS_ClientWritePlatformServiceJpaRepositoryImpl.class);

    private final PlatformSecurityContext context;
    private final ClientRepositoryWrapper clientRepository;
    private final CS_KycRepository kycRepository;
    private final CS_LoanApplicationWritePlatformService loanPlatformService;
    private final OfficeRepositoryWrapper officeRepositoryWrapper;
    private final ClientDataValidator fromApiJsonDeserializer;
    private final AccountNumberGenerator accountNumberGenerator;
    private final StaffRepositoryWrapper staffRepository;
    private final CodeValueRepositoryWrapper codeValueRepository;
    private final CommandProcessingService commandProcessingService;
    private final CS_CommandProcessingService cs_CommandProcessingService;
    private final AccountNumberFormatRepositoryWrapper accountNumberFormatRepository;
    private final ConfigurationReadPlatformService configurationReadPlatformService;
    private final AddressWritePlatformService addressWritePlatformService;
    private final ClientFamilyMembersWritePlatformService clientFamilyMembersWritePlatformService;
    private final BusinessEventNotifierService businessEventNotifierService;
    private final EntityDatatableChecksWritePlatformService entityDatatableChecksWritePlatformService;
    private final CS_KYCWritePlatformService kycPlatformService;
    private final CS_CoMakerWritePlatformService coMakerPlatformService;

    @Autowired
    public CS_ClientWritePlatformServiceJpaRepositoryImpl(final PlatformSecurityContext context,
            final ClientRepositoryWrapper clientRepository, 
            final CS_KycRepository kycRepository,
            final CS_LoanApplicationWritePlatformService loanPlatformService,
            final OfficeRepositoryWrapper officeRepositoryWrapper,
            final ClientDataValidator fromApiJsonDeserializer, 
            final AccountNumberGenerator accountNumberGenerator,
            final StaffRepositoryWrapper staffRepository,
            final CodeValueRepositoryWrapper codeValueRepository,
            final CommandProcessingService commandProcessingService, 
            final CS_CommandProcessingService cs_CommandProcessingService, 
            final AccountNumberFormatRepositoryWrapper accountNumberFormatRepository,
            final ConfigurationReadPlatformService configurationReadPlatformService,
            final AddressWritePlatformService addressWritePlatformService, 
            final ClientFamilyMembersWritePlatformService clientFamilyMembersWritePlatformService, 
            final BusinessEventNotifierService businessEventNotifierService,
            final EntityDatatableChecksWritePlatformService entityDatatableChecksWritePlatformService,
            final CS_KYCWritePlatformService kycPlatformService,
            final CS_CoMakerWritePlatformService coMakerPlatformService
            ) {
        this.context = context;
        this.clientRepository = clientRepository;
        this.kycRepository = kycRepository;
        this.loanPlatformService = loanPlatformService;
        this.officeRepositoryWrapper = officeRepositoryWrapper;
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;
        this.accountNumberGenerator = accountNumberGenerator;
        this.staffRepository = staffRepository;
        this.codeValueRepository = codeValueRepository;
        this.commandProcessingService = commandProcessingService;
        this.cs_CommandProcessingService = cs_CommandProcessingService;
        this.accountNumberFormatRepository = accountNumberFormatRepository;
        this.configurationReadPlatformService = configurationReadPlatformService;
        this.addressWritePlatformService = addressWritePlatformService;
        this.clientFamilyMembersWritePlatformService=clientFamilyMembersWritePlatformService;
        this.businessEventNotifierService = businessEventNotifierService;
        this.entityDatatableChecksWritePlatformService = entityDatatableChecksWritePlatformService;
        this.kycPlatformService = kycPlatformService;
        this.coMakerPlatformService = coMakerPlatformService;
    }

    /*
     * Guaranteed to throw an exception no matter what the data integrity issue
     * is.
     */
    private void handleDataIntegrityIssues(final CS_JsonCommand command, final Throwable realCause, final Exception dve) {
        JsonCommand jCom = command.thisToJsonCommand();
        if (realCause.getMessage().contains("external_id")) {

            final String externalId = jCom.stringValueOfParameterNamed("externalId");
            throw new PlatformDataIntegrityException("error.msg.client.duplicate.externalId", "Client with externalId `" + externalId
                    + "` already exists", "externalId", externalId);
        } else if (realCause.getMessage().contains("account_no_UNIQUE")) {
            final String accountNo = jCom.stringValueOfParameterNamed("accountNo");
            throw new PlatformDataIntegrityException("error.msg.client.duplicate.accountNo", "Client with accountNo `" + accountNo
                    + "` already exists", "accountNo", accountNo);
        } else if (realCause.getMessage().contains("mobile_no")) {
            final String mobileNo = jCom.stringValueOfParameterNamed("mobileNo");
            throw new PlatformDataIntegrityException("error.msg.client.duplicate.mobileNo", "Client with mobileNo `" + mobileNo
                    + "` already exists", "mobileNo", mobileNo);
        }

        logAsErrorUnexpectedDataIntegrityException(dve);
        throw new PlatformDataIntegrityException("error.msg.client.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource.");
    }

    @Transactional
    @Override
    public CommandProcessingResult createClient(final CS_JsonCommand command) {
        JsonCommand jCom = command.thisToJsonCommand();
        JsonCommand clientCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("client"));
        JsonCommand kycCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("kyc"));
        JsonCommand loanCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("loan"));
        JsonCommand coMakerCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("comaker"));
        JsonCommand coMakerInfoCommand = JsonCommand.fromExistingCommand(coMakerCommand, coMakerCommand.jsonElement("info"));
        JsonCommand coMakerKycCommand = JsonCommand.fromExistingCommand(coMakerCommand, coMakerCommand.jsonElement("kyc"));
        try {
            Client newClient = addClient(clientCommand);
            CS_KycInfo clientKYC = addKYC(kycCommand, newClient);
            loanCommand = addClientId(loanCommand, newClient.getId());
            Loan newLoan = addLoanFromClient(loanCommand);
            CS_KycInfo coMakerKyc = addKYC(coMakerKycCommand, null);
            CS_CoMaker coMakerInfo = addCoMaker(coMakerInfoCommand, newLoan, coMakerKyc);

            return new CommandProcessingResultBuilder() //
                    .withCommandId(jCom.commandId()) //
                    .withOfficeId(newClient.officeId()) //
                    .withLoanId(newLoan.getId()) //
                    .withClientId(newClient.getId()) //
                    .withEntityId(newClient.getId()) //
                    .build();
        } catch (final DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve.getMostSpecificCause(), dve);
            return CommandProcessingResult.empty();
        }catch(final PersistenceException dve) {
        	Throwable throwable = ExceptionUtils.getRootCause(dve.getCause()) ;
            handleDataIntegrityIssues(command, throwable, dve);
         	return CommandProcessingResult.empty();
        }
    }

    private Client addClient(final JsonCommand command){
            final AppUser currentUser = this.context.authenticatedUser();

            this.fromApiJsonDeserializer.validateForCreate(command.json());
            
            final GlobalConfigurationPropertyData configuration = this.configurationReadPlatformService
                    .retrieveGlobalConfiguration("Enable-Address");

            final Boolean isAddressEnabled = configuration.isEnabled();
            
            final Boolean isStaff = command.booleanObjectValueOfParameterNamed(ClientApiConstants.isStaffParamName);

            final Long officeId = command.longValueOfParameterNamed(ClientApiConstants.officeIdParamName);

            final Office clientOffice = this.officeRepositoryWrapper.findOneWithNotFoundDetection(officeId);

            Staff staff = null;
            final Long staffId = command.longValueOfParameterNamed(ClientApiConstants.staffIdParamName);
            if (staffId != null) {
                staff = this.staffRepository.findByOfficeHierarchyWithNotFoundDetection(staffId, clientOffice.getHierarchy());
            }

            CodeValue gender = null;
            final Long genderId = command.longValueOfParameterNamed(ClientApiConstants.genderIdParamName);
            if (genderId != null) {
                gender = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(ClientApiConstants.GENDER, genderId);
            }

            CodeValue clientType = null;
            final Long clientTypeId = command.longValueOfParameterNamed(ClientApiConstants.clientTypeIdParamName);
            if (clientTypeId != null) {
                clientType = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(ClientApiConstants.CLIENT_TYPE,
                        clientTypeId);
            }

            CodeValue clientClassification = null;
            final Long clientClassificationId = command.longValueOfParameterNamed(ClientApiConstants.clientClassificationIdParamName);
            if (clientClassificationId != null) {
                clientClassification = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(
                        ClientApiConstants.CLIENT_CLASSIFICATION, clientClassificationId);
            }
            
            final Integer legalFormParamValue = command.integerValueOfParameterNamed(ClientApiConstants.legalFormIdParamName);
            Integer legalFormValue = null;
            if(legalFormParamValue != null)
            {
                LegalForm legalForm = LegalForm.fromInt(legalFormParamValue);
                if(legalForm != null)
                {
                    legalFormValue = legalForm.getValue();
                }
            }
            
            final Client newClient = Client.createNew(
                currentUser, clientOffice, null, staff, null, gender,
                    clientType, clientClassification, legalFormValue, command);
            this.clientRepository.save(newClient);
            boolean rollbackTransaction = false;
            if (newClient.isActive()) {
                runEntityDatatableCheck(newClient.getId());
                final CommandWrapper commandWrapper = new CommandWrapperBuilder().activateClient(null).build();
                rollbackTransaction = this.commandProcessingService.validateCommand(commandWrapper, currentUser);
            }
            
            this.clientRepository.save(newClient);
            if (newClient.isActive()) {
                this.businessEventNotifierService.notifyBusinessEventWasExecuted(BUSINESS_EVENTS.CLIENTS_ACTIVATE,
                        constructEntityMap(BUSINESS_ENTITY.CLIENT, newClient));
            }
            if (newClient.isAccountNumberRequiresAutoGeneration()) {
                AccountNumberFormat accountNumberFormat = this.accountNumberFormatRepository.findByAccountType(EntityAccountType.CLIENT);
                newClient.updateAccountNo(accountNumberGenerator.generate(newClient, accountNumberFormat));
                this.clientRepository.save(newClient);
            }
                
            if (isAddressEnabled) {
                this.addressWritePlatformService.addNewClientAddress(newClient, command);
            }
            
            
            if(command.arrayOfParameterNamed("familyMembers")!=null)
            {
                this.clientFamilyMembersWritePlatformService.addClientFamilyMember(newClient, command);
            }

            if(command.parameterExists(ClientApiConstants.datatables)){
                this.entityDatatableChecksWritePlatformService.saveDatatables(StatusEnum.CREATE.getCode().longValue(),
                        EntityTables.CLIENT.getName(), newClient.getId(), null,
                        command.arrayOfParameterNamed(ClientApiConstants.datatables));
            }

            this.businessEventNotifierService.notifyBusinessEventWasExecuted(BUSINESS_EVENTS.CLIENTS_CREATE,
                    constructEntityMap(BUSINESS_ENTITY.CLIENT, newClient));

            this.entityDatatableChecksWritePlatformService.runTheCheck(newClient.getId(), EntityTables.CLIENT.getName(),
                    StatusEnum.CREATE.getCode().longValue(), EntityTables.CLIENT.getForeignKeyColumnNameOnDatatable());

            return newClient;
    }

    private CS_KycInfo addKYC(final JsonCommand command, Client client){
        return this.kycPlatformService.addKYC(command, client);
    }

    private Loan addLoanFromClient(final JsonCommand command){
        return this.loanPlatformService.addLoan(command);
    }

    private CS_CoMaker addCoMaker(final JsonCommand command, Loan loan, CS_KycInfo kycInfo){
        return this.coMakerPlatformService.CreateCoMaker(command, loan, kycInfo);
    }

    private JsonCommand addClientId(final JsonCommand command, final Long clientId){
        JsonObject jsonObject = command.parsedJson().getAsJsonObject();
        jsonObject.addProperty("clientId", clientId);
        return JsonCommand.fromExistingCommand(command, jsonObject);
    }
    // private Comaker addComaker(final JsonCommand command){

    // }
    
    public boolean isEmpty(final JsonElement element){
    	return element.toString().trim().length()<4;
    }

    @Transactional
    @Override
    public CommandProcessingResult updateClient(final Long clientId, final CS_JsonCommand command) {
        JsonCommand jCom = command.thisToJsonCommand();
        JsonCommand clientCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("client"));
        JsonCommand kycCommand = jCom.fromExistingCommand(jCom, jCom.jsonElement("kyc"));
        Long kycId = kycCommand.longValueOfParameterNamed("id");

        try {
            this.fromApiJsonDeserializer.validateForUpdate(clientCommand.json());

            final Client clientForUpdate = this.clientRepository.findOneWithNotFoundDetection(clientId);
            final String clientHierarchy = clientForUpdate.getOffice().getHierarchy();

            this.context.validateAccessRights(clientHierarchy);

            final Map<String, Object> changes = clientForUpdate.update(clientCommand);

            if (changes.containsKey(ClientApiConstants.staffIdParamName)) {

                final Long newValue = clientCommand.longValueOfParameterNamed(ClientApiConstants.staffIdParamName);
                Staff newStaff = null;
                if (newValue != null) {
                    newStaff = this.staffRepository.findByOfficeHierarchyWithNotFoundDetection(newValue, clientForUpdate.getOffice()
                            .getHierarchy());
                }
                clientForUpdate.updateStaff(newStaff);
            }

            if (changes.containsKey(ClientApiConstants.genderIdParamName)) {

                final Long newValue = clientCommand.longValueOfParameterNamed(ClientApiConstants.genderIdParamName);
                CodeValue gender = null;
                if (newValue != null) {
                    gender = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(ClientApiConstants.GENDER, newValue);
                }
                clientForUpdate.updateGender(gender);
            }

            if (changes.containsKey(ClientApiConstants.genderIdParamName)) {
                final Long newValue = clientCommand.longValueOfParameterNamed(ClientApiConstants.genderIdParamName);
                CodeValue newCodeVal = null;
                if (newValue != null) {
                    newCodeVal = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(ClientApiConstants.GENDER, newValue);
                }
                clientForUpdate.updateGender(newCodeVal);
            }

            if (changes.containsKey(ClientApiConstants.clientTypeIdParamName)) {
                final Long newValue = clientCommand.longValueOfParameterNamed(ClientApiConstants.clientTypeIdParamName);
                CodeValue newCodeVal = null;
                if (newValue != null) {
                    newCodeVal = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(ClientApiConstants.CLIENT_TYPE,
                            newValue);
                }
                clientForUpdate.updateClientType(newCodeVal);
            }

            if (changes.containsKey(ClientApiConstants.clientClassificationIdParamName)) {
                final Long newValue = clientCommand.longValueOfParameterNamed(ClientApiConstants.clientClassificationIdParamName);
                CodeValue newCodeVal = null;
                if (newValue != null) {
                    newCodeVal = this.codeValueRepository.findOneByCodeNameAndIdWithNotFoundDetection(
                            ClientApiConstants.CLIENT_CLASSIFICATION, newValue);
                }
                clientForUpdate.updateClientClassification(newCodeVal);
            }

            if (!changes.isEmpty()) {
                this.clientRepository.saveAndFlush(clientForUpdate);
            }
            
            if (changes.containsKey(ClientApiConstants.legalFormIdParamName)) {
            	Integer legalFormValue = clientForUpdate.getLegalForm();
            	boolean isChangedToEntity = false;
            	if(legalFormValue != null)
            	{
            		LegalForm legalForm = LegalForm.fromInt(legalFormValue);
            	}
            }
            if(kycId != null){
                CS_KycInfo kyc = this.kycPlatformService.updateKYC(kycId, kycCommand);
            }else{
                CS_KycInfo clientKYC = addKYC(kycCommand, clientForUpdate);
            }

            return new CommandProcessingResultBuilder() //
                    .withCommandId(clientCommand.commandId()) //
                    .withOfficeId(clientForUpdate.officeId()) //
                    .withClientId(clientId) //
                    .withEntityId(clientId) //
                    .with(changes) //
                    .build();
        } catch (final DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve.getMostSpecificCause(), dve);
            return CommandProcessingResult.empty();
        }catch(final PersistenceException dve) {
        	Throwable throwable = ExceptionUtils.getRootCause(dve.getCause()) ;
            handleDataIntegrityIssues(command, throwable, dve);
         	return CommandProcessingResult.empty();
        }
    }

    private void logAsErrorUnexpectedDataIntegrityException(final Exception dve) {
        logger.error(dve.getMessage(), dve);
    }

    private void runEntityDatatableCheck(final Long clientId) {
        entityDatatableChecksWritePlatformService.runTheCheck(clientId, EntityTables.CLIENT.getName(), StatusEnum.ACTIVATE.getCode()
                .longValue(), EntityTables.CLIENT.getForeignKeyColumnNameOnDatatable());
    }

    private Map<BUSINESS_ENTITY, Object> constructEntityMap(final BUSINESS_ENTITY entityEvent, Object entity) {
        Map<BUSINESS_ENTITY, Object> map = new HashMap<>(1);
        map.put(entityEvent, entity);
        return map;
    }
}
