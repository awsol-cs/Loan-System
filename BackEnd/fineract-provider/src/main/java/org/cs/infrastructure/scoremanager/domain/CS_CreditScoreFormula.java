package org.cs.infrastructure.scoremanager.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;

@Entity
@Table(name = "formula")
public class CS_CreditScoreFormula extends AbstractPersistableCustom<Integer> {
    
    @Column(name = "formula_name")
    private String formulaName;

    @Column(name = "formula")
    private String formula;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    private String updatedDate;
    
    public CS_CreditScoreFormula() {
        
    }
    
    public CS_CreditScoreFormula(String status) {
        this.status = status;
    }
    
    public CS_CreditScoreFormula(String formulaName, String formula, String status, String createdBy, String createdDate, String updatedBy, String updatedDate) {
        this.formulaName = formulaName;
        this.formula = formula;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }
    
    public static CS_CreditScoreFormula fromJson(final CS_JsonCommand command) {
        String formulaName = command.stringValueOfParameterNamed("formulaName");
        String formula = command.stringValueOfParameterNamed("formula");
        String status = command.stringValueOfParameterNamed("status");
        return new CS_CreditScoreFormula(formulaName, formula, status, null, null, null, null);
    }

    public String getFormulaName() {
        return this.formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}