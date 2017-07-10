package uk.gov.companieshouse.taf.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.domain.UpdateLed;

@Component
@Qualifier("UpdateLedData")
public class UpdateLedData extends RequestData {

    private UpdateLed updateLedJsonRequest;

    public UpdateLed getUpdateLedJsonRequest() {
        return updateLedJsonRequest;
    }

    public void setUpdateLedJsonRequest(UpdateLed updateLedJsonRequest) {
        this.updateLedJsonRequest = updateLedJsonRequest;
    }
}
