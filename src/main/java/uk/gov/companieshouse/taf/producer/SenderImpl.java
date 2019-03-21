package uk.gov.companieshouse.taf.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.domibus.plugin.bris.jaxb.delivery.Acknowledgement;
import eu.domibus.plugin.bris.jaxb.delivery.DeliveryBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionHeader;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
