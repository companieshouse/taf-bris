package uk.gov.companieshouse.taf.util;

import static uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants.PRIVATE_LIMITED_CODE;

import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.AddressType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationCompanyType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationContextType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyEUIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import eu.europa.ec.bris.v140.jaxb.components.basic.DateTimeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.EffectiveDateType;
import eu.europa.ec.bris.v140.jaxb.components.basic.LegalFormCodeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.MergerType;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;
import uk.gov.companieshouse.taf.data.CrossBorderMergerNotificationData;

public class CrossBorderMergerNotificationRequestBuilder extends RequestBuilder {

    /**
     * Create new instance of BRCrossBorderMergerReceptionNotification.
     */
    public static BRCrossBorderMergerReceptionNotification getCrossBorderMergerNotification(
            CrossBorderMergerNotificationData data) {

        BRCrossBorderMergerReceptionNotification cbmNotification =
                new BRCrossBorderMergerReceptionNotification();

        // Set up the message header
        cbmNotification.setMessageHeader(getMessageHeader(
                data.getCorrelationId(), data.getMessageId(),
                data.getBusinessRegisterId(), data.getCountryCode()));

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
        recipientCountry.setValue(BusinessRegisterConstants.UK_COUNTRY_CODE);

        BusinessRegisterIDType recipientRegister = new BusinessRegisterIDType();
        recipientRegister.setValue(BusinessRegisterConstants.EW_REGISTER_ID);

        BusinessRegisterType recipientOrganisation = new BusinessRegisterType();
        recipientOrganisation.setBusinessRegisterCountry(recipientCountry);
        recipientOrganisation.setBusinessRegisterID(recipientRegister);
        BusinessRegisterNameType recipientBusinessRegisterNameType = new BusinessRegisterNameType();
        recipientBusinessRegisterNameType.setValue(BusinessRegisterConstants.UK_REGISTER);
        recipientOrganisation.setBusinessRegisterName(recipientBusinessRegisterNameType);

        // Create merging company details.  Note that we are deliberately using 99990000
        // here as that test data gets loaded for each test
        CompanyEUIDType mergingEuid = new CompanyEUIDType();
        mergingEuid.setValue(BusinessRegisterConstants.UK_COUNTRY_CODE
                + BusinessRegisterConstants.EW_REGISTER_ID + ".99990000");
        NotificationCompanyType mergingCompany = new NotificationCompanyType();
        mergingCompany.setCompanyEUID(mergingEuid);

        // Set the address for the merging company
        AddressType mergingCompanyAddressType = getAddress("Merging Company Address line 1",
                "Merging Company Address line 2",
                "Merging Company Address line 3",
                "Merging Company Post Code",
                "Merging Company Post City",
                BusinessRegisterConstants.UK_COUNTRY_CODE);

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
        AddressType resultingAddress = getAddress("Resulting Company Address line 1",
                "Resulting Company Address line 2",
                "Resulting Company Address line 3",
                "Resulting Company Post Code",
                "Resulting Company City",
                data.getIssuingCountryCode());

        CompanyNameType companyType = new CompanyNameType();
        companyType.setValue("Resulting Company");
        resultingCompany.setCompanyName(companyType);
        resultingCompany.setCompanyRegisteredOffice(resultingAddress);

        resultingCompany.setCompanyLegalForm(legalFormCodeType);

        // Set issuance date
        DateTimeType issuanceDate = new DateTimeType();
        issuanceDate.setValue(getXmlGregorianCalendarNow());
        notificationContextType.setIssuanceDateTime(issuanceDate);

        // Set effective date
        EffectiveDateType effectiveDateType = new EffectiveDateType();
        effectiveDateType.setValue(getXmlGregorianCalendarNow());
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