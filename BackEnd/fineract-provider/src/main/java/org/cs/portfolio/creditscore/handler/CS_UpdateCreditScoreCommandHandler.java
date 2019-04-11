package org.cs.portfolio.creditscore.handler;

import org.apache.fineract.commands.annotation.CommandType;
import org.cs.commands.handler.CS_NewCommandSourceHandler;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.cs.portfolio.creditscore.service.CS_CreditScoreWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommandType(entity = "CREDIT_SCORE", action = "UPDATE")
public class CS_UpdateCreditScoreCommandHandler implements CS_NewCommandSourceHandler {

    private final CS_CreditScoreWritePlatformService writePlatformService;

    @Autowired
    public CS_UpdateCreditScoreCommandHandler(final CS_CreditScoreWritePlatformService writePlatformService) {
            this.writePlatformService = writePlatformService;
    }

    @Override
    public CommandProcessingResult processCommand(CS_JsonCommand command) {
        return this.writePlatformService.editCreditScoreRule(command.getCreditScoreId(), command);
    }

}
