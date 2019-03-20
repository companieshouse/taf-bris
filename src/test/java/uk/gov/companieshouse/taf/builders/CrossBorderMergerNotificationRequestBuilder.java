package uk.gov.companieshouse.taf.builders;


import eu.europa.ec.bris.jaxb.br.crossborder.merger.notification.reception.request.v1_4.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.AddressType;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.BusinessRegisterType;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.NotificationCompanyType;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.NotificationContextType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.*;
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
        cbmNotification.setMessageHeader(getMessageHeader(data));

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
        recipientCountry.setValue(data.getCountryCode());

        BusinessRegisterIDType recipientRegister = new BusinessRegisterIDType();
        recipientRegister.setValue(data.getBusinessRegisterId());

        BusinessRegisterType recipientOrganisation = new BusinessRegisterType();
        recipientOrganisation.setBusinessRegisterCountry(recipientCountry);
        recipientOrganisation.setBusinessRegisterID(recipientRegister);
        BusinessRegisterNameType recipientBusinessRegisterNameType = new BusinessRegisterNameType();
        recipientBusinessRegisterNameType.setValue(data.getRecipientBusinessRegisterName());
        recipientOrganisation.setBusinessRegisterName(recipientBusinessRegisterNameType);

        // Create merging company details.  Note that we are deliberately using 99990000
        // here as that test data gets loaded for each test
        CompanyEUIDType mergingEuid = new CompanyEUIDType();
        mergingEuid.setValue(data.getCountryCode() + data.getBusinessRegisterId() + "."
                + data.getCompanyNumber());
        NotificationCompanyType mergingCompany = new NotificationCompanyType();
        mergingCompany.setCompanyEUID(mergingEuid);

        // Set the address for the merging company
        AddressType mergingCompanyAddressType = getAddress("Merging Company Address line 2",
                "Merging Company Address line 2",
                "Merging Company Address line 3",
                BusinessRegisterConstants.TEST_POST_CODE,
                "Merging Company Post City",
                BusinessRegisterConstants.UK_COUNTRY_CODE);

        mergingCompany.setCompanyRegisteredOffice(mergingCompanyAddressType);

        BusinessRegisterNameType mergingCompanyBusinessRegisterName =
                new BusinessRegisterNameType();
        mergingCompanyBusinessRegisterName.setValue("Merging Company Name");
        mergingCompany.setBusinessRegisterName(mergingCompanyBusinessRegisterName);

        LegalFormCodeType legalFormCodeType = new LegalFormCodeType();
        legalFormCodeType.setValue(data.getLegalFormCode());
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
                BusinessRegisterConstants.TEST_POST_CODE,
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