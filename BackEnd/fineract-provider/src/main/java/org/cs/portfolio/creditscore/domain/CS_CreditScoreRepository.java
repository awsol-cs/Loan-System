package org.cs.portfolio.creditscore.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CS_CreditScoreRepository extends JpaRepository<CS_CreditScore, Long>, JpaSpecificationExecutor<CS_CreditScore> {

}
