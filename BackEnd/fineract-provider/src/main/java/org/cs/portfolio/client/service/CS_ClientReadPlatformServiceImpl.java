/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.cs.portfolio.client.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.apache.fineract.infrastructure.codes.service.CodeValueReadPlatformService;
import org.apache.fineract.infrastructure.configuration.data.GlobalConfigurationPropertyData;
import org.apache.fineract.infrastructure.configuration.service.ConfigurationReadPlatformService;
import org.apache.fineract.infrastructure.core.api.ApiParameterHelper;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.Page;
import org.apache.fineract.infrastructure.core.service.PaginationHelper;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.core.service.SearchParameters;
import org.apache.fineract.infrastructure.dataqueries.data.DatatableData;
import org.apache.fineract.infrastructure.dataqueries.data.EntityTables;
import org.apache.fineract.infrastructure.dataqueries.data.StatusEnum;
import org.apache.fineract.infrastructure.dataqueries.service.EntityDatatableChecksReadService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.infrastructure.security.utils.ColumnValidator;
import org.apache.fineract.organisation.office.data.OfficeData;
import org.apache.fineract.organisation.office.service.OfficeReadPlatformService;
import org.apache.fineract.organisation.staff.data.StaffData;
import org.apache.fineract.organisation.staff.service.StaffReadPlatformService;
import org.apache.fineract.portfolio.address.data.AddressData;
import org.apache.fineract.portfolio.address.service.AddressReadPlatformService;
import org.apache.fineract.portfolio.client.api.ClientApiConstants;
import org.cs.portfolio.client.data.CS_ClientData;
import org.apache.fineract.portfolio.client.data.ClientFamilyMembersData;
import org.apache.fineract.portfolio.client.data.ClientNonPersonData;
import org.apache.fineract.portfolio.client.data.ClientTimelineData;
import org.apache.fineract.portfolio.client.domain.ClientEnumerations;
import org.apache.fineract.portfolio.client.domain.ClientStatus;
import org.apache.fineract.portfolio.client.domain.LegalForm;
import org.apache.fineract.portfolio.client.exception.ClientNotFoundException;
import org.apache.fineract.portfolio.group.data.GroupGeneralData;
import org.apache.fineract.portfolio.loanaccount.domain.LoanStatus;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransactionType;
import org.apache.fineract.portfolio.savings.SavingsAccountTransactionType;
import org.apache.fineract.portfolio.savings.data.SavingsProductData;
import org.apache.fineract.portfolio.savings.domain.SavingsAccountStatusType;
import org.apache.fineract.portfolio.savings.service.SavingsProductReadPlatformService;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.apache.fineract.portfolio.client.service.ClientFamilyMembersReadPlatformService;

import org.cs.portfolio.client.data.CS_KycInfoData;

