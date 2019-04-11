package org.cs.infrastructure.scoremanager.handler;

import org.apache.fineract.commands.annotation.CommandType;
import org.cs.commands.handler.CS_NewCommandSourceHandler;
import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.cs.infrastructure.scoremanager.service.CS_CreditScoreFormulaWritePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommandType(entity = "CREDIT_SCORE_FORMULA", action = "CREATE")
public class CS_CreateCreditScoreFormulaCommandHandler implements CS_NewCommandSourceHandler {
    
    private final CS_CreditScoreFormulaWritePlatformService service;
    
    @Autowired
    public CS_CreateCreditScoreFormulaCommandHandler(CS_CreditScoreFormulaWritePlatformService service) {
        this.service = service;
    }

    @Override
    public CommandProcessingResult processCommand(CS_JsonCommand command) {
        
        return this.service.addCreditScoreFormula(command);
    }

}
