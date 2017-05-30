package uk.gov.companieshouse.taf.stepsdef;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsRequest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import eu.europa.ec.bris.v140.jaxb.br.company.detail.BRCompanyDetailsResponse;
import eu.europa.ec.bris.v140.jaxb.br.error.BRBusinessError;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.gov.companieshouse.taf.config.MongoConfig;
import uk.gov.companieshouse.taf.domain.OutgoingBrisMessage;
import uk.gov.companieshouse.taf.service.SendBrisTestMessageService;
import uk.gov.companieshouse.taf.service.RetrieveBrisTestMessageService;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class CompanyDetailsRequestSteps {

    private static final String COMPANY_PROFILE = "company_profile";
    private static final String COMPANY_FILING_HISTORY = "company_filing_history";
    private static final String TEST_COMPANY_PROFILE_FILENAME = "test-company-profile.json";
    private static final String TEST_COMPANY_FILING_HISTORY_FILENAME = "test-company-filing-history.json";
    private static final String COMPANY_NUMBER = "10000000";

    private String messageId = UUID.randomUUID().toString();
    private String correlationId = messageId;

    @Autowired
    private SendBrisTestMessageService companyDetailsRequest;

    @Autowired
    private RetrieveBrisTestMessageService retrieveMessage;

    @Autowired
    @Qualifier("CompanyProfileMongoDbTemplate")
    public MongoTemplate companyProfileMongoTemplate;

    @Autowired
    @Qualifier("CompanyFilingHistoryMongoDbTemplate")
    public MongoTemplate companyFilingHistoryMongoTemplate;

    private OutgoingBrisMessage outgoingBrisMessage;

    @Before
    public void setUpData() throws IOException, ParseException {
        companyProfileMongoTemplate.insert(getJSONFromFile(TEST_COMPANY_PROFILE_FILENAME), COMPANY_PROFILE);
        companyFilingHistoryMongoTemplate.insert(getJSONFromFile(TEST_COMPANY_FILING_HISTORY_FILENAME),
                COMPANY_FILING_HISTORY);
    }

    @After
    public void tearDownData() throws IOException, ParseException {
        companyProfileMongoTemplate.remove(new Query(Criteria.where("_id").is(COMPANY_NUMBER)), COMPANY_PROFILE);
        companyFilingHistoryMongoTemplate.remove(new Query(Criteria.where("company_number").is(COMPANY_NUMBER)),
                COMPANY_FILING_HISTORY);
    }

    private String getJSONFromFile(String filename) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        Object obj = parser.parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.toJSONString();
    }

    /**
     * Create valid company details request.
     */
    @Given("^I am requesting details for a valid company$")
    public void requestingDetailsForAValidCompany() throws Throwable {
        BRCompanyDetailsRequest request = CompanyDetailsHelper.newInstance(
                correlationId,
                messageId,
                COMPANY_NUMBER,
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    /**
     * Create a request with an invalid company number.
     */
    @Given("^I am requesting details for a company that does not exist$")
    public void requestingDetailsForACompanyThatDoesNotExist() throws Throwable {
        BRCompanyDetailsRequest request = CompanyDetailsHelper.newInstance(
                correlationId,
                messageId,
                "00000000",
                "EW",
                "UK");

        outgoingBrisMessage = companyDetailsRequest.createOutgoingBrisMessage(request, messageId);
    }

    @When("^I make a company details request$")
    public void makeACompanyDetailsRequest() throws Throwable {
        companyDetailsRequest.sendOutgoingBrisMessage(outgoingBrisMessage, messageId);
    }

    /**
     * Check the outgoing message has been placed in the right collection.
     */
    @Then("^the correct company details will be returned to the ECP$")
    public void theCorrectCompanyDetailsWillBeReturnedToTheEcp() throws Throwable {
        BRCompanyDetailsResponse response = retrieveMessage
                .checkForResponseByCorrelationId(correlationId);
        assertNotNull(response);
        assertEquals("Expected Correlation ID:", correlationId,
                response.getMessageHeader().getCorrelationID().getValue());
    }

    /**
     * Check the error message has been placed in the right collection.
     */
    @Then("^I should get a message with the error code \"([^\"]*)\"$")
    public void theCorrectErrorWillBeReturnedToTheEcp(String errorCode) throws Throwable {
        BRBusinessError businessError = retrieveMessage
                .checkForResponseByCorrelationId(correlationId);
        assertNotNull(businessError);

        assertEquals("Expected Error Code:", errorCode,
                businessError.getFaultError().get(0).getFaultErrorCode().getValue());
    }
}