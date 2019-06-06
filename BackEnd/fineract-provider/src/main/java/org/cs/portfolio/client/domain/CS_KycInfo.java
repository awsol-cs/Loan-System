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
package org.cs.portfolio.client.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.client.domain.Client;

@Entity
@Table(name = "m_kyc_info")
public final class CS_KycInfo extends AbstractPersistableCustom<Long> {

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    @Column(name = "nationality", length = 255, nullable = true)
    private String nationality;

    @Column(name = "no_of_dependents", nullable = true)
    private Integer noOfDependents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marital_status", nullable = true)
    private CodeValue maritalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "educational_attainment", nullable = true)
    private CodeValue educationalAttainment;

    @Column(name = "others_educational_attainment", length = 255, nullable = true)
    private String othersEducationalAttainment;

    @Column(name = "school_last_attended", length = 255, nullable = true)
    private String schoolLastAttended;

    @Column(name = "place_of_birth", length = 255, nullable = true)
    private String placeOfBirth;

    @Column(name = "mother_maiden_name", length = 255, nullable = true)
    private String motherMaidenName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_ownership", nullable = true)
    private CodeValue residenceOwnership;

    @Column(name = "rented_residence_ownership", nullable = true)
    private Integer rentedResidenceOwnership;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment", nullable = true)
    private CodeValue employment;

    @Column(name = "other_employment", length = 255, nullable = true)
    private String otherEmployment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "self_employed", nullable = true)
    private CodeValue selfEmployed;

    @Column(name = "years_in_operation", nullable = true)
    private Integer yearsInOperation;

    @Column(name = "no_of_employees", nullable = true)
    private Integer noOfEmployees;

    @Column(name = "name_of_present_employer_business", length = 255, nullable = true)
    private String nameOfPresentEmployerBusiness;

    @Column(name = "nature_of_business", length = 255, nullable = true)
    private String natureOfBusiness;

    @Column(name = "office_address", length = 255, nullable = true)
    private String officeAddress;

    @Column(name = "office_phone", length = 20, nullable = true)
    private String officePhone;

    @Column(name = "office_mobile_phone", length = 20, nullable = true)
    private String officeMobilePhone;

    @Column(name = "office_local_number", length = 20, nullable = true)
    private String officeLocalNumber;

    @Column(name = "office_fax_number", length = 20, nullable = true)
    private String officeFaxNumber;

    @Column(name = "office_email_address", length = 255, nullable = true)
    private String officeEmailAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank", nullable = true)
    private CodeValue rank;

    @Column(name = "title_or_position", length = 255, nullable = true)
    private String titleOrPosition;

    @Column(name = "gross_annual_income", nullable = true)
    private Integer grossAnnualIncome;

    @Column(name = "other_income", nullable = true)
    private Integer otherIncome;

    @Column(name = "name_of_previous_employer", length = 255, nullable = true)
    private String nameOfPreviousEmployer;

    @Column(name = "office_address_previous", length = 255, nullable = true)
    private String officeAddressPrevious;

    @Column(name = "years_with_present_employer", nullable = true)
    private Integer yearsWithPresentEmployer;

    @Column(name = "years_with_previous_employer", nullable = true)
    private Integer yearsWithPreviousEmployer;

    @Column(name = "name_reference", length = 255, nullable = true)
    private String nameReference;

    @Column(name = "relationship_reference", length = 255, nullable = true)
    private String relationshipReference;

    @Column(name = "employer_reference", length = 255, nullable = true)
    private String employerReference;

    @Column(name = "address_reference", length = 255, nullable = true)
    private String addressReference;

    @Column(name = "contact_number_reference", length = 20, nullable = true)
    private String contactNumberReference;

    @Column(name = "mobile_reference", length = 20, nullable = true)
    private String mobileReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_to_officer", nullable = true)
    private CodeValue relatedToOfficer;

    @Column(name = "name_of_officer", length = 255, nullable = true)
    private String nameOfOfficer;

    @Column(name = "contact_number_officer", length = 20, nullable = true)
    private String contactNumberOfficer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationship_of_officer", nullable = true)
    private CodeValue relationshipOfOfficer;

    public static CS_KycInfo createNew(Client client, CodeValue maritalStatus, CodeValue educationalAttainment, 
        CodeValue residenceOwnership, CodeValue employment, CodeValue selfEmployed, CodeValue rank, 
        CodeValue relatedToOfficer, CodeValue relationshipOfOfficer, JsonCommand command) {

        String nationality = command.stringValueOfParameterNamed("nationality");
        Integer noOfDependents = command.integerValueOfParameterNamed("noOfDependents");
        String othersEducationalAttainment = command.stringValueOfParameterNamed("othersEducationalAttainment");
        String schoolLastAttended = command.stringValueOfParameterNamed("schoolLastAttended");
        String placeOfBirth = command.stringValueOfParameterNamed("placeOfBirth");
        String motherMaidenName = command.stringValueOfParameterNamed("motherMaidenName");
        Integer rentedResidenceOwnership = command.integerValueOfParameterNamed("rentedResidenceOwnership");
        String otherEmployment = command.stringValueOfParameterNamed("otherEmployment");
        Integer yearsInOperation = command.integerValueOfParameterNamed("yearsInOperation");
        Integer noOfEmployees = command.integerValueOfParameterNamed("noOfEmployees");
        String nameOfPresentEmployerBusiness = command.stringValueOfParameterNamed("nameOfPresentEmployerBusiness");
        String natureOfBusiness = command.stringValueOfParameterNamed("natureOfBusiness");
        String officeAddress = command.stringValueOfParameterNamed("officeAddress");
        String officePhone  = command.stringValueOfParameterNamed("officePhone");
        String officeMobilePhone = command.stringValueOfParameterNamed("officeMobilePhone");
        String officeLocalNumber = command.stringValueOfParameterNamed("officeLocalNumber");
        String officeFaxNumber = command.stringValueOfParameterNamed("officeFaxNumber");
        String officeEmailAddress = command.stringValueOfParameterNamed("officeEmailAddress");
        String titleOrPosition = command.stringValueOfParameterNamed("titleOrPosition");
        Integer grossAnnualIncome = command.integerValueOfParameterNamed("grossAnnualIncome");
        Integer otherIncome = command.integerValueOfParameterNamed("otherIncome");
        String nameOfPreviousEmployer = command.stringValueOfParameterNamed("nameOfPreviousEmployer");
        String officeAddressPrevious = command.stringValueOfParameterNamed("officeAddressPrevious");
        Integer yearsWithPresentEmployer = command.integerValueOfParameterNamed("yearsWithPresentEmployer");
        Integer yearsWithPreviousEmployer = command.integerValueOfParameterNamed("yearsWithPreviousEmployer");
        String nameReference = command.stringValueOfParameterNamed("nameReference");
        String relationshipReference = command.stringValueOfParameterNamed("relationshipReference");
        String employerReference = command.stringValueOfParameterNamed("employerReference");
        String addressReference = command.stringValueOfParameterNamed("addressReference");
        String contactNumberReference  = command.stringValueOfParameterNamed("contactNumberReference");
        String mobileReference = command.stringValueOfParameterNamed("mobileReference");
        String nameOfOfficer = command.stringValueOfParameterNamed("nameOfOfficer");
        String contactNumberOfficer = command.stringValueOfParameterNamed("contactNumberOfficer");

        return new CS_KycInfo(client, nationality, noOfDependents, 
        maritalStatus, educationalAttainment, othersEducationalAttainment, schoolLastAttended,
        placeOfBirth, motherMaidenName, residenceOwnership, rentedResidenceOwnership,
        employment, otherEmployment, selfEmployed, yearsInOperation, noOfEmployees,
        nameOfPresentEmployerBusiness, natureOfBusiness, officeAddress, officePhone, 
        officeMobilePhone, officeLocalNumber, officeFaxNumber, officeEmailAddress,
        rank, titleOrPosition, grossAnnualIncome, otherIncome, nameOfPreviousEmployer,
        officeAddressPrevious, yearsWithPresentEmployer, yearsWithPreviousEmployer, nameReference,
        relationshipReference, employerReference, addressReference, contactNumberReference, 
        mobileReference, relatedToOfficer, nameOfOfficer, contactNumberOfficer, 
        relationshipOfOfficer);
    }

    private CS_KycInfo(Client client, String nationality, Integer noOfDependents, 
        CodeValue maritalStatus, CodeValue educationalAttainment, String othersEducationalAttainment, String schoolLastAttended,
        String placeOfBirth, String motherMaidenName, CodeValue residenceOwnership, Integer rentedResidenceOwnership,
        CodeValue employment, String otherEmployment, CodeValue selfEmployed, Integer yearsInOperation, Integer noOfEmployees,
        String nameOfPresentEmployerBusiness, String natureOfBusiness, String officeAddress, String officePhone, 
        String officeMobilePhone, String officeLocalNumber, String officeFaxNumber, String officeEmailAddress,
        CodeValue rank, String titleOrPosition, Integer grossAnnualIncome, Integer otherIncome, String nameOfPreviousEmployer,
        String officeAddressPrevious, Integer yearsWithPresentEmployer, Integer yearsWithPreviousEmployer, String nameReference,
        String relationshipReference, String employerReference, String addressReference, String contactNumberReference, 
        String mobileReference, CodeValue relatedToOfficer, String nameOfOfficer, String contactNumberOfficer, 
        CodeValue relationshipOfOfficer) {

        this.client = client;
        this.nationality = checkIfBlank(nationality);
        this.noOfDependents = noOfDependents;
        this.maritalStatus = maritalStatus;
        this.educationalAttainment = educationalAttainment;
        this.othersEducationalAttainment = checkIfBlank(othersEducationalAttainment);
        this.schoolLastAttended = checkIfBlank(schoolLastAttended);
        this.placeOfBirth = checkIfBlank(placeOfBirth);
        this.motherMaidenName = checkIfBlank(motherMaidenName);
        this.residenceOwnership = residenceOwnership;
        this.rentedResidenceOwnership = rentedResidenceOwnership;
        this.employment = employment;
        this.otherEmployment = checkIfBlank(otherEmployment);
        this.selfEmployed = selfEmployed;
        this.yearsInOperation = yearsInOperation;
        this.noOfEmployees = noOfEmployees;
        this.nameOfPresentEmployerBusiness = checkIfBlank(nameOfPresentEmployerBusiness);
        this.natureOfBusiness = checkIfBlank(natureOfBusiness);
        this.officeAddress = checkIfBlank(officeAddress);
        this.officePhone  = checkIfBlank(officePhone);
        this.officeMobilePhone = checkIfBlank(officeMobilePhone);
        this.officeLocalNumber = checkIfBlank(officeLocalNumber);
        this.officeFaxNumber = checkIfBlank(officeFaxNumber);
        this.officeEmailAddress = checkIfBlank(officeEmailAddress);
        this.rank = rank;
        this.titleOrPosition = checkIfBlank(titleOrPosition);
        this.grossAnnualIncome = grossAnnualIncome;
        this.otherIncome = otherIncome;
        this.nameOfPreviousEmployer = checkIfBlank(nameOfPreviousEmployer);
        this.officeAddressPrevious = checkIfBlank(officeAddressPrevious);
        this.yearsWithPresentEmployer = yearsWithPresentEmployer;
        this.yearsWithPreviousEmployer = yearsWithPreviousEmployer;
        this.nameReference = checkIfBlank(nameReference);
        this.relationshipReference = checkIfBlank(relationshipReference);
        this.employerReference = checkIfBlank(employerReference);
        this.addressReference = checkIfBlank(addressReference);
        this.contactNumberReference  = checkIfBlank(contactNumberReference);
        this.mobileReference = checkIfBlank(mobileReference);
        this.relatedToOfficer = relatedToOfficer;
        this.nameOfOfficer = checkIfBlank(nameOfOfficer);
        this.contactNumberOfficer  = checkIfBlank(contactNumberOfficer);
        this.relationshipOfOfficer = relationshipOfOfficer;


    }

    private String checkIfBlank(String input){
        if(StringUtils.isNotBlank(input)){
            return input.trim();
        }else {
            return null;
        }
    }
    
    public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(9);

        if (command.isChangeInStringParameterNamed("nationality", this.nationality)) {
            final String newValue = command.stringValueOfParameterNamed("nationality");
            actualChanges.put("nationality", newValue);
            this.nationality = newValue;
        }
        if (command.isChangeInStringParameterNamed("othersEducationalAttainment", this.othersEducationalAttainment)) {
            final String newValue = command.stringValueOfParameterNamed("othersEducationalAttainment");
            actualChanges.put("othersEducationalAttainment", newValue);
            this.othersEducationalAttainment = newValue;
        }
        if (command.isChangeInStringParameterNamed("schoolLastAttended", this.schoolLastAttended)) {
            final String newValue = command.stringValueOfParameterNamed("schoolLastAttended");
            actualChanges.put("schoolLastAttended", newValue);
            this.schoolLastAttended = newValue;
        }
        if (command.isChangeInStringParameterNamed("placeOfBirth", this.placeOfBirth)) {
            final String newValue = command.stringValueOfParameterNamed("placeOfBirth");
            actualChanges.put("placeOfBirth", newValue);
            this.placeOfBirth = newValue;
        }
        if (command.isChangeInStringParameterNamed("motherMaidenName", this.motherMaidenName)) {
            final String newValue = command.stringValueOfParameterNamed("motherMaidenName");
            actualChanges.put("motherMaidenName", newValue);
            this.motherMaidenName = newValue;
        }
        if (command.isChangeInStringParameterNamed("otherEmployment", this.otherEmployment)) {
            final String newValue = command.stringValueOfParameterNamed("otherEmployment");
            actualChanges.put("otherEmployment", newValue);
            this.otherEmployment = newValue;
        }
        if (command.isChangeInStringParameterNamed("nameOfPresentEmployerBusiness", this.nameOfPresentEmployerBusiness)) {
            final String newValue = command.stringValueOfParameterNamed("nameOfPresentEmployerBusiness");
            actualChanges.put("nameOfPresentEmployerBusiness", newValue);
            this.nameOfPresentEmployerBusiness = newValue;
        }
        if (command.isChangeInStringParameterNamed("natureOfBusiness", this.natureOfBusiness)) {
            final String newValue = command.stringValueOfParameterNamed("natureOfBusiness");
            actualChanges.put("natureOfBusiness", newValue);
            this.natureOfBusiness = newValue;
        }
        if (command.isChangeInStringParameterNamed("officeAddress", this.officeAddress)) {
            final String newValue = command.stringValueOfParameterNamed("officeAddress");
            actualChanges.put("officeAddress", newValue);
            this.officeAddress = newValue;
        }
        if (command.isChangeInStringParameterNamed("officePhone", this.officePhone)) {
            final String newValue = command.stringValueOfParameterNamed("officePhone");
            actualChanges.put("officePhone", newValue);
            this.officePhone = newValue;
        }
        if (command.isChangeInStringParameterNamed("officeMobilePhone", this.officeMobilePhone)) {
            final String newValue = command.stringValueOfParameterNamed("officeMobilePhone");
            actualChanges.put("officeMobilePhone", newValue);
            this.officeMobilePhone = newValue;
        }
        if (command.isChangeInStringParameterNamed("officeLocalNumber", this.officeLocalNumber)) {
            final String newValue = command.stringValueOfParameterNamed("officeLocalNumber");
            actualChanges.put("officeLocalNumber", newValue);
            this.officeLocalNumber = newValue;
        }
        if (command.isChangeInStringParameterNamed("officeFaxNumber", this.officeFaxNumber)) {
            final String newValue = command.stringValueOfParameterNamed("officeFaxNumber");
            actualChanges.put("officeFaxNumber", newValue);
            this.officeFaxNumber = newValue;
        }
        if (command.isChangeInStringParameterNamed("officeEmailAddress", this.officeEmailAddress)) {
            final String newValue = command.stringValueOfParameterNamed("officeEmailAddress");
            actualChanges.put("officeEmailAddress", newValue);
            this.officeEmailAddress = newValue;
        }
        if (command.isChangeInStringParameterNamed("titleOrPosition", this.titleOrPosition)) {
            final String newValue = command.stringValueOfParameterNamed("titleOrPosition");
            actualChanges.put("titleOrPosition", newValue);
            this.titleOrPosition = newValue;
        }
        if (command.isChangeInStringParameterNamed("nameOfPreviousEmployer", this.nameOfPreviousEmployer)) {
            final String newValue = command.stringValueOfParameterNamed("nameOfPreviousEmployer");
            actualChanges.put("nameOfPreviousEmployer", newValue);
            this.nameOfPreviousEmployer = newValue;
        }
        if (command.isChangeInStringParameterNamed("officeAddressPrevious", this.officeAddressPrevious)) {
            final String newValue = command.stringValueOfParameterNamed("officeAddressPrevious");
            actualChanges.put("officeAddressPrevious", newValue);
            this.officeAddressPrevious = newValue;
        }
        if (command.isChangeInStringParameterNamed("nameReference", this.nameReference)) {
            final String newValue = command.stringValueOfParameterNamed("nameReference");
            actualChanges.put("nameReference", newValue);
            this.nameReference = newValue;
        }
        if (command.isChangeInStringParameterNamed("relationshipReference", this.relationshipReference)) {
            final String newValue = command.stringValueOfParameterNamed("relationshipReference");
            actualChanges.put("relationshipReference", newValue);
            this.relationshipReference = newValue;
        }
        if (command.isChangeInStringParameterNamed("addressReference", this.addressReference)) {
            final String newValue = command.stringValueOfParameterNamed("addressReference");
            actualChanges.put("addressReference", newValue);
            this.addressReference = newValue;
        }
        if (command.isChangeInStringParameterNamed("contactNumberReference", this.contactNumberReference)) {
            final String newValue = command.stringValueOfParameterNamed("contactNumberReference");
            actualChanges.put("contactNumberReference", newValue);
            this.contactNumberReference = newValue;
        }
        if (command.isChangeInStringParameterNamed("mobileReference", this.mobileReference)) {
            final String newValue = command.stringValueOfParameterNamed("mobileReference");
            actualChanges.put("mobileReference", newValue);
            this.mobileReference = newValue;
        }
        if (command.isChangeInStringParameterNamed("nameOfOfficer", this.nameOfOfficer)) {
            final String newValue = command.stringValueOfParameterNamed("nameOfOfficer");
            actualChanges.put("nameOfOfficer", newValue);
            this.nameOfOfficer = newValue;
        }
        if (command.isChangeInStringParameterNamed("contactNumberOfficer", this.contactNumberOfficer)) {
            final String newValue = command.stringValueOfParameterNamed("contactNumberOfficer");
            actualChanges.put("contactNumberOfficer", newValue);
            this.contactNumberOfficer = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("noOfDependents", this.noOfDependents)) {
            final Integer newValue = command.integerValueOfParameterNamed("noOfDependents");
            actualChanges.put("noOfDependents", newValue);
            this.noOfDependents = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("rentedResidenceOwnership", this.rentedResidenceOwnership)) {
            final Integer newValue = command.integerValueOfParameterNamed("rentedResidenceOwnership");
            actualChanges.put("rentedResidenceOwnership", newValue);
            this.rentedResidenceOwnership = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("yearsInOperation", this.yearsInOperation)) {
            final Integer newValue = command.integerValueOfParameterNamed("yearsInOperation");
            actualChanges.put("yearsInOperation", newValue);
            this.yearsInOperation = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("noOfEmployees", this.noOfEmployees)) {
            final Integer newValue = command.integerValueOfParameterNamed("noOfEmployees");
            actualChanges.put("noOfEmployees", newValue);
            this.noOfEmployees = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("grossAnnualIncome", this.grossAnnualIncome)) {
            final Integer newValue = command.integerValueOfParameterNamed("grossAnnualIncome");
            actualChanges.put("grossAnnualIncome", newValue);
            this.grossAnnualIncome = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("otherIncome", this.otherIncome)) {
            final Integer newValue = command.integerValueOfParameterNamed("otherIncome");
            actualChanges.put("otherIncome", newValue);
            this.otherIncome = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("yearsWithPresentEmployer", this.yearsWithPresentEmployer)) {
            final Integer newValue = command.integerValueOfParameterNamed("yearsWithPresentEmployer");
            actualChanges.put("yearsWithPresentEmployer", newValue);
            this.yearsWithPresentEmployer = newValue;
        }
        if (command.isChangeInIntegerParameterNamed("yearsWithPreviousEmployer", this.yearsWithPreviousEmployer)) {
            final Integer newValue = command.integerValueOfParameterNamed("yearsWithPreviousEmployer");
            actualChanges.put("yearsWithPreviousEmployer", newValue);
        }
        if (command.isChangeInLongParameterNamed("maritalStatus", maritalStatusId())) {
            final Long newValue = command.longValueOfParameterNamed("maritalStatus");
            actualChanges.put("maritalStatus", newValue);
        }
        if (command.isChangeInLongParameterNamed("educationalAttainment", educationalAttainmentId())) {
            final Long newValue = command.longValueOfParameterNamed("educationalAttainment");
            actualChanges.put("educationalAttainment", newValue);
        }
        if (command.isChangeInLongParameterNamed("residenceOwnership", residenceOwnershipId())) {
            final Long newValue = command.longValueOfParameterNamed("residenceOwnership");
            actualChanges.put("residenceOwnership", newValue);
        }
        if (command.isChangeInLongParameterNamed("employment", employmentId())) {
            final Long newValue = command.longValueOfParameterNamed("employment");
            actualChanges.put("employment", newValue);
        }
        if (command.isChangeInLongParameterNamed("selfEmployed", selfEmployedId())) {
            final Long newValue = command.longValueOfParameterNamed("selfEmployed");
            actualChanges.put("selfEmployed", newValue);
        }
        if (command.isChangeInLongParameterNamed("rank", rankId())) {
            final Long newValue = command.longValueOfParameterNamed("rank");
            actualChanges.put("rank", newValue);
        }
        if (command.isChangeInLongParameterNamed("relatedToOfficer", relatedToOfficerId())) {
            final Long newValue = command.longValueOfParameterNamed("relatedToOfficer");
            actualChanges.put("relatedToOfficer", newValue);
        }
        if (command.isChangeInLongParameterNamed("relationshipOfOfficer", relationshipOfOfficerId())) {
            final Long newValue = command.longValueOfParameterNamed("relationshipOfOfficer");
            actualChanges.put("relationshipOfOfficer", newValue);
        }

        return actualChanges;
    }

    private Long maritalStatusId(){
        Long maritalStatusId = null;
        if (this.maritalStatus != null) {
            maritalStatusId = this.maritalStatus.getId();
        }
        return maritalStatusId;
    }

    private Long educationalAttainmentId(){
        Long educationalAttainmentId = null;
        if (this.educationalAttainment != null) {
            educationalAttainmentId = this.educationalAttainment.getId();
        }
        return educationalAttainmentId;
    }

    private Long residenceOwnershipId(){
        Long residenceOwnershipId = null;
        if (this.residenceOwnership != null) {
            residenceOwnershipId = this.residenceOwnership.getId();
        }
        return residenceOwnershipId;
    }

    private Long employmentId(){
        Long employmentId = null;
        if (this.employment != null) {
            employmentId = this.employment.getId();
        }
        return employmentId;
    }

    private Long selfEmployedId(){
        Long selfEmployedId = null;
        if (this.selfEmployed != null) {
            selfEmployedId = this.selfEmployed.getId();
        }
        return selfEmployedId;
    }

    private Long rankId(){
        Long rankId = null;
        if (this.rank != null) {
            rankId = this.rank.getId();
        }
        return rankId;
    }

    private Long relatedToOfficerId(){
        Long relatedToOfficerId = null;
        if (this.relatedToOfficer != null) {
            relatedToOfficerId = this.relatedToOfficer.getId();
        }
        return relatedToOfficerId;
    }

    private Long relationshipOfOfficerId(){
        Long relationshipOfOfficerId = null;
        if (this.relationshipOfOfficer != null) {
            relationshipOfOfficerId = this.relationshipOfOfficer.getId();
        }
        return relationshipOfOfficerId;
    }

    public void updateMaritalStatus(CodeValue maritalStatus){
        this.maritalStatus = maritalStatus;
    }

    public void updateEducationalAttainment(CodeValue educationalAttainment){
        this.educationalAttainment = educationalAttainment;
    }

    public void updateResidenceOwnership(CodeValue residenceOwnership){
        this.residenceOwnership = residenceOwnership;
    }

    public void updateEmployment(CodeValue employment){
        this.employment = employment;
    }

    public void updateSelfEmployed(CodeValue selfEmployed){
        this.selfEmployed = selfEmployed;
    }

    public void updateRank(CodeValue rank){
        this.rank = rank;
    }

    public void updateRelatedToOfficer(CodeValue relatedToOfficer){
        this.relatedToOfficer = relatedToOfficer;
    }

    public void updateRelationshipOfOfficer(CodeValue relationshipOfOfficer){
        this.relationshipOfOfficer = relationshipOfOfficer;
    }
}