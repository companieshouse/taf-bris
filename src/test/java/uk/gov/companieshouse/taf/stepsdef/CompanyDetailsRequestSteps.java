package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;
import eu.europa.ec.bris.v140.jaxb.br.error.BRBusinessError;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.transformer.MessageTransformer;
import uk.gov.companieshouse.taf.util.RequestHelper;


public class CompanyDetailsRequestSteps {

    private static final String COMPANY_NUMBER = "10000000";

    private String messageId = UUID.randomUUID().toString();
    private String correlationId = messageId;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTransformer.class);


    @Autowired
    private SendBrisTestMessageService companyDetailsRequest;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    private OutgoingBrisMessage outgoingBrisMessage;

    /**
     * Create valid company details request.
     */
    @Given("^I am requesting details for a valid company$")
    public void requestingDetailsForAValidCompany() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                correlationId,
                messageId,
                COMPANY_NUMBER,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a request with an invalid company number.
     */
    @Given("^I am requesting details for a company that does not exist$")
    public void requestingDetailsForACompanyThatDoesNotExist() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                correlationId,
                messageId,
                "00000000",
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create request with an invalid country code.
     */
    @Given("^the request has an invalid country code$")
    public void theRequestHasAnInvalidCountryCode() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                correlationId,
                messageId,
                COMPANY_NUMBER,
                "EW",
                "AA");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a request with a mismatch between the business register code and country code.
     */
    @Given("^the request business id and country do not match")
    public void theRequestBusinessIdAndCountryDoNotMatch() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                correlationId,
                messageId,
                COMPANY_NUMBER,
                "EW",
                "BRA");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    @Given("^a company details request for ([^\"]*) has been received$")
    public void companyDetailsRequestForMessageIdHasBeenReceived(String messageId)
            throws Throwable {
        //TODO Load json document - company details request with message id
        LOGGER.info("Inserting company details request document for {}", messageId);
    }

    /**
     * Create a company details request with a message id that is already present
     * in the mongo collection outgoing_messages.
     *
     * @param messageId the known message id
     */
    @Given("^a company details request for ([^\"]*) is created$")
    public void companyDetailsRequestForMessageIdIsCreated(String messageId) throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                messageId,
                messageId,
                COMPANY_NUMBER,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a company details request with an invalid correlation id. The id exceeds the 64
     * character limit set.
     */
    @Given("^the request contains an invalid correlation id$")
    public void theRequestContainsAnInvalidCorrelationId() throws Throwable {
        String invalidId = RandomStringUtils.randomAlphanumeric(65);
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                invalidId,
                invalidId,
                COMPANY_NUMBER,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a company details request with a correlation id that does not match the
     * message id.
     */
    @Given("^the request contains a correlation id that does not match the message id$")
    public void theRequestContainsACorrelationIdThatDoesNotMatchTheMessageId() throws Throwable {
        messageId = randomAlphanumeric(8);

        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                messageId,
                correlationId,
                COMPANY_NUMBER,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a company details request with a country code that does not exist.
     */
    @Given("^the request contains a business country that does not exist$")
    public void theRequestContainsABusinessCountryThatDoesNotExist() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                correlationId,
                messageId,
                COMPANY_NUMBER,
                "EW",
                "GBP");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a company details request with an invalid business register id.
     */
    @Given("^the request contains an invalid business register id$")
    public void theRequestContainsAnInvalidBusinessRegisterId() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                correlationId,
                messageId,
                COMPANY_NUMBER,
                "WALES",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a company details request with a foreign valid business register id and country code.
     */
    @Given("^the request is not correct for the receiving business register$")
    public void theRequestIsNotCorrectForTheReceivingBusinessRegister() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.newInstance(
                correlationId,
                messageId,
                COMPANY_NUMBER,
                "01005",
                "ES");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Enters the required company data into the mongo collections.
     *
     * @param legalEntity code that represents the company type
     */
    @Given("^the user is requesting the details of a \"([^\"]*)\" company$")
    public void theUserIsRequestingTheDetailsOfACompany(String legalEntity) throws Throwable {
        // Load the app data required for the legal entity
        switch (legalEntity.toUpperCase()) {
            case "LF_UK_001":
                // Load Private Limited by shares company
                break;
            case "LF_UK_002":
                // Load EEIG company
                break;
            case "LF_UK_003":
                // Load European Public Limited-Liability Company
                break;
            case "LF_UK_004":
                // Load Private Limited Company
                break;
            case "LF_UK_005":
                // Load Public Limited Company
                break;
            case "LF_UK_006":
                // Load Unregistered Company
                break;
            case "LF_UK_007":
                // Load Private Limited by Guarantee (NSC)
                break;
            case "LF_UK_008":
                // Load Private Limited by Guarantee (NSC) (Exempt)
                break;
            case "LF_UK_009":
                // Load Overseas Company
                break;
            default:
                throw new RuntimeException(legalEntity + " is not a known legal entity");
        }
    }

    @When("^I make a company details request$")
    public void makeACompanyDetailsRequest() throws Throwable {
        companyDetailsRequest.sendOutgoingBrisMessage(outgoingBrisMessage, messageId);
    }

    /**
     * Check the outgoing message has been placed in the right collection.
     */
    @Then("^the correct company details will be returned to the ECP$")
    public void theCorrectCompanyDetailsWillBeReturnedToTheEcp() throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(messageId);
        assertNotNull(response);
        assertEquals("Expected Correlation ID:", correlationId,
                response.getMessageHeader().getCorrelationID().getValue());
    }

    /**
     * Check the error message has been placed in the right collection.
     */
    @Then("^I should get a message with the error code ([^\"]*)$")
    public void theCorrectErrorWillBeReturnedToTheEcp(String errorCode) throws Throwable {
        BRBusinessError businessError = retrieveMessage
                .checkForResponseByCorrelationId(messageId);
        assertNotNull(businessError);

        assertEquals("Expected Error Code:", errorCode,
                businessError.getFaultError().get(0).getFaultErrorCode().getValue());
    }

    /**
     * Checks that no response has been created due to the schema validation.
     */
    @Then("^no response will be created$")
    public void noResponseWillBeCreated() throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(messageId);
        assertNull(response);
    }
}
