
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
import uk.gov.companieshouse.taf.domain.IncomingBRISMessage;
import uk.gov.companieshouse.taf.producer.Sender;
import uk.gov.companieshouse.taf.service.IncomingBRISMessageService;

public abstract class CreateIncomingBRISMessageImpl implements CreateIncomingBRISMessage
{
	@Autowired
	private IncomingBRISMessageService incomingBRISMessageService;

    @Autowired
    private Sender kafkaProducer;

    @Value("${kafka.producer.topic}")
    private String brisOutgoingTopic;

	private static final Logger log = LoggerFactory.getLogger(CreateIncomingBRISMessageImpl.class);

	private BRCompanyDetailsRequest constructBRCompanyDetailsRequest(String companyRef) {

		BRCompanyDetailsRequest companyDetailsRequest = new BRCompanyDetailsRequest();

		BusinessRegisterReferenceType businessRegisterReference = new BusinessRegisterReferenceType();
        BusinessRegisterIDType buinessRegisterID = new BusinessRegisterIDType();
        buinessRegisterID.setValue("1");
        businessRegisterReference.setBusinessRegisterID(buinessRegisterID);

        CountryType country = new CountryType();
        businessRegisterReference.setBusinessRegisterCountry(country);

        companyDetailsRequest.setBusinessRegisterReference(businessRegisterReference);
		log.info("Requesting company details for " + companyRef);
		return companyDetailsRequest;
	}

	public void sendMessageToBRIS(String companyRef){
        // Construct a request to simulate a BRIS Incoming Message
        BRCompanyDetailsRequest companyDetailsRequest = constructBRCompanyDetailsRequest(companyRef);

        IncomingBRISMessage incomingBRISMessage = new IncomingBRISMessage();
        incomingBRISMessage.setMessageId("JB1");
        incomingBRISMessage.setMessage(companyDetailsRequest.toString());

		// Store the request on MongoDB
		incomingBRISMessageService.save(incomingBRISMessage);

        //create new mongodb ObjectId for outgoing BRIS Message
        ObjectId objectId = new ObjectId().get();

		// Create a Kafka message
        kafkaProducer.sendMessage(brisOutgoingTopic, objectId.toString());
	}
}

