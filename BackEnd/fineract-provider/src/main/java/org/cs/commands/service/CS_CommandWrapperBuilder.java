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
package org.cs.commands.service;

import org.cs.commands.domain.CS_CommandWrapper;
import org.apache.fineract.infrastructure.accountnumberformat.service.AccountNumberFormatConstants;
import org.apache.fineract.portfolio.client.api.ClientApiConstants;
import org.apache.fineract.portfolio.paymenttype.api.PaymentTypeApiResourceConstants;
import org.apache.fineract.portfolio.savings.DepositsApiConstants;
import org.apache.fineract.useradministration.api.PasswordPreferencesApiConstants;

public class CS_CommandWrapperBuilder {

    private Long officeId;
    private Long groupId;
    private Long clientId;
    private Long loanId;
    private Long savingsId;
    private String actionName;
    private String entityName;
    private Long entityId;
    private Long subentityId;
    private String href;
    private String json = "{}";
    private String transactionId;
    private Long productId;
    private Long templateId;
    private Long creditBureauId;
    private Long organisationCreditBureauId;
    private Long creditScoreId;
    private Integer scoreManagerId;
    private Long formulaId;
   

    public CS_CommandWrapper build() {
        return new CS_CommandWrapper(this.officeId, this.groupId, this.clientId, this.loanId, this.savingsId, this.actionName, this.entityName, this.entityId,
                this.subentityId, this.href, this.json, this.transactionId, this.productId, this.templateId, this.creditBureauId, this.organisationCreditBureauId,
                this.creditScoreId, this.scoreManagerId, this.formulaId);
    }
  
    public CS_CommandWrapperBuilder withLoanId(final Long withLoanId) {
        this.loanId = withLoanId;
        return this;
    }

    public CS_CommandWrapperBuilder withSavingsId(final Long withSavingsId) {
        this.savingsId = withSavingsId;
        return this;
    }

    public CS_CommandWrapperBuilder withClientId(final Long withClientId) {
        this.clientId = withClientId;
        return this;
    }

    public CS_CommandWrapperBuilder withGroupId(final Long withGroupId) {
        this.groupId = withGroupId;
        return this;
    }

    public CS_CommandWrapperBuilder withEntityName(final String withEntityName) {
        this.entityName = withEntityName;
        return this;
    }

    public CS_CommandWrapperBuilder withSubEntityId(final Long withSubEntityId) {
        this.subentityId = withSubEntityId;
        return this;
    }

    public CS_CommandWrapperBuilder withJson(final String withJson) {
        this.json = withJson;
        return this;
    }

    public CS_CommandWrapperBuilder withNoJsonBody() {
        this.json = null;
        return this;
    }

    public CS_CommandWrapperBuilder updateUser(final Long userId) {
        this.actionName = "UPDATE";
        this.entityName = "USER";
        this.entityId = userId;
        this.href = "/users/" + userId;
        return this;
    }

    public CS_CommandWrapperBuilder transferLoan() {
        this.actionName = "TRANSFERLOAN";
        this.entityName = "LOAN";
        this.href = "/transferloan";
        return this;
    }
    
    public CS_CommandWrapperBuilder updateCreditScoreFormula(final long id) {
        this.actionName = "UPDATE";
        this.entityName = "CREDIT_SCORE_FORMULA";
        this.entityId = id;
        this.href = "/scoreformula/template";
        this.formulaId = id;
        return this;
    }
    
    public CS_CommandWrapperBuilder createCreditScoreFormula() {
        this.actionName = "CREATE";
        this.entityName = "CREDIT_SCORE_FORMULA";
        this.href = "/scoreformula/template";
        return this;
    }
    
    public CS_CommandWrapperBuilder updateScoreConfig(final int scoreManagerId) {
        this.actionName = "UPDATE";
        this.entityName = "SCORE_CONFIG";
        this.entityId = new Long(scoreManagerId);
        this.href = "/scoremanager/template";
        this.scoreManagerId = scoreManagerId;
        return this;
    }
    
    public CS_CommandWrapperBuilder updateScoreConfigStatus(final int scoreManagerId) {
        this.actionName = "UPDATE";
        this.entityName = "SCORE_CONFIG_STATUS";
        this.entityId = new Long(scoreManagerId);
        this.href = "/scoremanager/template";
        this.scoreManagerId = scoreManagerId;
        return this;
    }
    
    public CS_CommandWrapperBuilder createScoreConfig() {
        this.actionName = "CREATE";
        this.entityName = "SCORE_CONFIG";
        this.href = "/scoremanager/template";
        return this;
    }

    public CS_CommandWrapperBuilder createCreditScore() {
        this.actionName = "CREATE";
        this.entityName = "CREDIT_SCORE";
        this.href = "/creditscore/template";
        return this;
    }
    
    public CS_CommandWrapperBuilder updateCreditScore(final long creditScoreId) {
        this.actionName = "UPDATE";
        this.entityName = "CREDIT_SCORE";
        this.href = "/creditscore/template";
        this.entityId = creditScoreId;
        this.creditScoreId = creditScoreId;
        return this;
    }
    
    public CS_CommandWrapperBuilder createCreditBureau() {
        this.actionName = "CREATE";
        this.entityName = "CREDITBUREAU";
        this.href = "/creditBureauConfiguration/createCreditBureau/template";
        return this;
    }

    public CS_CommandWrapperBuilder rejectLoanApplication(final Long loanId) {
        this.actionName = "REJECT";
        this.entityName = "LOAN_APP";
        this.entityId = loanId;
        this.loanId = loanId;
        this.href = "/loans/" + loanId;
        return this;
    }

    public CS_CommandWrapperBuilder approveLoanApplication(final Long loanId) {
        this.actionName = "APPROVE";
        this.entityName = "LOAN_APP";
        this.entityId = loanId;
        this.loanId = loanId;
        this.href = "/loans/" + loanId;
        return this;
    }

    public CS_CommandWrapperBuilder cs_CreateClient() {
        this.actionName = "CREATE";
        this.entityName = "CS_CLIENT";
        this.href = "/clients/template";
        return this;
    }

    public CS_CommandWrapperBuilder cs_UpdateClient(final Long clientId) {
        this.actionName = "UPDATE";
        this.entityName = "CS_CLIENT";
        this.entityId = clientId;
        this.clientId = clientId;
        this.href = "/cs_clients/" + clientId;
        return this;
    }

    public CS_CommandWrapperBuilder cs_CreateLoan() {
        this.actionName = "CREATE";
        this.entityName = "CS_LOAN";
        this.href = "/loans";
        return this;
    }

    public CS_CommandWrapperBuilder cs_UpdateLoan(final Long comakerId) {
        this.actionName = "UPDATE";
        this.entityName = "CS_COMAKER";
        this.entityId = comakerId;
        this.href = "/loans/comaker/" + comakerId;
        return this;
    }
}
