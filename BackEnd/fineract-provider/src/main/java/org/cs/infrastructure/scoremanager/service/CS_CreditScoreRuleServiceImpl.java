package org.cs.infrastructure.scoremanager.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.math.BigDecimal;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.cs.infrastructure.scoremanager.data.CS_CreditScoreConfigurationData;
import org.cs.infrastructure.scoremanager.data.CS_RuleTypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Service
public class CS_CreditScoreRuleServiceImpl implements CS_CreditScoreRuleService {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CS_CreditScoreConfigurationData> scoringRuleDataRowMapper;
    private final RowMapper<CS_RuleTypeData> ruleTypeDataRowMapper;

    @Autowired
    public CS_CreditScoreRuleServiceImpl(final RoutingDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.scoringRuleDataRowMapper = new ScoringRuleRowMapper();
        this.ruleTypeDataRowMapper = new RuleTypeRowMapper();
    }
    
    @Override
    public Collection<CS_CreditScoreConfigurationData> getScoringRuleList() {
        String sql = "SELECT * FROM scoring_rule";
        final Collection<CS_CreditScoreConfigurationData> scoringRuleDataTempList =  this.jdbcTemplate.query(sql, this.scoringRuleDataRowMapper, new Object[] {});
        final Collection<CS_CreditScoreConfigurationData> scoringRuleDataList = new ArrayList<>();
        for(CS_CreditScoreConfigurationData scoringRuleData : scoringRuleDataTempList) {
            scoringRuleDataList.add(new CS_CreditScoreConfigurationData(scoringRuleData.getId(), scoringRuleData.getRuleName(), scoringRuleData.getWeightedValue(),
                    scoringRuleData.getRuleType(), this.getRuleTypeData(scoringRuleData.getId(), scoringRuleData.getRuleType()), scoringRuleData.getStatus()));
        }
        return scoringRuleDataList;
    }

    @Override
    public CS_CreditScoreConfigurationData getScoringRule(int id) {
        String sql = "SELECT * FROM scoring_rule WHERE id = ?";
        final CS_CreditScoreConfigurationData scoringRuleData =  this.jdbcTemplate.queryForObject(sql, this.scoringRuleDataRowMapper, new Object[] {id});
        return scoringRuleData;
    }

    @Override
    public int addScoringRule(CS_CreditScoreConfigurationData scoringRuleData) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int editScoringRule(CS_CreditScoreConfigurationData scoringRuleData) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteScoringRule(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

  
    private static final class ScoringRuleRowMapper implements RowMapper<CS_CreditScoreConfigurationData> {

        @Override
        public CS_CreditScoreConfigurationData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
                throws SQLException {

            final int id = rs.getInt("id");
            final String ruleName = rs.getString("rule_name");
            final int weightedValue = rs.getInt("weighted_value");
            final int ruleType = rs.getInt("rule_type");
            final int status = rs.getInt("status");
            
            return new CS_CreditScoreConfigurationData(id, ruleName, weightedValue, ruleType, null, status);
        }
    }


    @Override
    public Collection<CS_RuleTypeData> getRuleTypeDataList(int id, int ruleType) {
        String sql = "";
        if(ruleType == 1) {
            sql = "SELECT * FROM range_rule_type WHERE scoring_rule_id = ?";
        } else if(ruleType == 2) {
            sql = "SELECT * FROM choice_rule_type WHERE scoring_rule_id = ?";
        }
        final Collection<CS_RuleTypeData> ruleTypeDataList =  this.jdbcTemplate.query(sql, this.ruleTypeDataRowMapper, new Object[] {id});
        return ruleTypeDataList;
    }

    @Override
    public CS_RuleTypeData getRuleTypeData(int id, int ruleType) {
        String sql = "";
        if(ruleType == 1) {
            sql = "SELECT * FROM range_rule_type WHERE id = ?";
        } else if(ruleType == 2) {
            sql = "SELECT * FROM choice_rule_type WHERE id = ?";
        }
        final CS_RuleTypeData ruleTypeData =  this.jdbcTemplate.queryForObject(sql, this.ruleTypeDataRowMapper, new Object[] {id});
        return ruleTypeData;
    }
    
    private static final class RuleTypeRowMapper implements RowMapper<CS_RuleTypeData> {

        @Override
        public CS_RuleTypeData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            
            final int id = rs.getInt("id");
            final int scoringRuleId = rs.getInt("scoring_rule_id");
            final int minValue = rs.getInt("min_value");
            final int maxValue = rs.getInt("max_value");
            final String choiceName = rs.getString("choice_name");
            final BigDecimal relativeValue = rs.getBigDecimal("relative_value");
            
            return new CS_RuleTypeData(id, scoringRuleId, minValue, maxValue, choiceName, relativeValue);
        }
        
    }

}
