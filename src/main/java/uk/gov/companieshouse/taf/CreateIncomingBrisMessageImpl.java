package uk.gov.companieshouse.taf;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterReferenceType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;
import uk.gov.companieshouse.taf.producer.Sender;
import uk.gov.companieshouse.taf.service.IncomingBrisMessageService;

public abstract class CreateIncomingBrisMessageImpl implements CreateIncomingBrisMessage {

    @Autowired
    private IncomingBrisMessageService incomingBrisMessageService;

    @Autowired
    private Sender kafkaProducer;

    @Value("${kafka.producer.topic}")
    private String brisOutgoingTopic;

    private static final Logger log = LoggerFactory.getLogger(CreateIncomingBrisMessageImpl.class);

    private BRCompanyDetailsRequest constructBrCompanyDetailsRequest(String companyRef) {
        BusinessRegisterReferenceType businessRegisterReference =
                new BusinessRegisterReferenceType();
        BusinessRegisterIDType businessRegisterId = new BusinessRegisterIDType();
        businessRegisterId.setValue("1");
        businessRegisterReference.setBusinessRegisterID(businessRegisterId);

        CountryType country = new CountryType();
        businessRegisterReference.setBusinessRegisterCountry(country);

        BRCompanyDetailsRequest companyDetailsRequest = new BRCompanyDetailsRequest();

        companyDetailsRequest.setBusinessRegisterReference(businessRegisterReference);
        log.info("Requesting company details for %s", companyRef);
        return companyDetailsRequest;
    }

    /**
     * Simulate company details request from ECP.
     *
     * @param companyNumber the company number of company being searched
     */
    public void sendMessageToBris(String companyNumber) {
        // Construct a request to simulate a BRIS Incoming Message
        BRCompanyDetailsRequest companyDetailsRequest =
                constructBrCompanyDetailsRequest(companyNumber);

        IncomingBrisMessage incomingBrisMessage = new IncomingBrisMessage();
        incomingBrisMessage.setMessageId("JB1");
        incomingBrisMessage.setMessage(companyDetailsRequest.toString());

        // Store the request on MongoDB
        incomingBrisMessageService.save(incomingBrisMessage);

        //create new mongodb ObjectId for outgoing BRIS Message
        ObjectId objectId = new ObjectId().get();

        // Create a Kafka message
        kafkaProducer.sendMessage(brisOutgoingTopic, objectId.toString());
    }
}

