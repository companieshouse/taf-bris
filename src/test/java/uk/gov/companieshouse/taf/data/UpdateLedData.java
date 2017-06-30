package uk.gov.companieshouse.taf.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.domain.UpdateLED;

@Component
@Qualifier("UpdateLedData")
public class UpdateLedData extends RequestData {

    public UpdateLED getUpdateLedJsonRequest() {
        return updateLedJsonRequest;
    }

    public void setUpdateLedJsonRequest(UpdateLED updateLedJsonRequest) {
        this.updateLedJsonRequest = updateLedJsonRequest;
    }

    private UpdateLED updateLedJsonRequest;
}
