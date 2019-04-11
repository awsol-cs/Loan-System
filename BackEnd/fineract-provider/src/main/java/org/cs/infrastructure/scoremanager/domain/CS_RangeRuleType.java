package org.cs.infrastructure.scoremanager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;

@Entity
@Table(name = "range_rule_type")
public class CS_RangeRuleType extends AbstractPersistableCustom<Integer> {
    
    // @Column(name="id")
    // private int id;
    
    @Column(name="scoring_rule_id")
    private int scoringRuleId;

    @Column(name="min_value")
    private int minValue;

    @Column(name="max_value")
    private int maxValue;

    @Column(name="relative_value")
    private BigDecimal relativeValue;
    
    public CS_RangeRuleType(int scoringRuleId, int minValue, int maxValue, BigDecimal relativeValue) {
        this.scoringRuleId = scoringRuleId;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.relativeValue = relativeValue;
    }

    public CS_RangeRuleType(int minValue, int maxValue, BigDecimal relativeValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.relativeValue = relativeValue;
    }
    
    public CS_RangeRuleType() {
        
    }
    
    public static CS_RangeRuleType fromJson(final CS_JsonCommand command) {
        final int scoringRuleId = command.integerValueOfParameterNamed("scoringRuleId");
        final int minValue = command.integerValueOfParameterNamed("minValue");
        final int maxValue = command.integerValueOfParameterNamed("maxValue");
        final BigDecimal relativeValue = command.bigDecimalValueOfParameterNamed("relativeValue");
        return new CS_RangeRuleType(scoringRuleId, minValue, maxValue, relativeValue);
    }

    // public int getRangeRuleId() {
    //     return this.id;
    // }

    // public void setId(int id) {
    //     this.id = id;
    // }

    public int getScoringRuleId() {
        return this.scoringRuleId;
    }

    public void setScoringRuleId(int scoringRuleId) {
        this.scoringRuleId = scoringRuleId;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getRelativeValue() {
        return this.relativeValue;
    }

    public void setRelativeValue(BigDecimal relativeValue) {
        this.relativeValue = relativeValue;
    }

}
