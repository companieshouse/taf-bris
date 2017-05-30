package uk.gov.companieshouse.taf.service;

import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.producer.Sender;

@Component
public class SendBrisTestMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendBrisTestMessageService.class);
    private static final String PENDING_STATUS = "PENDING";

    @Autowired
    private OutgoingBrisMessageService outgoingBrisMessageService;

    @Autowired
    private Sender sender;

    @Autowired
    private Marshaller marshaller;

    /**
     * Create the Request message to be sent to test Domibus.
     *
     * @param requestMessage the request message to be sent to Domibus
     * @param messageId      messageId to be set
     */

    public <T> OutgoingBrisMessage createOutgoingBrisMessage(T requestMessage,
                                                             String messageId)
            throws Exception {
        OutgoingBrisMessage outgoingBrisMessage = new OutgoingBrisMessage();

        String xmlMessage = StringUtils.EMPTY;
        Reader requestStream;
        requestStream = marshal(requestMessage).getReader();
        if (requestStream != null) {
            xmlMessage = IOUtils.toString(requestStream);
        } else {
            throw new RuntimeException("Unable to marshal request");
        }

        outgoingBrisMessage.setMessage(xmlMessage);

        //create new mongodb ObjectId for outgoing BRIS Message
        LOGGER.info("Creating outgoingBrisMessage with id {} ", messageId);
        outgoingBrisMessage.setId(messageId);
        outgoingBrisMessage.setCorrelationId(messageId);
        outgoingBrisMessage.setCreatedOn(getDateTime());
        outgoingBrisMessage.setStatus(PENDING_STATUS);
        return outgoingBrisMessage;
    }

    /**
     * Send the test message through the BRIS platform.
     *
     * @param outgoingBrisMessage The Message to send to the BRIS platform
     */
    public void sendOutgoingBrisMessage(OutgoingBrisMessage outgoingBrisMessage,
                                        String messageId) {
        // Save message to MongoDB
        outgoingBrisMessageService.save(outgoingBrisMessage);

        // And also send a message to Kafka
        sender.sendMessage("bris_outgoing_test", messageId);
    }

    private StreamSource marshal(Object message) throws JAXBException {
        StringBuilderWriter writer = new StringBuilderWriter();
        marshaller.marshal(message, writer);
        return new StreamSource(new StringReader(writer.toString()));
    }

    private static DateTime getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date dt = new Date();
        String strDt = sdf.format(dt);

        DateTimeFormatter parser = ISODateTimeFormat.dateTime();
        return parser.parseDateTime(strDt);
    }
}