package uk.gov.companieshouse.taf.message;

import static org.springframework.test.util.AssertionErrors.assertEquals;

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

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.domain.IncomingBRISMessage;
import uk.gov.companieshouse.taf.service.IncomingBRISMessageService;

@Component
public class RetrieveMessage {

    private static final Logger log = LoggerFactory.getLogger(RetrieveMessage.class);

    @Autowired
    private IncomingBRISMessageService incomingBRISMessageService;

    /**
     * Check the mongo collection for the message by id.
     *
     * @param messageId the message id to be found
     */

    public void checkForMessageByMessageId(String messageId) throws Exception {
        IncomingBRISMessage message = null;
        int counter = 0;

        while (message == null && counter < 60) {
            log.info("Trying to find response message with ID : %s", messageId);
            message = incomingBRISMessageService.findByMessageId(messageId);

            if (message != null) {
                log.info("Message found " + message.getMessage());

                JAXBContext jaxbContext;
                Object obj;
                jaxbContext = getJaxbContext();
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(message.getMessage());
                obj = jaxbUnmarshaller.unmarshal(reader);

                BRCompanyDetailsResponse companyDetailsResponse =
                        (BRCompanyDetailsResponse) obj;

                assertEquals("Expected Message ID:", messageId,
                        companyDetailsResponse.getMessageHeader().getMessageID().getValue());

            }

            log.info("Looking for message, attempt: %s", counter);
            counter++;
            Thread.sleep(1000);

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
                SubmissionHeader.class);

        return context;
    }
}
