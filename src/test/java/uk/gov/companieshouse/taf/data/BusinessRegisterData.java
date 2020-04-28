package uk.gov.companieshouse.taf.data;

import eu.europa.ec.bris.jaxb.br.generic.notification.v2_0.BRNotification;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.DateTimeType;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;

@Component
@Qualifier("BusinessRegisterData")
public class BusinessRegisterData extends RequestData {


    private String senderBusinessRegisterId = "BRA";
    private String senderCountryCode = "AT";

    private BRNotification brNotification;

    private DateTimeType notificationDateTime;

    private MessageContainer brNotificationResponse;

    public BRNotification getBrNotification() {
        return brNotification;
    }

    public void setBrNotification(BRNotification brNotification) {
        this.brNotification = brNotification;
    }

    public DateTimeType getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(DateTimeType notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public MessageContainer getBrNotificationResponse() {
        return brNotificationResponse;
    }

    public void setBrNotificationResponse(MessageContainer brNotificationResponse) {
        this.brNotificationResponse = brNotificationResponse;
    }

    public String getSenderBusinessRegisterId() {
        return senderBusinessRegisterId;
    }

    public void setSenderBusinessRegisterId(String senderBusinessRegisterId) {
        this.senderBusinessRegisterId = senderBusinessRegisterId;
    }

    public String getSenderCountryCode() {
        return senderCountryCode;
    }

    public void setSenderCountryCode(String senderCountryCode) {
        this.senderCountryCode = senderCountryCode;
    }
}
