package uk.gov.companieshouse.taf.builders;


import eu.europa.ec.bris.jaxb.br.branch.disclosure.notification.reception.request.v1_4.BRBranchDisclosureReceptionNotification;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.*;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.*;
import uk.gov.companieshouse.taf.data.BranchDisclosureReceptionData;

/**
 * Used to build a request object for Branch Disclosures with default values.
 */
public class BranchDisclosureRequestBuilder extends RequestBuilder {

    /**
     * Create new instance of BRBranchDisclosureReceptionNotification.
     */
    public static BRBranchDisclosureReceptionNotification getBranchDisclosureReceptionNotification(
            BranchDisclosureReceptionData data) {

        BRBranchDisclosureReceptionNotification notification = new
                BRBranchDisclosureReceptionNotification();

        notification.setMessageHeader(getMessageHeader(data));

        notification.setNotificationContext(setNotificationContextType(data));
        notification.setProceeding(setProceedingType(data));
        notification.setDisclosureCompany(setNotificationCompanyType(data));
        notification.setRecipientOrganisation(setBusinessRegisterType(data));
        notification.setBranchEUIDs(setBranchEuidType(data));

        return notification;
    }

    private static BranchEUIDsType setBranchEuidType(BranchDisclosureReceptionData data) {
        CompanyEUIDType companyEuidType = new CompanyEUIDType();
        companyEuidType.setValue(data.getCountryCode() + data.getBusinessRegisterId()
                + "." + data.getCompanyNumber());
        BranchEUIDsType branchEuidsType = new BranchEUIDsType();
        branchEuidsType.getBranchEUID().add(companyEuidType);
        return branchEuidsType;
    }

    private static BusinessRegisterType setBusinessRegisterType(
            BranchDisclosureReceptionData data) {
        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();

        // BusinessRegisterID
        businessRegisterIdType.setValue(data.getBusinessRegisterId());

        BusinessRegisterType businessRegisterType = new BusinessRegisterType();
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue(data.getRegisterName());

        // BusinessRegisterCountry Country
        CountryType countryType = getCountry(data.getCountryCode());

        businessRegisterType.setBusinessRegisterCountry(countryType);
        businessRegisterType.setBusinessRegisterID(businessRegisterIdType);
        businessRegisterType.setBusinessRegisterName(businessRegisterNameType);

        return businessRegisterType;
    }

    private static NotificationCompanyType setNotificationCompanyType(
            BranchDisclosureReceptionData data) {
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue(data.getIssuingRegister());

        CompanyAlternateIDType companyAlternateIdType = new CompanyAlternateIDType();
        companyAlternateIdType.setValue("34EDED");

        CompanyAlternateIdentifiersType companyAlternateIdentifiersType =
                new CompanyAlternateIdentifiersType();
        companyAlternateIdentifiersType.getCompanyAlternateID().add(companyAlternateIdType);

        CompanyEUIDType companyEuidType = new CompanyEUIDType();
        companyEuidType.setValue(data.getIssuingCountryCode() + data.getIssuingBusinessRegId()
                + "." + data.getIssuingCompanyNumber());

        LegalFormCodeType legalFormCodeType = new LegalFormCodeType();
        legalFormCodeType.setValue(data.getLegalFormCode());

        CompanyNameType companyNameType = new CompanyNameType();
        companyNameType.setValue("CompanyName");

        // Set the address for the resulting company
        AddressType addressType = getAddress("1A Broadway Parade",
                "Pinner Road",
                "Middx",
                "HA27SY",
                "HARROW",
                data.getIssuingCountryCode());

        NotificationCompanyType notificationCompanyType = new NotificationCompanyType();
        notificationCompanyType.setBusinessRegisterName(businessRegisterNameType);
        notificationCompanyType.setCompanyAlternateIdentifiers(companyAlternateIdentifiersType);
        notificationCompanyType.setCompanyEUID(companyEuidType);
        notificationCompanyType.setCompanyLegalForm(legalFormCodeType);
        notificationCompanyType.setCompanyName(companyNameType);
        notificationCompanyType.setCompanyRegisteredOffice(addressType);

        return notificationCompanyType;
    }

    private static NotificationContextType setNotificationContextType(
            BranchDisclosureReceptionData data) {

        EffectiveDateType effectiveDate = new EffectiveDateType();

        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setValue(getXmlGregorianCalendarNow());
        effectiveDate.setValue(getXmlGregorianCalendarNow());

        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();

        // BusinessRegisterID
        businessRegisterIdType.setValue(data.getIssuingBusinessRegId());

        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue(data.getIssuingRegister());

        // BusinessRegisterCountry Country
        CountryType countryType = getCountry(data.getIssuingCountryCode());

        LegislationReferencesType legislationReferencesType = new LegislationReferencesType();
        legislationReferencesType.getLegislationReference();

        // set BusinessRegisterID and BusinessRegisterCountry
        BusinessRegisterType issuingOrganisation = new BusinessRegisterType();
        issuingOrganisation.setBusinessRegisterID(businessRegisterIdType);
        issuingOrganisation.setBusinessRegisterCountry(countryType);
        issuingOrganisation.setBusinessRegisterName(businessRegisterNameType);

        NotificationContextType notificationContextType = new NotificationContextType();
        notificationContextType.setIssuingOrganisation(issuingOrganisation);
        notificationContextType.setIssuanceDateTime(dateTimeType);
        notificationContextType.setEffectiveDate(effectiveDate);

        return notificationContextType;
    }

    private static ProceedingType setProceedingType(BranchDisclosureReceptionData data) {
        ProceedingType proceedingType = new ProceedingType();
        proceedingType.setValue(data.getProceedingType());
        return proceedingType;
    }
}
