package uk.gov.companieshouse.taf.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;

@Component
@Qualifier("BranchDisclosureReception")
public class BranchDisclosureReceptionData extends RequestData {

    private String issuingCountryCode = BusinessRegisterConstants.FRANCE_COUNTRY_CODE;
    private String issuingBusinessRegId = BusinessRegisterConstants.FR_BUSINESS_REGISTER_ID;
    private String issuingRegister = BusinessRegisterConstants.FRANCE_REGISTER;
    private String proceedingType = "WINDING_UP_OPENING";
    private String issuingCompanyNumber = BusinessRegisterConstants.DUMMY_COMPANY_NUMBER;
    private String legalFormCode = BusinessRegisterConstants.LEGAL_FORM_CODE;

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

    public String getIssuingRegister() {
        return issuingRegister;
    }

    public void setIssuingRegister(String issuingRegister) {
        this.issuingRegister = issuingRegister;
    }

    public String getProceedingType() {
        return proceedingType;
    }

    public void setProceedingType(String proceedingType) {
        this.proceedingType = proceedingType;
    }

    public String getIssuingCompanyNumber() {
        return issuingCompanyNumber;
    }

    public void setIssuingCompanyNumber(String issuingCompanyNumber) {
        this.issuingCompanyNumber = issuingCompanyNumber;
    }

    public String getLegalFormCode() {
        return legalFormCode;
    }
}
