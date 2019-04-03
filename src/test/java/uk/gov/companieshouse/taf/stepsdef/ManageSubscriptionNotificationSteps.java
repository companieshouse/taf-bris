package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.jaxb.br.subscription.request.v1_4.BRManageSubscriptionRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import uk.gov.companieshouse.taf.builders.ManageSubscriptionNotificationBuilder;
import uk.gov.companieshouse.taf.data.ManageSubscriptionData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.util.NotificationApiHelper;

public class ManageSubscriptionNotificationSteps extends BrisSteps{

    private static final String MANAGE_SUBSCRIPTION_CONTEXT = "manage-subscription";
    private static final String RESPONSE_MESSAGE = "response_message";
    private static final String MESSAGE_ID = "message_id";
    private static final String REMOVE_SUBSCRIPTION_TYPE = "REMOVE";

    @Autowired
    private ManageSubscriptionData data;

    @Autowired
    private NotificationApiHelper notificationApiHelper;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessageService;

    /**
     * Create a valid manage subscription notification.
     */
    @Given("^a valid manage subscription notification exists$")
    public void validManageSubscriptionExists() throws Throwable {
        data.setManageSubscription(ManageSubscriptionNotificationBuilder
                .createDefaultManageSubscription(data));
    }

    /**
     * Create manage subscription notification to remove a subscription.
     */
    @Given("^an unsubscribe message created$")
    public void anUnsubscribeMessageCreated() throws Throwable {
        data.setSubscriptionType("REMOVE");
        data.setManageSubscription(ManageSubscriptionNotificationBuilder
                .createDefaultManageSubscription(data));
    }

    /**
     * Submits the manage subscription to the notification api.
     */
    @When("^I submit a manage subscription notification$")
    public void submitAManageSubscriptionNotification() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();

        // Map the JSON for the manage subscription into a String to be used for REST call
        String requestBody = mapper.writeValueAsString(data.getManageSubscription());

        // Call the Notification REST API for Manage Subscription
        JSONObject json = notificationApiHelper.callNotificationRestApi(requestBody,
                MANAGE_SUBSCRIPTION_CONTEXT);

        assertTrue(StringUtils.equals("Successfully created BRManageSubscriptionRequest"
                        + " for company " + data.getCompanyNumber(),
                (String) json.get(RESPONSE_MESSAGE)));

        // Use the message id retrieved from the REST API so that it can be used
        // later to find the message in MongoDB
        String messageId = (String) json.get(MESSAGE_ID);
        data.setCorrelationId(messageId);
        data.setMessageId(messageId);
    }

    /**
     * Confirms the manage subscription notification request is created with the expected data.
     */
    @Then("^the notification subscription will be successfully sent to the ECP$")
    public void theNotificationSubscriptionWillBeSuccessfullySentToTheEcp() throws Throwable {
        BRManageSubscriptionRequest request;
        try {
            request = retrieveMessageService
                    .checkForMessageByCorrelationId(data.getCorrelationId());
        } catch (Exception ex) {
            throw new RuntimeException("Exception thrown searching for message " + ex.getMessage());
        }
        assertNotNull(request);
        assertNotNull(!CollectionUtils.isEmpty(request.getAction()));
        assertNotNull(request.getAction().get(0).getManageSubscriptionCode());

        assertTrue(StringUtils.equals(request.getAction().get(0).getManageSubscriptionCode()
                .getValue(), data.getSubscriptionType()));

        String companyEuid = request.getAction().get(0).getCompanyEUID().getValue();

        assertTrue(StringUtils.equalsIgnoreCase(companyEuid.substring(0, 2),
                data.getForeignCountryCode()));

        assertTrue(StringUtils.equalsIgnoreCase(companyEuid.substring(
                2, companyEuid.indexOf(".")), data.getForeignRegisterId()));

        // And assert that the header details are correct
        CommonSteps.validateHeader(createBrisMessageHeaderType(request),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }

    /**
     * Confirms the manage subscription notification is set to unsubscribe from the company.
     */
    @Then("^the notification will be sent to remove the subscription$")
    public void theNotificationWillBeSentToRemoveTheSubscription() throws Throwable {
        BRManageSubscriptionRequest request;
        try {
            request = retrieveMessageService
                    .checkForMessageByCorrelationId(data.getCorrelationId());
        } catch (Exception ex) {
            throw new RuntimeException("Exception thrown searching for message " + ex.getMessage());
        }
        assertNotNull(request);
        assertNotNull(!CollectionUtils.isEmpty(request.getAction()));

        assertTrue(StringUtils.equals(request.getAction().get(0).getManageSubscriptionCode()
                .getValue(), REMOVE_SUBSCRIPTION_TYPE));

        // And assert that the header details are correct
        CommonSteps.validateHeader(createBrisMessageHeaderType(request),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }
}
