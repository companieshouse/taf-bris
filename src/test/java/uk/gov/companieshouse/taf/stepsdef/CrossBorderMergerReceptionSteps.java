package uk.gov.companieshouse.taf.stepsdef;

import static junit.framework.TestCase.assertNotNull;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotification;
import eu.europa.ec.bris.v140.jaxb.br.merger.BRCrossBorderMergerReceptionNotificationAcknowledgement;
import eu.europa.ec.bris.v140.jaxb.components.basic.BusinessRegisterIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CompanyEUIDType;
import eu.europa.ec.bris.v140.jaxb.components.basic.CountryType;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.data.CrossBorderMergerNotificationData;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.util.CrossBorderMergerNotificationRequestBuilder;

public class CrossBorderMergerReceptionSteps {

    @Autowired
    private CrossBorderMergerNotificationData data;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    @Autowired
    private SendBrisTestMessageService sendBrisTestMessageService;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

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
        data.setIssuingCountryCode(RandomStringUtils.randomAlphabetic(3));
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        data.setOutgoingBrisMessage((sendBrisTestMessageService.createOutgoingBrisMessage(
                notification, data.getMessageId())));
    }

    /**
     * Create a cross border merger notification with an invalid company EUID.
     * Valid e.g. UKEW.00006400
     */
    @Given("^the notification does not have a valid (country|business register id)"
            + " in company EUID$")
    public void theNotificationDoesNotHaveAValidCompanyEuid(String euidElement) throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        CompanyEUIDType companyEuidType = new CompanyEUIDType();

        if ("country".equalsIgnoreCase(euidElement)) {
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
     */
    @Given("^the notification does not have a valid company EUID$")
    public void theNotificationDoesNotHaveAValidCompanyEuid() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        // Set valid country code
        CountryType countryType = new CountryType();
        countryType.setValue("UK");
        notification.getNotificationContext().getIssuingOrganisation()
                .setBusinessRegisterCountry(countryType);

        // Set invalid business register id. EW/SC/NI are valid
        BusinessRegisterIDType businessRegisterIdType = new BusinessRegisterIDType();
        businessRegisterIdType.setValue("AB");
        notification.getNotificationContext().getIssuingOrganisation()
                .setBusinessRegisterID(businessRegisterIdType);

        CompanyEUIDType companyEuidType = new CompanyEUIDType();
        companyEuidType.setValue("UKAB.99990000");
        notification.getResultingCompany().setCompanyEUID(companyEuidType);

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
    @Given("^the notification does not have a resulting company registered office$")
    public void theNotificationDoesNotHaveAResultingCompanyRegisteredOffice() throws Throwable {
        BRCrossBorderMergerReceptionNotification notification =
                CrossBorderMergerNotificationRequestBuilder
                        .getCrossBorderMergerNotification(data);

        // Address items have a minimal value of 1
        // Setting to space to ensure we throw expected error
        notification.getResultingCompany().getCompanyRegisteredOffice()
                .getAddressLine1().setValue(" ");
        notification.getResultingCompany().getCompanyRegisteredOffice()
                .getAddressLine2().setValue(" ");
        notification.getResultingCompany().getCompanyRegisteredOffice()
                .getAddressLine3().setValue(" ");
        notification.getResultingCompany().getCompanyRegisteredOffice()
                .getCity().setValue(" ");
        notification.getResultingCompany().getCompanyRegisteredOffice()
                .getPostalCode().setValue(" ");

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
                retrieveMessage.checkForResponseByCorrelationId(data.getCorrelationId());

        assertNotNull(ack);

        // And assert that the header details are correct
        CommonSteps.validateHeader(ack.getMessageHeader(), data.getCorrelationId(),
                data.getBusinessRegisterId(), data.getCountryCode());

    }
}
