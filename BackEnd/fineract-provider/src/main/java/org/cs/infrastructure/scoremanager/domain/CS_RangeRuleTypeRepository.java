package org.cs.infrastructure.scoremanager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;


public interface CS_RangeRuleTypeRepository extends JpaRepository<CS_RangeRuleType, Integer>, JpaSpecificationExecutor<CS_RangeRuleType> {

	List<CS_RangeRuleType> findByScoringRuleId(int scoringRuleId);
}
