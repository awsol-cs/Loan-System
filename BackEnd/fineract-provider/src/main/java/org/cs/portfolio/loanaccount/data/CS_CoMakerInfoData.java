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
package org.cs.portfolio.loanaccount.data;

import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.joda.time.LocalDate;

@SuppressWarnings("unused")
final public class CS_CoMakerInfoData implements Comparable<CS_CoMakerInfoData> {

    private final Long id;
    private final Long kycId;
    private final String firstname;
    private final String middlename;
    private final String lastname;
    private final String fullname;
    private final String mobileNo;
    private final String emailAddress;
    private final LocalDate dateOfBirth;
    private final CodeValueData gender;

    public static CS_CoMakerInfoData instance(Long id, Long kycId, String firstname, String middlename, String lastname,
        String fullname, String mobileNo, String emailAddress, LocalDate dateOfBirth, CodeValueData gender){
        return new CS_CoMakerInfoData(id, kycId, firstname, middlename, lastname, fullname, mobileNo, 
            emailAddress, dateOfBirth, gender);
    }

    private CS_CoMakerInfoData(Long id, Long kycId, String firstname, String middlename, String lastname,
        String fullname, String mobileNo, String emailAddress, LocalDate dateOfBirth, CodeValueData gender){
        this.id = id;
        this.kycId = kycId;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.fullname = fullname;
        this.mobileNo = mobileNo;
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;                                             
    }

    @Override
    public int compareTo(final CS_CoMakerInfoData obj) {
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

    public Long getKycId(){
        return this.kycId;
    }

}