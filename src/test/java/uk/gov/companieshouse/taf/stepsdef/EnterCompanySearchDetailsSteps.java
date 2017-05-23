package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import eu.domibus.plugin.bris.jaxb.delivery.Acknowledgement;
import eu.domibus.plugin.bris.jaxb.delivery.DeliveryBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionBody;
import eu.domibus.plugin.bris.jaxb.submission.SubmissionHeader;
import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageHeaderType;
import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageObjectType;
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
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterReferenceType;
import eu.europa.ec.bris.v140.jaxb.components.basic.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import uk.gov.companieshouse.taf.domain.IncomingBrisMessage;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.producer.Sender;
import uk.gov.companieshouse.taf.service.IncomingBrisMessageService;
import uk.gov.companieshouse.taf.service.OutgoingBrisMessageService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;


public class EnterCompanySearchDetailsSteps {

    private static final Logger log = LoggerFactory.getLogger(EnterCompanySearchDetailsSteps.class);

    private static final String PENDING_STATUS = "PENDING";
    private String MESSAGE_ID = UUID.randomUUID().toString();

    @Autowired
    protected Marshaller marshaller = null;

    @Autowired
    protected Unmarshaller unmarshaller = null;

    @Autowired
    private Sender sender;

    @Autowired
    private IncomingBrisMessageService incomingBrisMessageService;

    @Autowired
    private OutgoingBrisMessageService outgoingBrisMessageService;

    @Given("^a request has been made to retrieve company details$")
    public void enterCompanySearchDetailsSteps() {
        String CORRELATION_ID = MESSAGE_ID;

        //"00006400", "03977902"
        BRCompanyDetailsRequest request = CompanyDetailsHelper.newInstance(
                CORRELATION_ID,
                MESSAGE_ID,
                "00006400",
                "EW",
                "UK");


        OutgoingBrisMessage outgoingBrisMessage = new OutgoingBrisMessage();

        String xmlMessage = StringUtils.EMPTY;
        Reader requestStream = null;
        try {
            requestStream = marshal(request).getReader();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        try {
            xmlMessage = IOUtils.toString(requestStream);
        } catch (IOException e) {
            e.printStackTrace();

        }


        outgoingBrisMessage.setMessage(xmlMessage);

        //create new mongodb ObjectId for outgoing BRIS Message
        log.info("Listing outgoingBrisMessage with id: " + MESSAGE_ID);
        outgoingBrisMessage.setId(MESSAGE_ID);
        outgoingBrisMessage.setCorrelationId(CORRELATION_ID);
        outgoingBrisMessage.setCreatedOn(getDateTime());
        outgoingBrisMessage.setStatus(PENDING_STATUS);
        outgoingBrisMessageService.save(outgoingBrisMessage);
        sender.sendMessage("bris_outgoing_test", MESSAGE_ID);
    }

    @Then("^the company details should be retrieved$")
    public void checkCompanyDetailsRetrieved() {
        IncomingBrisMessage message = null;
        int counter = 0;

        while (message == null && counter < 60) {
            System.out.println("Trying to find response message with ID : " + MESSAGE_ID);
            message = incomingBrisMessageService.findByMessageId(MESSAGE_ID);

            if (message != null) {
                System.out.println("Message found " + message.getMessage());


                JAXBContext jaxbContext;
                Object obj = null;
                try {
                    jaxbContext = getJaxbContext();
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    JAXBSource source = new JAXBSource(jaxbContext, MessageObjectType.class);
                    StringReader reader = new StringReader(message.getMessage());
                    obj = jaxbUnmarshaller.unmarshal(reader);

                    BRCompanyDetailsResponse companyDetailsResponse = (BRCompanyDetailsResponse) obj;

                    assertEquals("Expected Country UK", "UK", companyDetailsResponse.getBusinessRegisterReference().getBusinessRegisterCountry().getValue());

                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }

            try {
                System.out.println("In loop : " + counter);
                counter++;
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private StreamSource marshal(Object message) throws JAXBException {
        StringBuilderWriter writer = new StringBuilderWriter();
        marshaller.marshal(message, writer);
        return new StreamSource(new StringReader(writer.toString()));
    }

    @Bean
    public JAXBContext getJaxbContext() {
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
            exception.printStackTrace();
        }

        return context;
    }

    private static MessageHeaderType getMessageHeader(String correlationId, String messageId) {
        MessageHeaderType messageHeaderType = new MessageHeaderType();
        CorrelationIDType correlationIDType = new CorrelationIDType();
        correlationIDType.setValue(correlationId);
        messageHeaderType.setCorrelationID(correlationIDType);
        MessageIDType messageIDType = new MessageIDType();
        messageIDType.setValue(messageId);
        messageHeaderType.setMessageID(messageIDType);

        //***** START --BusinessRegisterReference *******************//
        BusinessRegisterReferenceType businessRegisterReferenceType = new BusinessRegisterReferenceType();
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue("Companies House");

        BusinessRegisterIDType businessRegisterIDType = new BusinessRegisterIDType();

        //BusinessRegisterID
        businessRegisterIDType.setValue("EW");

        //BusinessRegisterCountry Country
        CountryType countryType = new CountryType();
        countryType.setValue("UK");

        //set BusinessRegisterID
        businessRegisterReferenceType.setBusinessRegisterID(businessRegisterIDType);

        // set BusinessRegisterCountry
        businessRegisterReferenceType.setBusinessRegisterCountry(countryType);
        // TODO BusinessRegisterName??

        // set BusinessRegisterReference to CompanyDetailsResponse
        messageHeaderType.setBusinessRegisterReference(businessRegisterReferenceType);
        return messageHeaderType;
    }

    public static BusinessRegisterReferenceType BusinessRegisterReferenceType(String countryCode, String businessRegisterId) {
        BusinessRegisterReferenceType businessRegisterReference = new BusinessRegisterReferenceType();
        businessRegisterReference.setBusinessRegisterCountry(CountryType(countryCode));
        businessRegisterReference.setBusinessRegisterID(BusinessRegisterIDType(businessRegisterId));
        return businessRegisterReference;
    }

    public static CompanyRegistrationNumberType CompanyRegistrationNumberType(String companyRegNumber) {
        CompanyRegistrationNumberType companyRegistrationNumber = new CompanyRegistrationNumberType();
        companyRegistrationNumber.setValue(companyRegNumber);
        return companyRegistrationNumber;
    }

    public static CountryType CountryType(String countryCode) {
        CountryType country = new CountryType();
        country.setValue(countryCode);
        return country;
    }

    public static BusinessRegisterIDType BusinessRegisterIDType(String identifier) {
        BusinessRegisterIDType businessRegisterId = new BusinessRegisterIDType();
        businessRegisterId.setValue(identifier);
        return businessRegisterId;
    }

    public static DateTime getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date dt = new Date();
        String strDt = sdf.format(dt);

        DateTimeFormatter parser = ISODateTimeFormat.dateTime();
        DateTime dateTimeResult = parser.parseDateTime(strDt);

        return dateTimeResult;
    }
}