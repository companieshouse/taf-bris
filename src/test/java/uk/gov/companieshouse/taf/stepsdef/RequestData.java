package uk.gov.companieshouse.taf.stepsdef;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;

/**
 * Allows data to be shared across Steps in different classes as this will be autowired
 * as a singleton and recreated after each scenario is executed.  Set defaults
 * that will be used across the majority of tests.
 */

@Component
public class RequestData {

    @Value("${default.company.number}")
    private String companyNumber;

    private String messageId = UUID.randomUUID().toString();
    private String correlationId = messageId;
    private String businessRegisterId = BusinessRegisterConstants.EW_REGISTER_ID;
    private String countryCode = BusinessRegisterConstants.UK_COUNTRY_CODE;
    private String issuingCountryCode = BusinessRegisterConstants.FRANCE_COUNTRY_CODE;
    private String issuingBusinessRegId = BusinessRegisterConstants.FR_BUSINESS_REGISTER_ID;
    private String issuingCompanyNumber = BusinessRegisterConstants.DUMMY_COMPANY_NUMBER;
    private BRCompanyDetailsResponse companyDetailsResponse;
    private OutgoingBrisMessage outgoingBrisMessage;
    private BRCrossBorderMergerReceptionNotification brCrossBorderMergerReceptionNotification;

    public String getMessageId() {
        return messageId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public OutgoingBrisMessage getOutgoingBrisMessage() {
        return outgoingBrisMessage;
    }

    public void setOutgoingBrisMessage(OutgoingBrisMessage outgoingBrisMessage) {
        this.outgoingBrisMessage = outgoingBrisMessage;
    }

    public BRCompanyDetailsResponse getCompanyDetailsResponse() {
        return companyDetailsResponse;
    }

    public void setCompanyDetailsResponse(BRCompanyDetailsResponse companyDetailsResponse) {
        this.companyDetailsResponse = companyDetailsResponse;
    }

    public String setMessageId(String messageId) {
        return this.messageId = messageId;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getBusinessRegisterId() {
        return businessRegisterId;
    }

    public void setBusinessRegisterId(String businessRegisterId) {
        this.businessRegisterId = businessRegisterId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public BRCrossBorderMergerReceptionNotification getBrCrossBorderMergerReceptionNotification() {
        return brCrossBorderMergerReceptionNotification;
    }

    public void setBrCrossBorderMergerReceptionNotification(
            BRCrossBorderMergerReceptionNotification brCrossBorderMergerReceptionNotification) {
        this.brCrossBorderMergerReceptionNotification = brCrossBorderMergerReceptionNotification;
    }

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
}
