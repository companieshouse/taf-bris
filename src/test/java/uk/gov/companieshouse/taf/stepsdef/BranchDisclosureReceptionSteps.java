package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.branch.disclosure.BRBranchDisclosureReceptionNotificationAcknowledgement;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.builders.BranchDisclosureRequestBuilder;
import uk.gov.companieshouse.taf.data.BranchDisclosureReceptionData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;

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
     * Create a Branch Disclosure Reception Notification that has a company EUID that does not
     * match the details of the disclosing company.
     * EUID = Country code + Business register . Company number
     */
    @Given("^the notification business register does not match the disclosure company$")
    public void theNotificationBusinessRegisterDoesNotMatchTheDisclosureCompany() throws Throwable {
        BRBranchDisclosureReceptionNotification notification =
                BranchDisclosureRequestBuilder.getBranchDisclosureReceptionNotification(data);

        notification.getDisclosureCompany().getCompanyEUID().setValue("UKAA.99990000");

        data.setOutgoingBrisMessage(sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId()));
    }

    /**
     * Create a Branch Disclosure Reception Notification with branch business register
     * set to a company that does not match the recipient company.
     */
    @Given("^the notification has a recipient business register that does not match the branch"
            + " business register$")
    public void notificationHasARecipientBusinessRegisterThatDoesNotMatchTheBranchBusinessRegister()
            throws Throwable {
        BRBranchDisclosureReceptionNotification notification =
                BranchDisclosureRequestBuilder.getBranchDisclosureReceptionNotification(data);

        notification.getRecipientOrganisation().getBusinessRegisterID().setValue("ES");

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
