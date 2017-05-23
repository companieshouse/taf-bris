package uk.gov.companieshouse.taf.stepsdef;

import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageHeaderType;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;
import eu.europa.ec.bris.v140.jaxb.br.error.BRBusinessError;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterReferenceType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyRegistrationNumberType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CorrelationIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import eu.europa.ec.bris.v140.jaxb.components.basic.MessageIDType;


public class CompanyDetailsHelper {

    /* ---- Constants ---- */

    /* ---- Instance Variables ---- */

    /* ---- Constructors ---- */

    /* ---- Business Methods ---- */

    /**
     * Create new instance of BRCompany Detail Request.
     */
    public static BRCompanyDetailsRequest newInstance(
            String correlationId,
            String messageId,
            String companyRegistrationNumber,
            String businessRegisterId,
            String countryCode) {

        BRCompanyDetailsRequest request = new BRCompanyDetailsRequest();
        request.setMessageHeader(getMessageHeader(correlationId, messageId));
        request.setBusinessRegisterReference(businessRegReference(countryCode,
                businessRegisterId));
        request.setCompanyRegistrationNumber(companyRegNumber(companyRegistrationNumber));
        return request;
    }

    /**
     * Create new instance of Business Error.
     */
    public static BRBusinessError newInstance(
            String correlationId,
            String messageId) {

        BRBusinessError request = new BRBusinessError();
        request.setMessageHeader(getMessageHeader(correlationId, messageId));
        return request;
    }

    private static MessageHeaderType getMessageHeader(String correlationId, String messageId) {
        MessageHeaderType messageHeaderType = new MessageHeaderType();
        CorrelationIDType correlationIdType = new CorrelationIDType();
        correlationIdType.setValue(correlationId);
        messageHeaderType.setCorrelationID(correlationIdType);
        MessageIDType messageIdType = new MessageIDType();
        messageIdType.setValue(messageId);
        messageHeaderType.setMessageID(messageIdType);

        //***** START --BusinessRegisterReference *******************//
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue("Companies House");

        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();

        //BusinessRegisterID
        businessRegisterIdType.setValue("EW");

        //BusinessRegisterCountry Country
        CountryType countryType = new CountryType();
        countryType.setValue("UK");

        BusinessRegisterReferenceType businessRegisterReferenceType
                = new BusinessRegisterReferenceType();

        //set BusinessRegisterID
        businessRegisterReferenceType.setBusinessRegisterID(businessRegisterIdType);

        // set BusinessRegisterCountry
        businessRegisterReferenceType.setBusinessRegisterCountry(countryType);
        // TODO BusinessRegisterName??

        // set BusinessRegisterReference to CompanyDetailsResponse
        messageHeaderType.setBusinessRegisterReference(businessRegisterReferenceType);
        return messageHeaderType;
    }

    private static BusinessRegisterReferenceType businessRegReference(String countryCode,
                                                                      String businessRegisterId) {
        BusinessRegisterReferenceType businessRegisterReference =
                new BusinessRegisterReferenceType();
        businessRegisterReference.setBusinessRegisterCountry(country(countryCode));
        businessRegisterReference.setBusinessRegisterID(businessRegisterId(businessRegisterId));
        return businessRegisterReference;
    }

    private static CompanyRegistrationNumberType companyRegNumber(String companyRegNumber) {
        CompanyRegistrationNumberType companyRegistrationNumber =
                new CompanyRegistrationNumberType();
        companyRegistrationNumber.setValue(companyRegNumber);
        return companyRegistrationNumber;
    }

    private static CountryType country(String countryCode) {
        CountryType country = new CountryType();
        country.setValue(countryCode);
        return country;
    }

    private static BusinessRegisterIDType businessRegisterId(String identifier) {
        BusinessRegisterIDType businessRegisterId = new BusinessRegisterIDType();
        businessRegisterId.setValue(identifier);
        return businessRegisterId;
    }

    /* ---- Getters and Setters ---- */
}
