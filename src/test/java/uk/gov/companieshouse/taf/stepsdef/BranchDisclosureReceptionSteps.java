package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotificationAcknowledgement;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;
import uk.gov.companieshouse.taf.data.BranchDisclosureReceptionData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.BranchDisclosureRequestBuilder;

public class BranchDisclosureReceptionSteps {

    @Autowired
    private BranchDisclosureReceptionData data;

    @Autowired
    private SendBrisTestMessageService sendBrisTestMessageService;

    @Autowired
    private RetrieveBrisTestMessageService retrieveBrisTestMessageService;

    /**
     * Create a Branch Disclosure Reception Notification.
     */
    @Given("^a branch disclosure request exists$")
    public void branchDisclosureRequestExists() throws Throwable {
        BRBranchDisclosureReceptionNotification notification =
                BranchDisclosureRequestBuilder.getBranchDisclosureReceptionNotification(data);

        data.setOutgoingBrisMessage(sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId()));
    }

    /**
     * Send the Branch Disclosure Reception Notification to BRIS.
     */
    @When("^I make a branch disclosure request$")
    public void makeABranchDisclosureRequest() throws Throwable {
        sendBrisTestMessageService.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
    }

    /**
     * Assert that the Branch Disclosure Reception Notification acknowledgement is received.
     */
    @Then("^I should get an acknowledgment confirming receipt of the branch disclosure$")
    public void shouldGetAnAcknowledgmentConfirmingReceiptOfTheBranchDisclosure() throws Throwable {
        BRBranchDisclosureReceptionNotificationAcknowledgement ack =
                retrieveBrisTestMessageService.checkForResponseByCorrelationId(
                        data.getCorrelationId());
        assertNotNull(ack);

        // And assert that the header details are correct
        CommonSteps.validateHeader(ack.getMessageHeader(), data.getCorrelationId(),
                data.getBusinessRegisterId(), data.getCountryCode());
    }
}
