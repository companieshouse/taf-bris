package uk.gov.companieshouse.taf.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationResponse {

    @JsonProperty("response_message")
    private String responseMessage;

    @JsonProperty("message_id")
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public NotificationResponse(String responseMessage) {
        super();
        this.responseMessage = responseMessage;
    }

    public NotificationResponse(String responseMessage,
                                String messageId) {
        super();
        this.responseMessage = responseMessage;
        this.messageId = messageId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}