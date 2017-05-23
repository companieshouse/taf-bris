package uk.gov.companieshouse.taf.message;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;

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
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.producer.Sender;
import uk.gov.companieshouse.taf.service.OutgoingBrisMessageService;

public class CompanyDetailsRequest {

    private static final Logger log = LoggerFactory.getLogger(CompanyDetailsRequest.class);
    private static final String PENDING_STATUS = "PENDING";


    @Autowired
    private OutgoingBrisMessageService outgoingBrisMessageService;

    @Autowired
    private Sender sender;

    @Autowired
    private Marshaller marshaller = null;

    /**
     * Create the BRCompanyDetailsRequest to be sent to test domibus.
     *
     * @param request   BRCompanyDetailsRequest
     * @param messageId messageId to be set
     */
    public void createOutGoingMessageAndSend(BRCompanyDetailsRequest request, String messageId)
            throws Exception {
        OutgoingBrisMessage outgoingBrisMessage = new OutgoingBrisMessage();

        String xmlMessage = StringUtils.EMPTY;
        Reader requestStream;
        requestStream = marshal(request).getReader();
        if (requestStream != null) {
            xmlMessage = IOUtils.toString(requestStream);
        }

        outgoingBrisMessage.setMessage(xmlMessage);

        //create new mongodb ObjectId for outgoing BRIS Message
        log.info("Listing outgoingBrisMessage with id: %s", messageId);
        outgoingBrisMessage.setId(messageId);
        outgoingBrisMessage.setCorrelationId(messageId);
        outgoingBrisMessage.setCreatedOn(getDateTime());
        outgoingBrisMessage.setStatus(PENDING_STATUS);
        outgoingBrisMessageService.save(outgoingBrisMessage);
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
