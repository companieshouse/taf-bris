package uk.gov.companieshouse.taf.builders;

import java.util.Date;

import uk.gov.companieshouse.taf.data.BranchDisclosureNotificationData;
import uk.gov.companieshouse.taf.domain.BranchDisclosureNotification;

public class BranchDisclosureNotificationBuilder {

    /**
     * Create default Cross Border Merger Notification Submission.
     */
    public static BranchDisclosureNotification getBranchDisclosureNotification(
            BranchDisclosureNotificationData data) {
        BranchDisclosureNotification branchDisclosureNotification =
                new BranchDisclosureNotification();
        branchDisclosureNotification.setProceedingType(data.getProceedingType());
        branchDisclosureNotification.setEffectiveDate(new Date());
        return branchDisclosureNotification;
    }
}
