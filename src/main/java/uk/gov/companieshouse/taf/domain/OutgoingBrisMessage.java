package uk.gov.companieshouse.taf.domain;

import org.bson.types.Binary;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Domain object for outgoing message in MongoDB
 * Collection specified using @Document annotation.
 */
@Document(collection = "outgoing_messages")
public class OutgoingBrisMessage {

    @Id
    private String id;

    @Field("correlation_id")
    private String correlationId;

    private String status;

    @Field("created_on")
    private DateTime createdOn;

    @Field("message_type")
    private String messageType;

    private String message;

    private Attachment attachment;

    /**
     * No-args constructor.
     */
    public OutgoingBrisMessage() {
    }

    /**
     * Constructor for outgoing BRIS message.
     *
     * @param correlationId The correlation id.
     * @param status        The status of message.
     * @param createdOn     The date created.
     * @param messageType   The type of message.
     * @param message       Message content.
     */
    public OutgoingBrisMessage(String correlationId, String status, DateTime createdOn,
                               String messageType, String message) {
        this.correlationId = correlationId;
        this.status = status;
        this.createdOn = createdOn;
        this.messageType = messageType;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
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

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return String.format(
                "OutgoingBrisMessage[id=%s, status='%s', "
                        + "createdOn=%s, messageType='%s', message='%s']",
                id, status, createdOn, messageType, message);
    }

    public static class Attachment {

        @Field("attachment_ref")
        private String attachmentRef;

        @Field("document_id")
        private String documentId;

        private Binary data;

        public String getAttachmentRef() {
            return attachmentRef;
        }

        public void setAttachmentRef(String attachmentRef) {
            this.attachmentRef = attachmentRef;
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public Binary getData() {
            return data;
        }

        public void setData(Binary data) {
            this.data = data;
        }

        public String toString() {
            return String.format("Attachment [ref=%s, data='%s',",
                    attachmentRef, data);
        }

    }

}
