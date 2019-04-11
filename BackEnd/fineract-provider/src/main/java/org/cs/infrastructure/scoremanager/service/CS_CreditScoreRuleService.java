package org.cs.infrastructure.scoremanager.service;

import java.util.Collection;

import org.cs.infrastructure.scoremanager.data.CS_CreditScoreConfigurationData;
import org.cs.infrastructure.scoremanager.data.CS_RuleTypeData;

public interface CS_CreditScoreRuleService {
    
    public Collection<CS_CreditScoreConfigurationData> getScoringRuleList();
    
    public CS_CreditScoreConfigurationData getScoringRule(int id);
    
    public int addScoringRule(CS_CreditScoreConfigurationData scoringRuleData);
    
    public int editScoringRule(CS_CreditScoreConfigurationData scoringRuleData);
    
    public int deleteScoringRule(int id);
    
    public Collection<CS_RuleTypeData> getRuleTypeDataList(int id, int ruleType);
    
    public CS_RuleTypeData getRuleTypeData(int id, int ruleType);

}
