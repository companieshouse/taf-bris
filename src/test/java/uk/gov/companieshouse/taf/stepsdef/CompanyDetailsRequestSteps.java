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

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.transformer.MessageTransformer;
import uk.gov.companieshouse.taf.util.RequestHelper;


public class CompanyDetailsRequestSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTransformer.class);

    @Autowired
    private RequestData data;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    @Autowired
    private SendBrisTestMessageService companyDetailsRequest;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    private OutgoingBrisMessage outgoingBrisMessage;

    @Value("${plc.company.number}")
    private String plc;
    @Value("${ltd.section30.company.number}")
    private String privateLimitedSharesSection30Exemption;
    @Value("${eeig.company.number}")
    private String eeig;
    @Value("${europeanPlcSe.company.number}")
    private String europeanPublicLimitedLiabilityCompanySe;
    @Value("${unregistered.company.number}")
    private String unregisteredCompany;
    @Value("${ltdGuarantNsc.company.number}")
    private String privateLimitedGuarantNsc;
    @Value("${ltdGuarantNscLtdExemption.company.number}")
    private String privateLimitedGuarantNscLimitedExemption;
    @Value("${overseas.company.number}")
    private String overseaCompany;

    /**
     * Create valid company details request.
     */
    @Given("^I am requesting details for a valid company$")
    public void requestingDetailsForAValidCompany() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                //"00006400",
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    /**
     * Create a request with an invalid company number.
     */
    @Given("^I am requesting details for a company that does not exist$")
    public void requestingDetailsForACompanyThatDoesNotExist() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                "00000000",
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    /**
     * Create request with an invalid country code.
     */
    @Given("^the request has an invalid country code$")
    public void theRequestHasAnInvalidCountryCode() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "AA");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    /**
     * Create a request with a mismatch between the business register code and country code.
     */
    @Given("^the request business id and country do not match")
    public void theRequestBusinessIdAndCountryDoNotMatch() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "BRA");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    @Given("^a company details request exists$")
    public void companyDetailRequestExists()
            throws Throwable {
        LOGGER.info("This step is deliberately empty as the pre-requisite data "
                + "loaded in the @Before hook.");
    }

    /**
     * Create a company details request with a message id that is already present
     * in the mongo collection outgoing_messages.
     *
     */
    @Given("^a company details request for is created for the same message$")
    public void companyDetailsRequestForMessageIdIsCreated() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest
                .createOutgoingBrisMessage(request, data.getMessageId());
    }

    /**
     * Create a company details request with an invalid correlation id. The id exceeds the 64
     * character limit set.
     */
    @Given("^the request contains an invalid correlation id$")
    public void theRequestContainsAnInvalidCorrelationId() throws Throwable {
        String invalidId = RandomStringUtils.randomAlphanumeric(65);
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                invalidId,
                invalidId,
                defaultCompanyNumber,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    /**
     * Create a company details request with a correlation id that does not match the
     * message id.
     */
    @Given("^the request contains a correlation id that does not match the message id$")
    public void theRequestContainsACorrelationIdThatDoesNotMatchTheMessageId() throws Throwable {
        String messageId = randomAlphanumeric(8);

        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.setMessageId(messageId),
                defaultCompanyNumber,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a company details request with a country code that does not exist.
     */
    @Given("^the request contains a business country that does not exist$")
    public void theRequestContainsABusinessCountryThatDoesNotExist() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "EW",
                "GBP");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    /**
     * Create a company details request with an invalid business register id.
     */
    @Given("^the request contains an invalid business register id$")
    public void theRequestContainsAnInvalidBusinessRegisterId() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "WALES",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    /**
     * Create a company details request with a foreign valid business register id and country code.
     */
    @Given("^the request is not correct for the receiving business register$")
    public void theRequestIsNotCorrectForTheReceivingBusinessRegister() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                defaultCompanyNumber,
                "01005",
                "ES");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    /**
     * Creates a company details request based on the company type.
     *
     * @param companyType the company type
     */
    @Given("^the user is requesting the details of a ([^\"]*) company$")
    public void theUserIsRequestingTheDetailsOfACompany(String companyType) throws Throwable {
        // Load the app data required for the legal entity
        switch (companyType) {
            case "private-limited-shares-section-30-exemption":
                // Load Private Limited by shares company
                LOGGER.info("Testing against the cloned data for company {}",
                        privateLimitedSharesSection30Exemption);
                requestingTheCompanyDetailsForCompany(privateLimitedSharesSection30Exemption);
                break;
            case "eeig":
                // Load EEIG company
                LOGGER.info("Testing against the cloned data for company {}", eeig);
                requestingTheCompanyDetailsForCompany(eeig);
                break;
            case "european-public-limited-liability-company-se":
                // Load European Public Limited-Liability Company
                LOGGER.info("Testing against the cloned data for company {}",
                        europeanPublicLimitedLiabilityCompanySe);
                requestingTheCompanyDetailsForCompany(europeanPublicLimitedLiabilityCompanySe);
                break;
            case "ltd":
                // Load Private Limited Company
                LOGGER.info("Testing against the cloned data for company {}", defaultCompanyNumber);
                requestingTheCompanyDetailsForCompany(defaultCompanyNumber);
                break;
            case "plc":
                // Load Public Limited Company
                LOGGER.info("Testing against the cloned data for company {}", plc);
                requestingTheCompanyDetailsForCompany(plc);
                break;
            case "unregistered-company":
                // Load Unregistered Company
                LOGGER.info("Testing against the cloned data for company {}", unregisteredCompany);
                requestingTheCompanyDetailsForCompany(unregisteredCompany);
                break;
            case "private-limited-guarant-nsc":
                // Load Private Limited by Guarantee (NSC)
                LOGGER.info("Testing against the cloned data for company {}",
                        privateLimitedGuarantNsc);
                requestingTheCompanyDetailsForCompany(privateLimitedGuarantNsc);
                break;
            case "private-limited-guarant-nsc-limited-exemption":
                // Load Private Limited by Guarantee (NSC) (Exempt)
                LOGGER.info("Testing against the cloned data for company {}",
                        privateLimitedGuarantNscLimitedExemption);
                requestingTheCompanyDetailsForCompany(privateLimitedGuarantNscLimitedExemption);
                break;
            case "oversea-company":
                // Load Overseas Company
                LOGGER.info("Testing against the cloned data for company {}", overseaCompany);
                requestingTheCompanyDetailsForCompany(overseaCompany);
                break;
            default:
                throw new RuntimeException(companyType + " is not a known legal entity");
        }
    }

    /**
     * Creates a request with the requested company number. ONLY caveat here is that the company
     * must be loaded in the test data base.
     *
     * @param companyNumber the company that is being requested
     */
    @Given("^I am requesting the company details for company ([^\"]*)$")
    public void requestingTheCompanyDetailsForCompany(String companyNumber) throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(
                data.getCorrelationId(),
                data.getMessageId(),
                companyNumber,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId());
    }

    @When("^I make a company details request$")
    public void makeACompanyDetailsRequest() throws Throwable {
        companyDetailsRequest.sendOutgoingBrisMessage(outgoingBrisMessage, data.getMessageId());
    }

    /**
     * Check the outgoing message has been placed in the right collection.
     */
    @Then("^the correct company details will be returned to the ECP$")
    public void theCorrectCompanyDetailsWillBeReturnedToTheEcp() throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(data.getCorrelationId());
        assertNotNull(response);

        data.setCompanyDetailsResponse(response);
        assertEquals("Expected Correlation ID:", data.getCorrelationId(),
                response.getMessageHeader().getCorrelationID().getValue());
    }

    /**
     * Compares the legal entity id in the response to the expected value from the feature.
     *
     * @param legalEntity the expected legal entity id
     */
    @Then("^the company details response will have the legal entity code ([^\"]*)$")
    public void theCompanyDetailsResponseWillHaveTheLegalEntityCodeCompany_type(String legalEntity)
            throws Throwable {
        BRCompanyDetailsResponse response = data.getCompanyDetailsResponse();

        assertEquals("The Legal Entity ID is incorrect: ", legalEntity,
                response.getCompany().getCompanyLegalForm().getValue());
    }

    /**
     * Check the error message has been placed in the right collection.
     */
    @Then("^I should get a message with the error code ([^\"]*)$")
    public void theCorrectErrorWillBeReturnedToTheEcp(String errorCode) throws Throwable {
        BRBusinessError businessError = retrieveMessage
                .checkForResponseByCorrelationId(data.getCorrelationId());
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
                .checkForResponseByCorrelationId(data.getMessageId());
        assertNull(response);
    }

    /**
     * Checks that the company EUID is correctly formed.
     */
    @Then("^the response will contain a valid formed EUID$")
    public void theResponseWillContainAValidFormedEuid() throws Throwable {
        BRCompanyDetailsResponse response = data.getCompanyDetailsResponse();

        assertEquals("Expected EUID is incorrect: ", String.format("UKEW.%s", defaultCompanyNumber),
                response.getCompany().getCompanyEUID().getValue());
    }

    /**
     * Checks that the company number in the response is the expected company number.
     *
     * @param companyNumber the company number to match in the response
     */
    @Then("^the response will contain the company details for ([^\"]*)$")
    public void theResponseWillContainTheCompanyDetailsFor(String companyNumber) throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(data.getMessageId());
        assertNotNull(response);

        data.setCompanyDetailsResponse(response);

        assertNotNull(response.getCompany().getCompanyRegistrationNumber());
        assertEquals("Expected Company Number appears incorrect: ", companyNumber,
                response.getCompany().getCompanyRegistrationNumber().getValue());
    }

    /**
     * Checks that the company registered office details are correct.
     *
     * @param addressDetails List containing address details to match against
     */
    @Then("^the response should have the following address details$")
    public void theResponseShouldHaveTheFollowingAddressDetails(List<String> addressDetails)
            throws Throwable {
        BRCompanyDetailsResponse response = data.getCompanyDetailsResponse();

        assertEquals("Expected Postal code is incorrect: ", addressDetails.get(0),
                response.getCompany().getCompanyRegisteredOffice().getPostalCode().getValue());

        assertEquals("Expected Address Line 1 is incorrect: ", addressDetails.get(1),
                response.getCompany().getCompanyRegisteredOffice().getAddressLine1().getValue());

        assertEquals("Expected Address Line 2 is incorrect: ", addressDetails.get(2),
                response.getCompany().getCompanyRegisteredOffice().getAddressLine2().getValue());

        assertEquals("Expected Address Line 3 is incorrect: ", addressDetails.get(3),
                response.getCompany().getCompanyRegisteredOffice().getAddressLine3().getValue());

        assertEquals("Expected Country is incorrect: ", addressDetails.get(4),
                response.getCompany().getCompanyRegisteredOffice().getCountry().getValue());
    }
}
