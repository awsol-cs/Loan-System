package org.cs.portfolio.creditscore.serialization;

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
public class CS_CreditScoreCommandFromApiJsonDeserializer {
    
    private final Set<String> supportedParameters = new HashSet<>(Arrays.asList("id", "client_id", "creditscore"));
    
    private final FromJsonHelper fromApiJsonHelper;
    
    @Autowired
    public CS_CreditScoreCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
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
                .resource("CREDIT_SCORE");
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        final int creditscore = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("creditscore", element);
        baseDataValidator.reset().parameter("creditscore").value(creditscore).notBlank();
        
        final long client_id = this.fromApiJsonHelper.extractLongNamed("client_id", element);
        baseDataValidator.reset().parameter("client_id").value(client_id).notBlank().integerGreaterThanZero();

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

        final int creditscore = this.fromApiJsonHelper.extractIntegerSansLocaleNamed("creditscore", element);
        baseDataValidator.reset().parameter("creditscore").value(creditscore).notBlank();
        
        final long client_id = this.fromApiJsonHelper.extractLongNamed("client_id", element);
        baseDataValidator.reset().parameter("client_id").value(client_id).notBlank().integerGreaterThanZero();
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
                throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                                "Validation errors exist.", dataValidationErrors);
        }
    }

}
