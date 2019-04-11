package org.cs.infrastructure.scoremanager.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Locale;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Component
public class CS_CreditScoreConfigCommandFromApiJsonDeserializer {
    
    private final Set<String> supportedParameters = new HashSet<>(Arrays.asList("id", "ruleName", "weightedValue", "ruleType", "ruleTypeDataList", "status",
            "tag", "createdBy", "createdDate", "updatedBy", "updatedDate"));
    
    private final FromJsonHelper fromApiJsonHelper;
    
    @Autowired
    public CS_CreditScoreConfigCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }
    
    public void validateForCreate(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }
        
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {
        }.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, this.supportedParameters);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors)
                .resource("SCORE_CONFIG");
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        // final int id = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("id", element);
        // baseDataValidator.reset().parameter("id").value(id).notBlank().integerGreaterThanZero();
        
        final String ruleName = this.fromApiJsonHelper.extractStringNamed("ruleName", element);
        baseDataValidator.reset().parameter("ruleName").value(ruleName).notNull().notBlank().notExceedingLengthOf(100);
        
        final int weightedValue = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("weightedValue", element);
        baseDataValidator.reset().parameter("weightedValue").value(weightedValue).notBlank().integerGreaterThanZero();
        
        final int ruleType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("ruleType", element);
        baseDataValidator.reset().parameter("ruleType").value(ruleType).notBlank().integerGreaterThanZero();
        
        final String status = this.fromApiJsonHelper.extractStringNamed("status", element);
        baseDataValidator.reset().parameter("status").value(status).notNull().notBlank().notExceedingLengthOf(100);
        
        final String tag = this.fromApiJsonHelper.extractStringNamed("tag", element);
        baseDataValidator.reset().parameter("tag").value(tag).notNull().notBlank().notExceedingLengthOf(100);
        
        // final String createdBy = this.fromApiJsonHelper.extractStringNamed("createdBy", element);
        // baseDataValidator.reset().parameter("createdBy").value(createdBy).notNull().notBlank().notExceedingLengthOf(100);
        
        // final String createdDate = this.fromApiJsonHelper.extractStringNamed("createdDate", element);
        // baseDataValidator.reset().parameter("createdDate").value(createdDate).notNull().notBlank().notExceedingLengthOf(100);
        
        // final String updatedBy = this.fromApiJsonHelper.extractStringNamed("updatedBy", element);
        // baseDataValidator.reset().parameter("updatedBy").value(updatedBy).notNull().notBlank().notExceedingLengthOf(100);
        
        // final String updatedDate = this.fromApiJsonHelper.extractStringNamed("updatedDate", element);
        // baseDataValidator.reset().parameter("updatedDate").value(updatedDate).notNull().notBlank().notExceedingLengthOf(100);
        
        checkForRuleTypeDataList(element, baseDataValidator);
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
    
    public void validateForUpdate(final long creditScoreId, final String json) {
        if (StringUtils.isBlank(json)) {
                throw new InvalidJsonException();
        }
        
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, this.supportedParameters);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("CreditScore");
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        baseDataValidator.reset().value(creditScoreId).notBlank().integerGreaterThanZero();
        
        final String ruleName = this.fromApiJsonHelper.extractStringNamed("ruleName", element);
        baseDataValidator.reset().parameter("ruleName").value(ruleName).notNull().notBlank().notExceedingLengthOf(100);
        
        final int weightedValue = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("weightedValue", element);
        baseDataValidator.reset().parameter("weightedValue").value(weightedValue).notBlank().integerGreaterThanZero();
        
        final int ruleType = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("ruleType", element);
        baseDataValidator.reset().parameter("ruleType").value(ruleType).notBlank().integerGreaterThanZero();
        
        final String status = this.fromApiJsonHelper.extractStringNamed("status", element);
        baseDataValidator.reset().parameter("status").value(status).notNull().notBlank().notExceedingLengthOf(100);
        
        final String tag = this.fromApiJsonHelper.extractStringNamed("tag", element);
        baseDataValidator.reset().parameter("tag").value(tag).notNull().notBlank().notExceedingLengthOf(100);

        checkForRuleTypeDataList(element, baseDataValidator);
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
    
    public void validateForUpdateStatus(final long creditScoreId, final String json) {
        if (StringUtils.isBlank(json)) {
                throw new InvalidJsonException();
        }
        
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, this.supportedParameters);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("CreditScore");
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        baseDataValidator.reset().value(creditScoreId).notBlank().integerGreaterThanZero();
        final String status = this.fromApiJsonHelper.extractStringNamed("status", element);
        baseDataValidator.reset().parameter("status").value(status).notNull().notBlank().notExceedingLengthOf(100);
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
                throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                                "Validation errors exist.", dataValidationErrors);
        }
    }

    private void checkForRuleTypeDataList(final JsonElement element, final DataValidatorBuilder baseDataValidator){
        if(this.fromApiJsonHelper.parameterExists("ruleTypeDataList", element)){

            final JsonArray variationArray = this.fromApiJsonHelper.extractJsonArrayNamed("ruleTypeDataList",
                    element);
            if (variationArray != null && variationArray.size() > 0) {
                Locale locale = Locale.US;
                int ruleType = this.fromApiJsonHelper.extractIntegerNamed("ruleType", element, locale);
                int i = 0;
                do{
                    final JsonObject jsonObject = variationArray.get(i).getAsJsonObject();
                    if(1 == ruleType){
                        int minValue = this.fromApiJsonHelper.extractIntegerNamed("minValue", jsonObject, locale);
                        baseDataValidator.reset().parameter("ruleTypeDataList").parameterAtIndexArray("minValue", i)
                        .value(minValue).notBlank();

                        int maxValue = this.fromApiJsonHelper.extractIntegerNamed("maxValue", jsonObject, locale);
                        baseDataValidator.reset().parameter("ruleTypeDataList").parameterAtIndexArray("maxValue", i)
                        .value(maxValue).notBlank();
                    }else{
                        String name = this.fromApiJsonHelper.extractStringNamed("choiceName", jsonObject);
                        baseDataValidator.reset().parameter("ruleTypeDataList").parameterAtIndexArray("choiceName", i)
                        .value(name).notNull().notBlank().notExceedingLengthOf(100);
                    }

                    BigDecimal relativeValue = this.fromApiJsonHelper.extractBigDecimalNamed("relativeValue", jsonObject, locale);
                        baseDataValidator.reset().parameter("ruleTypeDataList").parameterAtIndexArray("relativeValue", i)
                        .value(relativeValue).notBlank();
                    i++;
                } while (i < variationArray.size());
            }else{
                baseDataValidator.reset().parameter("ruleTypeDataList")
                             .failWithCode("rule.type.data.list.is.required");
            }
        }else{
            baseDataValidator.reset().parameter("ruleTypeDataList")
                             .failWithCode("rule.type.data.list.is.required");
        }
    }

}
