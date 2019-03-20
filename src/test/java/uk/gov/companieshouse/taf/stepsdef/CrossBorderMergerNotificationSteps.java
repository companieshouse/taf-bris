package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.jaxb.br.crossborder.merger.notification.submission.request.v1_4.BRCrossBorderMergerSubmissionNotification;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.NotificationCompanyType;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import uk.gov.companieshouse.taf.builders.CrossBorderMergerBuilder;
import uk.gov.companieshouse.taf.data.MergingCompanyData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.util.NotificationApiHelper;

public class CrossBorderMergerNotificationSteps {

    private static final String CROSS_BORDER_MERGER_CONTEXT = "cross-border-merger";
    private static final String RESPONSE_MESSAGE = "response_message";
    private static final String MESSAGE_ID = "message_id";

    @Autowired
    private MergingCompanyData data;

    @Autowired
    private NotificationApiHelper notificationApiHelper;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessageService;

    /**
     * Create Cross Border Merger Notification based on merger type.
     *
     * @param mergerType the type of notification to be created. Options include;
     *                   <p>Acquisition</p>
     *                   <p>Formation of a new company</p>
     *                   <p>Owned company</p>
     */
    @Given("^a valid ([^\"]*) cross border merger notification exists$")
    public void validCrossBorderMergerNotificationExists(String mergerType) throws Throwable {
        data.setMergerType(mergerType);
        data.setCrossBorderMergerJsonRequest(CrossBorderMergerBuilder
                .createDefaultCrossBorderMerger(data, 1));
    }

    @Given("^a cross border merger notification contains (\\d+) merging companies$")
    public void crossBorderMergerNotificationContainsMergingCompanies(int numberOfCompanies)
            throws Throwable {
        data.setCrossBorderMergerJsonRequest(CrossBorderMergerBuilder
                .createDefaultCrossBorderMerger(data, numberOfCompanies));
    }

    /**
     * Submits the Cross Border Merger Notification.
     */
    @When("^I make a cross border merger notification request$")
    public void makeACrossBorderMergerNotificationRequest() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();

        // Map the JSON for the Cross Border Merger Notification
        // into a String to be used for REST call
        String requestBody = mapper.writeValueAsString(data.getCrossBorderMergerJsonRequest());

        // Call the Notification REST API for Cross Border Merger Notification
        JSONObject json = notificationApiHelper.callNotificationRestApi(requestBody,
                CROSS_BORDER_MERGER_CONTEXT);

        assertTrue(StringUtils.equals("Successfully created "
                        + "BRCrossBorderMergerSubmissionNotification for company "
                        + data.getCompanyNumber(),
                (String) json.get(RESPONSE_MESSAGE)));

        // Use the message id retrieved from the REST API so that it can be used
        // later to find the message in MongoDB
        String messageId = (String) json.get(MESSAGE_ID);
        data.setCorrelationId(messageId);
        data.setMessageId(messageId);
    }

    /**
     * Checks the Cross Border Merger Notification contains the correct details.
     */
    @Then("^I should have sent a cross border merger notification to the ECP$")
    public void shouldHaveSentACrossBorderMergerNotificationToTheEcp() throws Throwable {
        BRCrossBorderMergerSubmissionNotification request;
        
        try {
            request = retrieveMessageService
                    .checkForMessageByCorrelationId(data.getCorrelationId());
        } catch (Exception ex) {
            throw new RuntimeException("Exception thrown searching for message " + ex.getMessage());
        }
        assertNotNull(request);

        // Check the request contains a merger
        assertNotNull(request.getMerger());

        // Check notification is for the correct merger type
        assertTrue(StringUtils.equals(request.getMerger().getValue(), data.getMergerType()));

        // Check notification has a merging company
        assertNotNull(!CollectionUtils.isEmpty(request.getMergingCompany()));

        for (int i = 0; i < request.getMergingCompany().size(); i++) {
            NotificationCompanyType mergingCompany = request.getMergingCompany().get(i);
            String companyEuid = mergingCompany.getCompanyEUID().getValue();

            // Check details of merging company
            assertTrue(StringUtils.equals(companyEuid.substring(0, 2),
                    data.getForeignCountryCode()));

            assertTrue(StringUtils.equals(companyEuid.substring(2, companyEuid.indexOf(".")),
                    data.getForeignRegisterId()));

            assertTrue(StringUtils.equals(companyEuid.substring(companyEuid.indexOf(".") + 1),
                    data.getForeignCompanyNumber()));

            // Check the address details
            assertEquals("Address line 1 does not match", mergingCompany
                            .getCompanyRegisteredOffice().getAddressLine1().getValue(),
                    data.getAddressLine1() + i);

            assertEquals("Address line 2 does not match", mergingCompany
                            .getCompanyRegisteredOffice().getAddressLine2().getValue(),
                    data.getAddressLine2() + i);

            assertEquals("Address line 3 does not match", mergingCompany
                            .getCompanyRegisteredOffice().getAddressLine3().getValue(),
                    data.getAddressLine3() + i);

            assertEquals("City does not match", mergingCompany
                    .getCompanyRegisteredOffice().getCity().getValue(), data.getCity());

            assertEquals("Postal code does not match", mergingCompany
                            .getCompanyRegisteredOffice().getPostalCode().getValue(),
                    data.getPostalCode());
        }


        // And assert that the header details are correct
        CommonSteps.validateHeader(request.getMessageHeader(),
                data.getCorrelationId(), data.getBusinessRegisterId(), data.getCountryCode());
    }
}
