package uk.gov.companieshouse.taf.util;

import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotification;

import eu.europa.ec.bris.v140.jaxb.components.aggregate.AddressType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.BusinessRegisterType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.CompanyAlternateIdentifiersType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.LegislationReferencesType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationCompanyType;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.NotificationContextType;
import eu.europa.ec.bris.v140.jaxb.components.basic.AddressLine1Type;
import eu.europa.ec.bris.v140.jaxb.components.basic.AddressLine2Type;
import eu.europa.ec.bris.v140.jaxb.components.basic.AddressLine3Type;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CityType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyAlternateIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyEUIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyNameType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import eu.europa.ec.bris.v140.jaxb.components.basic.DateTimeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.EffectiveDateType;
import eu.europa.ec.bris.v140.jaxb.components.basic.LegalFormCodeType;
import eu.europa.ec.bris.v140.jaxb.components.basic.ProceedingType;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;
import uk.gov.companieshouse.taf.data.RequestData;

/**
 * Used to build a request object for Branch Disclosures with default values.
 */
public class BranchDisclosureRequestBuilder extends RequestBuilder {

    private static final String WINDING_UP_OPENING = "WINDING_UP_OPENING";

    /**
     * Create new instance of BRBranchDisclosureReceptionNotification.
     */
    public static BRBranchDisclosureReceptionNotification getBranchDisclosureReceptionNotification(
            RequestData data) {

        BRBranchDisclosureReceptionNotification notification = new
                BRBranchDisclosureReceptionNotification();

        notification.setMessageHeader(getMessageHeader(data.getCorrelationId(), data.getMessageId(),
                data.getBusinessRegisterId(), data.getCountryCode()));

        notification.setNotificationContext(setNotificationContextType());
        notification.setProceeding(setProceedingType(WINDING_UP_OPENING));
        notification.setDisclosureCompany(setNotificationCompanyType());
        notification.setRecipientOrganisation(setBusinessRegisterType());

        return notification;
    }

    private static BusinessRegisterType setBusinessRegisterType() {
        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();

        // BusinessRegisterID
        businessRegisterIdType.setValue(BusinessRegisterConstants.EW_REGISTER_ID);

        BusinessRegisterType businessRegisterType = new BusinessRegisterType();
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue(BusinessRegisterConstants.UK_REGISTER);

        // BusinessRegisterCountry Country
        CountryType countryType = getCountry(BusinessRegisterConstants.UK_COUNTRY_CODE);

        businessRegisterType.setBusinessRegisterCountry(countryType);
        businessRegisterType.setBusinessRegisterID(businessRegisterIdType);
        businessRegisterType.setBusinessRegisterName(businessRegisterNameType);

        return businessRegisterType;
    }

    private static NotificationCompanyType setNotificationCompanyType() {
        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue(BusinessRegisterConstants.EW_REGISTER_ID);

        CompanyAlternateIDType companyAlternateIdType = new CompanyAlternateIDType();
        companyAlternateIdType.setValue("34EDED");

        CompanyAlternateIdentifiersType companyAlternateIdentifiersType =
                new CompanyAlternateIdentifiersType();
        companyAlternateIdentifiersType.getCompanyAlternateID().add(companyAlternateIdType);

        CompanyEUIDType companyEuidType = new CompanyEUIDType();
        companyEuidType.setValue("UKEW.2010012341-Z<");

        LegalFormCodeType legalFormCodeType = new LegalFormCodeType();
        legalFormCodeType.setValue("LF-NL-001");

        CompanyNameType companyNameType = new CompanyNameType();
        companyNameType.setValue("CompanyName");

        // Set the address for the resulting company
        AddressType addressType = getAddress("1A Broadway Parade",
                "Pinner Road",
                "Middx",
                "HA27SY",
                "HARROW",
                BusinessRegisterConstants.UK_COUNTRY_CODE);

        NotificationCompanyType notificationCompanyType = new NotificationCompanyType();
        notificationCompanyType.setBusinessRegisterName(businessRegisterNameType);
        notificationCompanyType.setCompanyAlternateIdentifiers(companyAlternateIdentifiersType);
        notificationCompanyType.setCompanyEUID(companyEuidType);
        notificationCompanyType.setCompanyLegalForm(legalFormCodeType);
        notificationCompanyType.setCompanyName(companyNameType);
        notificationCompanyType.setCompanyRegisteredOffice(addressType);

        return notificationCompanyType;
    }

    private static NotificationContextType setNotificationContextType() {

        EffectiveDateType effectiveDate = new EffectiveDateType();

        DateTimeType dateTimeType = new DateTimeType();
        dateTimeType.setValue(getXmlGregorianCalendarNow());
        effectiveDate.setValue(getXmlGregorianCalendarNow());

        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();

        // BusinessRegisterID
        businessRegisterIdType.setValue(BusinessRegisterConstants.EW_REGISTER_ID);

        BusinessRegisterNameType businessRegisterNameType = new BusinessRegisterNameType();
        businessRegisterNameType.setValue(BusinessRegisterConstants.UK_REGISTER);

        // BusinessRegisterCountry Country
        CountryType countryType = getCountry(BusinessRegisterConstants.UK_COUNTRY_CODE);

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

    private static ProceedingType setProceedingType(String proceedingTypeValue) {
        ProceedingType proceedingType = new ProceedingType();
        proceedingType.setValue(proceedingTypeValue);
        return proceedingType;
    }
}
