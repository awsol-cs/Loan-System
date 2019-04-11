package org.cs.infrastructure.scoremanager.service;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.cs.infrastructure.scoremanager.domain.CS_CreditScoreFormula;
import org.cs.infrastructure.scoremanager.domain.CS_CreditScoreFormulaRepository;
import org.cs.infrastructure.scoremanager.serialization.CS_CreditScoreFormulaCommandFromApiJsonDeserializer;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CS_CreditScoreFormulaWritePlatformServiceImpl implements CS_CreditScoreFormulaWritePlatformService {
    
    private final CS_PlatformSecurityContext context;
    private final CS_CreditScoreFormulaRepository repository;
    private final CS_CreditScoreFormulaCommandFromApiJsonDeserializer deserializer;
    
    @Autowired
    public CS_CreditScoreFormulaWritePlatformServiceImpl(CS_PlatformSecurityContext context, CS_CreditScoreFormulaRepository repository,
            CS_CreditScoreFormulaCommandFromApiJsonDeserializer deserializer) {
        this.context = context;
        this.repository = repository;
        this.deserializer = deserializer;
    }

    @Transactional
    @Override
    public CommandProcessingResult addCreditScoreFormula(CS_JsonCommand command) {
        this.context.authenticatedUser();
        this.deserializer.validateForCreate(command.json());
        
        final CS_CreditScoreFormula creditScoreFormula = CS_CreditScoreFormula.fromJson(command);
        
        this.repository.save(creditScoreFormula);
        
        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditScoreFormula.getId()).build();
    }

    @Transactional
    @Override
    public CommandProcessingResult editCreditScoreFormula(long id, CS_JsonCommand command) {
        this.context.authenticatedUser();
        this.deserializer.validateForUpdate(id, command.json());
        
        final String formulaName = command.stringValueOfParameterNamed("formulaName");
        final String formula = command.stringValueOfParameterNamed("formula");
        final String status = command.stringValueOfParameterNamed("status");
        
        final CS_CreditScoreFormula creditScoreFormula = this.repository.getOne(id);
        
        creditScoreFormula.setFormulaName(formulaName);
        creditScoreFormula.setFormula(formula);
        creditScoreFormula.setStatus(status);
        
        this.repository.saveAndFlush(creditScoreFormula);
        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditScoreFormula.getId()).build();
    }

}
