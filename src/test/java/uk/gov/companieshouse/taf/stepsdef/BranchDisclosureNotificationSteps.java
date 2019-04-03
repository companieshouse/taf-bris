package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.jaxb.br.branch.disclosure.notification.submission.request.v1_4.BRBranchDisclosureSubmissionNotification;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.builders.BranchDisclosureNotificationBuilder;
import uk.gov.companieshouse.taf.data.BranchDisclosureNotificationData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.util.NotificationApiHelper;

public class BranchDisclosureNotificationSteps extends BrisSteps{

    private static final String BRANCH_DISCLOSURE_CONTEXT = "branch-disclosure";
    private static final String RESPONSE_MESSAGE = "response_message";
    private static final String MESSAGE_ID = "message_id";

    @Autowired
    private BranchDisclosureNotificationData data;

    @Autowired
    private NotificationApiHelper notificationApiHelper;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessageService;

    /**
     * Create Branch Disclosure Notification based on proceeding type.
     *
     * @param proceedingType the type of brandh disclosure
     */
    @Given("^a valid ([^\"]*) branch disclosure notification exists$")
    public void validBranchDisclosureNotificationExists(String proceedingType) throws Throwable {
        data.setProceedingType(proceedingType);
        data.setBranchDisclosureNotification(BranchDisclosureNotificationBuilder
                .getBranchDisclosureNotification(data));
    }

    /**
     * Submit Branch Disclosure Notification.
     */
    @When("^I submit an branch disclosure notification$")
    public void submitAnBranchDisclosureNotification() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();

        // Map the JSON for the Branch Disclosure into a String to be used for REST call
        String requestBody = mapper.writeValueAsString(data.getBranchDisclosureNotification());

        // Call the Notification REST API for Branch Disclosure
        JSONObject json = notificationApiHelper.callNotificationRestApi(requestBody,
                BRANCH_DISCLOSURE_CONTEXT);

        assertTrue(StringUtils.equals("Successfully created "
                        + "BRBranchDisclosureSubmissionNotification for company "
                        + data.getCompanyNumber(),
                (String) json.get(RESPONSE_MESSAGE)));

        // Use the message id retrieved from the REST API so that it can be used
        // later to find the message in MongoDB
        String messageId = (String) json.get(MESSAGE_ID);
        data.setCorrelationId(messageId);
        data.setMessageId(messageId);
    }

    /**
     * Confirm Branch Disclosure Submission is successfully created.
     */
    @Then("^I should have sent a branch disclosure notification to the ECP$")
    public void shouldHaveSentABranchDisclosureNotificationToTheEcp() throws Throwable {
        BRBranchDisclosureSubmissionNotification request;
        try {
            request = retrieveMessageService
                    .checkForMessageByCorrelationId(data.getCorrelationId());
        } catch (Exception ex) {
            throw new RuntimeException("Exception thrown searching for message " + ex.getMessage());
        }
        assertNotNull(request);

        assertTrue(StringUtils.equals(request.getProceeding().getValue(),
                data.getProceedingType()));

        // And assert that the header details are correct
        CommonSteps.validateHeader(createBrisMessageHeaderType(request),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }
}
