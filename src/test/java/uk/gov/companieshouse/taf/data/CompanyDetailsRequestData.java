package uk.gov.companieshouse.taf.data;


import eu.europa.ec.bris.jaxb.br.company.details.response.v1_4.BRCompanyDetailsResponse;
import org.springframework.stereotype.Component;

@Component
public class CompanyDetailsRequestData extends RequestData {

    private BRCompanyDetailsResponse companyDetailsResponse;

    public BRCompanyDetailsResponse getCompanyDetailsResponse() {
        return companyDetailsResponse;
    }

    public void setCompanyDetailsResponse(BRCompanyDetailsResponse companyDetailsResponse) {
        this.companyDetailsResponse = companyDetailsResponse;
    }

}
