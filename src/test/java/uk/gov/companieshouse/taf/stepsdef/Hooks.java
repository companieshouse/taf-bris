package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class Hooks {

    private static final String COMPANY_PROFILE = "company_profile";
    private static final String COMPANY_FILING_HISTORY = "company_filing_history";
    private static final String TEST_COMPANY_PROFILE_FILENAME = "test-company-profile.json";
    private static final String TEST_COMPANY_FILING_HISTORY_FILENAME = "test-company-filing"
            + "-history.json";
    private static final String COMPANY_NUMBER = "10000000";

    @Autowired
    @Qualifier("CompanyProfileMongoDbTemplate")
    private MongoTemplate companyProfileMongoTemplate;

    @Autowired
    @Qualifier("CompanyFilingHistoryMongoDbTemplate")
    private MongoTemplate companyFilingHistoryMongoTemplate;

    /**
     * Inserts the company details data prior to executing the tests to ensure data consistency.
     */
    @Before
    public void setUpData() throws IOException, ParseException {
        companyProfileMongoTemplate.insert(getJsonFromFile(TEST_COMPANY_PROFILE_FILENAME),
                COMPANY_PROFILE);
        companyFilingHistoryMongoTemplate.insert(getJsonFromFile(
                TEST_COMPANY_FILING_HISTORY_FILENAME),
                COMPANY_FILING_HISTORY);
    }

    /**
     * Cleans up the data that has been injected for testing purposes.
     */
    @After
    public void tearDownData() throws IOException, ParseException {
        companyProfileMongoTemplate.remove(new Query(Criteria.where("_id").is(COMPANY_NUMBER)),
                COMPANY_PROFILE);
        companyFilingHistoryMongoTemplate.remove(new Query(Criteria.where("company_number")
                        .is(COMPANY_NUMBER)),
                COMPANY_FILING_HISTORY);
    }

    private String getJsonFromFile(String filename) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        Object obj = parser.parse(new FileReader(file));
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.toJSONString();
    }
}
