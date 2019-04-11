package org.cs.infrastructure.scoremanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CS_CreditScoreFormulaRepository
        extends JpaRepository<CS_CreditScoreFormula, Long>, JpaSpecificationExecutor<CS_CreditScoreFormula> {

}
