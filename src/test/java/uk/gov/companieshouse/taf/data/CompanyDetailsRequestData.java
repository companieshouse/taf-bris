package uk.gov.companieshouse.taf.data;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;

public class CompanyDetailsRequestData extends RequestData {

    private BRCompanyDetailsResponse companyDetailsResponse;

    public BRCompanyDetailsResponse getCompanyDetailsResponse() {
        return companyDetailsResponse;
    }

    public void setCompanyDetailsResponse(BRCompanyDetailsResponse companyDetailsResponse) {
        this.companyDetailsResponse = companyDetailsResponse;
    }

}
