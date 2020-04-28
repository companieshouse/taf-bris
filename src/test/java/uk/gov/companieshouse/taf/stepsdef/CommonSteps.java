package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.en.Then;
import eu.europa.ec.bris.jaxb.br.error.v1_4.BRBusinessError;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.data.BranchDisclosureReceptionData;
import uk.gov.companieshouse.taf.data.BusinessRegisterData;
import uk.gov.companieshouse.taf.data.CompanyDetailsRequestData;
import uk.gov.companieshouse.taf.data.CrossBorderMergerNotificationData;
import uk.gov.companieshouse.taf.data.DocumentRequestData;
import uk.gov.companieshouse.taf.domain.BrisMessageHeaderType;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class CommonSteps {

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    @Autowired
    private CompanyDetailsRequestData companyDetailsRequestData;

    @Autowired
    private DocumentRequestData documentDetailsRequestData;

    @Autowired
    private CrossBorderMergerNotificationData crossBorderMergerNotificationData;

    @Autowired
    private BranchDisclosureReceptionData branchDisclosureReceptionData;

    @Autowired
    private BusinessRegisterData addBusinessRegisterData;

    @Autowired
    private BusinessRegisterData removeBusinessRegisterData;

    /**
     * Check the error message has been placed in MongoDB.
     */
    @Then("^I should get a "
            + "(company details|document details|cross border merger|branch disclosure|add Br Notification|remove Br Notification|) "
            + "error message with the error code ([^\"]*)$")
    public void theCorrectErrorWillBeReturnedToTheEcp(String errorType,
            String errorCode) throws Throwable {
        String messageId;

        switch (errorType) {
        case "add Br Notification":
            messageId = addBusinessRegisterData.getMessageId();
            break;
        case "remove Br Notification":
            messageId = removeBusinessRegisterData.getMessageId();
            break;
        case "company details":
            messageId = companyDetailsRequestData.getMessageId();
            break;
        case "document details":
            messageId = documentDetailsRequestData.getMessageId();
            break;
        case "cross border merger":
            messageId = crossBorderMergerNotificationData.getMessageId();
            break;
        case "branch disclosure":
            messageId = branchDisclosureReceptionData.getMessageId();
            break;
        default:
            throw new RuntimeException(errorType + " is not a known error type");
        }

        // and now locate the message in MongoDB and validate it
        BRBusinessError businessError = retrieveMessage
                .checkForMessageByCorrelationId(messageId);
        assertNotNull(businessError);

        assertEquals("Expected Error Code:", errorCode,
                businessError.getFaultError().get(0).getFaultErrorCode().getValue());
    }

    /**
     * Check that the message header is as expected.
     */
    static void validateHeader(BrisMessageHeaderType messageHeader,
            String correlationId,
            String businessRegisterId,
            String countryCode) {
        assertEquals("Correlation ID in header is not as expected",
                correlationId,
                messageHeader.getCorrelationId());

        assertEquals("Business Register ID in header is not as expected",
                businessRegisterId,
                messageHeader.getBusinessRegisterId());

        assertEquals("Business Register Country in header is not as expected",
                countryCode,
                messageHeader.getBusinessRegisterCountry());
    }

}
