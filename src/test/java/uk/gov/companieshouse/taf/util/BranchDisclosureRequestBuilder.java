package uk.gov.companieshouse.taf.util;

import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotification;

import eu.europa.ec.bris.v140.jaxb.components.aggregate.AddressType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BranchEUIDsType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.CompanyAlternateIdentifiersType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.LegislationReferencesType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationCompanyType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationContextType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyAlternateIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyEUIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import eu.europa.ec.bris.v140.jaxb.components.basic.DateTimeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.EffectiveDateType;
import eu.europa.ec.bris.v140.jaxb.components.basic.LegalFormCodeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.ProceedingType;
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
