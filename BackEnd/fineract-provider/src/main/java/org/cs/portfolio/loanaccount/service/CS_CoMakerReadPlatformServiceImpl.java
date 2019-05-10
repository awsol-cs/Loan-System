package org.cs.portfolio.loanaccount.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.math.BigDecimal;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.cs.portfolio.client.domain.CS_KycInfoData;
import org.cs.portfolio.loanaccount.domain.CS_CoMakerData;
import org.cs.portfolio.loanaccount.domain.CS_CoMakerInfoData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.cs.portfolio.client.service.CS_KYCReadPlatformService;
import org.joda.time.LocalDate;
import org.springframework.dao.EmptyResultDataAccessException;

@Service
public class CS_CoMakerReadPlatformServiceImpl implements CS_CoMakerReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final CS_PlatformSecurityContext context;
    private final CS_KYCReadPlatformService kycReadPlatformService;

    private CoMakerInfoMapper coMakerInfoMapper = new CoMakerInfoMapper();

    @Autowired
    public CS_CoMakerReadPlatformServiceImpl(final CS_PlatformSecurityContext context, final RoutingDataSource dataSource,
            final CS_KYCReadPlatformService kycReadPlatformService) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.context = context;
        this.kycReadPlatformService = kycReadPlatformService;
    }

    private static final class CoMakerInfoMapper implements RowMapper<CS_CoMakerInfoData> {
        @Override
        public CS_CoMakerInfoData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final Long id = JdbcSupport.getLong(rs, "id");
            final Long kycId = JdbcSupport.getLong(rs, "kyc_id");
            final String firstname = rs.getString("firstname");
            final String middlename = rs.getString("middlename");
            final String lastname = rs.getString("lastname");
            final String fullname = rs.getString("fullname");
            final String mobileNo = rs.getString("mobile_no");
            final String emailAddress = rs.getString("email_address");
            final LocalDate dateOfBirth = JdbcSupport.getLocalDate(rs, "date_of_birth");
            final Long genderId = JdbcSupport.getLong(rs, "genderId");
            final String genderValue = rs.getString("genderValue");
            final CodeValueData gender = CodeValueData.instance(genderId, genderValue);
            return CS_CoMakerInfoData.instance(id, kycId, firstname, middlename, lastname, fullname, mobileNo, 
                emailAddress, dateOfBirth, gender);
        }
    }

    @Override
    public CS_CoMakerData retrieveOne(Long loanId){
        CS_CoMakerData comaker = null;
        String sql = "SELECT " + 
            " c.id as id," +
            " c.kyc_id as kyc_id," + 
            " c.firstname as firstname," + 
            " c.middlename as middlename," +
            " c.lastname as lastname," + 
            " c.fullname as fullname," +
            " c.mobile_no as mobile_no," +
            " c.gender as genderId," +
            " cv.code_value as genderValue," +
            " c.date_of_birth as date_of_birth," +
            " c.email_address as email_address" +
            " FROM m_co_maker c" +
            " LEFT JOIN m_code_value cv ON cv.id = c.gender" +
            " WHERE c.loan_id = ?;";
        try {
            CS_CoMakerInfoData coMakerInfo = this.jdbcTemplate.queryForObject(sql, coMakerInfoMapper, new Object[] {loanId});
            CS_KycInfoData kycInfo = this.kycReadPlatformService.getKycInfoData(null, coMakerInfo.getKycId());

            return CS_CoMakerData.instance(kycInfo, coMakerInfo);

        } catch(final EmptyResultDataAccessException e){
            return null;
        }
    }

}
