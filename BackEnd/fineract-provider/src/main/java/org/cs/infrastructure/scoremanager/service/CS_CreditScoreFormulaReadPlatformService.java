package org.cs.infrastructure.scoremanager.service;

import java.util.Collection;

import org.cs.infrastructure.scoremanager.data.CS_CreditScoreFormulaData;

public interface CS_CreditScoreFormulaReadPlatformService {
    
    Collection<CS_CreditScoreFormulaData> getCreditScoreFormulaDataList();
    
    CS_CreditScoreFormulaData getCreditScoreFormulaDataById(long id);

}