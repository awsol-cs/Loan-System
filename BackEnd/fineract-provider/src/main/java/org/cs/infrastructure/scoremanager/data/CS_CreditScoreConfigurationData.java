package org.cs.infrastructure.scoremanager.data;

public class CS_CreditScoreConfigurationData {
    
    private final int id;
    private final String ruleName;
    private final int weightedValue;
    private final int ruleType;
    private final CS_RuleTypeData ruleTypeData;
    private final int status;
    
    public CS_CreditScoreConfigurationData(int id, String ruleName, int weightedValue, int ruleType, CS_RuleTypeData ruleTypeData, int status) {
        this.id = id;
        this.ruleName = ruleName;
        this.weightedValue = weightedValue;
        this.ruleType = ruleType;
        this.ruleTypeData = ruleTypeData;
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public int getWeightedValue() {
        return this.weightedValue;
    }

    public int getRuleType() {
        return this.ruleType;
    }

    public CS_RuleTypeData getCS_RuleTypeData() {
        return this.ruleTypeData;
    }

    public int getStatus() {
        return this.status;
    }

}