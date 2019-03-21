package uk.gov.companieshouse.taf.builders;


import eu.europa.ec.bris.jaxb.br.company.details.request.v1_4.BRCompanyDetailsRequest;
import uk.gov.companieshouse.taf.data.RequestData;

/**
 * Used to build a request object for Company Details with default values.
 */
public class CompanyDetailsRequestBuilder extends RequestBuilder {
    /**
     * Create new instance of BRCompany Detail Request.
     */
    public static BRCompanyDetailsRequest getCompanyDetailsRequest(RequestData requestData) {

        BRCompanyDetailsRequest request = new BRCompanyDetailsRequest();
        request.setMessageHeader(getMessageHeader(requestData));
        request.setBusinessRegisterReference(businessRegReference(
                requestData.getCountryCode(),
                requestData.getBusinessRegisterId()));
        request.setCompanyRegistrationNumber(companyRegNumber(requestData.getCompanyNumber()));
        return request;
    }
}