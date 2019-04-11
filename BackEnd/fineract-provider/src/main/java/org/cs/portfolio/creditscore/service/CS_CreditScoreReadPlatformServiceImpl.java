package org.cs.portfolio.creditscore.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.cs.portfolio.creditscore.data.CS_CreditScoreData;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CS_CreditScoreReadPlatformServiceImpl implements CS_CreditScoreReadPlatformService {
    
    private final JdbcTemplate jdbcTemplate;
    private final CS_PlatformSecurityContext context;
    
    @Autowired
    public CS_CreditScoreReadPlatformServiceImpl(final CS_PlatformSecurityContext context, final RoutingDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.context = context;
    }
    
    private static final class CreditScoreMapper implements RowMapper<CS_CreditScoreData> {
        public String schema() {
            return " * FROM credit_score";
        }

        @Override
        public CS_CreditScoreData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final long id = rs.getLong("id");
            final long client_id = rs.getLong("client_id");
            final int creditscore = rs.getInt("creditscore");
            return CS_CreditScoreData.instance(id, client_id, creditscore);
        }
    }

    @Override
    public CS_CreditScoreData getCreditScoreDataById(long id) {
        
        CS_CreditScoreData data;

        try{
            this.context.authenticatedUser();
            final CreditScoreMapper mapper = new CreditScoreMapper();
            final String sql = "SELECT" + mapper.schema() + " WHERE client_id = ?";
            data = this.jdbcTemplate.queryForObject(sql, mapper, new Object[] {id});
        }catch(final EmptyResultDataAccessException e){
            data = new CS_CreditScoreData(0, 0, 0);
        }
        return data;
    }

}
