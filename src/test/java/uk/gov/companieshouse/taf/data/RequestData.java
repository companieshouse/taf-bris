package uk.gov.companieshouse.taf.data;

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
public abstract class RequestData {

    @Value("${ltd.company.number}")
    private String companyNumber;

    private String messageId = UUID.randomUUID().toString();
    private String correlationId = messageId;
    private String businessRegisterId = BusinessRegisterConstants.EW_REGISTER_ID;
    private String countryCode = BusinessRegisterConstants.UK_COUNTRY_CODE;
    private String registerName = BusinessRegisterConstants.UK_REGISTER;
    private OutgoingBrisMessage outgoingBrisMessage;

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

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }
}
