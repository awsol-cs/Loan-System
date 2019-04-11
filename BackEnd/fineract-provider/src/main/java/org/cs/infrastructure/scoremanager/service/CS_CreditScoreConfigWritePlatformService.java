package org.cs.infrastructure.scoremanager.service;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;

public interface CS_CreditScoreConfigWritePlatformService {
    
    CommandProcessingResult addCreditScoreRule(CS_JsonCommand command);
    
    CommandProcessingResult editCreditScoreRule(final int scoringRuleId, CS_JsonCommand command);
    
    CommandProcessingResult editCreditScoreRuleStatus(final int scoringRuleId, CS_JsonCommand command);

}
