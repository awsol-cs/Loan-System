package org.cs.infrastructure.scoremanager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;

@Entity
@Table(name = "choice_rule_type")
public class CS_ChoiceRuleType extends AbstractPersistableCustom<Integer> {

    // @Column(name="id")
    // private int id;

    @Column(name="scoring_rule_id")
    private long scoringRuleId;

    @Column(name="choice_name")
    private String choiceName;

    @Column(name="relative_value")
    private BigDecimal relativeValue;
    
    public CS_ChoiceRuleType(int scoringRuleId, String choiceName, BigDecimal relativeValue) {
        this.scoringRuleId = scoringRuleId;
        this.choiceName = choiceName;
        this.relativeValue = relativeValue;
    }
    
    public CS_ChoiceRuleType(String choiceName, BigDecimal relativeValue) {
        this.choiceName = choiceName;
        this.relativeValue = relativeValue;
    }
    
    public CS_ChoiceRuleType() {
        
    }
    
    public static CS_ChoiceRuleType fromJson(CS_JsonCommand command) {
        
        final int scoringRuleId = command.integerValueOfParameterNamed("scoringRuleId");
        final String choiceName = command.stringValueOfParameterNamed("choiceName");
        final BigDecimal relativeValue = command.bigDecimalValueOfParameterNamed("relativeValue");
        
        return new CS_ChoiceRuleType(scoringRuleId, choiceName, relativeValue);
    }

    // public int getChoiceRuleId() {
    //     return this.id;
    // }

    // public void setId(int id) {
    //     this.id = id;
    // }

    public long getScoringRuleId() {
        return this.scoringRuleId;
    }

    public void setScoringRuleId(long scoringRuleId) {
        this.scoringRuleId = scoringRuleId;
    }

    public String getChoiceName() {
        return this.choiceName;
    }

    public void setChoiceName(String choiceName) {
        this.choiceName = choiceName;
    }

    public BigDecimal getRelativeValue() {
        return this.relativeValue;
    }

    public void setRelativeValue(BigDecimal relativeValue) {
        this.relativeValue = relativeValue;
    }
}