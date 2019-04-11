package org.cs.infrastructure.scoremanager.handler;

import org.apache.fineract.commands.annotation.CommandType;
import org.cs.commands.handler.CS_NewCommandSourceHandler;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.cs.infrastructure.scoremanager.service.CS_CreditScoreConfigWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommandType(entity = "SCORE_CONFIG_STATUS", action = "UPDATE")
public class CS_UpdateCreditScoreConfigStatusCommandHandler implements CS_NewCommandSourceHandler {

    private final CS_CreditScoreConfigWritePlatformService writePlatformService;

    @Autowired
    public CS_UpdateCreditScoreConfigStatusCommandHandler(final CS_CreditScoreConfigWritePlatformService writePlatformService) {
            this.writePlatformService = writePlatformService;
    }

    @Override
    public CommandProcessingResult processCommand(CS_JsonCommand command) {
        return this.writePlatformService.editCreditScoreRuleStatus(command.getScoreManagerId(), command);
    }

}
