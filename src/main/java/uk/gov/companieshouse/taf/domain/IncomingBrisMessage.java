package uk.gov.companieshouse.taf.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Size;

import org.bson.types.Binary;
import org.joda.time.DateTime;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


/**
 * Domain object for incoming message in MongoDB
 * Collection specified using @Document annotation.
 */
@Document(collection = "incoming_messages")
public class IncomingBrisMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * No-args constructor.
     */
    public IncomingBrisMessage() {
    }

    /**
     * Constructor.
     */
    public IncomingBrisMessage(String messageId, String correlationId, String message) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
    }

    /**
     * Constructor for Incoming BRIS message.
     *
     * @param messageId     The message id.
     * @param correlationId The correlation id.
     * @param message       The message content.
     * @param status        Status of the message.
     */
    public IncomingBrisMessage(String messageId, String correlationId,
                               String message, String status) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
        this.status = status;
    }


    /**
     * Constructor for Incoming BRIS message.
     *
     * @param messageId     The message id.
     * @param correlationId The correlation id.
     * @param message       The message content.
     * @param status        Status of the message.
     * @param createdOn     The date message created.
     */
    public IncomingBrisMessage(String messageId, String correlationId, String message,
                               String status, DateTime createdOn) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
        this.status = status;
        this.createdOn = createdOn;
    }

    /**
     * Constructor for Incoming BRIS message.
     *
     * @param messageId     The message id.
     * @param correlationId The correlation id.
     * @param message       The message content.
     * @param status        Status of the message.
     * @param createdOn     The date message created.
     * @param messageType   The type of message.
     */
    public IncomingBrisMessage(String messageId, String correlationId, String message,
                               String status, DateTime createdOn, String messageType) {
        this.messageId = messageId;
        this.correlationId = correlationId;
        this.message = message;
        this.status = status;
        this.createdOn = createdOn;
        this.messageType = messageType;
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

    @Field("binary_attachment")
    private Binary data;

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

    public Binary getData() {
        return data;
    }

    public void setData(Binary data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        IncomingBrisMessage incomingBrisMessage = (IncomingBrisMessage) obj;

        return Objects.equals(id, incomingBrisMessage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IncomingBrisMessage{"
                + "_id=" + id
                + ", messageId='" + messageId + "'"
                + ", correlationId='" + correlationId + "'"
                + ", message='" + message + "'"
                + ", messageType='" + messageType + "'"
                + ", status='" + status + "'"
                + ", timestamp='" + createdOn + "'"
                + '}';
    }
}
