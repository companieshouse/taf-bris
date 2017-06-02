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
    private static final String COMPANY_NUMBER = "10000000";
    private static final String COMPANY_PROFILES_FOLDER = "src/test/resources/data/"
            + "company_profiles";
    private static final String COMPANY_FILING_HISTORY_FOLDER = "src/test/resources/data/"
            + "company_filing_history";


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
        // Add all company profiles from the data source folder
        addCompanyProfileData();
        // Add all company filing history data
        addCompanyFilingHistoryData();
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

    private void addCompanyProfileData() throws IOException, ParseException {
        File folder = new File(COMPANY_PROFILES_FOLDER);
        File[] listOfFiles = folder.listFiles((file, name) -> name.endsWith(".json"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String fileNames = file.getName();
                companyProfileMongoTemplate.insert(getJsonFromFile(fileNames),
                        COMPANY_PROFILE);
            }
        }
    }

    private void addCompanyFilingHistoryData() throws IOException, ParseException {
        File folder = new File(COMPANY_FILING_HISTORY_FOLDER);
        File[] listOfFiles = folder.listFiles((file, name) -> name.endsWith(".json"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String fileNames = file.getName();
                companyFilingHistoryMongoTemplate.insert(getJsonFromFile(
                        fileNames),
                        COMPANY_FILING_HISTORY);
            }
        }
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
