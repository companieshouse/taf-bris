package uk.gov.companieshouse.taf.util;

import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageHeaderType;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.AddressType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterReferenceType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationCompanyType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationContextType;
import eu.europa.ec.bris.v140.jaxb.components.basic.AddressLine1Type;
import eu.europa.ec.bris.v140.jaxb.components.basic.AddressLine2Type;
import eu.europa.ec.bris.v140.jaxb.components.basic.AddressLine3Type;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyEUIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyRegistrationNumberType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CorrelationIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import eu.europa.ec.bris.v140.jaxb.components.basic.DateTimeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.DocumentIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.EffectiveDateType;
import eu.europa.ec.bris.v140.jaxb.components.basic.LegalFormCodeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.MergerType;
import eu.europa.ec.bris.v140.jaxb.components.basic.MessageIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.PaymentReferenceType;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;
import uk.gov.companieshouse.taf.stepsdef.RequestData;

@Component
public class RequestHelper {

    private static final String PAYMENT_REF = "PR";

    /**
     * Create new instance of BRCompany Detail Request.
     */
    public static BRCompanyDetailsRequest getCompanyDetailsRequest(RequestData requestData) {

        BRCompanyDetailsRequest request = new BRCompanyDetailsRequest();
        request.setMessageHeader(getMessageHeader(requestData.getCorrelationId(),
                requestData.getMessageId(),
                requestData.getBusinessRegisterId(),
                requestData.getCountryCode()));
        request.setBusinessRegisterReference(businessRegReference(
                requestData.getCountryCode(),
                requestData.getBusinessRegisterId()));
        request.setCompanyRegistrationNumber(companyRegNumber(requestData.getCompanyNumber()));
        return request;
    }

    /**
     * Create new instance of BRRetrieveDocumentRequest.
     *
     * @param correlationId             the correlation id of the message header
     * @param messageId                 the message id of the message header
     * @param companyRegistrationNumber the company number
     * @param businessRegisterId        the business registration id e.g EW
     * @param countryCode               the business country code e.g. UK
     * @param documentId                the document to be requested id
     */
    public static BRRetrieveDocumentRequest getRetrieveDocumentRequest(
            String correlationId,
            String messageId,
            String companyRegistrationNumber,
            String businessRegisterId,
            String countryCode,
            String documentId) {

        BRRetrieveDocumentRequest request = new BRRetrieveDocumentRequest();
        request.setMessageHeader(getMessageHeader(correlationId, messageId,
                businessRegisterId, countryCode));
        request.setBusinessRegisterReference(businessRegReference(countryCode,
                businessRegisterId));

        PaymentReferenceType paymentReference = new PaymentReferenceType();
        paymentReference.setValue(PAYMENT_REF);
        request.setPaymentReference(paymentReference);
        request.setCompanyRegistrationNumber(companyRegNumber(companyRegistrationNumber));
        request.setDocumentID(documentIdType(documentId));
        return request;
    }

