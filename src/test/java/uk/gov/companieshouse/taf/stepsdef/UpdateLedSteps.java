package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.led.BRUpdateLEDRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.builders.UpdateLedNotificationBuilder;
import uk.gov.companieshouse.taf.data.UpdateLedData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.util.NotificationApiHelper;

/**
 * Implementation of Update LED steps.
 */
public class UpdateLedSteps {

    private static final String RESPONSE_MESSAGE = "response_message";
    private static final String MESSAGE_ID = "message_id";
    private static final String UPDATE_LED_CONTEXT = "update-led";
    private static final String UPDATE_TYPE = "UPDATE";

    @Autowired
    private UpdateLedData data;

    @Autowired
    private NotificationApiHelper notificationApiHelper;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessageService;

    /**
     * Create a notification of an update to Legal Entity Data.
     */
    @Given("^a valid LED update notification exists$")
    public void validLedUpdateNotificationExists() {
        // Set the LED ready to fire in the when step
        data.setUpdateLedJsonRequest(UpdateLedNotificationBuilder
                .createDefaultUpdateLed(UPDATE_TYPE));
    }

    /**
     * Submit a notification of an update to Legal Entity Data.
     */
    @When("^I submit an LED update notification$")
    public void sendAnLedUpdateNotification() throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();

        // Map the JSON for the LED into a String to be used for REST call
        String requestBody = mapper.writeValueAsString(data.getUpdateLedJsonRequest());

        // Call the Notification REST API for Update LED
        JSONObject json = notificationApiHelper.callNotificationRestApi(requestBody,
                UPDATE_LED_CONTEXT);

        assertTrue(StringUtils.equals("Successfully created BRUpdateLEDRequest for company "
                        + data.getCompanyNumber(),
                (String)json.get(RESPONSE_MESSAGE)));

        // Use the message id retrieved from the REST API so that it can be used
        // later to find the message in MongoDB
        String messageId = (String) json.get(MESSAGE_ID);
        data.setCorrelationId(messageId);
        data.setMessageId(messageId);
    }

    /**
     * Assert that a notification of an update to Legal Entity Data has been sent to the ECP.
     */
    @Then("^I should have sent an update LED notification to the ECP$")
    public void shouldHaveSentAnUpdateLedNotificationToTheEcp() {
        BRUpdateLEDRequest request;
        try {
            request = retrieveMessageService
                    .checkForResponseByCorrelationId(data.getCorrelationId());
        } catch (Exception ex) {
            throw new RuntimeException("Exception thrown searching for message " + ex.getMessage());
        }
        assertNotNull(request);

        assertTrue(StringUtils.equals(request.getUpdateLED().get(0).getUpdateLEDCode().getValue(),
                UPDATE_TYPE));

        // And assert that the header details are correct
        CommonSteps.validateHeader(request.getMessageHeader(),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }
}