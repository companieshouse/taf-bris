package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotificationAcknowledgement;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyEUIDType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.builders.CrossBorderMergerNotificationRequestBuilder;
import uk.gov.companieshouse.taf.data.CrossBorderMergerNotificationData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;

public class CrossBorderMergerReceptionSteps {

    @Autowired
    @Qualifier("CrossBorderMergerNotification")
    private CrossBorderMergerNotificationData data;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    @Autowired
    private SendBrisTestMessageService sendBrisTestMessageService;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    private static final String EUID_COUNTRY = "country";

    /**
     * Create a cross border merger.
     */
    @Given("^a valid cross border merger request is created$")
    public void crossBorderMergerRequestExists() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
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
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
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
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid legal form code.
     */
    @Given("^the notification does not have a valid legal form code$")
    public void theNotificationDoesNotHaveALegalFormCode() throws Throwable {
        data.setLegalFormCode("LF_UK_999");
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid issuing country code.
     */
    @Given("^the notification has an invalid issuing country code$")
    public void theNotificationHasAnInvalidIssuingCountryCode() throws Throwable {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('A', 'Z').build();
        data.setIssuingCountryCode(generator.generate(3));

        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid company EUID.
     * Valid e.g. UKEW.00006400
     *
     * @param euidElement part of the EUID to be set.
     */
    @Given("^the notification does not have a valid (country|business register id)"
            + " in company EUID$")
    public void theNotificationDoesNotHaveAValidCompanyEuid(String euidElement) throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        CompanyEUIDType companyEuidType = new CompanyEUIDType();

        if (StringUtils.equalsAnyIgnoreCase(EUID_COUNTRY, euidElement)) {
            companyEuidType.setValue("ULEW.99990000");
        } else {
            companyEuidType.setValue("UKRR.99990000");
        }

        notification.getResultingCompany().setCompanyEUID(companyEuidType);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid company EUID.
     * Sets the business register id to an invalid entry. Valid UK entries include;
     * EW
     * SC
     * NI
     */
    @Given("^the notification does not have a valid company EUID$")
    public void theNotificationDoesNotHaveAValidCompanyEuid() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        // Set valid country code
        notification.getNotificationContext().getIssuingOrganisation()
                .getBusinessRegisterCountry().setValue("UK");

        // Set invalid business register id. EW/SC/NI are valid
        notification.getNotificationContext().getIssuingOrganisation()
                .getBusinessRegisterID().setValue("AB");

        // Set the EUID with the above details making it invalid
        notification.getResultingCompany().getCompanyEUID().setValue("UKAB.99990000");

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with a merging company EUID that
     * does not match the recipient organisation.
     */
    @Given("^the notification has an EUID that is not for the recipient organisation$")
    public void theNotificationHasAnEuidThatIsNotForTheRecipientOrganisation() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        notification.getMergingCompany().get(0).getCompanyEUID().setValue("UKAA.99990000");

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid address.
     * Address MUST have at least country field completed.
     */
    @Given("^the notification does not have a merging company registered office$")
    public void theNotificationDoesNotHaveACompanyRegisteredOffice() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        // Address lines have a minimal length of 1 so setting address
        // line 1 so that it passes schema validation but fails business validation
        // address parts should not be empty
        notification.getMergingCompany().get(0).getCompanyRegisteredOffice()
                .getAddressLine1().setValue(" ");

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with a business id that does not match the
     * recipient business register id.
     */
    @Given("^the notification has a business register that does not match business register"
            + " in the message$")
    public void theNotificationHasABusinessRegisterThatDoesNotMatchBusinessRegisterInTheMessage()
            throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        notification.getRecipientOrganisation().getBusinessRegisterID().setValue("NI");
        notification.getMergingCompany().get(0).getCompanyEUID().setValue("UKNI.99990000");

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
                retrieveMessage.checkForMessageByCorrelationId(data.getCorrelationId());

        assertNotNull(ack);

        // And assert that the header details are correct
        CommonSteps.validateHeader(ack.getMessageHeader(), data.getCorrelationId(),
                data.getBusinessRegisterId(), data.getCountryCode());

    }
}
