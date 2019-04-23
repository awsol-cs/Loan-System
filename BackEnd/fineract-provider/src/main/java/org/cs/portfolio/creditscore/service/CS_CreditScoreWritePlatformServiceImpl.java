package org.cs.portfolio.creditscore.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigDecimal;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.cs.portfolio.creditscore.domain.CS_CreditScore;
import org.cs.portfolio.creditscore.domain.CS_CreditScoreRepository;
import org.cs.portfolio.creditscore.serialization.CS_CreditScoreCommandFromApiJsonDeserializer;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class CS_CreditScoreWritePlatformServiceImpl implements CS_CreditScoreWritePlatformService {
    
    private final CS_PlatformSecurityContext context;
    private final CS_CreditScoreRepository repository;
    private final CS_CreditScoreCommandFromApiJsonDeserializer deserializer;

    @Autowired
    public CS_CreditScoreWritePlatformServiceImpl(CS_PlatformSecurityContext context, 
            CS_CreditScoreRepository repository,
            CS_CreditScoreCommandFromApiJsonDeserializer deserializer) {
        this.context = context;
        this.repository = repository;
        this.deserializer = deserializer;
    }

    @Transactional
    @Override
    public CommandProcessingResult addCreditScoreRule(CS_JsonCommand command) {
        this.context.authenticatedUser();
        this.deserializer.validateForCreate(command.json());
        final CS_CreditScore creditScore = CS_CreditScore.fromJson(command);
        this.repository.save(creditScore);

        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditScore.getId()).build();
    }

    @Transactional
    @Override
    public CommandProcessingResult editCreditScoreRule(final long creditScoreId, CS_JsonCommand command) {
        this.context.authenticatedUser();
        this.deserializer.validateForUpdate(creditScoreId, command.json());
        final CS_CreditScore creditScoreToUpdate = this.repository.getOne(creditScoreId);
        final CS_CreditScore creditScore = CS_CreditScore.fromJson(command);

        if(creditScoreToUpdate.getClientId() != creditScore.getClientId()){
            creditScoreToUpdate.setClientId(creditScore.getClientId());
        }
        if(creditScoreToUpdate.getCreditScore() != creditScore.getCreditScore()){
            creditScoreToUpdate.setCreditScore(creditScore.getCreditScore());
        }

        this.repository.saveAndFlush(creditScoreToUpdate);

        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditScoreToUpdate.getId()).build();
    }
}
