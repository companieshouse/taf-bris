package uk.gov.companieshouse.taf.data;

import eu.europa.ec.bris.jaxb.br.crossborder.merger.notification.reception.request.v1_4.BRCrossBorderMergerReceptionNotification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;
import uk.gov.companieshouse.taf.domain.CrossBorderMerger;

@Component
@Qualifier("CrossBorderMergerNotification")
public class CrossBorderMergerNotificationData extends RequestData {

    private String issuingCountryCode = BusinessRegisterConstants.FRANCE_COUNTRY_CODE;
    private String issuingBusinessRegId = BusinessRegisterConstants.FR_BUSINESS_REGISTER_ID;
    private String issuingCompanyNumber = BusinessRegisterConstants.DUMMY_COMPANY_NUMBER;
    private String recipientBusinessRegisterName = BusinessRegisterConstants.UK_REGISTER;
    private String legalFormCode = BusinessRegisterConstants.PRIVATE_LIMITED_CODE;
    private BRCrossBorderMergerReceptionNotification brCrossBorderMergerReceptionNotification;
    private CrossBorderMerger crossBorderMergerJsonRequest;
    private String mergerType = "ACQUISITION";

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

    public String getRecipientBusinessRegisterName() {
        return recipientBusinessRegisterName;
    }

    public void setRecipientBusinessRegisterName(String recipientBusinessRegisterName) {
        this.recipientBusinessRegisterName = recipientBusinessRegisterName;
    }

    public String getLegalFormCode() {
        return legalFormCode;
    }

    public void setLegalFormCode(String legalFormCode) {
        this.legalFormCode = legalFormCode;
    }

    public void setCrossBorderMergerJsonRequest(CrossBorderMerger crossBorderMergerJsonRequest) {
        this.crossBorderMergerJsonRequest = crossBorderMergerJsonRequest;
    }

    public CrossBorderMerger getCrossBorderMergerJsonRequest() {
        return crossBorderMergerJsonRequest;
    }

    public String getMergerType() {
        return mergerType;
    }

    public void setMergerType(String mergerType) {
        this.mergerType = mergerType;
    }

}
