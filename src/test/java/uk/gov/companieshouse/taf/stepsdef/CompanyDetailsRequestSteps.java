package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.util.CollectionUtils.isEmpty;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageHeaderType;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;
import eu.europa.ec.bris.v140.jaxb.br.error.BRBusinessError;
import eu.europa.ec.bris.v140.jaxb.components.aggregate.DocumentType;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import uk.gov.companieshouse.taf.domain.ValidationError;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.transformer.MessageTransformer;
import uk.gov.companieshouse.taf.util.RequestHelper;


public class CompanyDetailsRequestSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTransformer.class);

    @Autowired
    private RequestData data;

    @Autowired
    private SendBrisTestMessageService companyDetailsRequest;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

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
     * Create a request with an invalid company number.
     */
    @Given("^I am requesting details for a company that does not exist$")
    public void requestingDetailsForACompanyThatDoesNotExist() throws Throwable {
        data.setCompanyNumber("00000000");
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);

        data.setOutgoingBrisMessage((companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId())));
    }

    /**
     * Create a request with a mismatch between the business register code and country code.
     */
    @Given("^the request business id and country do not match")
    public void theRequestBusinessIdAndCountryDoNotMatch() throws Throwable {
        data.setCountryCode("BRA");
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);

        data.setOutgoingBrisMessage(companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId()));
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
     */
    @Given("^a new company details request is created using the same message id$")
    public void companyDetailsRequestForMessageIdIsCreated() throws Throwable {
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);

        data.setOutgoingBrisMessage(companyDetailsRequest
                .createOutgoingBrisMessage(request, data.getMessageId()));
    }

    /**
     * Create a company details request with an invalid correlation id. The id exceeds the 64
     * character limit set.
     */
    @Given("^the request contains an invalid correlation id$")
    public void theRequestContainsAnInvalidCorrelationId() throws Throwable {
        String invalidId = RandomStringUtils.randomAlphanumeric(65);

        data.setMessageId(invalidId);
        data.setCorrelationId(invalidId);
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);
        data.setOutgoingBrisMessage(companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId()));
    }

    /**
     * Create a company details request with a correlation id that does not match the
     * message id.
     */
    @Given("^the request contains a correlation id that does not match the message id$")
    public void theRequestContainsACorrelationIdThatDoesNotMatchTheMessageId() throws Throwable {
        String messageId = randomAlphanumeric(8);
        data.setMessageId(messageId);

        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);
        data.setOutgoingBrisMessage(companyDetailsRequest.createOutgoingBrisMessage(request,
                messageId));
    }

    /**
     * Create a company details request with a country code that does not exist.
     */
    @Given("^the request contains a business country ([^\"]*) that does not exist$")
    public void theRequestContainsABusinessCountryThatDoesNotExist(String countryCode)
            throws Throwable {
        data.setCountryCode(countryCode);
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);

        data.setOutgoingBrisMessage(companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId()));
    }

    /**
     * Create a company details request with an invalid business register id.
     */
    @Given("^the request contains an invalid business register id ([^\"]*)$")
    public void theRequestContainsAnInvalidBusinessRegisterId(String businessRegisterId)
            throws Throwable {
        data.setBusinessRegisterId(businessRegisterId);
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);

        data.setOutgoingBrisMessage(companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId()));
    }

    /**
     * Create a company details request with a foreign valid business register id and country code.
     */
    @Given("^the request is not correct for the receiving business register$")
    public void theRequestIsNotCorrectForTheReceivingBusinessRegister() throws Throwable {
        data.setCountryCode("ES");
        data.setBusinessRegisterId("01005");
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);

        data.setOutgoingBrisMessage(companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId()));
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
                requestingTheCompanyDetailsForCompany(privateLimitedSharesSection30Exemption);
                break;
            case "eeig":
                // Load EEIG company
                requestingTheCompanyDetailsForCompany(eeig);
                break;
            case "european-public-limited-liability-company-se":
                // Load European Public Limited-Liability Company
                requestingTheCompanyDetailsForCompany(europeanPublicLimitedLiabilityCompanySe);
                break;
            case "ltd":
                // Load Private Limited Company
                requestingTheCompanyDetailsForCompany(data.getCompanyNumber());
                break;
            case "plc":
                // Load Public Limited Company
                requestingTheCompanyDetailsForCompany(plc);
                break;
            case "unregistered-company":
                // Load Unregistered Company
                requestingTheCompanyDetailsForCompany(unregisteredCompany);
                break;
            case "private-limited-guarant-nsc":
                // Load Private Limited by Guarantee (NSC)
                requestingTheCompanyDetailsForCompany(privateLimitedGuarantNsc);
                break;
            case "private-limited-guarant-nsc-limited-exemption":
                // Load Private Limited by Guarantee (NSC) (Exempt)
                requestingTheCompanyDetailsForCompany(privateLimitedGuarantNscLimitedExemption);
                break;
            case "oversea-company":
                // Load Overseas Company
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
        LOGGER.info("Testing against the cloned data for company {}", companyNumber);
        data.setCompanyNumber(companyNumber);
        BRCompanyDetailsRequest request = RequestHelper.getCompanyDetailsRequest(data);

        data.setOutgoingBrisMessage(companyDetailsRequest.createOutgoingBrisMessage(
                request, data.getMessageId()));
    }

    /**
     * Creates a request for the known companies that will have the expected test data.
     *
     * @param formType the expected form included in the filing history
     */
    @Given("^a request for a company that has a ([^\"]*) form$")
    public void requestForACompanyThatHasAForm(String formType) throws Throwable {
        // As we're reliant on predetermined data we will need to use the loaded companies
        switch (formType) {
            case "288a":
                requestingTheCompanyDetailsForCompany(unregisteredCompany);
                break;
            case "EEIG-ADD":
                requestingTheCompanyDetailsForCompany(eeig);
                break;
            case "288b":
                requestingTheCompanyDetailsForCompany(privateLimitedGuarantNsc);
                break;
            default:
                throw new RuntimeException("There is no known company to test the form "
                        + formType);
        }
    }

    /**
     * Creates a request for the company 99990001 which currently contains two filing history
     * entries for forms GAZ1 and CH01.
     * GAZ1 - currently not mapped so should not be returned
     * CH01 - mapped to EL_UK_011
     */
    @Given("^a company has a restricted document present in it's filing history$")
    public void companyHasARestrictedDocumentPresentInItSFilingHistory() throws Throwable {
        requestingTheCompanyDetailsForCompany(plc);
    }

    @When("^I make a company details request$")
    public void makeACompanyDetailsRequest() throws Throwable {
        companyDetailsRequest.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
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

        // And assert that the header details are correct
        validateHeader(response.getMessageHeader());
    }

    /**
     * Compares the legal entity id in the response to the expected value from the feature.
     *
     * @param legalEntity the expected legal entity id
     */
    @Then("^the company details response will have the legal entity code ([^\"]*)$")
    public void theCompanyDetailsResponseWillHaveTheLegalEntityCodeCompanyType(String legalEntity)
            throws Throwable {
        BRCompanyDetailsResponse response = data.getCompanyDetailsResponse();

        assertEquals("The Legal Entity ID is incorrect: ", legalEntity,
                response.getCompany().getCompanyLegalForm().getValue());

        // And assert that the header details are correct
        validateHeader(response.getMessageHeader());
    }

    /**
     * Check the error message has been placed in the right collection.
     */
    @Then("^I should get a message with the error code ([^\"]*)$")
    public void theCorrectErrorWillBeReturnedToTheEcp(String errorCode) throws Throwable {
        BRBusinessError businessError = retrieveMessage
                .checkForResponseByCorrelationId(data.getMessageId());
        assertNotNull(businessError);

        assertEquals("Expected Error Code:", errorCode,
                businessError.getFaultError().get(0).getFaultErrorCode().getValue());

        // And assert that the header details are correct
        validateHeader(businessError.getMessageHeader());
    }

    /**
     * Checks that no response has been created due to the schema validation.
     */
    @Then("^no response will be created$")
    public void noResponseWillBeCreated() throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(data.getCorrelationId());
        assertNull(response);
    }

    /**
     * Checks that the company EUID is correctly formed.
     */
    @Then("^the response will contain a valid formed EUID$")
    public void theResponseWillContainAValidFormedEuid() throws Throwable {
        BRCompanyDetailsResponse response = data.getCompanyDetailsResponse();

        assertEquals("Expected EUID is incorrect: ", String.format("UKEW.%s",
                data.getCompanyNumber()),
                response.getCompany().getCompanyEUID().getValue());

        // And assert that the header details are correct
        validateHeader(response.getMessageHeader());
    }

    /**
     * Checks that the company number in the response is the expected company number.
     *
     * @param companyNumber the company number to match in the response
     */
    @Then("^the response will contain the company details for ([^\"]*)$")
    public void theResponseWillContainTheCompanyDetailsFor(String companyNumber) throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(data.getCorrelationId());
        assertNotNull(response);

        data.setCompanyDetailsResponse(response);

        assertNotNull(response.getCompany().getCompanyRegistrationNumber());
        assertEquals("Expected Company Number appears incorrect: ", companyNumber,
                response.getCompany().getCompanyRegistrationNumber().getValue());

        // And assert that the header details are correct
        validateHeader(response.getMessageHeader());
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

    /**
     * Checks that the company details response contains the expected document by asserting the
     * expected explanatory label. The explanatory label will map to a form type.
     * e.g. AD01 = EL_UK_016
     *
     * @param explanatoryLabel the code that relates to the document type
     */
    @Then("^the response will contain the explanatory label ([^\"]*)$")
    public void theResponseWillContainTheExplanatoryLabel(String explanatoryLabel)
            throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(data.getCorrelationId());
        assertNotNull(response);

        assertTrue("The label does not match.",
                checkResponseContainsExpectedLabel(explanatoryLabel, response));

        // And assert that the header details are correct
        validateHeader(response.getMessageHeader());

    }

    /**
     * Compares the expected amount of documents returned in the response.
     */
    @Then("^the response will not include the details of the restricted document$")
    public void theResponseWillNotIncludeTheDetailsOfTheRestrictedDocument() throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(data.getCorrelationId());
        assertNotNull(response);

        // Ensure the response contains documents
        assertNotNull(response.getDocuments());
        assertTrue("There are no documents in this response",
                !isEmpty(response.getDocuments().getDocument()));

        // Check the expected amount of documents
        assertEquals("Incorrect document count.", 1,
                response.getDocuments().getDocument().size());
        // Assert that the document is the expected document
        assertEquals("Incorrect document attached.", "EL_UK_011",
                response.getDocuments().getDocument().get(0).getCompanyItem()
                        .getCompanyItemExplanatoryLabel().getValue());

        // And assert that the header details are correct
        validateHeader(response.getMessageHeader());
    }

    /**
     * Checks for a validation error. Validation errors are created upon schema validation.
     */
    @Then("^I should receive a validation error$")
    public void shouldReceiveAValidationError() throws Throwable {
        ValidationError validationError = retrieveMessage
                .checkForResponseByCorrelationId(data.getCorrelationId());

        assertNotNull(validationError);

        // And assert that the header details are correct
        validateHeader(validationError.getMessageHeader());
    }

    private boolean checkResponseContainsExpectedLabel(String explanatoryLabel,
                                                       BRCompanyDetailsResponse response) {
        for (DocumentType documentType : response.getDocuments().getDocument()) {
            final String label = documentType.getCompanyItem()
                    .getCompanyItemExplanatoryLabel().getValue();
            LOGGER.info("Label Text: {}", label);
            if (StringUtils.equalsAnyIgnoreCase(explanatoryLabel, label)) {
                return true;
            }
        }
        return false;
    }

    /*
     Check that the message header is as expected.
     */
    private void validateHeader(MessageHeaderType messageHeader) {
        assertEquals("Correlation ID in header is not as expected",
                data.getMessageId(),
                messageHeader.getCorrelationID().getValue());

        assertEquals("Business Register ID in header is not as expected",
                data.getBusinessRegisterId(),
                messageHeader.getBusinessRegisterReference().getBusinessRegisterID().getValue());

        assertEquals("Business Register Country in header is not as expected",
                data.getCountryCode(),
                messageHeader.getBusinessRegisterReference()
                        .getBusinessRegisterCountry().getValue());
    }
}
