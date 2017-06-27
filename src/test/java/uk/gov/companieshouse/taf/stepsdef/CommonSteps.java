package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import cucumber.api.java.en.Then;
import eu.europa.ec.bris.v140.jaxb.br.aggregate.MessageHeaderType;
import eu.europa.ec.bris.v140.jaxb.br.error.BRBusinessError;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.taf.data.RequestData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;

public class CommonSteps {

    @Autowired
    private RequestData data;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;


    /**
     * Check the error message has been placed in the right collection.
     */
    @Then("^I should get a message with the error code ([^\"]*)$")
    public void theCorrectErrorWillBeReturnedToTheEcp(String errorCode) throws Throwable {
        BRBusinessError businessError = retrieveMessage
                .checkForResponseByCorrelationId(data.getMessageId());
        assertNotNull(businessError);

        assertEquals("Expected Error Code:", errorCode,
                businessError.getFaultError().get(0).getFaultErrorCode().getValue());

        // And assert that the header details are correct
        validateHeader(businessError.getMessageHeader());
    }

    /*
     Check that the message header is as expected.
     */
    protected void validateHeader(MessageHeaderType messageHeader) {
        assertEquals("Correlation ID in header is not as expected",
                data.getMessageId(),
                messageHeader.getCorrelationID().getValue());

        assertEquals("Business Register ID in header is not as expected",
                data.getBusinessRegisterId(),
                messageHeader.getBusinessRegisterReference().getBusinessRegisterID().getValue());

        assertEquals("Business Register Country in header is not as expected",
                data.getCountryCode(),
                messageHeader.getBusinessRegisterReference()
                        .getBusinessRegisterCountry().getValue());
    }
}
