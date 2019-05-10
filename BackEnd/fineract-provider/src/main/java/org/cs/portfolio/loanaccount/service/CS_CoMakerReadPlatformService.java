package org.cs.portfolio.loanaccount.service;

import org.cs.portfolio.loanaccount.domain.CS_CoMakerData;

public interface CS_CoMakerReadPlatformService {
    
    CS_CoMakerData retrieveOne(Long loanId);

}
