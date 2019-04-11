package org.cs.portfolio.creditscore.service;

import java.util.Collection;

import org.cs.portfolio.creditscore.data.CS_CreditScoreData;

public interface CS_CreditScoreReadPlatformService {
    
    CS_CreditScoreData getCreditScoreDataById(long id);

}