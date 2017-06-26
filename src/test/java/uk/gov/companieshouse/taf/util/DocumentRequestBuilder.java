package uk.gov.companieshouse.taf.util;

import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.v140.jaxb.components.basic.DocumentIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.PaymentReferenceType;

/**
 * Used to build a request object for Documents with default values.
 */
public class DocumentRequestBuilder extends RequestBuilder {

    private static final String PAYMENT_REF = "PR";

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

    private static DocumentIDType documentIdType(String documentId) {
        DocumentIDType documentIdType = new DocumentIDType();
        documentIdType.setValue(documentId);
        return documentIdType;
    }
}
