package org.cs.portfolio.client.service;

import org.cs.portfolio.client.domain.CS_KycInfoData;

public interface CS_KYCReadPlatformService {
    
    CS_KycInfoData getKycInfoData(Long clientId, Long kycId);

}
