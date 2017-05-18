package uk.gov.companieshouse.taf.transformer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncomingMessage {

    @JsonProperty("incoming_id")
    private String incomingMessageId;

    public String getIncomingMessageId() {
        return incomingMessageId;
    }
    
    public void setIncomingMessageId(String incomingMessageId) {
        this.incomingMessageId = incomingMessageId;
    }

}