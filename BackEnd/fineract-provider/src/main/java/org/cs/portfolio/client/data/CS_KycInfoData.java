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

import org.apache.fineract.infrastructure.codes.data.CodeValueData;

@SuppressWarnings("unused")
final public class CS_KycInfoData implements Comparable<CS_KycInfoData> {

    private Long id;
    private String nationality;
    private Integer noOfDependents;
    private CodeValueData maritalStatus;
    private CodeValueData educationalAttainment;
    private String othersEducationalAttainment;
    private String schoolLastAttended;
    private String placeOfBirth;
    private String motherMaidenName;
    private CodeValueData residenceOwnership;
    private Integer rentedResidenceOwnership;
    private CodeValueData employment;
    private String otherEmployment;
    private CodeValueData selfEmployed;
    private Integer yearsInOperation;
    private Integer noOfEmployees;
    private String nameOfPresentEmployerBusiness;
    private String natureOfBusiness;
    private String officeAddress;
    private String officePhone;
    private String officeMobilePhone;
    private String officeLocalNumber;
    private String officeFaxNumber;
    private String officeEmailAddress;
    private CodeValueData rank;
    private String titleOrPosition;
    private Integer grossAnnualIncome;
    private Integer otherIncome;
    private String nameOfPreviousEmployer;
    private String officeAddressPrevious;
    private Integer yearsWithPresentEmployer;
    private Integer yearsWithPreviousEmployer;
    private String nameReference;
    private String relationshipReference;
    private String employerReference;
    private String addressReference;
    private String contactNumberReference;
    private String mobileReference;
    private CodeValueData relatedToOfficer;
    private String nameOfOfficer;
    private String contactNumberOfficer;
    private CodeValueData relationshipOfOfficer;

    public static CS_KycInfoData instance(Long id, String nationality, Integer noOfDependents, CodeValueData maritalStatus,
            CodeValueData educationalAttainment, String othersEducationalAttainment, String schoolLastAttended, String placeOfBirth,
            String motherMaidenName, CodeValueData residenceOwnership, Integer rentedResidenceOwnership, CodeValueData employment,
            String otherEmployment, CodeValueData selfEmployed, Integer yearsInOperation, Integer noOfEmployees, String nameOfPresentEmployerBusiness,
            String natureOfBusiness, String officeAddress, String officePhone, String officeMobilePhone, String officeLocalNumber,
            String officeFaxNumber, String officeEmailAddress, CodeValueData rank, String titleOrPosition, Integer grossAnnualIncome,
            Integer otherIncome, String nameOfPreviousEmployer, String officeAddressPrevious, Integer yearsWithPresentEmployer,
            Integer yearsWithPreviousEmployer, String nameReference, String relationshipReference, String employerReference,
            String addressReference, String contactNumberReference, String mobileReference, CodeValueData relatedToOfficer,
            String nameOfOfficer, String contactNumberOfficer, CodeValueData relationshipOfOfficer){
        return new CS_KycInfoData(id, nationality, noOfDependents, maritalStatus, educationalAttainment, othersEducationalAttainment,
            schoolLastAttended, placeOfBirth, motherMaidenName, residenceOwnership, rentedResidenceOwnership, employment,
            otherEmployment, selfEmployed, yearsInOperation, noOfEmployees, nameOfPresentEmployerBusiness, natureOfBusiness,
            officeAddress, officePhone, officeMobilePhone, officeLocalNumber, officeFaxNumber, officeEmailAddress, rank,
            titleOrPosition, grossAnnualIncome, otherIncome, nameOfPreviousEmployer, officeAddressPrevious, yearsWithPresentEmployer,
            yearsWithPreviousEmployer, nameReference, relationshipReference, employerReference, addressReference,
            contactNumberReference, mobileReference, relatedToOfficer, nameOfOfficer, contactNumberOfficer, relationshipOfOfficer);
    }

    private CS_KycInfoData(Long id, String nationality, Integer noOfDependents, CodeValueData maritalStatus,
            CodeValueData educationalAttainment, String othersEducationalAttainment, String schoolLastAttended, String placeOfBirth,
            String motherMaidenName, CodeValueData residenceOwnership, Integer rentedResidenceOwnership, CodeValueData employment,
            String otherEmployment, CodeValueData selfEmployed, Integer yearsInOperation, Integer noOfEmployees, String nameOfPresentEmployerBusiness,
            String natureOfBusiness, String officeAddress, String officePhone, String officeMobilePhone, String officeLocalNumber,
            String officeFaxNumber, String officeEmailAddress, CodeValueData rank, String titleOrPosition, Integer grossAnnualIncome,
            Integer otherIncome, String nameOfPreviousEmployer, String officeAddressPrevious, Integer yearsWithPresentEmployer,
            Integer yearsWithPreviousEmployer, String nameReference, String relationshipReference, String employerReference,
            String addressReference, String contactNumberReference, String mobileReference, CodeValueData relatedToOfficer,
            String nameOfOfficer, String contactNumberOfficer, CodeValueData relationshipOfOfficer){
        
        this.id = id;
        this.nationality = nationality;
        this.noOfDependents = noOfDependents;
        this.maritalStatus = maritalStatus;
        this.educationalAttainment = educationalAttainment;
        this.othersEducationalAttainment = othersEducationalAttainment;
        this.schoolLastAttended = schoolLastAttended;
        this.placeOfBirth = placeOfBirth;
        this.motherMaidenName = motherMaidenName;
        this.residenceOwnership = residenceOwnership;
        this.rentedResidenceOwnership = rentedResidenceOwnership;
        this.employment = employment;
        this.otherEmployment = otherEmployment;
        this.selfEmployed = selfEmployed;
        this.yearsInOperation = yearsInOperation;
        this.noOfEmployees = noOfEmployees;
        this.nameOfPresentEmployerBusiness = nameOfPresentEmployerBusiness;
        this.natureOfBusiness = natureOfBusiness;
        this.officeAddress = officeAddress;
        this.officePhone = officePhone;
        this.officeMobilePhone = officeMobilePhone;
        this.officeLocalNumber = officeLocalNumber;
        this.officeFaxNumber = officeFaxNumber;
        this.officeEmailAddress = officeEmailAddress;
        this.rank = rank;
        this.titleOrPosition = titleOrPosition;
        this.grossAnnualIncome = grossAnnualIncome;
        this.otherIncome = otherIncome;
        this.nameOfPreviousEmployer = nameOfPreviousEmployer;
        this.officeAddressPrevious = officeAddressPrevious;
        this.yearsWithPresentEmployer = yearsWithPresentEmployer;
        this.yearsWithPreviousEmployer = yearsWithPreviousEmployer;
        this.nameReference = nameReference;
        this.relationshipReference = relationshipReference;
        this.employerReference = employerReference;
        this.addressReference = addressReference;
        this.contactNumberReference = contactNumberReference;
        this.mobileReference = mobileReference;
        this.relatedToOfficer = relatedToOfficer;
        this.nameOfOfficer = nameOfOfficer;
        this.contactNumberOfficer = contactNumberOfficer;
        this.relationshipOfOfficer = relationshipOfOfficer;                                                    
    }

    @Override
    public int compareTo(final CS_KycInfoData obj) {
        return 0;
    }

    @Override
    public boolean equals(final Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}