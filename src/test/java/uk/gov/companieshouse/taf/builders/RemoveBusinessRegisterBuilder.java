package uk.gov.companieshouse.taf.builders;

import eu.europa.ec.bris.jaxb.br.generic.notification.template.br.removal.v2_0.RemoveBusinessRegisterNotificationTemplateType;
import eu.europa.ec.bris.jaxb.br.generic.notification.v2_0.BRNotification;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_5.BusinessRegisterReference;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.BusinessRegisterCodeType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.CountryType;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import uk.gov.companieshouse.taf.data.BusinessRegisterData;

import javax.xml.bind.JAXBElement;

public class RemoveBusinessRegisterBuilder {

    public static MessageContainer getRemoveBrNotification(BusinessRegisterData data) throws Exception {

        BRNotification brNotification = new BRNotification();

        eu.europa.ec.bris.jaxb.br.generic.notification.template.br.removal.v2_0.ObjectFactory objectFactory
                = new eu.europa.ec.bris.jaxb.br.generic.notification.template.br.removal.v2_0.ObjectFactory();

        RemoveBusinessRegisterNotificationTemplateType ackTemplate = objectFactory.createRemoveBusinessRegisterNotificationTemplateType();
        JAXBElement<? extends RemoveBusinessRegisterNotificationTemplateType> template = objectFactory.createRemoveBusinessRegisterNotificationTemplate(ackTemplate);

        //set country and business register
        CountryType countryType = new CountryType();
        countryType.setValue(data.getCountryCode());
        BusinessRegisterCodeType businessRegisterCodeType = new BusinessRegisterCodeType();
        businessRegisterCodeType.setValue(data.getBusinessRegisterId());

        BusinessRegisterReference businessRegisterReference = new BusinessRegisterReference();
        businessRegisterReference.setBusinessRegisterCountry(countryType);
        businessRegisterReference.setBusinessRegisterCode(businessRegisterCodeType);

        template.getValue().setBusinessRegisterReference(businessRegisterReference);

        //set notification date
        template.getValue().setNotificationDateTime(data.getNotificationDateTime());

        brNotification.setNotificationTemplate(template);

        return MessageContainerBuilder.createMessageContainer(brNotification, data, null);
    }

}
