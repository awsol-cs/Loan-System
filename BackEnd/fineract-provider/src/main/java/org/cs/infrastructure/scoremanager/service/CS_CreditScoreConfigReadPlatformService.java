package org.cs.infrastructure.scoremanager.service;

import java.util.Collection;

import org.cs.infrastructure.scoremanager.data.CS_ScoreManagerData;
import org.cs.infrastructure.scoremanager.data.CS_RuleTypeData;

public interface CS_CreditScoreConfigReadPlatformService {
    
    Collection<CS_ScoreManagerData> getScoreConfigList();
    
    CS_ScoreManagerData getCreditScoreById(int id);
    
    Collection<CS_RuleTypeData> getRuleTypeDataList(int ruleType, int scoringRuleId);
    
    CS_RuleTypeData getRuleTypeData(int ruleType, int id);

}
