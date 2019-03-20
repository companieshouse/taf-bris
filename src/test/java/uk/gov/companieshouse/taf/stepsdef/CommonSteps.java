package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Then;
import eu.europa.ec.bris.jaxb.br.components.aggregate.v1_4.MessageHeaderType;
import eu.europa.ec.bris.jaxb.br.error.v1_4.BRBusinessError;
import eu.europa.ec.digit.message.container.jaxb.v1_0.ContainerHeader;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.data.BranchDisclosureReceptionData;
import uk.gov.companieshouse.taf.data.CompanyDetailsRequestData;
import uk.gov.companieshouse.taf.data.CrossBorderMergerNotificationData;
import uk.gov.companieshouse.taf.data.DocumentRequestData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;

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

    /**
     * Check the error message has been placed in MongoDB.
     */
    @Then("^I should get a "
            + "(company details|document details|cross border merger|branch disclosure) "
            + "error message with the error code ([^\"]*)$")
    public void theCorrectErrorWillBeReturnedToTheEcp(String errorType,
                                                      String errorCode) throws Throwable {
        String messageId;

        switch (errorType) {
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
     Check that the message header is as expected.
     */
    static void validateHeader(MessageHeaderType messageHeader,
            String correlationId,
            String businessRegisterId,
            String countryCode) {
        assertEquals("Correlation ID in header is not as expected",
                correlationId,
                messageHeader.getCorrelationID().getValue());

        assertEquals("Business Register ID in header is not as expected",
                businessRegisterId,
                messageHeader.getBusinessRegisterReference().getBusinessRegisterID().getValue());

        assertEquals("Business Register Country in header is not as expected",
                countryCode,
                messageHeader.getBusinessRegisterReference()
                        .getBusinessRegisterCountry().getValue());
    }

    /**
        Check that the message header is as expected.
     */
    static void validateHeader(ContainerHeader messageHeader,
                                      String correlationId,
                                      String businessRegisterId,
                                      String countryCode) {
        assertEquals("Correlation ID in header is not as expected",
                correlationId,
                messageHeader.getMessageInfo().getCorrelationID());
        assertEquals("Business Register ID in header is not as expected",
                businessRegisterId,
                messageHeader.getAddressInfo().getSender().getCode());

        assertEquals("Business Register Country in header is not as expected",
                countryCode,
                messageHeader.getAddressInfo().getSender().getCountryCode());
    }
}