@Service
public class CS_ClientReadPlatformServiceImpl implements CS_ClientReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformSecurityContext context;
    private final OfficeReadPlatformService officeReadPlatformService;
    private final StaffReadPlatformService staffReadPlatformService;
    private final CodeValueReadPlatformService codeValueReadPlatformService;
    private final SavingsProductReadPlatformService savingsProductReadPlatformService;
    // data mappers
    private final PaginationHelper<CS_ClientData> paginationHelper = new PaginationHelper<>();
    private final ClientMapper clientMapper = new ClientMapper();
    private final ParentGroupsMapper clientGroupsMapper = new ParentGroupsMapper();

    private final AddressReadPlatformService addressReadPlatformService;
    private final ClientFamilyMembersReadPlatformService clientFamilyMembersReadPlatformService;
    private final ConfigurationReadPlatformService configurationReadPlatformService;
    private final EntityDatatableChecksReadService entityDatatableChecksReadService;
    private final ColumnValidator columnValidator;
    private final CS_KYCReadPlatformService kycReadPlatformService;

    @Autowired
    public CS_ClientReadPlatformServiceImpl(final PlatformSecurityContext context, final RoutingDataSource dataSource,
            final OfficeReadPlatformService officeReadPlatformService, final StaffReadPlatformService staffReadPlatformService,
            final CodeValueReadPlatformService codeValueReadPlatformService,
            final SavingsProductReadPlatformService savingsProductReadPlatformService,
            final AddressReadPlatformService addressReadPlatformService,final ClientFamilyMembersReadPlatformService clientFamilyMembersReadPlatformService,
            final ConfigurationReadPlatformService configurationReadPlatformService,
            final EntityDatatableChecksReadService entityDatatableChecksReadService,
			final ColumnValidator columnValidator,
            final CS_KYCReadPlatformService kycReadPlatformService) {
		this.context = context;
        this.officeReadPlatformService = officeReadPlatformService;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.staffReadPlatformService = staffReadPlatformService;
        this.codeValueReadPlatformService = codeValueReadPlatformService;
        this.savingsProductReadPlatformService = savingsProductReadPlatformService;
        this.addressReadPlatformService=addressReadPlatformService;
        this.clientFamilyMembersReadPlatformService=clientFamilyMembersReadPlatformService;
        this.configurationReadPlatformService=configurationReadPlatformService;
        this.entityDatatableChecksReadService = entityDatatableChecksReadService;
        this.columnValidator = columnValidator;
        this.kycReadPlatformService = kycReadPlatformService;
	}

    @Override
    public CS_ClientData retrieveTemplate(final Long officeId, final boolean staffInSelectedOfficeOnly) {
        this.context.authenticatedUser();

        final Long defaultOfficeId = defaultToUsersOfficeIfNull(officeId);
        AddressData address=null;

        final Collection<OfficeData> offices = this.officeReadPlatformService.retrieveAllOfficesForDropdown();

        final Collection<SavingsProductData> savingsProductDatas = this.savingsProductReadPlatformService.retrieveAllForLookupByType(null);

        final GlobalConfigurationPropertyData configuration=this.configurationReadPlatformService.retrieveGlobalConfiguration("Enable-Address");

        final Boolean isAddressEnabled=configuration.isEnabled();
        if(isAddressEnabled)
        {
        	 address = this.addressReadPlatformService.retrieveTemplate();
        }

        final ClientFamilyMembersData familyMemberOptions=this.clientFamilyMembersReadPlatformService.retrieveTemplate();

        Collection<StaffData> staffOptions = null;

        final boolean loanOfficersOnly = false;
        if (staffInSelectedOfficeOnly) {
            staffOptions = this.staffReadPlatformService.retrieveAllStaffForDropdown(defaultOfficeId);
        } else {
            staffOptions = this.staffReadPlatformService.retrieveAllStaffInOfficeAndItsParentOfficeHierarchy(defaultOfficeId,
                    loanOfficersOnly);
        }
        if (CollectionUtils.isEmpty(staffOptions)) {
            staffOptions = null;
        }
        final List<CodeValueData> genderOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode(ClientApiConstants.GENDER));

        final List<CodeValueData> clientTypeOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode(ClientApiConstants.CLIENT_TYPE));

        final List<CodeValueData> clientClassificationOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode(ClientApiConstants.CLIENT_CLASSIFICATION));

        final List<CodeValueData> clientNonPersonConstitutionOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode(ClientApiConstants.CLIENT_NON_PERSON_CONSTITUTION));

        final List<CodeValueData> clientNonPersonMainBusinessLineOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode(ClientApiConstants.CLIENT_NON_PERSON_MAIN_BUSINESS_LINE));

        final List<EnumOptionData> clientLegalFormOptions = ClientEnumerations.legalForm(LegalForm.values());
        
        final List<CodeValueData> clientEducationalAttainmentOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("educationalAttainment"));
        
        final List<CodeValueData> clientResidenceOwnershipOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("residenceOwnership"));
        
        final List<CodeValueData> clientEmploymentOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("employment"));
        
        final List<CodeValueData> clientSelfEmployedOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("selfEmployed"));
        
        final List<CodeValueData> clientRankOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("rank"));
        
        final List<CodeValueData> clientRelationshipOfOfficerOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("relationshipOfOfficer"));

        final List<CodeValueData> clientMaritalStatusOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("MARITAL STATUS"));

        final List<CodeValueData> clientRelatedToOfficerOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("YesNo"));

        final List<DatatableData> datatableTemplates = this.entityDatatableChecksReadService
                .retrieveTemplates(StatusEnum.CREATE.getCode().longValue(), EntityTables.CLIENT.getName(), null);

        return CS_ClientData.template(defaultOfficeId, new LocalDate(), offices, staffOptions, null, genderOptions, savingsProductDatas,
                clientTypeOptions, clientClassificationOptions, clientNonPersonConstitutionOptions, clientNonPersonMainBusinessLineOptions,
				clientLegalFormOptions, familyMemberOptions, new ArrayList<AddressData>(Arrays.asList(address)),
				isAddressEnabled, datatableTemplates, clientEducationalAttainmentOptions, clientResidenceOwnershipOptions, clientEmploymentOptions,
				clientSelfEmployedOptions, clientRankOptions, clientRelationshipOfOfficerOptions, clientMaritalStatusOptions, 
				clientRelatedToOfficerOptions);
	}

    @Override
    public CS_ClientData retrieveOne(final Long clientId) {
        try {
            final String hierarchy = this.context.officeHierarchy();
            final String hierarchySearchString = hierarchy + "%";

            final String sql = "select " + this.clientMapper.schema()
                    + " where ( o.hierarchy like ? or transferToOffice.hierarchy like ?) and c.id = ?";
            CS_ClientData clientData = this.jdbcTemplate.queryForObject(sql, this.clientMapper, new Object[] { hierarchySearchString,
                    hierarchySearchString, clientId });

            final String clientGroupsSql = "select " + this.clientGroupsMapper.parentGroupsSchema();

            final Collection<GroupGeneralData> parentGroups = this.jdbcTemplate.query(clientGroupsSql, this.clientGroupsMapper,
                    new Object[] { clientId });

            final CS_KycInfoData kycInfo = this.kycReadPlatformService.getKycInfoData(clientId, null);

            clientData = CS_ClientData.setParentGroups(clientData, parentGroups);

            clientData.setKycInfo(kycInfo);

            return clientData;

        } catch (final EmptyResultDataAccessException e) {
            throw new ClientNotFoundException(clientId);
        }
    }

    private static final class ClientMapper implements RowMapper<CS_ClientData> {

        private final String schema;

        public ClientMapper() {
            final StringBuilder builder = new StringBuilder(400);

            builder.append("c.id as id, c.account_no as accountNo, c.external_id as externalId, c.status_enum as statusEnum,c.sub_status as subStatus, ");
            builder.append("cvSubStatus.code_value as subStatusValue,cvSubStatus.code_description as subStatusDesc,c.office_id as officeId, o.name as officeName, ");
            builder.append("c.transfer_to_office_id as transferToOfficeId, transferToOffice.name as transferToOfficeName, ");
            builder.append("c.firstname as firstname, c.middlename as middlename, c.lastname as lastname, ");
            builder.append("c.fullname as fullname, c.display_name as displayName, ");
            builder.append("c.mobile_no as mobileNo, ");
			builder.append("c.is_staff as isStaff, ");
			builder.append("c.email_address as emailAddress, ");
            builder.append("c.date_of_birth as dateOfBirth, ");
            builder.append("c.gender_cv_id as genderId, ");
            builder.append("cv.code_value as genderValue, ");
            builder.append("c.client_type_cv_id as clienttypeId, ");
            builder.append("cvclienttype.code_value as clienttypeValue, ");
            builder.append("c.client_classification_cv_id as classificationId, ");
            builder.append("cvclassification.code_value as classificationValue, ");
            builder.append("c.legal_form_enum as legalFormEnum, ");

            builder.append("c.submittedon_date as submittedOnDate, ");
            builder.append("sbu.username as submittedByUsername, ");
            builder.append("sbu.firstname as submittedByFirstname, ");
            builder.append("sbu.lastname as submittedByLastname, ");

            builder.append("c.closedon_date as closedOnDate, ");
            builder.append("clu.username as closedByUsername, ");
            builder.append("clu.firstname as closedByFirstname, ");
            builder.append("clu.lastname as closedByLastname, ");

            // builder.append("c.submittedon as submittedOnDate, ");
            builder.append("acu.username as activatedByUsername, ");
            builder.append("acu.firstname as activatedByFirstname, ");
            builder.append("acu.lastname as activatedByLastname, ");

            builder.append("cnp.constitution_cv_id as constitutionId, ");
            builder.append("cvConstitution.code_value as constitutionValue, ");
            builder.append("cnp.incorp_no as incorpNo, ");
            builder.append("cnp.incorp_validity_till as incorpValidityTill, ");
            builder.append("cnp.main_business_line_cv_id as mainBusinessLineId, ");
            builder.append("cvMainBusinessLine.code_value as mainBusinessLineValue, ");
            builder.append("cnp.remarks as remarks, ");

            builder.append("c.activation_date as activationDate, c.image_id as imageId, ");
            builder.append("c.staff_id as staffId, s.display_name as staffName, ");
            builder.append("c.default_savings_product as savingsProductId, sp.name as savingsProductName, ");
            builder.append("c.default_savings_account as savingsAccountId ");
            builder.append("from m_client c ");
            builder.append("join m_office o on o.id = c.office_id ");
            builder.append("left join m_client_non_person cnp on cnp.client_id = c.id ");
            builder.append("left join m_staff s on s.id = c.staff_id ");
            builder.append("left join m_savings_product sp on sp.id = c.default_savings_product ");
            builder.append("left join m_office transferToOffice on transferToOffice.id = c.transfer_to_office_id ");
            builder.append("left join m_appuser sbu on sbu.id = c.submittedon_userid ");
            builder.append("left join m_appuser acu on acu.id = c.activatedon_userid ");
            builder.append("left join m_appuser clu on clu.id = c.closedon_userid ");
            builder.append("left join m_code_value cv on cv.id = c.gender_cv_id ");
            builder.append("left join m_code_value cvclienttype on cvclienttype.id = c.client_type_cv_id ");
            builder.append("left join m_code_value cvclassification on cvclassification.id = c.client_classification_cv_id ");
            builder.append("left join m_code_value cvSubStatus on cvSubStatus.id = c.sub_status ");
            builder.append("left join m_code_value cvConstitution on cvConstitution.id = cnp.constitution_cv_id ");
            builder.append("left join m_code_value cvMainBusinessLine on cvMainBusinessLine.id = cnp.main_business_line_cv_id ");

            this.schema = builder.toString();
        }

        public String schema() {
            return this.schema;
        }

        @Override
        public CS_ClientData mapRow(final ResultSet rs, final int rowNum) throws SQLException {

            final String accountNo = rs.getString("accountNo");

            final Integer statusEnum = JdbcSupport.getInteger(rs, "statusEnum");
            final EnumOptionData status = ClientEnumerations.status(statusEnum);

            final Long subStatusId = JdbcSupport.getLong(rs, "subStatus");
            final String subStatusValue = rs.getString("subStatusValue");
            final String subStatusDesc = rs.getString("subStatusDesc");
            final boolean isActive = false;
            final CodeValueData subStatus = CodeValueData.instance(subStatusId, subStatusValue, subStatusDesc, isActive);

            final Long officeId = JdbcSupport.getLong(rs, "officeId");
            final String officeName = rs.getString("officeName");

            final Long transferToOfficeId = JdbcSupport.getLong(rs, "transferToOfficeId");
            final String transferToOfficeName = rs.getString("transferToOfficeName");

            final Long id = JdbcSupport.getLong(rs, "id");
            final String firstname = rs.getString("firstname");
            final String middlename = rs.getString("middlename");
            final String lastname = rs.getString("lastname");
            final String fullname = rs.getString("fullname");
            final String displayName = rs.getString("displayName");
            final String externalId = rs.getString("externalId");
            final String mobileNo = rs.getString("mobileNo");
			final boolean isStaff = rs.getBoolean("isStaff");
			final String emailAddress = rs.getString("emailAddress");
            final LocalDate dateOfBirth = JdbcSupport.getLocalDate(rs, "dateOfBirth");
            final Long genderId = JdbcSupport.getLong(rs, "genderId");
            final String genderValue = rs.getString("genderValue");
            final CodeValueData gender = CodeValueData.instance(genderId, genderValue);

            final Long clienttypeId = JdbcSupport.getLong(rs, "clienttypeId");
            final String clienttypeValue = rs.getString("clienttypeValue");
            final CodeValueData clienttype = CodeValueData.instance(clienttypeId, clienttypeValue);

            final Long classificationId = JdbcSupport.getLong(rs, "classificationId");
            final String classificationValue = rs.getString("classificationValue");
            final CodeValueData classification = CodeValueData.instance(classificationId, classificationValue);

            final LocalDate activationDate = JdbcSupport.getLocalDate(rs, "activationDate");
            final Long imageId = JdbcSupport.getLong(rs, "imageId");
            final Long staffId = JdbcSupport.getLong(rs, "staffId");
            final String staffName = rs.getString("staffName");

            final Long savingsProductId = JdbcSupport.getLong(rs, "savingsProductId");
            final String savingsProductName = rs.getString("savingsProductName");
            final Long savingsAccountId = JdbcSupport.getLong(rs, "savingsAccountId");

            final LocalDate closedOnDate = JdbcSupport.getLocalDate(rs, "closedOnDate");
            final String closedByUsername = rs.getString("closedByUsername");
            final String closedByFirstname = rs.getString("closedByFirstname");
            final String closedByLastname = rs.getString("closedByLastname");

            final LocalDate submittedOnDate = JdbcSupport.getLocalDate(rs, "submittedOnDate");
            final String submittedByUsername = rs.getString("submittedByUsername");
            final String submittedByFirstname = rs.getString("submittedByFirstname");
            final String submittedByLastname = rs.getString("submittedByLastname");

            final String activatedByUsername = rs.getString("activatedByUsername");
            final String activatedByFirstname = rs.getString("activatedByFirstname");
            final String activatedByLastname = rs.getString("activatedByLastname");

            final Integer legalFormEnum = JdbcSupport.getInteger(rs, "legalFormEnum");
            EnumOptionData legalForm = null;
            if(legalFormEnum != null)
            		legalForm = ClientEnumerations.legalForm(legalFormEnum);

            final Long constitutionId = JdbcSupport.getLong(rs, "constitutionId");
            final String constitutionValue = rs.getString("constitutionValue");
            final CodeValueData constitution = CodeValueData.instance(constitutionId, constitutionValue);
            final String incorpNo = rs.getString("incorpNo");
            final LocalDate incorpValidityTill = JdbcSupport.getLocalDate(rs, "incorpValidityTill");
            final Long mainBusinessLineId = JdbcSupport.getLong(rs, "mainBusinessLineId");
            final String mainBusinessLineValue = rs.getString("mainBusinessLineValue");
            final CodeValueData mainBusinessLine = CodeValueData.instance(mainBusinessLineId, mainBusinessLineValue);
            final String remarks = rs.getString("remarks");

            final ClientNonPersonData clientNonPerson = new ClientNonPersonData(constitution, incorpNo, incorpValidityTill, mainBusinessLine, remarks);

            final ClientTimelineData timeline = new ClientTimelineData(submittedOnDate, submittedByUsername, submittedByFirstname,
                    submittedByLastname, activationDate, activatedByUsername, activatedByFirstname, activatedByLastname, closedOnDate,
                    closedByUsername, closedByFirstname, closedByLastname);

            return CS_ClientData.instance(accountNo, status, subStatus, officeId, officeName, transferToOfficeId, transferToOfficeName, id,
                    firstname, middlename, lastname, fullname, displayName, externalId, mobileNo, emailAddress, dateOfBirth, gender, activationDate,
                    imageId, staffId, staffName, timeline, savingsProductId, savingsProductName, savingsAccountId, clienttype,
                    classification, legalForm, clientNonPerson, isStaff);

        }
    }

    private static final class ParentGroupsMapper implements RowMapper<GroupGeneralData> {

        public String parentGroupsSchema() {
            return "gp.id As groupId , gp.account_no as accountNo, gp.display_name As groupName from m_client cl JOIN m_group_client gc ON cl.id = gc.client_id "
                    + "JOIN m_group gp ON gp.id = gc.group_id WHERE cl.id  = ?";
        }

        @Override
        public GroupGeneralData mapRow(final ResultSet rs, final int rowNum) throws SQLException {

            final Long groupId = JdbcSupport.getLong(rs, "groupId");
            final String groupName = rs.getString("groupName");
            final String accountNo = rs.getString("accountNo");

            return GroupGeneralData.lookup(groupId, accountNo, groupName);
        }
    }

    private Long defaultToUsersOfficeIfNull(final Long officeId) {
        Long defaultOfficeId = officeId;
        if (defaultOfficeId == null) {
            defaultOfficeId = this.context.authenticatedUser().getOffice().getId();
        }
        return defaultOfficeId;
    }
}