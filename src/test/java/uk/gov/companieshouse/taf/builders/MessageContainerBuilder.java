package uk.gov.companieshouse.taf.builders;

import eu.europa.ec.bris.jaxb.br.company.details.response.v2_0.BRCompanyDetailsResponse;
import eu.europa.ec.bris.jaxb.br.generic.acknowledgement.v2_0.BRAcknowledgement;
import eu.europa.ec.bris.jaxb.br.generic.notification.v2_0.BRNotification;
import eu.europa.ec.digit.message.container.jaxb.v1_0.ContainerBody;
import eu.europa.ec.digit.message.container.jaxb.v1_0.ContainerHeader;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageInfo;
import eu.europa.ec.digit.message.container.jaxb.v1_0.PartyType;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@Component
public class MessageContainerBuilder {

    private final static String DATA_SOURCE_TYPE = "text/plain; charset=UTF-8";
    private final static String RECEIVER_ID = "blue";
    private final static String SENDER_ID = "red";
    public static final String UK_BUSINESS_REGISTER_ID = "EW";
    public static final String UK_BUSINESS_REGISTER_COUNTRY = "UK";

    public static MessageContainer createMessageContainer(Object response, String messageId, String correlationId, MessageInfo.TestData testData) throws JAXBException, DatatypeConfigurationException {

        MessageContainer messageContainer = new MessageContainer();
        messageContainer.setContainerHeader(createContainerHeader(messageId, correlationId, testData));
        messageContainer.setContainerBody(createContainerBody(response));

        return messageContainer;
    }

    private static ContainerBody createContainerBody(Object response) throws JAXBException {

        ContainerBody containerBody = new ContainerBody();
        containerBody.setMessageContent(createMessageContent(response));

        return containerBody;
    }

    private static ContainerBody.MessageContent createMessageContent(Object response) throws JAXBException {

        ContainerBody.MessageContent messageContent = new ContainerBody.MessageContent();
        messageContent.setValue(createDataHandler(response));

        return messageContent;
    }

    private static DataHandler createDataHandler(Object response) throws JAXBException {

        String data = marshalToString(response);

        return new DataHandler(new ByteArrayDataSource(data.getBytes(StandardCharsets.UTF_8), DATA_SOURCE_TYPE));
    }

    private static String marshalToString(Object object) throws JAXBException {
        StringWriter writer = new StringWriter();

        JAXBContext jaxbContext = JAXBContext.newInstance(BRAcknowledgement.class, BRCompanyDetailsResponse.class, BRNotification.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(object, writer);

        return writer.toString();
    }

    private static ContainerHeader createContainerHeader(String messageId, String correlationId, MessageInfo.TestData testData) throws DatatypeConfigurationException {

        ContainerHeader containerHeader = new ContainerHeader();
        containerHeader.setMessageInfo(createMessageInfo(messageId, correlationId, testData));
        containerHeader.setAddressInfo(createAddressInfo());

        return containerHeader;
    }

    private static ContainerHeader.AddressInfo createAddressInfo(){

        ContainerHeader.AddressInfo addressInfo = new ContainerHeader.AddressInfo();
        addressInfo.setSender(createPartyType(SENDER_ID,"BRIS","EU"));
        addressInfo.setReceiver(createPartyType(RECEIVER_ID, UK_BUSINESS_REGISTER_ID, UK_BUSINESS_REGISTER_COUNTRY));

        return addressInfo;
    }

    private static PartyType createPartyType(String id, String code, String countryCode){

        PartyType partyType = new PartyType();
        partyType.setId(id);
        partyType.setCode(code);
        partyType.setCountryCode(countryCode);

        return partyType;
    }

    private static MessageInfo createMessageInfo(String messageId, String correlationId, MessageInfo.TestData testData) throws DatatypeConfigurationException {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTestData(testData);
        messageInfo.setMessageID(messageId);
        messageInfo.setCorrelationID(correlationId);

        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());;
        messageInfo.setTimestamp(xmlGregorianCalendar);

        return messageInfo;
    }



}