package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import eu.europa.ec.bris.jaxb.br.generic.acknowledgement.template.br.addition.v2_0.AddBusinessRegisterAcknowledgementTemplateType;
import eu.europa.ec.bris.jaxb.br.generic.acknowledgement.template.br.removal.v2_0.RemoveBusinessRegisterAcknowledgementTemplateType;
import eu.europa.ec.bris.jaxb.br.generic.acknowledgement.v2_0.BRAcknowledgement;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.DateTimeType;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.builders.AddBusinessRegisterBuilder;
import uk.gov.companieshouse.taf.builders.RemoveBusinessRegisterBuilder;
import uk.gov.companieshouse.taf.data.BusinessRegisterData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.MessageContainerHelper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertNotNull;

public class BusinessRegisterSteps extends BrisSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDetailsRequestSteps.class);

    @Autowired
    BusinessRegisterData data;

    @Autowired
    private SendBrisTestMessageService sendBrisMessageService;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;


    @Then("^the response will contain an Add Business Register Acknowledgement Template Type$")
    public void theResponseWillContainTheAddBusinessRegisterAcknowledgementTemplateType$() throws Throwable {
        MessageContainer response = retrieveMessage
                .checkForMessageByCorrelationId(data.getCorrelationId());
        assertNotNull(response);

        data.setBrNotificationResponse(response);

        BRAcknowledgement brAcknowledgement = MessageContainerHelper.getObjectFromContainer(response);

        AddBusinessRegisterAcknowledgementTemplateType template = (AddBusinessRegisterAcknowledgementTemplateType) brAcknowledgement.getAcknowledgementTemplate().getValue();

        assertNotNull(template.getSendingDateTime());

        // And assert that the header details are correct
        CommonSteps.validateHeader(createBrisMessageHeaderType(response),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }

    @Then("^the response will contain a Remove Business Register Acknowledgement Template Type$")
    public void theResponseWillContainTheRemoveBusinessRegisterAcknowledgementTemplateType$() throws Throwable {
        MessageContainer response = retrieveMessage
                .checkForMessageByCorrelationId(data.getCorrelationId());
        assertNotNull(response);

        data.setBrNotificationResponse(response);

        BRAcknowledgement brAcknowledgement = MessageContainerHelper.getObjectFromContainer(response);

        RemoveBusinessRegisterAcknowledgementTemplateType template = (RemoveBusinessRegisterAcknowledgementTemplateType) brAcknowledgement.getAcknowledgementTemplate().getValue();

        assertNotNull(template.getSendingDateTime());

        // And assert that the header details are correct
        CommonSteps.validateHeader(createBrisMessageHeaderType(response),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }


    @Given("^I am creating an AddBrNotification with details$")
    public void makeAnAddBrNotification(DataTable brNotificationDetails) throws Exception {

        setDataFromUser(brNotificationDetails);


        MessageContainer addBrNotification = AddBusinessRegisterBuilder.getAddBrNotification(data);

        data.setOutgoingBrisMessage(sendBrisMessageService
                .createOutgoingBrisMessage(addBrNotification, data.getMessageId(), data.getCorrelationId()));

        sendBrisMessageService.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
    }

    @Given("^I am creating a RemoveBrNotification with details$")
    public void makeARemoveBrNotification(DataTable brNotificationDetails) throws Exception {

        setDataFromUser(brNotificationDetails);

        MessageContainer removeBrNotification = RemoveBusinessRegisterBuilder.getRemoveBrNotification(data);

        data.setOutgoingBrisMessage(sendBrisMessageService
                .createOutgoingBrisMessage(removeBrNotification, data.getMessageId(), data.getCorrelationId()));

        sendBrisMessageService.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
    }

    private void setDataFromUser(DataTable brNotificationDetails) throws Exception {

        for (Map<String, String> testData : brNotificationDetails.asMaps(String.class, String.class)) {

            //set data from user
            String companyNumber = testData.get("companyNumber");
            String messageId = testData.get("messageId");
            String correlationId = testData.get("correlationId");
            String businessRegisterId = testData.get("receiverBusinessRegisterId");
            String countryCode = testData.get("receiverCountryCode");
            String registerName = testData.get("registerName");
            String notificationDateTime = testData.get("notificationDateTime");
            String senderBusinessRegisterId = testData.get("senderBusinessRegisterId");
            String senderCountryCode = testData.get("senderCountryCode");

            setValue(companyNumber, data::setCompanyNumber);
            setValue(messageId, data::setMessageId);
            setValue(correlationId, data::setCorrelationId);
            setValue(businessRegisterId, data::setBusinessRegisterId);
            setValue(countryCode, data::setCountryCode);
            setValue(registerName, data::setRegisterName);
            setValue(senderBusinessRegisterId, data::setSenderBusinessRegisterId);
            setValue(senderCountryCode, data::setSenderCountryCode);

            //only allow user to set to null or current time
            if (notificationDateTime.equals("NULL")) {
                data.setNotificationDateTime(null);
            } else {
                DateTimeType dateTimeType = new DateTimeType();
                dateTimeType.setValue(getXMLGregorianCalendar(null));
                data.setNotificationDateTime(dateTimeType);
            }
        }
    }

    private void setValue(String value, Consumer<String> c) {
        if (value.equals("NULL")) {
            c.accept(null);
        } else if (value.equals("EMPTY")) {
            c.accept("");
        } else if (!value.equals("")) {
            c.accept(value);
        }
    }

    /**
     * Return date passed in as XMLGregorianCalendar
     * If date is null, return current date/time
     *
     * @param date
     * @return XMLGregorianCalendar
     */
    private static XMLGregorianCalendar getXMLGregorianCalendar(Date date) throws Exception {
        XMLGregorianCalendar now = null;
        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            if (date != null) {
                gregorianCalendar.setTime(date);
            }
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

        } catch (DatatypeConfigurationException exception) {
            throw new Exception(exception);
        }
        return now;
    }

}
