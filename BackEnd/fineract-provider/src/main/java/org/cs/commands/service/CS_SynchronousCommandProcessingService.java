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
package org.cs.commands.service;

import java.util.Map;

import org.cs.commands.domain.CS_CommandSource;
import org.cs.commands.domain.CS_CommandSourceRepository;
import org.cs.commands.domain.CS_CommandWrapper;
import org.cs.commands.exception.CS_RollbackTransactionAsCommandIsNotApprovedByCheckerException;
import org.apache.fineract.commands.exception.UnsupportedCommandException;
import org.cs.commands.handler.CS_NewCommandSourceHandler;
import org.cs.commands.provider.CS_CommandHandlerProvider;
import org.apache.fineract.infrastructure.configuration.domain.ConfigurationDomainService;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.serialization.ToApiJsonSerializer;
import org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil;
import org.apache.fineract.infrastructure.hooks.event.HookEvent;
import org.apache.fineract.infrastructure.hooks.event.HookEventSource;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CS_SynchronousCommandProcessingService implements CS_CommandProcessingService {

    private CS_PlatformSecurityContext context;
    private final ApplicationContext applicationContext;
    private final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer;
    private final ToApiJsonSerializer<CommandProcessingResult> toApiResultJsonSerializer;
    private CS_CommandSourceRepository commandSourceRepository;
    private final ConfigurationDomainService configurationDomainService;
    private final CS_CommandHandlerProvider commandHandlerProvider;

    @Autowired
    public CS_SynchronousCommandProcessingService(final CS_PlatformSecurityContext context, final ApplicationContext applicationContext,
            final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer,
            final ToApiJsonSerializer<CommandProcessingResult> toApiResultJsonSerializer,
            final CS_CommandSourceRepository commandSourceRepository, final ConfigurationDomainService configurationDomainService,
            final CS_CommandHandlerProvider commandHandlerProvider) {
        this.context = context;
        this.context = context;
        this.applicationContext = applicationContext;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.toApiResultJsonSerializer = toApiResultJsonSerializer;
        this.commandSourceRepository = commandSourceRepository;
        this.commandSourceRepository = commandSourceRepository;
        this.configurationDomainService = configurationDomainService;
        this.commandHandlerProvider = commandHandlerProvider;
    }

    @Transactional
    @Override
    public CommandProcessingResult processAndLogCommand(final CS_CommandWrapper wrapper, final CS_JsonCommand command,
            final boolean isApprovedByChecker) {

        final boolean rollbackTransaction = this.configurationDomainService.isMakerCheckerEnabledForTask(wrapper.taskPermissionName());

        final CS_NewCommandSourceHandler handler = findCommandHandler(wrapper);

        final CommandProcessingResult result = handler.processCommand(command);

        final AppUser maker = this.context.authenticatedUser(wrapper);

        CS_CommandSource commandSourceResult = null;
        if (command.commandId() != null) {
            commandSourceResult = this.commandSourceRepository.findOne(command.commandId());
            commandSourceResult.markAsChecked(maker, DateTime.now());
        } else {
            commandSourceResult = CS_CommandSource.fullEntryFrom(wrapper, command, maker);
        }
        commandSourceResult.updateResourceId(result.resourceId());
        commandSourceResult.updateForAudit(result.getOfficeId(), result.getGroupId(), result.getClientId(), result.getLoanId(),
                result.getSavingsId(), result.getProductId(), result.getTransactionId());

        String changesOnlyJson = null;
        boolean rollBack = (rollbackTransaction || result.isRollbackTransaction()) && !isApprovedByChecker ;
        if (result.hasChanges() && !rollBack) {
            changesOnlyJson = this.toApiJsonSerializer.serializeResult(result.getChanges());
            commandSourceResult.updateJsonTo(changesOnlyJson);
        }

        if (!result.hasChanges() && wrapper.isUpdateOperation() && !wrapper.isUpdateDatatable()) {
            commandSourceResult.updateJsonTo(null);
        }

        if (commandSourceResult.hasJson()) {
            this.commandSourceRepository.save(commandSourceResult);
        }

        if ((rollbackTransaction || result.isRollbackTransaction()) && !isApprovedByChecker) {
            /*
             * JournalEntry will generate a new transactionId every time.
             * Updating the transactionId with old transactionId, because as
             * there are no entries are created with new transactionId, will
             * throw an error when checker approves the transaction
             */
            commandSourceResult.updateTransaction(command.getTransactionId());
            /*
             * Update CS_CommandSource json data with CS_JsonCommand json data, line
             * 77 and 81 may update the json data
             */
            commandSourceResult.updateJsonTo(command.json());
            throw new CS_RollbackTransactionAsCommandIsNotApprovedByCheckerException(commandSourceResult);
        }
        result.setRollbackTransaction(null);

        publishEvent(wrapper.entityName(), wrapper.actionName(), result);

        return result;
    }

    @Transactional
    @Override
    public CommandProcessingResult logCommand(CS_CommandSource commandSourceResult) {

        commandSourceResult.markAsAwaitingApproval();
        commandSourceResult = this.commandSourceRepository.save(commandSourceResult);

        return new CommandProcessingResultBuilder().withCommandId(commandSourceResult.getId())
                .withEntityId(commandSourceResult.getResourceId()).build();
    }

    private CS_NewCommandSourceHandler findCommandHandler(final CS_CommandWrapper wrapper) {
        CS_NewCommandSourceHandler handler = null;

        if (wrapper.isDatatableResource()) {
            if (wrapper.isCreateDatatable()) {
                handler = this.applicationContext.getBean("createDatatableCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isDeleteDatatable()) {
                handler = this.applicationContext.getBean("deleteDatatableCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isUpdateDatatable()) {
                handler = this.applicationContext.getBean("updateDatatableCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isCreate()) {
                handler = this.applicationContext.getBean("createDatatableEntryCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isUpdateMultiple()) {
                handler = this.applicationContext.getBean("updateOneToManyDatatableEntryCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isUpdateOneToOne()) {
                handler = this.applicationContext.getBean("updateOneToOneDatatableEntryCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isDeleteMultiple()) {
                handler = this.applicationContext.getBean("deleteOneToManyDatatableEntryCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isDeleteOneToOne()) {
                handler = this.applicationContext.getBean("deleteOneToOneDatatableEntryCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isRegisterDatatable()) {
                handler = this.applicationContext.getBean("registerDatatableCommandHandler", CS_NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isNoteResource()) {
            if (wrapper.isCreate()) {
                handler = this.applicationContext.getBean("createNoteCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isUpdate()) {
                handler = this.applicationContext.getBean("updateNoteCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isDelete()) {
                handler = this.applicationContext.getBean("deleteNoteCommandHandler", CS_NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isSurveyResource()) {
            if (wrapper.isRegisterSurvey()) {
                handler = this.applicationContext.getBean("registerSurveyCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.isFullFilSurvey()) {
                handler = this.applicationContext.getBean("fullFilSurveyCommandHandler", CS_NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else if (wrapper.isLoanDisburseDetailResource()) {
            if (wrapper.isUpdateDisbursementDate()) {
                handler = this.applicationContext.getBean("updateLoanDisbuseDateCommandHandler", CS_NewCommandSourceHandler.class);
            } else if (wrapper.addAndDeleteDisbursementDetails()) {
                handler = this.applicationContext.getBean("addAndDeleteLoanDisburseDetailsCommandHandler", CS_NewCommandSourceHandler.class);
            } else {
                throw new UnsupportedCommandException(wrapper.commandName());
            }
        } else {
            handler = this.commandHandlerProvider.getHandler(wrapper.entityName(), wrapper.actionName());
        }

        return handler;
    }

    @Override
    public boolean validateCommand(final CS_CommandWrapper commandWrapper, final AppUser user) {
        boolean rollbackTransaction = this.configurationDomainService.isMakerCheckerEnabledForTask(commandWrapper.taskPermissionName());
        user.validateHasPermissionTo(commandWrapper.getTaskPermissionName());
        return rollbackTransaction;
    }

    private void publishEvent(final String entityName, final String actionName, final CommandProcessingResult result) {

        final String authToken = ThreadLocalContextUtil.getAuthToken();
        final String tenantIdentifier = ThreadLocalContextUtil.getTenant().getTenantIdentifier();
        final AppUser appUser = this.context.authenticatedUser(CS_CommandWrapper.wrap(actionName, 
                entityName, null, null));

        final HookEventSource hookEventSource = new HookEventSource(entityName, actionName);

        final String serializedResult = this.toApiResultJsonSerializer.serialize(result);

        final HookEvent applicationEvent = new HookEvent(hookEventSource, serializedResult, tenantIdentifier, appUser, authToken);

        applicationContext.publishEvent(applicationEvent);
    }
}
