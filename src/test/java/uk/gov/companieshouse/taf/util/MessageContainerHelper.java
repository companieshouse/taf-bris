package uk.gov.companieshouse.taf.util;

import eu.europa.ec.bris.jaxb.br.company.details.response.v2_0.BRCompanyDetailsResponse;
import eu.europa.ec.bris.jaxb.br.generic.acknowledgement.v2_0.BRAcknowledgement;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class MessageContainerHelper {

    public static <E> E getObjectFromContainer(MessageContainer messageContainer) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        messageContainer.getContainerBody().getMessageContent().getValue().writeTo(output);
        String xmlMessage = new String(output.toByteArray(), StandardCharsets.UTF_8);
        return (E) unmarshall(xmlMessage);
    }

    protected static Object unmarshall(String messageString) throws JAXBException {
        JAXBContext jc = getJaxbContext();
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        StringReader json = new StringReader(messageString);
        return unmarshaller.unmarshal(json);
    }

    protected static JAXBContext getJaxbContext() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(eu.europa.ec.bris.jaxb.br.generic.acknowledgement.template.br.addition.v2_0.ObjectFactory.class,
                eu.europa.ec.bris.jaxb.br.generic.acknowledgement.template.br.removal.v2_0.ObjectFactory.class,
                BRAcknowledgement.class, BRCompanyDetailsResponse.class, MessageContainer.class);

        return context;
    }
}
