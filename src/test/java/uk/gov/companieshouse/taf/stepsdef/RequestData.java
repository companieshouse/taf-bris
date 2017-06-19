package uk.gov.companieshouse.taf.stepsdef;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * Allows data to be shared across Steps in different classes as this will be autowired
 * as a singleton and recreated after each scenario is executed.
 */

@Component
public class RequestData {

    private String messageId = UUID.randomUUID().toString();
    private String correlationId = messageId;
    private BRCompanyDetailsResponse companyDetailsResponse;

    public String getMessageId() {
        return messageId;
    }

    public String getCorrelationId() {
        return correlationId;
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
}
