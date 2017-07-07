package uk.gov.companieshouse.taf.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.domain.ManageSubscription;

@Component
@Qualifier("ManageSubscriptionData")
public class ManageSubscriptionData extends RequestData {

    private String subscriptionType = "ADD";
    private String foreignRegisterId = "BRA";
    private String foreignCountryCode = "AT";


    private ManageSubscription manageSubscription;

    public ManageSubscription getManageSubscription() {
        return manageSubscription;
    }

    public void setManageSubscription(ManageSubscription manageSubscription) {
        this.manageSubscription = manageSubscription;
    }


    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getForeignRegisterId() {
        return foreignRegisterId;
    }

    public void setForeignRegisterId(String foreignRegisterId) {
        this.foreignRegisterId = foreignRegisterId;
    }

    public String getForeignCountryCode() {
        return foreignCountryCode;
    }

    public void setForeignCountryCode(String foreignCountryCode) {
        this.foreignCountryCode = foreignCountryCode;
    }
}
