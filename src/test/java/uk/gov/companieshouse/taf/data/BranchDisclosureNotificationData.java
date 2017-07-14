package uk.gov.companieshouse.taf.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.domain.BranchDisclosureNotification;

@Component
@Qualifier("BranchDisclosureNotification")
public class BranchDisclosureNotificationData extends RequestData {

    private String proceedingType = "WINDING_UP_OPENING";
    private BranchDisclosureNotification branchDisclosureNotification;

    public String getProceedingType() {
        return proceedingType;
    }

    public void setProceedingType(String proceedingType) {
        this.proceedingType = proceedingType;
    }

    public void setBranchDisclosureNotification(BranchDisclosureNotification
                                                        branchDisclosureNotification) {
        this.branchDisclosureNotification = branchDisclosureNotification;
    }

    public BranchDisclosureNotification getBranchDisclosureNotification() {
        return branchDisclosureNotification;
    }
}
