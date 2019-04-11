package org.cs.portfolio.creditscore.service;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;

public interface CS_CreditScoreWritePlatformService {
    
    CommandProcessingResult addCreditScoreRule(CS_JsonCommand command);
    
    CommandProcessingResult editCreditScoreRule(final long creditScoreId, CS_JsonCommand command);

}
