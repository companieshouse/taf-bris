package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotificationAcknowledgement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.config.constants.BusinessRegisterConstants;
import uk.gov.companieshouse.taf.data.CrossBorderMergerNotificationData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.CrossBorderMergerNotificationBuilder;

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
        BRCrossBorderMergerReceptionNotification notification = CrossBorderMergerNotificationBuilder
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
        BRCrossBorderMergerReceptionNotification notification = CrossBorderMergerNotificationBuilder
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
        BRCrossBorderMergerReceptionNotification notification = CrossBorderMergerNotificationBuilder
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
        assertEquals("Business Register ID is not as expected",
                BusinessRegisterConstants.EW_REGISTER_ID,
                ack.getMessageHeader().getBusinessRegisterReference()
                        .getBusinessRegisterID().getValue());

        assertEquals("Business Register Country is not as expected",
                BusinessRegisterConstants.UK_COUNTRY_CODE,
                ack.getMessageHeader().getBusinessRegisterReference()
                        .getBusinessRegisterCountry().getValue());

        assertEquals("Correlation ID in header is not as expected",
                data.getCorrelationId(),
                ack.getMessageHeader().getCorrelationID().getValue());
    }

}
