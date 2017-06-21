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
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.RequestHelper;

public class CrossBorderMergerReceptionSteps {

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
     * Create a cross border merger.
     */
    @Given("^a cross border merger request exists$")
    public void crossBorderMergerRequestExists() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification = RequestHelper
                .getCrossBorderMergerNotification(
                        data.getCorrelationId(),
                        data.getMessageId(),
                        BusinessRegisterConstants.EW_REGISTER_ID,
                        BusinessRegisterConstants.UK_COUNTRY_CODE
                );

        outgoingBrisMessage = documentRequest.createOutgoingBrisMessage(notification,
                data.getMessageId());
    }

    @When("^I make a cross border merger request$")
    public void makeACrossBorderMergerRequest() throws Throwable {
        documentRequest.sendOutgoingBrisMessage(outgoingBrisMessage, data.getMessageId());
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