    /**
     * Create new instance of BRCrossBorderMergerReceptionNotification.
     * @param correlationId             the correlation id of the message header
     * @param messageId                 the message id of the message header
     * @param businessRegisterId        the business registration id e.g EW
     * @param countryCode               the business country code e.g. UK
     */
    public static BRCrossBorderMergerReceptionNotification getCrossBorderMergerNotification(
            String correlationId,
            String messageId,
            String businessRegisterId,
            String countryCode) {

        // Set up the message header
        BusinessRegisterReferenceType businessRegisterReferenceType =
                new BusinessRegisterReferenceType();
        BusinessRegisterIDType headerRegister = new BusinessRegisterIDType();
        headerRegister.setValue(BusinessRegisterConstants.EW_REGISTER_ID);
        CountryType headerCountry = new CountryType();
        headerCountry.setValue(BusinessRegisterConstants.UK_COUNTRY_CODE);
        businessRegisterReferenceType.setBusinessRegisterCountry(headerCountry);
        businessRegisterReferenceType.setBusinessRegisterID(headerRegister);

        MessageHeaderType header = getMessageHeader(correlationId, messageId,
                businessRegisterId, countryCode);
        header.setBusinessRegisterReference(businessRegisterReferenceType);

        BRCrossBorderMergerReceptionNotification cbmNotification =
                new BRCrossBorderMergerReceptionNotification();

        cbmNotification.setMessageHeader(header);

        //Create issuing organisation details
        CountryType issuingCountry = new CountryType();
        issuingCountry.setValue("FR");

        BusinessRegisterIDType issuingRegister = new BusinessRegisterIDType();
        issuingRegister.setValue("1104");

        BusinessRegisterType issuingOrganisation = new BusinessRegisterType();
        issuingOrganisation.setBusinessRegisterCountry(issuingCountry);
        issuingOrganisation.setBusinessRegisterID(issuingRegister);

        BusinessRegisterNameType issuingBusinessRegisterNameType = new BusinessRegisterNameType();
        issuingBusinessRegisterNameType.setValue("Issuing Company Name");
        issuingOrganisation.setBusinessRegisterName(issuingBusinessRegisterNameType);

        NotificationContextType notificationContextType = new NotificationContextType();
        notificationContextType.setIssuingOrganisation(issuingOrganisation);

        // Create recipient organisation details
        CountryType recipientCountry = new CountryType();
        recipientCountry.setValue(BusinessRegisterConstants.UK_COUNTRY_CODE);

        BusinessRegisterIDType recipientRegister = new BusinessRegisterIDType();
        recipientRegister.setValue(BusinessRegisterConstants.EW_REGISTER_ID);

        BusinessRegisterType recipientOrganisation = new BusinessRegisterType();
        recipientOrganisation.setBusinessRegisterCountry(recipientCountry);
        recipientOrganisation.setBusinessRegisterID(recipientRegister);
        BusinessRegisterNameType recipientBusinessRegisterNameType = new BusinessRegisterNameType();
        recipientBusinessRegisterNameType.setValue("Companies House");
        recipientOrganisation.setBusinessRegisterName(recipientBusinessRegisterNameType);

        // Create merging company details.  Note that we are deliberately using 99990000
        // here as that test date gets loaded for each test
        CompanyEUIDType mergingEuid = new CompanyEUIDType();
        mergingEuid.setValue("UKEW.99990000");
        NotificationCompanyType mergingCompany = new NotificationCompanyType();
        mergingCompany.setCompanyEUID(mergingEuid);

        // Set the address for the merging company
        AddressType mergingCompanyaddressType = new AddressType();
        AddressLine1Type mergingCompanyAddressLine1Type = new AddressLine1Type();
        mergingCompanyAddressLine1Type.setValue("Merging Company Address line 1");
        mergingCompanyaddressType.setAddressLine1(mergingCompanyAddressLine1Type);

        AddressLine2Type mergingCompanyAddressLine2Type = new AddressLine2Type();
        mergingCompanyAddressLine2Type.setValue("Merging Company Address line 2");
        mergingCompanyaddressType.setAddressLine2(mergingCompanyAddressLine2Type);

        AddressLine3Type mergingCompanyAddressLine3Type = new AddressLine3Type();
        mergingCompanyAddressLine3Type.setValue("Merging Company Address line 3");
        mergingCompanyaddressType.setAddressLine3(mergingCompanyAddressLine3Type);

        CountryType mergingCountry = new CountryType();
        mergingCountry.setValue(BusinessRegisterConstants.UK_COUNTRY_CODE);

        mergingCompanyaddressType.setCountry(mergingCountry);

        mergingCompany.setCompanyRegisteredOffice(mergingCompanyaddressType);

        BusinessRegisterNameType mergingCompanyBusinessRegisterName =
                new BusinessRegisterNameType();
        mergingCompanyBusinessRegisterName.setValue("Merging Company Name");
        mergingCompany.setBusinessRegisterName(mergingCompanyBusinessRegisterName);

        LegalFormCodeType legalFormCodeType = new LegalFormCodeType();
        legalFormCodeType.setValue("LF_UK_001");
        mergingCompany.setCompanyLegalForm(legalFormCodeType);

        CompanyNameType mergingCompanyName = new CompanyNameType();
        mergingCompanyName.setValue("Merging Company");
        mergingCompany.setCompanyName(mergingCompanyName);

        //Create resulting company details
        CompanyEUIDType resultingEuid = new CompanyEUIDType();
        resultingEuid.setValue("FR1104.123456");

        NotificationCompanyType resultingCompany = new NotificationCompanyType();
        resultingCompany.setCompanyEUID(resultingEuid);

        BusinessRegisterNameType mergingCompanyBusinessRegisterNameType =
                new BusinessRegisterNameType();
        mergingCompanyBusinessRegisterNameType.setValue("Resulting Company");
        resultingCompany.setBusinessRegisterName(mergingCompanyBusinessRegisterNameType);

        // Set the address for the resulting company
        AddressType resultingAddress = new AddressType();
        AddressLine1Type resultingCompanyAddressLine1Type = new AddressLine1Type();
        resultingCompanyAddressLine1Type.setValue("Resulting Company Address line 1");
        resultingAddress.setAddressLine1(resultingCompanyAddressLine1Type);

        AddressLine2Type resultingCompanyAddressLine2Type = new AddressLine2Type();
        resultingCompanyAddressLine2Type.setValue("Resulting Company Address line 2");
        resultingAddress.setAddressLine2(resultingCompanyAddressLine2Type);

        AddressLine3Type resultingCompanyAddressLine3Type = new AddressLine3Type();
        resultingCompanyAddressLine3Type.setValue("Resulting Company Address line 3");
        resultingAddress.setAddressLine3(resultingCompanyAddressLine3Type);

        CountryType resultingCompanyCountry = new CountryType();
        resultingCompanyCountry.setValue("FR");
        resultingAddress.setCountry(resultingCompanyCountry);

        CompanyNameType companyType = new CompanyNameType();
        companyType.setValue("Resulting Company");
        resultingCompany.setCompanyName(companyType);
        resultingCompany.setCompanyRegisteredOffice(resultingAddress);

        resultingCompany.setCompanyLegalForm(legalFormCodeType);

        // Set fields on Notification
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException("Unable to create a data type "
                    + "factory to set the Issuance date/time ");
        }

        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);

        // Set issuance date
        DateTimeType issuanceDate = new DateTimeType();
        issuanceDate.setValue(now);
        notificationContextType.setIssuanceDateTime(issuanceDate);

        // Set effective date
        EffectiveDateType effectiveDateType = new EffectiveDateType();
        effectiveDateType.setValue(now);
        notificationContextType.setEffectiveDate(effectiveDateType);

        //Assign values to notification
        cbmNotification.setResultingCompany(resultingCompany);
        cbmNotification.setNotificationContext(notificationContextType);
        cbmNotification.setRecipientOrganisation(recipientOrganisation);
        cbmNotification.getMergingCompany().add(mergingCompany);

        // Set up the Merger
        MergerType merger = new MergerType();
        merger.setName("Merger Name");
        merger.setValue("ACQUISITION");
        cbmNotification.setMerger(merger);

        return cbmNotification;
    }

    /*
        Populate the message header for the request.
     */
    private static MessageHeaderType getMessageHeader(String correlationId,
                                                      String messageId,
                                                      String businessRegisterId,
                                                      String countryCode) {
        MessageHeaderType messageHeaderType = new MessageHeaderType();
        CorrelationIDType correlationIdType = new CorrelationIDType();
        correlationIdType.setValue(correlationId);
        messageHeaderType.setCorrelationID(correlationIdType);
        MessageIDType messageIdType = new MessageIDType();
        messageIdType.setValue(messageId);
        messageHeaderType.setMessageID(messageIdType);

        // BusinessRegisterType
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue("Companies House");

        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();

        //BusinessRegisterID
        businessRegisterIdType.setValue(businessRegisterId);

        //BusinessRegisterCountry Country
        CountryType countryType = new CountryType();
        countryType.setValue(countryCode);

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

    private static DocumentIDType documentIdType(String documentId) {
        DocumentIDType documentIdType = new DocumentIDType();
        documentIdType.setValue(documentId);
        return documentIdType;
    }
}
