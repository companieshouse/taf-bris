package uk.gov.companieshouse.taf.stepsdef;

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
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

public class BusinessRegisterSteps extends BrisSteps{

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

    @Then("^the response will contain a RemoveBusinessRegisterAcknowledgementTemplateType$")
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
    public void makeAnAddBrNotification(List<String> brNotificationDetails) throws Exception {

        setDataFromUser(brNotificationDetails);


        MessageContainer addBrNotification = AddBusinessRegisterBuilder.getAddBrNotification(data);

        data.setOutgoingBrisMessage(sendBrisMessageService
                .createOutgoingBrisMessage(addBrNotification, data.getMessageId(), data.getCorrelationId()));

        sendBrisMessageService.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
    }

    @Given("^I am creating a RemoveBrNotification with details$")
    public void makeARemoveBrNotification(List<String> brNotificationDetails) throws Exception {

        setDataFromUser(brNotificationDetails);

        MessageContainer removeBrNotification = RemoveBusinessRegisterBuilder.getRemoveBrNotification(data);

        data.setOutgoingBrisMessage(sendBrisMessageService
                .createOutgoingBrisMessage(removeBrNotification, data.getMessageId(), data.getCorrelationId()));

        sendBrisMessageService.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
    }

    private void setDataFromUser(List<String> brNotificationDetails) throws Exception {

        //set data from user
        String companyNumber = brNotificationDetails.get(0);
        String messageId = brNotificationDetails.get(1) ;
        String correlationId = brNotificationDetails.get(2);
        String businessRegisterId = brNotificationDetails.get(3);
        String countryCode = brNotificationDetails.get(4);
        String registerName = brNotificationDetails.get(5);
        String notificatioDateTime = brNotificationDetails.get(6);

        if(companyNumber.equals("NULL")){
            data.setCompanyNumber(null);
        }else if(!companyNumber.equals("")){
            data.setCompanyNumber(companyNumber);
        }

        if(messageId.equals("NULL")){
            data.setMessageId(null);
        }else if(!messageId.equals("")){
            data.setMessageId(messageId);
        }

        if(correlationId.equals("NULL")){
            data.setCorrelationId(null);
        }else if(!correlationId.equals("")){
            data.setCorrelationId(correlationId);
        }

        if(businessRegisterId.equals("NULL")){
            data.setBusinessRegisterId(null);
        }else if(!businessRegisterId.equals("")){
            data.setBusinessRegisterId(businessRegisterId);
        }

        if(countryCode.equals("NULL")){
            data.setCountryCode(null);
        }else if(!countryCode.equals("")){
            data.setCountryCode(countryCode);
        }

        if(registerName.equals("NULL")){
            data.setRegisterName(null);
        }else if(!registerName.equals("")){
            data.setRegisterName(registerName);
        }

        if(notificatioDateTime.equals("NULL")){
            data.setNotificationDateTime(null);
        }else {
            DateTimeType dateTimeType = new DateTimeType();
            dateTimeType.setValue(getXMLGregorianCalendar(null));
            data.setNotificationDateTime(dateTimeType);
        }
    }

    /**
     * Return date passed in as XMLGregorianCalendar
     * If date is null, return current date/time
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
