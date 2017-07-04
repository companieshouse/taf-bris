package uk.gov.companieshouse.taf.builders;

import uk.gov.companieshouse.taf.domain.UpdateLed;

/**
 * Used to build a request object for an Update LED request.
 */
public class UpdateLedNotificationBuilder {

    /**
     * Create an LED update object.
     * @return an Update LED
     */
    public static UpdateLed createDefaultUpdateLed(String updateType) {
        UpdateLed updateLed = new UpdateLed();
        updateLed.setUpdateType(updateType);
        return updateLed;
    }
}
