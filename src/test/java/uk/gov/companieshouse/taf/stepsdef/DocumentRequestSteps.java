package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentResponse;

import java.util.Arrays;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.RequestHelper;

public class DocumentRequestSteps {
    private static final String BUSINESS_REGISTER_ID = "EW";
    private static final String BUSINESS_REGISTER_COUNTRY = "UK";

    @Autowired
    private RequestData data;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    @Autowired
    private SendBrisTestMessageService documentRequest;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    private OutgoingBrisMessage outgoingBrisMessage;

    /**
     * Create valid document request.
     *
     * @param documentId document id to be set.
     */
    @Given("^the request contains a valid document id of (.*)$")
    public void theRequestContainsAValidDocumentIdOf(String documentId) throws Throwable {
        BRRetrieveDocumentRequest retrieveDocumentRequest = RequestHelper
                .getRetrieveDocumentRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                BUSINESS_REGISTER_ID,
                BUSINESS_REGISTER_COUNTRY,
                documentId);

        outgoingBrisMessage = documentRequest.createOutgoingBrisMessage(retrieveDocumentRequest,
                data.getMessageId());
    }

    /**
     * Create document request with random document id.
     */
    @Given("^the request contains a document id that does not exist$")
    public void theRequestContainsADocumentIdThatDoesNotExist() throws Throwable {
        BRRetrieveDocumentRequest retrieveDocumentRequest = RequestHelper
                .getRetrieveDocumentRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                BUSINESS_REGISTER_ID,
                BUSINESS_REGISTER_COUNTRY,
                RandomStringUtils.randomAlphanumeric(8));

        outgoingBrisMessage = documentRequest.createOutgoingBrisMessage(retrieveDocumentRequest,
                data.getMessageId());
    }

    /**
     * Create document request with an invalid document id.
     */
    @Given("^the request contains an invalid document id$")
    public void theRequestContainsAnInvalidDocumentId() throws Throwable {
        BRRetrieveDocumentRequest retrieveDocumentRequest = RequestHelper
                .getRetrieveDocumentRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                BUSINESS_REGISTER_ID,
                BUSINESS_REGISTER_COUNTRY,
                RandomStringUtils.randomAlphanumeric(65));

        outgoingBrisMessage = documentRequest.createOutgoingBrisMessage(retrieveDocumentRequest,
                data.getMessageId());
    }

    @When("^I make a document details request$")
    public void makeADocumentDetailsRequest() throws Throwable {
        documentRequest.sendOutgoingBrisMessage(outgoingBrisMessage, data.getMessageId());
    }

    /**
     * Checks the response contains the correct document id.
     *
     * @param documentId document id to check for
     */
    @Then("^the response should contain a document with the id (.*)$")
    public void theResponseShouldContainADocumentWithTheId(String documentId) throws Throwable {
        BRRetrieveDocumentResponse retrieveDocumentResponse =
                retrieveMessage.checkForResponseByCorrelationId(data.getCorrelationId());
        assertNotNull(retrieveDocumentResponse);
        assertEquals("Expected Document ID:", documentId,
                retrieveDocumentResponse.getDocumentID().getValue());
        
        assertEquals("Business Register ID is not as expected",
                BUSINESS_REGISTER_ID,
                retrieveDocumentResponse.getBusinessRegisterReference()
                        .getBusinessRegisterID().getValue());
        
        assertEquals("Business Register Country is not as expected",
                BUSINESS_REGISTER_COUNTRY,
                retrieveDocumentResponse.getBusinessRegisterReference()
                        .getBusinessRegisterCountry().getValue());

        assertEquals("Company Registration Number is not as expected",
                defaultCompanyNumber,
                retrieveDocumentResponse.getCompanyRegistrationNumber().getValue());

        assertEquals("Correlation ID in header is not as expected",
                data.getCorrelationId(),
                retrieveDocumentResponse.getMessageHeader().getCorrelationID().getValue());

        assertEquals("Business Register ID in header is not as expected",
                BUSINESS_REGISTER_ID,
                retrieveDocumentResponse.getMessageHeader().getBusinessRegisterReference()
                        .getBusinessRegisterID().getValue());

        assertEquals("Business Register Country in header is not as expected",
                BUSINESS_REGISTER_COUNTRY,
                retrieveDocumentResponse.getMessageHeader().getBusinessRegisterReference()
                        .getBusinessRegisterCountry().getValue());
    }

    /**
     * Checks the response contains the expected document.
     */
    @Then("^the attached document is the expected document$")
    public void theAttachedDocumentIsTheExpectedDocument() throws Throwable {

        // Get the expected document for the company..
        byte[] expectedPdfDocument = retrieveMessage.getExpectedPdfDocument();
        assertNotNull(expectedPdfDocument);

        // And then get the actual document held against the company
        byte[] actualPdfDocument = retrieveMessage.getActualPdfDocument(data.getCorrelationId());
        assertNotNull(actualPdfDocument);

        assertTrue("The PDF documents do not match",
                Arrays.equals(expectedPdfDocument, actualPdfDocument));
    }
}
