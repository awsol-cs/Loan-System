package org.cs.infrastructure.scoremanager.service;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;

public interface CS_CreditScoreFormulaWritePlatformService {
    
    CommandProcessingResult addCreditScoreFormula(CS_JsonCommand command);
    
    CommandProcessingResult editCreditScoreFormula(final long id, CS_JsonCommand command);

}