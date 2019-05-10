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
package org.cs.portfolio.loanaccount.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.service.DateUtils;
import org.apache.fineract.infrastructure.documentmanagement.domain.Image;
import org.apache.fineract.infrastructure.security.service.RandomPasswordGenerator;
import org.apache.fineract.useradministration.domain.AppUser;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.cs.portfolio.client.domain.CS_KycInfo;

@Entity
@Table(name = "m_co_maker")
public final class CS_CoMaker extends AbstractPersistableCustom<Long> {

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = true)
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "kyc_id", nullable = true)
    private CS_KycInfo kycInfo;

    @Column(name = "firstname", length = 50, nullable = true)
    private String firstname;

    @Column(name = "middlename", length = 50, nullable = true)
    private String middlename;

    @Column(name = "lastname", length = 50, nullable = true)
    private String lastname;

    @Column(name = "fullname", length = 100, nullable = true)
    private String fullname;

    @Column(name = "mobile_no", length = 50, nullable = true, unique = true)
    private String mobileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender", nullable = true)
    private CodeValue gender;

    @Column(name = "date_of_birth", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "email_address", length = 50, unique = true)
    private String emailAddress;

    public static CS_CoMaker createNew(Loan loan, CS_KycInfo kycInfo, CodeValue gender, JsonCommand command) {

        final String mobileNo = command.stringValueOfParameterNamed("mobileNo");
        final String emailAddress = command.stringValueOfParameterNamed("emailAddress");

        final String firstname = command.stringValueOfParameterNamed("firstname");
        final String middlename = command.stringValueOfParameterNamed("middlename");
        final String lastname = command.stringValueOfParameterNamed("lastname");
        final String fullname = command.stringValueOfParameterNamed("fullname");

        final LocalDate dateOfBirth = command.localDateValueOfParameterNamed("dateOfBirth");

        return new CS_CoMaker(loan, kycInfo, firstname, middlename, lastname, fullname, mobileNo, gender, 
            dateOfBirth, emailAddress);
    }

    private CS_CoMaker(Loan loan, CS_KycInfo kycInfo, String firstname, String middlename, String lastname, 
        String fullname, String mobileNo, CodeValue gender, LocalDate dateOfBirth, String emailAddress) {

        this.loan = loan;
        this.kycInfo = kycInfo;
        this.firstname = checkIfBlank(firstname);
        this.middlename = checkIfBlank(middlename);
        this.lastname = checkIfBlank(lastname);
        this.fullname = checkIfBlank(fullname);
        this.mobileNo = checkIfBlank(mobileNo);
        this.gender = gender;
        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth.toDateTimeAtStartOfDay().toDate();
        }
        this.emailAddress = checkIfBlank(emailAddress);
    }

    private String checkIfBlank(String input){
        if(StringUtils.isNotBlank(input)){
            return input.trim();
        }else {
            return null;
        }
    }
    
    public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(5);


        if (command.isChangeInStringParameterNamed("firstname", this.firstname)) {
            final String newValue = command.stringValueOfParameterNamed("firstname");
            actualChanges.put("firstname", newValue);
             this.firstname = newValue;
        }

        if (command.isChangeInStringParameterNamed("middlename", this.middlename)) {
            final String newValue = command.stringValueOfParameterNamed("middlename");
            actualChanges.put("middlename", newValue);
             this.middlename = newValue;
        }

        if (command.isChangeInStringParameterNamed("lastname", this.lastname)) {
            final String newValue = command.stringValueOfParameterNamed("lastname");
            actualChanges.put("lastname", newValue);
             this.lastname = newValue;
        }

        if (command.isChangeInStringParameterNamed("fullname", this.fullname)) {
            final String newValue = command.stringValueOfParameterNamed("fullname");
            actualChanges.put("fullname", newValue);
             this.fullname = newValue;
        }

        if (command.isChangeInStringParameterNamed("mobileNo", this.mobileNo)) {
            final String newValue = command.stringValueOfParameterNamed("mobileNo");
            actualChanges.put("mobileNo", newValue);
             this.mobileNo = newValue;
        }

        if (command.isChangeInStringParameterNamed("emailAddress", this.emailAddress)) {
            final String newValue = command.stringValueOfParameterNamed("emailAddress");
            actualChanges.put("emailAddress", newValue);
             this.emailAddress = newValue;
        }

        if (command.isChangeInLongParameterNamed("gender", genderId())) {
            final Long newValue = command.longValueOfParameterNamed("gender");
            actualChanges.put("gender", newValue);
        }

        if (command.isChangeInLocalDateParameterNamed("dateOfBirth", dateOfBirthLocalDate())) {
            final String valueAsInput = command.stringValueOfParameterNamed("dateOfBirth");
            actualChanges.put("dateOfBirth", valueAsInput);

            final LocalDate newValue = command.localDateValueOfParameterNamed("dateOfBirth");
            this.dateOfBirth = newValue.toDate();
        }

        return actualChanges;
    }
    
    public void updateGender(CodeValue gender){
        this.gender = gender;
    }

    private Long genderId(){
        Long genderId = null;
        if (this.gender != null) {
            genderId = this.gender.getId();
        }
        return genderId;
    }

    public LocalDate dateOfBirthLocalDate() {
        LocalDate dateOfBirth = null;
        if (this.dateOfBirth != null) {
            dateOfBirth = LocalDate.fromDateFields(this.dateOfBirth);
        }
        return dateOfBirth;
    }

}