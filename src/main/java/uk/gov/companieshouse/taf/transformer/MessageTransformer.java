package uk.gov.companieshouse.taf.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class to extract outgoing message id from byte array message.
 */
public class MessageTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTransformer.class);

    /**
     * Transform message to extract message id.
     *
     * @param message the outgoing message
     * @return String - message id
     */
    public String transform(byte[] message) {
        String outgoingMessageId = null;
        final String messageString = new String(message);
        LOGGER.info("message ... %s", messageString);
        ObjectMapper mapper = new ObjectMapper();
        try {
            IncomingMessage outgoingMessage = mapper.readValue(messageString,
                    IncomingMessage.class);
            outgoingMessageId = outgoingMessage.getIncomingMessageId();
        } catch (Exception ex) {
            LOGGER.error("Transformation error " + ex.getMessage());
            throw new RuntimeException("Transformation error");
        }

        return outgoingMessageId;
    }
}
