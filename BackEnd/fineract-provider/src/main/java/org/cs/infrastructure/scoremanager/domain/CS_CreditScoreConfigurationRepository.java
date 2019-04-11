package org.cs.infrastructure.scoremanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CS_CreditScoreConfigurationRepository extends JpaRepository<CS_CreditScoreConfiguration, Integer>, JpaSpecificationExecutor<CS_CreditScoreConfiguration> {

}
