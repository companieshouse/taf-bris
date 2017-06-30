package uk.gov.companieshouse.taf.builders;

import uk.gov.companieshouse.taf.domain.UpdateLED;

/**
 * Used to build a request object for an Update LED request.
 */
public class UpdateLedRequestBuilder {
    public static UpdateLED createDefaultUpdateLed() {
        UpdateLED updateLED = new UpdateLED();
        updateLED.setUpdateType("UPDATE");
        return updateLED;
    }
}
