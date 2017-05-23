package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.message.CompanyDetailsRequest;
import uk.gov.companieshouse.taf.message.RetrieveMessage;

public class CompanyDetailsRequestSteps {

    private static final String MESSAGE_ID = UUID.randomUUID().toString();
    private String correlationId = MESSAGE_ID;

    @Autowired
    private CompanyDetailsRequest companyDetailsRequest;

    @Autowired
    private RetrieveMessage retrieveMessage;

    /**
     * Create valid company details request.
     */
    @Given("^I am requesting details for a valid company$")
    public void requestingDetailsForAValidCompany() throws Throwable {
        BRCompanyDetailsRequest request = CompanyDetailsHelper.newInstance(
                correlationId,
                MESSAGE_ID,
                "00006400",
                "EW",
                "UK");

        companyDetailsRequest.createOutGoingMessageAndSend(request, MESSAGE_ID);
    }

    /**
     * Create a request with an invalid company number.
     */
    @Given("^I am requesting details for a company that does not exist$")
    public void requestingDetailsForACompanyThatDoesNotExist() throws Throwable {
        BRCompanyDetailsRequest request = CompanyDetailsHelper.newInstance(
                correlationId,
                MESSAGE_ID,
                "00000000",
                "EW",
                "UK");

        companyDetailsRequest.createOutGoingMessageAndSend(request, MESSAGE_ID);
    }

    @When("^I make a company details request$")
    public void makeACompanyDetailsRequest() throws Throwable {
        // Empty method for feature context atm
    }

    /**
     * Check the outgoing message has been placed in the right collection.
     */
    @Then("^the the correct company details will be returned to the ECP$")
    public void theTheCorrectCompanyDetailsWillBeReturnedToTheECP() throws Throwable {
        retrieveMessage.checkForMessageByMessageId(MESSAGE_ID);
    }


}
