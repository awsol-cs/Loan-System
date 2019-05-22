package org.cs.portfolio.client.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.math.BigDecimal;

import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.cs.infrastructure.security.service.CS_PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.cs.portfolio.client.data.CS_KycInfoData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.springframework.dao.EmptyResultDataAccessException;

@Service
public class CS_KYCReadPlatformServiceImpl implements CS_KYCReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final CS_PlatformSecurityContext context;

    @Autowired
    public CS_KYCReadPlatformServiceImpl(final CS_PlatformSecurityContext context, final RoutingDataSource dataSource) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.context = context;
    }

    private static final class KycInfoMapper implements RowMapper<CS_KycInfoData> {
        @Override
        public CS_KycInfoData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final Long id = JdbcSupport.getLong(rs, "id");
            final String nationality = rs.getString("nationality");
            final Integer noOfDependents = rs.getInt("no_of_dependents");

            final Long maritalStatusId = JdbcSupport.getLong(rs, "marital_status");
            final String maritalStatusValue = rs.getString("marital_status_value");
            final CodeValueData maritalStatus = CodeValueData.instance(maritalStatusId, maritalStatusValue);

            final Long educationalAttainmentId = JdbcSupport.getLong(rs, "educational_attainment");
            final String educationalAttainmentValue = rs.getString("educational_attainment_value");
            final CodeValueData educationalAttainment = CodeValueData.instance(educationalAttainmentId, educationalAttainmentValue);

            final String othersEducationalAttainment = rs.getString("others_educational_attainment");
            final String schoolLastAttended = rs.getString("school_last_attended");
            final String placeOfBirth = rs.getString("place_of_birth");
            final String motherMaidenName = rs.getString("mother_maiden_name");

            final Long residenceOwnershipId = JdbcSupport.getLong(rs, "residence_ownership");
            final String residenceOwnershipValue = rs.getString("residence_ownership_value");
            final CodeValueData residenceOwnership = CodeValueData.instance(residenceOwnershipId, residenceOwnershipValue);

            final Integer rentedResidenceOwnership = rs.getInt("rented_residence_ownership");

            final Long employmentId = JdbcSupport.getLong(rs, "employment");
            final String employmentValue = rs.getString("employment_value");
            final CodeValueData employment = CodeValueData.instance(employmentId, employmentValue);

            final String otherEmployment = rs.getString("other_employment");

            final Long selfEmployedId = JdbcSupport.getLong(rs, "self_employed");
            final String selfEmployedValue = rs.getString("self_employed_value");
            final CodeValueData selfEmployed = CodeValueData.instance(selfEmployedId, selfEmployedValue);

            final Integer yearsInOperation = rs.getInt("years_in_operation");
            final Integer noOfEmployees = rs.getInt("no_of_employees");
            final String nameOfPresentEmployerBusiness = rs.getString("name_of_present_employer_business");
            final String natureOfBusiness = rs.getString("nature_of_business");
            final String officeAddress = rs.getString("office_address");
            final String officePhone = rs.getString("office_phone");
            final String officeMobilePhone = rs.getString("office_mobile_phone");
            final String officeLocalNumber = rs.getString("office_local_number");
            final String officeFaxNumber = rs.getString("office_fax_number");
            final String officeEmailAddress = rs.getString("office_email_address");

            final Long rankId = JdbcSupport.getLong(rs, "rank");
            final String rankValue = rs.getString("rank_value");
            final CodeValueData rank = CodeValueData.instance(rankId, rankValue);

            final String titleOrPosition = rs.getString("title_or_position");
            final Integer grossAnnualIncome = rs.getInt("gross_annual_income");
            final Integer otherIncome = rs.getInt("other_income");
            final String nameOfPreviousEmployer = rs.getString("name_of_previous_employer");
            final String officeAddressPrevious = rs.getString("office_address_previous");
            final Integer yearsWithPresentEmployer = rs.getInt("years_with_present_employer");
            final Integer yearsWithPreviousEmployer = rs.getInt("years_with_previous_employer");
            final String nameReference = rs.getString("name_reference");
            final String relationshipReference = rs.getString("relationship_reference");
            final String employerReference = rs.getString("employer_reference");
            final String addressReference = rs.getString("address_reference");
            final String contactNumberReference = rs.getString("contact_number_reference");
            final String mobileReference = rs.getString("mobile_reference");

            final Long relatedToOfficerId = JdbcSupport.getLong(rs, "related_to_officer");
            final String relatedToOfficerValue = rs.getString("related_to_officer_value");
            final CodeValueData relatedToOfficer = CodeValueData.instance(relatedToOfficerId, relatedToOfficerValue);

            final String nameOfOfficer = rs.getString("name_of_officer");
            final String contactNumberOfficer = rs.getString("contact_number_officer");

            final Long relationshipOfOfficerId = JdbcSupport.getLong(rs, "relationship_of_officer");
            final String relationshipOfOfficerValue = rs.getString("relationship_of_officer_value");
            final CodeValueData relationshipOfOfficer = CodeValueData.instance(relationshipOfOfficerId, relationshipOfOfficerValue);

            return CS_KycInfoData.instance(id, nationality, noOfDependents, maritalStatus,
                educationalAttainment, othersEducationalAttainment, schoolLastAttended, placeOfBirth,
                motherMaidenName, residenceOwnership, rentedResidenceOwnership, employment,
                otherEmployment, selfEmployed, yearsInOperation, noOfEmployees, nameOfPresentEmployerBusiness,
                natureOfBusiness, officeAddress, officePhone, officeMobilePhone, officeLocalNumber,
                officeFaxNumber, officeEmailAddress, rank, titleOrPosition, grossAnnualIncome,
                otherIncome, nameOfPreviousEmployer, officeAddressPrevious, yearsWithPresentEmployer,
                yearsWithPreviousEmployer, nameReference, relationshipReference, employerReference,
                addressReference, contactNumberReference, mobileReference, relatedToOfficer,
                nameOfOfficer, contactNumberOfficer, relationshipOfOfficer);
        }
    }

    @Override
    public CS_KycInfoData getKycInfoData(Long clientId, Long kycId){
        CS_KycInfoData kycInfo = null;
        String sql = "SELECT " +
            " k.id as id," + 
            " k.nationality as nationality," + 
            " k.no_of_dependents as no_of_dependents," + 
            " k.marital_status as marital_status," + 
            " ms.code_value as marital_status_value," + 
            " k.educational_attainment as educational_attainment," + 
            " ea.code_value as educational_attainment_value," + 
            " k.others_educational_attainment as others_educational_attainment," + 
            " k.school_last_attended as school_last_attended," + 
            " k.place_of_birth as place_of_birth," +
            " k.mother_maiden_name as mother_maiden_name," + 
            " k.residence_ownership as residence_ownership," + 
            " ro.code_value as residence_ownership_value," + 
            " k.rented_residence_ownership as rented_residence_ownership," + 
            " k.employment as employment," + 
            " e.code_value as employment_value," + 
            " k.other_employment as other_employment," + 
            " k.self_employed as self_employed," + 
            " se.code_value as self_employed_value," + 
            " k.years_in_operation as years_in_operation," + 
            " k.no_of_employees as no_of_employees," + 
            " k.name_of_present_employer_business as name_of_present_employer_business," + 
            " k.nature_of_business as nature_of_business," + 
            " k.office_address as office_address," +
            " k.office_phone as office_phone," + 
            " k.office_mobile_phone as office_mobile_phone," +
            " k.office_local_number as office_local_number," +
            " k.office_fax_number as office_fax_number," +
            " k.office_email_address as office_email_address," +
            " k.rank as rank," +
            " r.code_value as rank_value," +
            " k.title_or_position as title_or_position," +
            " k.gross_annual_income as gross_annual_income," +
            " k.other_income as other_income," + 
            " k.name_of_previous_employer as name_of_previous_employer," + 
            " k.office_address_previous as office_address_previous," + 
            " k.years_with_present_employer as years_with_present_employer," + 
            " k.years_with_previous_employer as years_with_previous_employer," +
            " k.name_reference as name_reference," +
            " k.relationship_reference as relationship_reference," +
            " k.employer_reference as employer_reference," + 
            " k.address_reference as address_reference," +
            " k.contact_number_reference as contact_number_reference," + 
            " k.mobile_reference as mobile_reference," +
            " k.related_to_officer as related_to_officer," + 
            " rto.code_value as related_to_officer_value," + 
            " k.name_of_officer as name_of_officer," + 
            " k.contact_number_officer as contact_number_officer," + 
            " k.relationship_of_officer as relationship_of_officer," + 
            " roo.code_value as relationship_of_officer_value" +
        " FROM m_kyc_info k" +
        " LEFT JOIN m_code_value ms on ms.id = k.marital_status" +
        " LEFT JOIN m_code_value ea on ea.id = k.educational_attainment" +
        " LEFT JOIN m_code_value ro on ro.id = k.residence_ownership" +
        " LEFT JOIN m_code_value e on e.id = k.employment" +
        " LEFT JOIN m_code_value se on se.id = k.self_employed" +
        " LEFT JOIN m_code_value r on r.id = k.rank" +
        " LEFT JOIN m_code_value rto on rto.id = k.related_to_officer" + 
        " LEFT JOIN m_code_value roo on roo.id = k.relationship_of_officer";
        try{
            if(clientId != null){
                final KycInfoMapper kycInfoMapper = new KycInfoMapper();
                sql = sql + " WHERE k.client_id = ?";
                kycInfo = this.jdbcTemplate.queryForObject(sql, kycInfoMapper, new Object[] {clientId});
            }else {
                final KycInfoMapper kycInfoMapper = new KycInfoMapper();
                sql = sql + " WHERE k.id = ?";
                kycInfo = this.jdbcTemplate.queryForObject(sql, kycInfoMapper, new Object[] {kycId});
            }
            return kycInfo;
        }catch(final EmptyResultDataAccessException e){
            return null;
        }
    }

}
