package org.cs.infrastructure.scoremanager.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigDecimal;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.cs.infrastructure.scoremanager.domain.CS_CreditScoreConfiguration;
import org.cs.infrastructure.scoremanager.domain.CS_RangeRuleType;
import org.cs.infrastructure.scoremanager.domain.CS_ChoiceRuleType;
import org.cs.infrastructure.scoremanager.domain.CS_CreditScoreConfigurationRepository;
import org.cs.infrastructure.scoremanager.domain.CS_RangeRuleTypeRepository;
import org.cs.infrastructure.scoremanager.domain.CS_ChoiceRuleTypeRepository;
import org.cs.infrastructure.scoremanager.serialization.CS_CreditScoreConfigCommandFromApiJsonDeserializer;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class CS_CreditScoreConfigWritePlatformServiceImpl implements CS_CreditScoreConfigWritePlatformService {
    
    private final CS_PlatformSecurityContext context;
    private final CS_CreditScoreConfigurationRepository repository;
    private final CS_RangeRuleTypeRepository rangeRepository;
    private final CS_ChoiceRuleTypeRepository choiceRepository;
    private final CS_CreditScoreConfigCommandFromApiJsonDeserializer deserializer;

    @Autowired
    public CS_CreditScoreConfigWritePlatformServiceImpl(CS_PlatformSecurityContext context, CS_CreditScoreConfigurationRepository repository,
            CS_RangeRuleTypeRepository rangeRepository, CS_ChoiceRuleTypeRepository choiceRepository, 
            CS_CreditScoreConfigCommandFromApiJsonDeserializer deserializer) {
        this.context = context;
        this.repository = repository;
        this.rangeRepository = rangeRepository;
        this.choiceRepository = choiceRepository;
        this.deserializer = deserializer;
    }

    @Transactional
    @Override
    public CommandProcessingResult addCreditScoreRule(CS_JsonCommand command) {
        this.context.authenticatedUser();
        this.deserializer.validateForCreate(command.json());
        final CS_CreditScoreConfiguration creditScore = CS_CreditScoreConfiguration.fromJson(command);
        this.repository.save(creditScore);
        int id = creditScore.getId().intValue();
        if(null != creditScore.getRangeRuleTypeList() && !creditScore.getRangeRuleTypeList().isEmpty()){
            for(int i = 0; i < creditScore.getRangeRuleTypeList().size(); i++){
                creditScore.getRangeRuleTypeList().get(i).setScoringRuleId(id);
                this.rangeRepository.save(creditScore.getRangeRuleTypeList().get(i));
            }
        }else if(null != creditScore.getChoiceRuleTypeList() && !creditScore.getChoiceRuleTypeList().isEmpty()){
            for(int i = 0; i < creditScore.getChoiceRuleTypeList().size(); i++){
                creditScore.getChoiceRuleTypeList().get(i).setScoringRuleId(id);
                this.choiceRepository.save(creditScore.getChoiceRuleTypeList().get(i));
            }
        }
        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditScore.getId()).build();
    }

    @Transactional
    @Override
    public CommandProcessingResult editCreditScoreRule(final int scoringRuleId, CS_JsonCommand command) {
        this.context.authenticatedUser();
        this.deserializer.validateForUpdate(scoringRuleId, command.json());
        final CS_CreditScoreConfiguration creditScoreToUpdate = this.repository.getOne(scoringRuleId);
        final CS_CreditScoreConfiguration creditScore = CS_CreditScoreConfiguration.fromJson(command);
        if(!creditScoreToUpdate.getRuleName().equals(creditScore.getRuleName())){
            creditScoreToUpdate.setRuleName(creditScore.getRuleName());
        }
        if(creditScoreToUpdate.getRuleType() != creditScore.getRuleType()){
            creditScoreToUpdate.setRuleType(creditScore.getRuleType());
        }
        if(creditScoreToUpdate.getWeightedValue() != creditScore.getWeightedValue()){
            creditScoreToUpdate.setWeightedValue(creditScore.getWeightedValue());
        }
        if(!creditScoreToUpdate.getStatus().equals(creditScore.getStatus())){
            creditScoreToUpdate.setStatus(creditScore.getStatus());
        }
        if(!creditScoreToUpdate.getTag().equals(creditScore.getTag())){
            creditScoreToUpdate.setTag(creditScore.getTag());
        }
        this.repository.saveAndFlush(creditScoreToUpdate);
        JsonArray ruleTypeDataList = command.arrayOfParameterNamed("ruleTypeDataList");
        if(1 == creditScoreToUpdate.getRuleType()){
            List<CS_RangeRuleType> rangeRules = this.rangeRepository.findByScoringRuleId(scoringRuleId);
            List<CS_RangeRuleType> rangeToDelete = new ArrayList<CS_RangeRuleType>();
            for(int i = 0; i < rangeRules.size(); i++){
                int ctr = 0;
                for(int rule = 0; rule < ruleTypeDataList.size(); rule++){
                    final JsonObject jsonObject = ruleTypeDataList.get(rule).getAsJsonObject();
                    if(jsonObject.has("id")){
                        if(rangeRules.get(i).getId() == jsonObject.get("id").getAsLong()){
                            CS_RangeRuleType rangeRule = rangeRules.get(i);
                            CS_RangeRuleType object = creditScore.getRangeRuleTypeList().get(rule);
                            rangeRule.setMinValue(object.getMinValue());
                            rangeRule.setMaxValue(object.getMaxValue());
                            rangeRule.setRelativeValue(object.getRelativeValue());
                            this.rangeRepository.saveAndFlush(rangeRule);
                            break;
                        }else{
                            ctr++;
                        }
                    }else{
                        ctr++;
                    }
                }
                if(ctr == ruleTypeDataList.size()){
                    rangeToDelete.add(rangeRules.get(i));
                }
            }
            this.rangeRepository.deleteInBatch(rangeToDelete);
            for(int i = 0; i < ruleTypeDataList.size(); i++){
                final JsonObject jsonObject = ruleTypeDataList.get(i).getAsJsonObject();
                if(!jsonObject.has("id")){
                    CS_RangeRuleType object = creditScore.getRangeRuleTypeList().get(i);
                    object.setScoringRuleId(scoringRuleId);
                    this.rangeRepository.save(object);
                }
            }
        }else{
            List<CS_ChoiceRuleType> choiceRules = this.choiceRepository.findByScoringRuleId(scoringRuleId);
            List<CS_ChoiceRuleType> choiceToDelete = new ArrayList<CS_ChoiceRuleType>();
            for(int i = 0; i < choiceRules.size(); i++){
                int ctr = 0;
                for(int rule = 0; rule < ruleTypeDataList.size(); rule++){
                    final JsonObject jsonObject = ruleTypeDataList.get(rule).getAsJsonObject();
                    if(jsonObject.has("id")){
                        if(choiceRules.get(i).getId() == jsonObject.get("id").getAsLong()){
                            CS_ChoiceRuleType choiceRule = choiceRules.get(i);
                            CS_ChoiceRuleType object = creditScore.getChoiceRuleTypeList().get(rule);
                            choiceRule.setChoiceName(object.getChoiceName());
                            choiceRule.setRelativeValue(object.getRelativeValue());
                            this.choiceRepository.saveAndFlush(choiceRule);
                            break;
                        }else{
                            ctr++;
                        }
                    }else{
                        ctr++;
                    }
                }
                if(ctr == ruleTypeDataList.size()){
                    choiceToDelete.add(choiceRules.get(i));
                }
            }
            this.choiceRepository.deleteInBatch(choiceToDelete);
            for(int i = 0; i < ruleTypeDataList.size(); i++){
                final JsonObject jsonObject = ruleTypeDataList.get(i).getAsJsonObject();
                if(!jsonObject.has("id")){
                    CS_ChoiceRuleType object = creditScore.getChoiceRuleTypeList().get(i);
                    object.setScoringRuleId(scoringRuleId);
                    this.choiceRepository.save(object);
                }
            }
        }
        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditScoreToUpdate.getId()).build();
    }

    @Transactional
    @Override
    public CommandProcessingResult editCreditScoreRuleStatus(final int scoringRuleId, CS_JsonCommand command) {
        this.context.authenticatedUser();
        this.deserializer.validateForUpdateStatus(scoringRuleId, command.json());
        final CS_CreditScoreConfiguration creditScoreToUpdate = this.repository.getOne(scoringRuleId);
        final CS_CreditScoreConfiguration creditScore = CS_CreditScoreConfiguration.forUpdateStatus(command);
        if(!creditScoreToUpdate.getStatus().equals(creditScore.getStatus())){
            creditScoreToUpdate.setStatus(creditScore.getStatus());
        }
        this.repository.saveAndFlush(creditScoreToUpdate);
        return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(creditScoreToUpdate.getId()).build();
    }
}
