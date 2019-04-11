package org.cs.infrastructure.scoremanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;


public interface CS_ChoiceRuleTypeRepository extends JpaRepository<CS_ChoiceRuleType, Integer>, JpaSpecificationExecutor<CS_ChoiceRuleType> {

	List<CS_ChoiceRuleType> findByScoringRuleId(int scoringRuleId);
}
