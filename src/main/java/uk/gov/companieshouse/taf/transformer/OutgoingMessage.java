package uk.gov.companieshouse.taf.transformer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutgoingMessage {

    @JsonProperty("outgoing_id")
    private String outgoingMessageId;

    public String getOutgoingMessageId() {
        return outgoingMessageId;
    }
    
    public void setOutgoingMessageId(String outgoingMessageId) {
        this.outgoingMessageId = outgoingMessageId;
    }

}