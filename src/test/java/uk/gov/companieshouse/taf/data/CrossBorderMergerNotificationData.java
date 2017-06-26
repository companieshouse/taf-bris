package uk.gov.companieshouse.taf.data;

import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;

public class CrossBorderMergerNotificationData extends RequestData {

    private String issuingCountryCode = BusinessRegisterConstants.FRANCE_COUNTRY_CODE;
    private String issuingBusinessRegId = BusinessRegisterConstants.FR_BUSINESS_REGISTER_ID;
    private String issuingCompanyNumber = BusinessRegisterConstants.DUMMY_COMPANY_NUMBER;
    private BRCrossBorderMergerReceptionNotification brCrossBorderMergerReceptionNotification;



    public String getIssuingCountryCode() {
        return issuingCountryCode;
    }

    public void setIssuingCountryCode(String issuingCountryCode) {
        this.issuingCountryCode = issuingCountryCode;
    }

    public String getIssuingBusinessRegId() {
        return issuingBusinessRegId;
    }

    public void setIssuingBusinessRegId(String issuingBusinessRegId) {
        this.issuingBusinessRegId = issuingBusinessRegId;
    }

    public String getIssuingCompanyNumber() {
        return issuingCompanyNumber;
    }

    public void setIssuingCompanyNumber(String issuingCompanyNumber) {
        this.issuingCompanyNumber = issuingCompanyNumber;
    }


    public BRCrossBorderMergerReceptionNotification getBrCrossBorderMergerReceptionNotification() {
        return brCrossBorderMergerReceptionNotification;
    }

    public void setBrCrossBorderMergerReceptionNotification(
            BRCrossBorderMergerReceptionNotification brCrossBorderMergerReceptionNotification) {
        this.brCrossBorderMergerReceptionNotification = brCrossBorderMergerReceptionNotification;
    }
}
