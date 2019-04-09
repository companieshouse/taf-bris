package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.en.Given;
import eu.europa.ec.bris.jaxb.br.generic.notification.v2_0.BRNotification;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.DateTimeType;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.builders.AddBusinessRegisterBuilder;
import uk.gov.companieshouse.taf.data.AddBusinessRegisterData;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BusinessRegisterSteps extends BrisSteps{

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDetailsRequestSteps.class);

    @Autowired
    AddBusinessRegisterData data;

    @Autowired
    private SendBrisTestMessageService sendBrisMessageService;

    @Given("^I am creating an AddBrNotification$")
    public void makeAnAddBrNotification() throws Exception {

        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setValue(getXMLGregorianCalendar(null));
        data.setNotificationDateTime(dateTimeType);
        MessageContainer addBrNotification = AddBusinessRegisterBuilder.getBrNotification(data);

        data.setOutgoingBrisMessage(sendBrisMessageService
                .createOutgoingBrisMessage(addBrNotification, data.getMessageId()));

        sendBrisMessageService.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
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
