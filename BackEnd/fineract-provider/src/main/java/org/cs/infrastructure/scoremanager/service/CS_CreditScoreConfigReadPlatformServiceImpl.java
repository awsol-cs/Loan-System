package org.cs.infrastructure.scoremanager.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.math.BigDecimal;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.cs.infrastructure.scoremanager.data.CS_ScoreManagerData;
import org.cs.infrastructure.scoremanager.data.CS_RuleTypeData;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class CS_CreditScoreConfigReadPlatformServiceImpl implements CS_CreditScoreConfigReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final CS_PlatformSecurityContext context;

    @Autowired
    public CS_CreditScoreConfigReadPlatformServiceImpl(final CS_PlatformSecurityContext context, final RoutingDataSource dataSource) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.context = context;
    }
    
    private static final class CreditScoreMapper implements RowMapper<CS_ScoreManagerData> {
        public String schema() {
            return " * FROM scoring_rule";
        }

        @Override
        public CS_ScoreManagerData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final int id = rs.getInt("id");
            final String ruleName = rs.getString("rule_name");
            final int weightedValue = rs.getInt("weighted_value");
            final int ruleType = rs.getInt("rule_type");
            final String status = rs.getString("status");
            final String tag = rs.getString("tag");
            final String createdBy = rs.getString("created_by");
            final String createdDate = rs.getString("created_date");
            final String updatedBy = rs.getString("updated_by");
            final String updatedDate = rs.getString("updated_date");
            return CS_ScoreManagerData.instance(id, ruleName, weightedValue, ruleType, null, status, tag, createdBy, createdDate, updatedBy, updatedDate);
        }
    }
    
    private static final class RangeRuleTypeMapper implements RowMapper<CS_RuleTypeData> {
        @Override
        public CS_RuleTypeData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final int id = rs.getInt("id");
            final int scoringRuleId = rs.getInt("scoring_rule_id");
            final int minValue = rs.getInt("min_value");
            final int maxValue = rs.getInt("max_value");
            final BigDecimal relativeValue = rs.getBigDecimal("relative_value");
            return CS_RuleTypeData.instance(id, scoringRuleId, minValue, maxValue, null, relativeValue);
        }
        
    }
    
    private static final class ChoiceRuleTypeMapper implements RowMapper<CS_RuleTypeData> {
        @Override
        public CS_RuleTypeData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final int id = rs.getInt("id");
            final int scoringRuleId = rs.getInt("scoring_rule_id");
            final String choiceName = rs.getString("choice_name");
            final BigDecimal relativeValue = rs.getBigDecimal("relative_value");
            return CS_RuleTypeData.instance(id, scoringRuleId, 0, 0, choiceName, relativeValue);
        }
        
    }

    @Override
    public Collection<CS_ScoreManagerData> getScoreConfigList() {
        this.context.authenticatedUser();
        final CreditScoreMapper creditScoreMapper = new CreditScoreMapper();
        final String sql = "SELECT" + creditScoreMapper.schema();
        Collection<CS_ScoreManagerData> tempCreditScoreDataList = this.jdbcTemplate.query(sql, creditScoreMapper, new Object[] {});
        Collection<CS_ScoreManagerData> creditScoreDataList = new ArrayList<>();
        for(CS_ScoreManagerData creditScoreData : tempCreditScoreDataList) {
            CS_ScoreManagerData tempCreditScoreData = new CS_ScoreManagerData(creditScoreData);
            tempCreditScoreData.setRuleTypeDataList(this.getRuleTypeDataList(tempCreditScoreData.getRuleType(), tempCreditScoreData.getId()));
            creditScoreDataList.add(tempCreditScoreData);
        }
        return creditScoreDataList;
    }

    @Override
    public CS_ScoreManagerData getCreditScoreById(int id) {
        this.context.authenticatedUser();
        final CreditScoreMapper creditScoreMapper = new CreditScoreMapper();
        final String sql = "SELECT" + creditScoreMapper.schema() + " WHERE id = ?";
        CS_ScoreManagerData tempCreditScoreData = this.jdbcTemplate.queryForObject(sql, creditScoreMapper, new Object[] {id});
        CS_ScoreManagerData creditScoreData = new CS_ScoreManagerData(tempCreditScoreData);
        creditScoreData.setRuleTypeDataList(this.getRuleTypeDataList(tempCreditScoreData.getRuleType(), tempCreditScoreData.getId()));
        return creditScoreData;
    }

    @Override
    public Collection<CS_RuleTypeData> getRuleTypeDataList(int ruleType, int scoringRuleId) {
        this.context.authenticatedUser();
        Collection<CS_RuleTypeData> ruleTypeDataList = null;
        String sql = "";
        if(ruleType == 1) {
            final RangeRuleTypeMapper rangeRuleTypeMapper = new RangeRuleTypeMapper();
            sql = "SELECT * FROM range_rule_type WHERE scoring_rule_id = ?";
            ruleTypeDataList =  this.jdbcTemplate.query(sql, rangeRuleTypeMapper, new Object[] {scoringRuleId});
        } else {
            final ChoiceRuleTypeMapper choiceRuleTypeMapper = new ChoiceRuleTypeMapper();
            sql = "SELECT * FROM choice_rule_type WHERE scoring_rule_id = ?";
            ruleTypeDataList =  this.jdbcTemplate.query(sql, choiceRuleTypeMapper, new Object[] {scoringRuleId});
        }
        return ruleTypeDataList;
    }

    @Override
    public CS_RuleTypeData getRuleTypeData(int ruleType, int id) {
        this.context.authenticatedUser();
        CS_RuleTypeData ruleTypeData = null;
        String sql = "";
        if(ruleType == 1) {
            final RangeRuleTypeMapper rangeRuleTypeMapper = new RangeRuleTypeMapper();
            sql = "SELECT * FROM range_rule_type WHERE id = ?";
            ruleTypeData = this.jdbcTemplate.queryForObject(sql, rangeRuleTypeMapper, new Object[] {id});
        } else {
            final ChoiceRuleTypeMapper choiceRuleTypeMapper = new ChoiceRuleTypeMapper();
            sql = "SELECT * FROM choice_rule_type WHERE id = ?";
            ruleTypeData = this.jdbcTemplate.queryForObject(sql, choiceRuleTypeMapper, new Object[] {id});
        }
        return ruleTypeData;
    }

}
