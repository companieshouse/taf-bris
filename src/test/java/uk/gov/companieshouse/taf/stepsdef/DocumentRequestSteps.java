package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.document.BRRetrieveDocumentResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.RequestHelper;

public class DocumentRequestSteps {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRequestSteps.class);

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
        BRRetrieveDocumentRequest retrieveDocumentRequest = RequestHelper.newInstance(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "UK",
                documentId);

        outgoingBrisMessage = documentRequest.createOutgoingBrisMessage(retrieveDocumentRequest,
                data.getMessageId());
    }

    /**
     * Create document request with random document id.
     */
    @Given("^the request contains a document id that does not exist$")
    public void theRequestContainsADocumentIdThatDoesNotExist() throws Throwable {
        BRRetrieveDocumentRequest retrieveDocumentRequest = RequestHelper.newInstance(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "UK",
                RandomStringUtils.randomAlphanumeric(8));

        outgoingBrisMessage = documentRequest.createOutgoingBrisMessage(retrieveDocumentRequest,
                data.getMessageId());
    }

    /**
     * Create document request with an invalid document id.
     */
    @Given("^the request contains an invalid document id$")
    public void theRequestContainsAnInvalidDocumentId() throws Throwable {
        // Unsure as to what makes document id invalid. Setting to null as placeholder for now
        BRRetrieveDocumentRequest retrieveDocumentRequest = RequestHelper.newInstance(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "UK",
                null);

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
    }
}
