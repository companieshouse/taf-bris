package uk.gov.companieshouse.taf.data;


import eu.europa.ec.bris.jaxb.br.company.details.response.v1_4.BRCompanyDetailsResponse;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import org.springframework.stereotype.Component;

@Component
public class CompanyDetailsRequestData extends RequestData {

    private MessageContainer companyDetailsResponse;

    public MessageContainer getCompanyDetailsResponse() {
        return companyDetailsResponse;
    }

    public void setCompanyDetailsResponse(MessageContainer companyDetailsResponse) {
        this.companyDetailsResponse = companyDetailsResponse;
    }

}
