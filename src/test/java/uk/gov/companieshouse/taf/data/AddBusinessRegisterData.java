package uk.gov.companieshouse.taf.data;

import eu.europa.ec.bris.jaxb.br.generic.notification.v2_0.BRNotification;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.DateTimeType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("AddBusinessRegisterData")
public class AddBusinessRegisterData extends RequestData {

    private BRNotification brNotification;

    private DateTimeType notificationDateTime;

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
}
