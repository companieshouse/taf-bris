package uk.gov.companieshouse.taf.service;

import eu.domibus.plugin.bris.jaxb.delivery.Acknowledgement;
import eu.domibus.plugin.bris.jaxb.delivery.DeliveryBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionHeader;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotificationAcknowledgement;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureSubmissionNotification;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureSubmissionNotificationAcknowledgement;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentResponse;
import eu.europa.ec.bris.v140.jaxb.br.error.BRBusinessError;
import eu.europa.ec.bris.v140.jaxb.br.fault.BRFaultResponse;
import eu.europa.ec.bris.v140.jaxb.br.led.BRUpdateLEDRequest;
import eu.europa.ec.bris.v140.jaxb.br.led.BRUpdateLEDStatus;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotificationAcknowledgement;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerSubmissionNotification;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerSubmissionNotificationAcknowledgement;
import eu.europa.ec.bris.v140.jaxb.br.subscription.BRManageSubscriptionRequest;
import eu.europa.ec.bris.v140.jaxb.br.subscription.BRManageSubscriptionStatus;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;
import uk.gov.companieshouse.taf.domain.ValidationError;

@Component
public class RetrieveBrisTestMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            RetrieveBrisTestMessageService.class);
    private static final String MESSAGE_WAIT_TIME = "message.wait.time";
    private static final String EXPECTED_PDF_DOC = "expected-document.pdf";

    @Autowired
    private Environment env;

    @Autowired
    private IncomingBrisMessageService incomingBrisMessageService;

    /**
     * Check the mongo collection to retrieve the message by correlation id.
     * @param correlationId the message id to be retrieved
     * @return T the object retrieved from MongoDB
     */
    public <T> T checkForResponseByCorrelationId(String correlationId) throws Exception {
        IncomingBrisMessage incomingBrisMessage = getIncomingBrisMessageFromMongo(correlationId);

        if (incomingBrisMessage != null) {
            LOGGER.info("Message found in MongoDB : " + incomingBrisMessage.toString());
        }

        // If we have a message after iteration, then set it on the response
        if (incomingBrisMessage != null) {
            LOGGER.info("Found message with correlation ID {} !!", correlationId);
            JAXBContext jaxbContext = getJaxbContext();
            StringReader reader = new StringReader(incomingBrisMessage.getMessage());
            Object obj = jaxbContext.createUnmarshaller().unmarshal(reader);
            return (T)obj;
        }

        return null;
    }

    /*
        Loop for n seconds and check if the expected message is in MongoDB
     */
    private IncomingBrisMessage getIncomingBrisMessageFromMongo(String correlationId) {

        IncomingBrisMessage incomingBrisMessage = null;
        int counter = 0;

        int messageWaitTime = Integer.parseInt(env.getProperty(MESSAGE_WAIT_TIME));

        // Keep checking the test MongoDB instance to ensure that the message
        // has been processed successfully
        while (incomingBrisMessage == null && counter <= messageWaitTime) {
            LOGGER.info("Iteration {}, Correlation Id {}", counter, correlationId);
            incomingBrisMessage = incomingBrisMessageService.findOneByCorrelationId(correlationId);

            if (incomingBrisMessage == null) {
                counter++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Unexpected thread interrupt: " + ex.getMessage());
                }
            }
        }
        return incomingBrisMessage;
    }

    /**
       Get the Document from the Document Details response using the correlation id.
     */
    public byte[] getActualPdfDocument(String correlationId) {
        IncomingBrisMessage incomingBrisMessage = getIncomingBrisMessageFromMongo(correlationId);

        if (incomingBrisMessage.getData() == null) {
            throw new RuntimeException("No binary object retrieved for the document."
                    + " Check that the TEST_MODE flag is set to 1 on the components.");
        }
        return incomingBrisMessage.getData().getData();
    }

    /**
     Get the expected Document from the file system.
     */
    public byte[] getExpectedPdfDocument() {
        try {
            return IOUtils.toByteArray(new ClassPathResource(EXPECTED_PDF_DOC).getInputStream());
        } catch (IOException ex) {
            throw new RuntimeException("Unexpected error reading PDF from file system "
                    + ex.getMessage());
        }
    }

    @Bean
    private JAXBContext getJaxbContext() throws JAXBException {
        JAXBContext context;
        context = JAXBContext.newInstance(BRBranchDisclosureReceptionNotification.class,
                BRBranchDisclosureReceptionNotificationAcknowledgement.class,
                BRBranchDisclosureSubmissionNotification.class,
                BRBranchDisclosureSubmissionNotificationAcknowledgement.class,
                BRBusinessError.class,
                BRCompanyDetailsRequest.class,
                BRCompanyDetailsResponse.class,
                BRCrossBorderMergerReceptionNotification.class,
                BRCrossBorderMergerReceptionNotificationAcknowledgement.class,
                BRCrossBorderMergerSubmissionNotification.class,
                BRCrossBorderMergerSubmissionNotificationAcknowledgement.class,
                BRFaultResponse.class,
                BRManageSubscriptionRequest.class,
                BRManageSubscriptionStatus.class,
                BRRetrieveDocumentRequest.class,
                BRRetrieveDocumentResponse.class,
                BRUpdateLEDRequest.class,
                BRUpdateLEDStatus.class,
                Acknowledgement.class,
                DeliveryBody.class,
                SubmissionBody.class,
                SubmissionHeader.class,
                ValidationError.class);

        return context;
    }
}
