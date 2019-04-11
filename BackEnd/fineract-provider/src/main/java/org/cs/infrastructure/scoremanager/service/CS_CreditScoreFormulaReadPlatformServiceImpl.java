package org.cs.infrastructure.scoremanager.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.cs.infrastructure.scoremanager.data.CS_CreditScoreFormulaData;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CS_CreditScoreFormulaReadPlatformServiceImpl implements CS_CreditScoreFormulaReadPlatformService {
    
    private final JdbcTemplate jdbcTemplate;
    private final CS_PlatformSecurityContext context;
    
    @Autowired
    public CS_CreditScoreFormulaReadPlatformServiceImpl(final CS_PlatformSecurityContext context, final RoutingDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.context = context;
    }
    
    private static final class CreditScoreFormulaMapper implements RowMapper<CS_CreditScoreFormulaData> {
        public String schema() {
            return " * FROM formula";
        }

        @Override
        public CS_CreditScoreFormulaData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final int id = rs.getInt("id");
            final String formulaName = rs.getString("formula_name");
            final String formula = rs.getString("formula");
            final String status = rs.getString("status");
            final String createdBy = rs.getString("created_by");
            final String createdDate = rs.getString("created_date");
            final String updatedBy = rs.getString("updated_by");
            final String updatedDate = rs.getString("updated_date");
            return CS_CreditScoreFormulaData.instance(id, formulaName, formula, status, createdBy, createdDate, updatedBy, updatedDate);
        }
    }

    @Override
    public Collection<CS_CreditScoreFormulaData> getCreditScoreFormulaDataList() {
        this.context.authenticatedUser();
        final CreditScoreFormulaMapper mapper = new CreditScoreFormulaMapper();
        final String sql = "SELECT" + mapper.schema();
        Collection<CS_CreditScoreFormulaData> dataList = this.jdbcTemplate.query(sql, mapper, new Object[] {});
        return dataList;
    }

    @Override
    public CS_CreditScoreFormulaData getCreditScoreFormulaDataById(long id) {
        this.context.authenticatedUser();
        final CreditScoreFormulaMapper mapper = new CreditScoreFormulaMapper();
        final String sql = "SELECT" + mapper.schema() + " WHERE id = ?";
        CS_CreditScoreFormulaData data = this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {id});
        return data;
    }

}
