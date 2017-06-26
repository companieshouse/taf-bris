package uk.gov.companieshouse.taf.util;

import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.v140.jaxb.components.basic.PaymentReferenceType;
import uk.gov.companieshouse.taf.data.DocumentRequestData;

import static uk.gov.companieshouse.taf.util.RequestBuilder.businessRegReference;
import static uk.gov.companieshouse.taf.util.RequestBuilder.companyRegNumber;
import static uk.gov.companieshouse.taf.util.RequestBuilder.documentIdType;
import static uk.gov.companieshouse.taf.util.RequestBuilder.getMessageHeader;

public class DocumentRequestBuilder {

    private static final String PAYMENT_REF = "PR";

    /**
     * Create new instance of BRRetrieveDocumentRequest.
     */
    public static BRRetrieveDocumentRequest getRetrieveDocumentRequest(
            DocumentRequestData data) {

        BRRetrieveDocumentRequest request = new BRRetrieveDocumentRequest();
        request.setMessageHeader(getMessageHeader(data.getCorrelationId(), data.getMessageId(),
                data.getBusinessRegisterId(), data.getCountryCode()));
        request.setBusinessRegisterReference(businessRegReference(data.getCountryCode(),
                data.getBusinessRegisterId()));

        PaymentReferenceType paymentReference = new PaymentReferenceType();
        paymentReference.setValue(PAYMENT_REF);
        request.setPaymentReference(paymentReference);
        request.setCompanyRegistrationNumber(companyRegNumber(data.getCompanyNumber()));
        request.setDocumentID(documentIdType(data.getDocumentId()));
        return request;
    }

}
