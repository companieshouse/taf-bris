package uk.gov.companieshouse.taf.builders;

import eu.europa.ec.bris.jaxb.br.generic.notification.template.br.addition.v2_0.AddBusinessRegisterNotificationTemplateType;
import eu.europa.ec.bris.jaxb.br.generic.notification.v2_0.BRNotification;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_5.BusinessRegisterType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.BusinessRegisterCodeType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.CountryType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.LanguageCodeType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.LocalisedBusinessRegisterNameType;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import uk.gov.companieshouse.taf.data.BusinessRegisterData;

import javax.xml.bind.JAXBElement;

public class AddBusinessRegisterBuilder{

    public static MessageContainer getAddBrNotification(BusinessRegisterData data) throws Exception {

        BRNotification brNotification = new BRNotification();

        eu.europa.ec.bris.jaxb.br.generic.notification.template.br.addition.v2_0.ObjectFactory objectFactory
                = new eu.europa.ec.bris.jaxb.br.generic.notification.template.br.addition.v2_0.ObjectFactory();

        AddBusinessRegisterNotificationTemplateType ackTemplate = objectFactory.createAddBusinessRegisterNotificationTemplateType();
        JAXBElement<? extends AddBusinessRegisterNotificationTemplateType> template = objectFactory.createAddBusinessRegisterNotificationTemplate(ackTemplate);

        //set country and business register
        CountryType countryType = new CountryType();
        countryType.setValue(data.getCountryCode());
        BusinessRegisterCodeType businessRegisterCodeType = new BusinessRegisterCodeType();
        businessRegisterCodeType.setValue(data.getBusinessRegisterId());

        BusinessRegisterType businessRegisterType = new BusinessRegisterType();
        businessRegisterType.setBusinessRegisterCountry(countryType);
        businessRegisterType.setBusinessRegisterCode(businessRegisterCodeType);

        LocalisedBusinessRegisterNameType englishName = createBusinessRegisterName(LanguageCodeType.EN.value(),
                data.getRegisterName());
        businessRegisterType.getLocalisedBusinessRegisterName().add(englishName);
        LocalisedBusinessRegisterNameType otherName = createBusinessRegisterName(LanguageCodeType.ES.value(),
                "El nuevo registro");
        businessRegisterType.getLocalisedBusinessRegisterName().add(otherName);
        template.getValue().setBusinessRegister(businessRegisterType);
        //set notification date
        template.getValue().setNotificationDateTime(data.getNotificationDateTime());

        brNotification.setNotificationTemplate(template);

        return MessageContainerBuilder.createMessageContainer(brNotification, data, null);
    }

    private static LocalisedBusinessRegisterNameType createBusinessRegisterName(String languageId,
                                                                                String registerName) {
        LocalisedBusinessRegisterNameType name = new LocalisedBusinessRegisterNameType();
        name.setLanguageID(languageId);
        name.setValue(registerName);
        return name;
    }

}
