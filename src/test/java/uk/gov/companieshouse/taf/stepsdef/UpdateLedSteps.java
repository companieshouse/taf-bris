package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import eu.europa.ec.bris.v140.jaxb.br.led.BRUpdateLEDRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.gov.companieshouse.taf.builders.UpdateLedNotificationBuilder;
import uk.gov.companieshouse.taf.config.Env;
import uk.gov.companieshouse.taf.data.UpdateLedData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;

/**
 * Implementation of Update LED steps.
 */
public class UpdateLedSteps implements En {

    private static final String RESPONSE_MESSAGE = "response_message";
    private static final String MESSAGE_ID = "message_id";
    private static final String UPDATE_LED_CONTEXT = "update-led";
    private static final String BRIS_NOTIFICATION_API_URL = "bris.notification.api.url";

    @Autowired
    private UpdateLedData data;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessageService;

    @Autowired
    private ClientHttpRequestFactory httpRequest;

    @Autowired
    private Env env;

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateLedSteps.class);

    /**
     * Create a notification of an update to Legal Entity Data.
     */
    @Given("^a valid LED update notification exists$")
    public void validLedUpdateNotificationExists() {
        // Set the LED ready to fire in the when step
        data.setUpdateLedJsonRequest(UpdateLedNotificationBuilder.createDefaultUpdateLed());
    }

    /**
     * Submit a notification of an update to Legal Entity Data.
     */
    @When("^I submit an LED update notification$")
    public void sendAnLedUpdateNotification() {
        ObjectMapper mapper = new ObjectMapper();
        String requestBody;

        // Map the JSON for the LED into a String to be used for REST call
        try {
            requestBody = mapper.writeValueAsString(data.getUpdateLedJsonRequest());
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Unable to parse JSON object " + ex.getMessage());
        }

        RestTemplate restTemplate = new RestTemplate(httpRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<>(requestBody, headers);

        String url = env.config.getString(BRIS_NOTIFICATION_API_URL) + UPDATE_LED_CONTEXT + "/"
                + data.getCompanyNumber();

        ResponseEntity<String> result = restTemplate.exchange(url,
                HttpMethod.POST, entity, String.class);

        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(result.getBody());
        } catch (org.json.simple.parser.ParseException ex) {
            throw new RuntimeException("Unable to retrieve body from JSON object "
                    + ex.getMessage());
        }

        assertTrue(StringUtils.equals("Successfully created BRUpdateLEDRequest for company "
                        + data.getCompanyNumber(),
                (String) json.get(RESPONSE_MESSAGE)));

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
                "UPDATE"));

        // And assert that the header details are correct
        CommonSteps.validateHeader(request.getMessageHeader(),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }
}