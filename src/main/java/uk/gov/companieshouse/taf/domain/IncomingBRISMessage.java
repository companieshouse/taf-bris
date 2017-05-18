package uk.gov.companieshouse.taf.domain;

import org.joda.time.DateTime;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@ComponentScan
@Document(collection = "incoming_messages")
public class IncomingBRISMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** No-args constructor */
    public IncomingBRISMessage() {
    }

    /** Constructor */
    public IncomingBRISMessage(String messageId, String correlationId, String message) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
    }
    
    public IncomingBRISMessage(String messageId, String correlationId, String message, String status) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
        this.status = status;
    }
    
    public IncomingBRISMessage(String messageId, String correlationId, String message, String status, DateTime createdOn) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
        this.status = status;
        this.createdOn = createdOn;
    }
    
    public IncomingBRISMessage(String messageId, String correlationId, String message, String status, DateTime createdOn, String messageType) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
        this.status = status;
        this.createdOn = createdOn;
        this.messageType= messageType;
    }
    
    @Id
    private String id;

    @Size(min = 5, max = 100)
    @Field("message_id")
    private String messageId;

    @Field("correlation_id")
    private String correlationId;
    
    @Field("message_type")
    private String messageType;
    
    @Field("message")
    private String message;
    
    @Field("status")
    private String status;
    
    @Field("created_on")
    private DateTime createdOn;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }    
    
    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }
    
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomingBRISMessage incomingBRISMessage = (IncomingBRISMessage) o;

        if ( ! Objects.equals(id, incomingBRISMessage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IncomingBRISMessage{" +
                "_id=" + id +
                ", messageId='" + messageId + "'" +
                ", correlationId='" + correlationId + "'" +
                ", message='" + message + "'" +
                ", messageType='" + messageType + "'" +
                ", status='" + status + "'" +
                ", timestamp='" + createdOn + "'" +
                '}';
    }
}
