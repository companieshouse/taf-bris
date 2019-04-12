package uk.gov.companieshouse.taf.service;

import eu.domibus.plugin.bris.jaxb.delivery.Acknowledgement;
import eu.domibus.plugin.bris.jaxb.delivery.DeliveryBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionHeader;


import java.io.IOException;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import eu.europa.ec.bris.jaxb.br.branch.disclosure.notification.reception.request.v1_4.BRBranchDisclosureReceptionNotification;
import eu.europa.ec.bris.jaxb.br.branch.disclosure.notification.reception.response.v1_4.BRBranchDisclosureReceptionNotificationAcknowledgement;
import eu.europa.ec.bris.jaxb.br.branch.disclosure.notification.submission.request.v1_4.BRBranchDisclosureSubmissionNotification;
import eu.europa.ec.bris.jaxb.br.branch.disclosure.notification.submission.response.v1_4.BRBranchDisclosureSubmissionNotificationAcknowledgement;
import eu.europa.ec.bris.jaxb.br.company.details.request.v1_4.BRCompanyDetailsRequest;
import eu.europa.ec.bris.jaxb.br.company.details.response.v2_0.BRCompanyDetailsResponse;
import eu.europa.ec.bris.jaxb.br.crossborder.merger.notification.reception.request.v1_4.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.jaxb.br.crossborder.merger.notification.reception.response.v1_4.BRCrossBorderMergerReceptionNotificationAcknowledgement;
import eu.europa.ec.bris.jaxb.br.crossborder.merger.notification.submission.request.v1_4.BRCrossBorderMergerSubmissionNotification;
import eu.europa.ec.bris.jaxb.br.crossborder.merger.notification.submission.response.v1_4.BRCrossBorderMergerSubmissionNotificationAcknowledgement;
import eu.europa.ec.bris.jaxb.br.document.retrieval.request.v1_4.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.jaxb.br.document.retrieval.response.v1_4.BRRetrieveDocumentResponse;
import eu.europa.ec.bris.jaxb.br.error.v1_4.BRBusinessError;
import eu.europa.ec.bris.jaxb.br.led.update.request.v1_4.BRUpdateLEDRequest;
import eu.europa.ec.bris.jaxb.br.led.update.response.v1_4.BRUpdateLEDStatus;
import eu.europa.ec.bris.jaxb.br.subscription.request.v1_4.BRManageSubscriptionRequest;
import eu.europa.ec.bris.jaxb.br.subscription.response.v1_4.BRManageSubscriptionStatus;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;

@Component
public class RetrieveBrisTestMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            RetrieveBrisTestMessageService.class);
    private static final String MESSAGE_WAIT_TIME = "message.wait.time";
    private static final String EXPECTED_PDF_DOC = "expected-document.pdf";
    private static final String BRIS_INCOMING_TEST_COLLECTION = "incoming_messages";

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("BrisTestMongoDbOperations")
    private MongoOperations brisTestMongoDbOperations;

    /**
     * Check the mongo collection to retrieve the message by correlation id.
     *
     * @param correlationId the message id to be retrieved
     * @return T the object retrieved from MongoDB
     */
    @SuppressWarnings("unchecked")
    public <T> T checkForMessageByCorrelationId(String correlationId) throws Exception {
        IncomingBrisMessage incomingBrisMessage = getIncomingBrisMessageFromMongo(correlationId);

        // If we have a message after iteration, then set it on the response
        if (incomingBrisMessage != null) {
            LOGGER.info("Message found in MongoDB : {}", incomingBrisMessage.toString());
            JAXBContext jaxbContext = getJaxbContext();
            StringReader reader = new StringReader(incomingBrisMessage.getMessage());
            Object obj = jaxbContext.createUnmarshaller().unmarshal(reader);

            return (T) obj;
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

            incomingBrisMessage = brisTestMongoDbOperations.findOne(
                    new Query(Criteria.where("correlation_id")
                            .is(correlationId)), IncomingBrisMessage.class,
                    BRIS_INCOMING_TEST_COLLECTION);

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
     * Get the Document from the Document Details response using the correlation id.
     */
    public byte[] getActualPdfDocument(String correlationId) {
        IncomingBrisMessage incomingBrisMessage = getIncomingBrisMessageFromMongo(correlationId);

        if (null == incomingBrisMessage.getData()) {
            throw new RuntimeException("No binary object retrieved for the document."
                    + " Check that the TEST_MODE flag is set to 1 on the components.");
        }
        return incomingBrisMessage.getData().getData();
    }

    /**
     * Get the expected Document from the file system.
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
                MessageContainer.class);

        return context;
    }
}
