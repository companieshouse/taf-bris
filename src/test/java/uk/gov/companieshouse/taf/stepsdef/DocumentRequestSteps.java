package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Arrays;
import java.util.UUID;

import eu.europa.ec.bris.jaxb.br.document.retrieval.request.v1_4.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.jaxb.br.document.retrieval.response.v1_4.BRRetrieveDocumentResponse;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.builders.DocumentRequestBuilder;
import uk.gov.companieshouse.taf.data.DocumentRequestData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;

public class DocumentRequestSteps extends BrisSteps{
    private static final String BUSINESS_REGISTER_ID = "EW";
    private static final String BUSINESS_REGISTER_COUNTRY = "UK";

    @Autowired
    private DocumentRequestData data;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    @Autowired
    private SendBrisTestMessageService documentRequest;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    /**
     * Create valid document request.
     *
     * @param documentId document id to be set.
     */
    @Given("^the request contains a valid document id of (.*)$")
    public void theRequestContainsAValidDocumentIdOf(String documentId) throws Throwable {
        data.setDocumentId(documentId);
        BRRetrieveDocumentRequest retrieveDocumentRequest = DocumentRequestBuilder
                .getRetrieveDocumentRequest(data);

        data.setOutgoingBrisMessage((documentRequest.createOutgoingBrisMessage(
                retrieveDocumentRequest, data.getMessageId())));
    }

    /**
     * Create document request with random document id.
     */
    @Given("^the request contains a document id that does not exist$")
    public void theRequestContainsADocumentIdThatDoesNotExist() throws Throwable {
        String randomRequest = UUID.randomUUID().toString().replace("-", "");
        data.setDocumentId(randomRequest);
        BRRetrieveDocumentRequest retrieveDocumentRequest = DocumentRequestBuilder
                .getRetrieveDocumentRequest(data);

        data.setOutgoingBrisMessage((documentRequest.createOutgoingBrisMessage(
                retrieveDocumentRequest, data.getMessageId())));
    }

    /**
     * Create document request with an invalid document id.
     */
    @Given("^the request contains an invalid document id$")
    public void theRequestContainsAnInvalidDocumentId() throws Throwable {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('A', 'Z').build();
        String invalidCorrelationId = generator.generate(65);

        data.setDocumentId(invalidCorrelationId);
        BRRetrieveDocumentRequest retrieveDocumentRequest = DocumentRequestBuilder
                .getRetrieveDocumentRequest(data);

        data.setOutgoingBrisMessage((documentRequest.createOutgoingBrisMessage(
                retrieveDocumentRequest, data.getMessageId())));
    }

    @When("^I make a document details request$")
    public void makeADocumentDetailsRequest() throws Throwable {
        documentRequest.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(), data.getMessageId());
    }

    /**
     * Checks the response contains the correct document id.
     *
     * @param documentId document id to check for
     */
    @Then("^the response should contain a document with the id (.*)$")
    public void theResponseShouldContainADocumentWithTheId(String documentId) throws Throwable {
        BRRetrieveDocumentResponse retrieveDocumentResponse =
                retrieveMessage.checkForMessageByCorrelationId(data.getCorrelationId());
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


        // And assert that the header details are correct
        CommonSteps.validateHeader(createBrisMessageHeaderType(retrieveDocumentResponse),
                data.getCorrelationId(),
                data.getBusinessRegisterId(),
                data.getCountryCode());
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
