package org.cs.infrastructure.scoremanager.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;

@Component
public class CS_CreditScoreFormulaCommandFromApiJsonDeserializer {
    
    private final Set<String> supportedParameters = new HashSet<>(Arrays.asList("id", "formulaName", "formula", "status", "createdBy", "createdDate", "updatedBy",
            "updatedDate"));
    
    private final FromJsonHelper fromApiJsonHelper;
    
    @Autowired
    public CS_CreditScoreFormulaCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }
    
    public void validateForCreate(final String json) {
        if(StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }
        
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors);
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        final String formulaName = this.fromApiJsonHelper.extractStringNamed("formulaName", element);
        baseDataValidator.reset().parameter("formulaName").value(formulaName).notNull().notBlank().notExceedingLengthOf(100);
        
        final String formula = this.fromApiJsonHelper.extractStringNamed("formula", element);
        baseDataValidator.reset().parameter("formula").value(formula).notNull().notBlank().notExceedingLengthOf(100);
        
        final String status = this.fromApiJsonHelper.extractStringNamed("status", element);
        baseDataValidator.reset().parameter("status").value(status).notNull().notBlank().notExceedingLengthOf(100);
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
    
    public void validateForUpdate(final long id, final String json) {
        if(StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }
        
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors);
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        baseDataValidator.reset().value(id).notBlank().integerGreaterThanZero();
        
        final String formulaName = this.fromApiJsonHelper.extractStringNamed("formulaName", element);
        baseDataValidator.reset().parameter("formulaName").value(formulaName).notNull().notBlank().notExceedingLengthOf(100);
        
        final String formula = this.fromApiJsonHelper.extractStringNamed("formula", element);
        baseDataValidator.reset().parameter("formula").value(formula).notNull().notBlank().notExceedingLengthOf(100);
        
        final String status = this.fromApiJsonHelper.extractStringNamed("status", element);
        baseDataValidator.reset().parameter("status").value(status).notNull().notBlank().notExceedingLengthOf(100);
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
    
    public void validateForStatusUpdate(final int id, final String json) {
        if(StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }
        
        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors);
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        baseDataValidator.reset().value(id).notBlank().integerGreaterThanZero();
        
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

}