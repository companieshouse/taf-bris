package uk.gov.companieshouse.taf.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.kafka.message.Message;
import uk.gov.companieshouse.taf.service.KafkaService;
import uk.gov.companieshouse.taf.transformer.OutgoingMessage;

@Component
public class SenderImpl implements Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderImpl.class);

    @Autowired
    private KafkaService kafkaService;

    @Override
    public boolean sendMessage(String topic, String messageId) {

        boolean successful = false;

        Message kafkaMessage = new Message();

        //Create object to represent outgoing message
        OutgoingMessage outgoingMessage = new OutgoingMessage();
        outgoingMessage.setOutgoingMessageId(messageId);

        //Convert object to byte array to insert into kafka message
        ObjectMapper mapper = new ObjectMapper();
        try {
            byte[] value = mapper.writeValueAsString(outgoingMessage).getBytes();
            kafkaMessage.setValue(value);
            kafkaMessage.setTopic(topic);
            kafkaService.send(kafkaMessage);
            successful = true;
        } catch (JsonProcessingException jpe) {
            LOGGER.error("Unable to send message id " + messageId);
            LOGGER.info(jpe.getMessage());
        }

        return successful;
    }

    @Bean
    private JAXBContext getJaxbContext() {
        JAXBContext context = null;
        try {
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

        } catch (JAXBException exception) {
            LOGGER.error(exception.getMessage());
        }

        return context;
    }

    @Bean
    public Marshaller marshaller() throws JAXBException {
        return getJaxbContext().createMarshaller();
    }

    @Bean
    public Unmarshaller unmarshaller() throws JAXBException {
        return getJaxbContext().createUnmarshaller();
    }
}
