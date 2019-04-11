package org.cs.infrastructure.scoremanager.data;

public class CS_CreditScoreFormulaData {
    
    private final int id;
    private final String formulaName;
    private final String formula;
    private final String status;
    private final String createdBy;
    private final String createdDate;
    private final String updatedBy;
    private final String updatedDate;
    
    public CS_CreditScoreFormulaData(int id, String formulaName, String formula, String status, String createdBy, String createdDate, String updatedBy, String updatedDate) {
        this.id = id;
        this.formulaName = formulaName;
        this.formula = formula;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }
    
    public static CS_CreditScoreFormulaData instance(int id, String formulaName, String formula, String status, String createdBy, String createdDate, String updatedBy,
            String updatedDate) {
        return new CS_CreditScoreFormulaData(id, formulaName, formula, status, createdBy, createdDate, updatedBy, updatedDate);
    }

    public int getId() {
        return this.id;
    }

    public String getFormulaName() {
        return this.formulaName;
    }

    public String getFormula() {
        return this.formula;
    }

    public String getStatus() {
        return this.status;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public String getUpdatedDate() {
        return this.updatedDate;
    }

}