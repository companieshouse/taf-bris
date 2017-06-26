package uk.gov.companieshouse.taf.util;

import static uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants.EW_REGISTER_ID;
import static uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants.PRIVATE_LIMITED_CODE;
import static uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants.UK_COUNTRY_CODE;
import static uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants.UK_REGISTER;

import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageHeaderType;
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
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import eu.europa.ec.bris.v140.jaxb.components.basic.DateTimeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.EffectiveDateType;
import eu.europa.ec.bris.v140.jaxb.components.basic.LegalFormCodeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.MergerType;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import uk.gov.companieshouse.taf.stepsdef.RequestData;

public class CrossBorderMergerNotificationHelper {

    /**
     * Create new instance of BRCrossBorderMergerReceptionNotification.
     */
    public static BRCrossBorderMergerReceptionNotification getCrossBorderMergerNotification(
            RequestData data) {

        // Set up the message header
        BusinessRegisterReferenceType businessRegisterReferenceType =
                new BusinessRegisterReferenceType();
        BusinessRegisterIDType headerRegister = new BusinessRegisterIDType();
        headerRegister.setValue(EW_REGISTER_ID);
        CountryType headerCountry = new CountryType();
        headerCountry.setValue(UK_COUNTRY_CODE);
        businessRegisterReferenceType.setBusinessRegisterCountry(headerCountry);
        businessRegisterReferenceType.setBusinessRegisterID(headerRegister);

        MessageHeaderType header = RequestHelper.getMessageHeader(data.getCorrelationId(),
                data.getMessageId(), data.getBusinessRegisterId(), data.getCountryCode());
        header.setBusinessRegisterReference(businessRegisterReferenceType);

        BRCrossBorderMergerReceptionNotification cbmNotification =
                new BRCrossBorderMergerReceptionNotification();

        cbmNotification.setMessageHeader(header);

        //Create issuing organisation details
        CountryType issuingCountry = new CountryType();
        issuingCountry.setValue(data.getIssuingCountryCode());

        BusinessRegisterIDType issuingRegister = new BusinessRegisterIDType();
        issuingRegister.setValue(data.getIssuingBusinessRegId());

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
        recipientCountry.setValue(UK_COUNTRY_CODE);

        BusinessRegisterIDType recipientRegister = new BusinessRegisterIDType();
        recipientRegister.setValue(EW_REGISTER_ID);

        BusinessRegisterType recipientOrganisation = new BusinessRegisterType();
        recipientOrganisation.setBusinessRegisterCountry(recipientCountry);
        recipientOrganisation.setBusinessRegisterID(recipientRegister);
        BusinessRegisterNameType recipientBusinessRegisterNameType = new BusinessRegisterNameType();
        recipientBusinessRegisterNameType.setValue(UK_REGISTER);
        recipientOrganisation.setBusinessRegisterName(recipientBusinessRegisterNameType);

        // Create merging company details.  Note that we are deliberately using 99990000
        // here as that test data gets loaded for each test
        CompanyEUIDType mergingEuid = new CompanyEUIDType();
        mergingEuid.setValue(UK_COUNTRY_CODE + EW_REGISTER_ID + ".99990000");
        NotificationCompanyType mergingCompany = new NotificationCompanyType();
        mergingCompany.setCompanyEUID(mergingEuid);

        // Set the address for the merging company
        AddressType mergingCompanyAddressType = new AddressType();
        AddressLine1Type mergingCompanyAddressLine1Type = new AddressLine1Type();
        mergingCompanyAddressLine1Type.setValue("Merging Company Address line 1");
        mergingCompanyAddressType.setAddressLine1(mergingCompanyAddressLine1Type);

        AddressLine2Type mergingCompanyAddressLine2Type = new AddressLine2Type();
        mergingCompanyAddressLine2Type.setValue("Merging Company Address line 2");
        mergingCompanyAddressType.setAddressLine2(mergingCompanyAddressLine2Type);

        AddressLine3Type mergingCompanyAddressLine3Type = new AddressLine3Type();
        mergingCompanyAddressLine3Type.setValue("Merging Company Address line 3");
        mergingCompanyAddressType.setAddressLine3(mergingCompanyAddressLine3Type);

        CountryType mergingCountry = new CountryType();
        mergingCountry.setValue(UK_COUNTRY_CODE);

        mergingCompanyAddressType.setCountry(mergingCountry);

        mergingCompany.setCompanyRegisteredOffice(mergingCompanyAddressType);

        BusinessRegisterNameType mergingCompanyBusinessRegisterName =
                new BusinessRegisterNameType();
        mergingCompanyBusinessRegisterName.setValue("Merging Company Name");
        mergingCompany.setBusinessRegisterName(mergingCompanyBusinessRegisterName);

        LegalFormCodeType legalFormCodeType = new LegalFormCodeType();
        legalFormCodeType.setValue(PRIVATE_LIMITED_CODE);
        mergingCompany.setCompanyLegalForm(legalFormCodeType);

        CompanyNameType mergingCompanyName = new CompanyNameType();
        mergingCompanyName.setValue("Merging Company");
        mergingCompany.setCompanyName(mergingCompanyName);

        //Create resulting company details
        CompanyEUIDType resultingEuid = new CompanyEUIDType();
        resultingEuid.setValue(String.format("%s%s.%s", data.getIssuingCountryCode(),
                data.getIssuingBusinessRegId(), data.getCompanyNumber()));

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
        resultingCompanyCountry.setValue(data.getIssuingCountryCode());
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

}
