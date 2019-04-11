package org.cs.portfolio.creditscore.domain;

import java.io.IOException;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;

import org.cs.infrastructure.core.api.CS_JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity
@Table(name = "credit_score")
public class CS_CreditScore extends AbstractPersistableCustom<Integer> {

    @Column(name = "client_id")
    private long client_id;

    @Column(name = "creditscore")
    private int creditscore;
    
    public CS_CreditScore() {
        
    }

    public CS_CreditScore(long client_id, int creditscore) {
        this.client_id = client_id;
        this.creditscore = creditscore;
    }
    
    public static CS_CreditScore fromJson(final CS_JsonCommand command) {
        Locale locale = Locale.US;  
        long client_id = command.longValueOfParameterNamed("client_id");
        int creditscore = command.integerValueOfParameterNamed("creditscore", locale);
        return new CS_CreditScore(client_id, creditscore);
    }

    public long getClientId(){
        return this.client_id;
    }

    public void setClientId(long client_id){
        this.client_id = client_id;
    }

    public int getCreditScore(){
        return this.creditscore;
    }

    public void setCreditScore(int creditscore){
        this.creditscore = creditscore;
    }

}