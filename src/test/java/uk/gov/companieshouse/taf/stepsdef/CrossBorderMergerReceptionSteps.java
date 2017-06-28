package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotificationAcknowledgement;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.data.CrossBorderMergerNotificationData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.CrossBorderMergerNotificationRequestBuilder;

public class CrossBorderMergerReceptionSteps {

    @Autowired
    private CrossBorderMergerNotificationData data;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    @Autowired
    private SendBrisTestMessageService sendBrisTestMessageService;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    /**
     * Create a cross border merger.
     */
    @Given("^a valid cross border merger request is created$")
    public void crossBorderMergerRequestExists() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create an invalid cross border merger with an invalid merging country code.
     */
    @Given("^the notification does not have a valid merging country code$")
    public void theNotificationDoesNotHaveAValidMergingCountryCode() throws Throwable {
        data.setIssuingCountryCode("GG");
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification merger with an invalid business register id.
     * This example included whitespaces.
     */
    @Given("^the notification does not have a valid business register id$")
    public void theNotificationDoesNotHaveAValidBusinessRegisterId() throws Throwable {
        data.setIssuingBusinessRegId("12     04");
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid legal form code.
     */
    @Given("^the notification does not have a valid legal form code$")
    public void theNotificationDoesNotHaveALegalFormCode() throws Throwable {
        data.setLegalFormCode(RandomStringUtils.randomAlphabetic(8));
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid issuing country code.
     */
    @Given("^the notification has an invalid issuing country code$")
    public void theNotificationHasAnInvalidIssuingCountryCode() throws Throwable {
        data.setIssuingCountryCode(RandomStringUtils.randomAlphabetic(3));
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    @When("^I make a cross border merger request$")
    public void makeACrossBorderMergerRequest() throws Throwable {
        sendBrisTestMessageService.sendOutgoingBrisMessage(data.getOutgoingBrisMessage(),
                data.getMessageId());
    }

    /**
     * Assert the acknowledgement for the Cross Border Merger.
     */
    @Then("^I should get an acknowledgment confirming receipt of the merger$")
    public void shouldGetAnAcknowledgmentConfirmingReceiptOfTheMerger() throws Throwable {
        BRCrossBorderMergerReceptionNotificationAcknowledgement ack =
                retrieveMessage.checkForResponseByCorrelationId(data.getCorrelationId());

        assertNotNull(ack);

        // And assert that the header details are correct
        CommonSteps.validateHeader(ack.getMessageHeader(), data.getCorrelationId(),
                data.getBusinessRegisterId(), data.getCountryCode());

    }
}
