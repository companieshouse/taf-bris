package uk.gov.companieshouse.taf.builders;

import eu.europa.ec.bris.jaxb.br.document.retrieval.request.v1_4.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.DocumentIDType;
import eu.europa.ec.bris.jaxb.components.basic.v1_4.PaymentReferenceType;
import uk.gov.companieshouse.taf.data.DocumentRequestData;

/**
 * Used to build a request object for Documents with default values.
 */
public class DocumentRequestBuilder extends RequestBuilder {

    private static final String PAYMENT_REF = "PR";

    /**
     * Create new instance of BRRetrieveDocumentRequest.
     */
    public static BRRetrieveDocumentRequest getRetrieveDocumentRequest(
            DocumentRequestData data) {

        BRRetrieveDocumentRequest request = new BRRetrieveDocumentRequest();
        request.setMessageHeader(getMessageHeader(data));
        request.setBusinessRegisterReference(businessRegReference(data.getCountryCode(),
                data.getBusinessRegisterId()));
        PaymentReferenceType paymentReference = new PaymentReferenceType();
        paymentReference.setValue(PAYMENT_REF);
        request.setPaymentReference(paymentReference);
        request.setCompanyRegistrationNumber(companyRegNumber(data.getCompanyNumber()));
        request.setDocumentID(documentIdType(data.getDocumentId()));
        return request;
    }


    private static DocumentIDType documentIdType(String documentId) {
        DocumentIDType documentIdType = new DocumentIDType();
        documentIdType.setValue(documentId);
        return documentIdType;
    }
}
