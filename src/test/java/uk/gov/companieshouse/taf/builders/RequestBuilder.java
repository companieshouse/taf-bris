package uk.gov.companieshouse.taf.builders;



import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.europa.ec.bris.jaxb.br.components.aggregate.v1_4.MessageHeaderType;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.AddressType;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.BusinessRegisterReferenceType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.AddressLine1Type;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.AddressLine2Type;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.AddressLine3Type;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.BusinessRegisterIDType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.BusinessRegisterNameType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.CityType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.CompanyRegistrationNumberType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.CorrelationIDType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.CountryType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.MessageIDType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.PostalCodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.data.RequestData;

@Component
public class RequestBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBuilder.class);

    /*
        Populate the message header for the request.
     */
    static MessageHeaderType getMessageHeader(RequestData data) {
        MessageHeaderType messageHeaderType = new MessageHeaderType();
        CorrelationIDType correlationIdType = new CorrelationIDType();
        correlationIdType.setValue(data.getCorrelationId());
        messageHeaderType.setCorrelationID(correlationIdType);
        MessageIDType messageIdType = new MessageIDType();
        messageIdType.setValue(data.getMessageId());
        messageHeaderType.setMessageID(messageIdType);

        // BusinessRegisterType
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue(data.getRegisterName());

        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();

        //BusinessRegisterID
        businessRegisterIdType.setValue(data.getBusinessRegisterId());

        //BusinessRegisterCountry Country
        CountryType countryType = new CountryType();
        countryType.setValue(data.getCountryCode());

        BusinessRegisterReferenceType businessRegisterReferenceType
                = new BusinessRegisterReferenceType();

        //set BusinessRegisterID
        businessRegisterReferenceType.setBusinessRegisterID(businessRegisterIdType);

        // set BusinessRegisterCountry
        businessRegisterReferenceType.setBusinessRegisterCountry(countryType);

        // set BusinessRegisterReference to CompanyDetailsResponse
        messageHeaderType.setBusinessRegisterReference(businessRegisterReferenceType);
        return messageHeaderType;
    }

    static XMLGregorianCalendar getXmlGregorianCalendarNow() {
        XMLGregorianCalendar now = null;
        try {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

            LOGGER.info("XML Gregorian Calendar instance " + now);
        } catch (DatatypeConfigurationException exception) {
            LOGGER.error("unable to create new XML Gregorian Calendar instance",
                    "Datatype Configuration Exception",
                    exception);
        }

        return now;
    }

    static CompanyRegistrationNumberType companyRegNumber(String companyRegNumber) {
        CompanyRegistrationNumberType companyRegistrationNumber =
                new CompanyRegistrationNumberType();
        companyRegistrationNumber.setValue(companyRegNumber);
        return companyRegistrationNumber;
    }

    static BusinessRegisterReferenceType businessRegReference(String countryCode,
                                                              String businessRegisterId) {
        BusinessRegisterReferenceType businessRegisterReference =
                new BusinessRegisterReferenceType();
        businessRegisterReference.setBusinessRegisterCountry(getCountry(countryCode));
        businessRegisterReference.setBusinessRegisterID(businessRegisterId(businessRegisterId));
        return businessRegisterReference;
    }

    static CountryType getCountry(String countryCode) {
        CountryType country = new CountryType();
        country.setValue(countryCode);
        return country;
    }

    private static BusinessRegisterIDType businessRegisterId(String identifier) {
        BusinessRegisterIDType businessRegisterId = new BusinessRegisterIDType();
        businessRegisterId.setValue(identifier);
        return businessRegisterId;
    }

    static AddressType getAddress(String addressLine1,
                                  String addressLine2,
                                  String addressLine3,
                                  String postCode,
                                  String cityCode,
                                  String countryCode) {

        AddressType address = new AddressType();
        AddressLine1Type addressLine1Type = new AddressLine1Type();
        addressLine1Type.setValue(addressLine1);
        address.setAddressLine1(addressLine1Type);

        AddressLine2Type addressLine2Type = new AddressLine2Type();
        addressLine2Type.setValue(addressLine2);
        address.setAddressLine2(addressLine2Type);

        AddressLine3Type addressLine3Type = new AddressLine3Type();
        addressLine3Type.setValue(addressLine3);
        address.setAddressLine3(addressLine3Type);

        PostalCodeType postalCode = new PostalCodeType();
        postalCode.setValue(postCode);
        address.setPostalCode(postalCode);

        CityType city = new CityType();
        city.setValue(cityCode);
        address.setCity(city);

        CountryType countryType = new CountryType();
        countryType.setValue(countryCode);
        address.setCountry(countryType);

        return address;
    }

}