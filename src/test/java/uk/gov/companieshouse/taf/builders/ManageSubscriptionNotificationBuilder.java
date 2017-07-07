package uk.gov.companieshouse.taf.builders;

import uk.gov.companieshouse.taf.data.ManageSubscriptionData;
import uk.gov.companieshouse.taf.domain.ManageSubscription;

public class ManageSubscriptionNotificationBuilder {

    /**
     * Creates a manage subscription notification.
     *
     * @return ManageSubscription
     */
    public static ManageSubscription createDefaultManageSubscription(ManageSubscriptionData data) {
        ManageSubscription manageSubscription = new ManageSubscription();
        manageSubscription.setSubscriptionType(data.getSubscriptionType());
        manageSubscription.setForeignRegisterId(data.getForeignRegisterId());
        manageSubscription.setForeignCountryCode(data.getForeignCountryCode());
        return manageSubscription;
    }
}
