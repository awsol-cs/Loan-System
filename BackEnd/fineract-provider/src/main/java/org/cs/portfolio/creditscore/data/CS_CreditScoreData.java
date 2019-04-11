package org.cs.portfolio.creditscore.data;

public class CS_CreditScoreData {
    
    private final long id;
    private final long client_id;
    private final int creditscore;
    
    public CS_CreditScoreData(long id, long client_id, int creditscore) {
        this.id = id;
        this.client_id = client_id;
        this.creditscore = creditscore;
    }
    
    public static CS_CreditScoreData instance(long id, long client_id, int creditscore) {
        return new CS_CreditScoreData(id, client_id, creditscore);
    }

    public long getId() {
        return this.id;
    }

    public long getClientId(){
        return this.client_id;
    }

    public int getCreditScore(){
        return this.creditscore;
    }

}