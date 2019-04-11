package org.cs.infrastructure.scoremanager.domain;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.cs.infrastructure.scoremanager.domain.CS_RangeRuleType;
import org.cs.infrastructure.scoremanager.domain.CS_ChoiceRuleType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity
@Table(name = "scoring_rule")
public class CS_CreditScoreConfiguration extends AbstractPersistableCustom<Integer> {

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "weighted_value")
    private int weightedValue;

    @Column(name = "rule_type")
    private int ruleType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "scoring_rule_id")
    private List<CS_RangeRuleType> rangeRuleTypeList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "scoring_rule_id")
    private List<CS_ChoiceRuleType> choiceRuleTypeList;

    @Column(name = "status")
    private String status;
    
    @Column(name = "tag")
    private String tag;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    private String updatedDate;
    
    public CS_CreditScoreConfiguration() {
        
    }

    public CS_CreditScoreConfiguration(long id, String ruleName, int weightedValue, int ruleType, List<CS_RangeRuleType> rangeRuleTypeList,
            List<CS_ChoiceRuleType> choiceRuleTypeList, String status, String tag, String createdBy, String createdDate, String updatedBy,
            String updatedDate) {
        // this.id = id;
        this.ruleName = ruleName;
        this.weightedValue = weightedValue;
        this.ruleType = ruleType;
        this.rangeRuleTypeList = rangeRuleTypeList;
        this.choiceRuleTypeList = choiceRuleTypeList;
        this.status = status;
        this.tag = tag;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    public CS_CreditScoreConfiguration(String ruleName, int weightedValue, int ruleType, List<CS_RangeRuleType> rangeRuleTypeList,
            List<CS_ChoiceRuleType> choiceRuleTypeList, String status, String tag, String createdBy, String createdDate, String updatedBy,
            String updatedDate) {
        this.ruleName = ruleName;
        this.weightedValue = weightedValue;
        this.ruleType = ruleType;
        this.rangeRuleTypeList = rangeRuleTypeList;
        this.choiceRuleTypeList = choiceRuleTypeList;
        this.status = status;
        this.tag = tag;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    public CS_CreditScoreConfiguration(String status) {
        this.status = status;
    }

    public static CS_CreditScoreConfiguration forUpdateStatus(final CS_JsonCommand command){
        String status = command.stringValueOfParameterNamed("status");
        return new CS_CreditScoreConfiguration(status);
    }
    
    public static CS_CreditScoreConfiguration fromJson(final CS_JsonCommand command) {
        Locale locale = Locale.US;  
        String ruleName = command.stringValueOfParameterNamed("ruleName");
        int weightedValue = command.integerValueOfParameterNamed("weightedValue", locale);
        int ruleType = command.integerValueOfParameterNamed("ruleType", locale);
        JsonArray ruleTypeDataList = command.arrayOfParameterNamed("ruleTypeDataList");
        List<CS_RangeRuleType> rangeRuleTypeList = new ArrayList<CS_RangeRuleType>();
        List<CS_ChoiceRuleType> choiceRuleTypeList = new ArrayList<CS_ChoiceRuleType>();
        int i = 0;
        do{
            final JsonObject jsonObject = ruleTypeDataList.get(i).getAsJsonObject();
            if(1 == ruleType){
                int minValue = jsonObject.get("minValue").getAsInt();
                int maxValue = jsonObject.get("maxValue").getAsInt();
                BigDecimal relativeValue = jsonObject.get("relativeValue").getAsBigDecimal();
                CS_RangeRuleType rangeRule = new CS_RangeRuleType(minValue, maxValue, relativeValue);
                rangeRuleTypeList.add(rangeRule);
            }else{
                String name = jsonObject.get("choiceName").getAsString();
                BigDecimal relativeValue = jsonObject.get("relativeValue").getAsBigDecimal();
                CS_ChoiceRuleType choiceRule = new CS_ChoiceRuleType(name, relativeValue);
                choiceRuleTypeList.add(choiceRule);
            }
            i++;
        } while (i < ruleTypeDataList.size());
        String status = command.stringValueOfParameterNamed("status");
        String tag = command.stringValueOfParameterNamed("tag");
        // String createdBy = command.stringValueOfParameterNamed("createdBy");
        // String createdDate = command.stringValueOfParameterNamed("createdDate");
        // String updatedBy = command.stringValueOfParameterNamed("updatedBy");
        // String updatedDate = command.stringValueOfParameterNamed("updatedDate");
        String createdBy = null;
        String createdDate = null;
        String updatedBy = null;
        String updatedDate = null;
        return new CS_CreditScoreConfiguration(ruleName, weightedValue, ruleType, rangeRuleTypeList, choiceRuleTypeList, status, tag, createdBy, createdDate, updatedBy, updatedDate);
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getWeightedValue() {
        return this.weightedValue;
    }

    public void setWeightedValue(int weightedValue) {
        this.weightedValue = weightedValue;
    }

    public int getRuleType() {
        return this.ruleType;
    }

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public List<CS_RangeRuleType> getRangeRuleTypeList() {
        return this.rangeRuleTypeList;
    }

    public void setRangeRuleTypeList(List<CS_RangeRuleType> rangeRuleTypeList) {
        this.rangeRuleTypeList = rangeRuleTypeList;
    }

    public List<CS_ChoiceRuleType> getChoiceRuleTypeList() {
        return this.choiceRuleTypeList;
    }

    public void setChoiceRuleTypeList(List<CS_ChoiceRuleType> choiceRuleTypeList) {
        this.choiceRuleTypeList = choiceRuleTypeList;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}