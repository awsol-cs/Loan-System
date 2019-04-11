package org.cs.infrastructure.scoremanager.data;

import java.util.ArrayList;
import java.util.Collection;

public class CS_ScoreManagerData {

    private final int id;
    private final String ruleName;
    private final int weightedValue;
    private final int ruleType;
    private Collection<CS_RuleTypeData> ruleTypeDataList = new ArrayList<>();
    private final String status;
    private final String tag;
    private final String createdBy;
    private final String createdDate;
    private final String updatedBy;
    private final String updatedDate;
    
    public CS_ScoreManagerData(int id, String ruleName, int weightedValue, int ruleType, Collection<CS_RuleTypeData> ruleTypeDataList, String status, String tag, 
            String createdBy, String createdDate, String updatedBy, String updatedDate) {
        this.id = id;
        this.ruleName = ruleName;
        this.weightedValue = weightedValue;
        this.ruleType = ruleType;
        this.ruleTypeDataList = ruleTypeDataList;
        this.status = status;
        this.tag = tag;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }
    
    public CS_ScoreManagerData(CS_ScoreManagerData scoreManagerData) {
        this.id = scoreManagerData.getId();
        this.ruleName = scoreManagerData.getRuleName();
        this.weightedValue = scoreManagerData.getWeightedValue();
        this.ruleType = scoreManagerData.getRuleType();
        this.status = scoreManagerData.getStatus();
        this.tag = scoreManagerData.getTag();
        this.createdBy = scoreManagerData.getCreatedBy();
        this.createdDate = scoreManagerData.getCreatedDate();
        this.updatedBy = scoreManagerData.getUpdatedBy();
        this.updatedDate = scoreManagerData.getUpdatedDate();
    }
    
    public static CS_ScoreManagerData instance(final int id, final String ruleName, final int weightedValue, final int ruleType, 
            final Collection<CS_RuleTypeData> ruleTypeDataList, final String status, final String tag, final String createdBy, final String createdDate,
            final String updatedBy, final String updatedDate) {
        return new CS_ScoreManagerData(id, ruleName, weightedValue, ruleType, ruleTypeDataList, status, tag, createdBy, createdDate, updatedBy, updatedDate);
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
    
    public Collection<CS_RuleTypeData> getRuleTypeDataList() {
        return this.ruleTypeDataList;
    }
    
    public void setRuleTypeDataList(Collection<CS_RuleTypeData> ruleTypeDataList) {
        this.ruleTypeDataList = ruleTypeDataList;
    }

    public String getStatus() {
        return this.status;
    }
        
    public String getTag() {
        return this.tag;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }
    
    public String getUpdatedBy() {
        return this.updatedBy;
    }
    
    public String getUpdatedDate() {
        return this.updatedDate;
    }
    
}
