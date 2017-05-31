package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentResponse;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.RequestHelper;

public class DocumentRequestSteps {

    private String messageId = UUID.randomUUID().toString();
    private String correlationId = messageId;
    private static final String DEFAULT_COMPANY_NUMBER = "00006400";

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRequestSteps.class);

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
        BRRetrieveDocumentRequest retrieveDocumentRequest = RequestHelper.newInstance(
                correlationId,
                messageId,
                DEFAULT_COMPANY_NUMBER,
                "EW",
                "UK",
                documentId);

        outgoingBrisMessage = documentRequest.createOutgoingBrisMessage(retrieveDocumentRequest,
                messageId);
    }

    @When("^I make a document details request$")
    public void makeADocumentDetailsRequest() throws Throwable {
        documentRequest.sendOutgoingBrisMessage(outgoingBrisMessage, messageId);
    }

    /**
     * Checks the response contains the correct document id.
     *
     * @param documentId document id to check for
     */
    @Then("^the response should contain a document with the id (.*)$")
    public void theResponseShouldContainADocumentWithTheId(String documentId) throws Throwable {
        BRRetrieveDocumentResponse retrieveDocumentResponse =
                retrieveMessage.checkForResponseByCorrelationId(correlationId);
        assertNotNull(retrieveDocumentResponse);
        assertEquals("Expected Document ID:", documentId,
                retrieveDocumentResponse.getDocumentID().toString());
    }
}
