package uk.gov.companieshouse.taf.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * This class can be used to load the relevant test data for the test framework.
 */

@Component
public class TestDataHelper {
    private static final String COMPANY_PROFILE = "company_profile";
    private static final String COMPANY_FILING_HISTORY = "company_filing_history";
    private static final String TEST_DATA_FOLDER = "testdata";

    @Autowired
    @Qualifier("CompanyProfileMongoDbTemplate")
    private MongoTemplate companyProfileMongoTemplate;

    @Autowired
    @Qualifier("CompanyFilingHistoryMongoDbTemplate")
    private MongoTemplate companyFilingHistoryMongoTemplate;

    /**
     * Set up the test data for the specified company.
     * @param companyNumber The company number to load JSON files for
     */
    public void setUpTestData(String companyNumber) {

        ClassLoader classLoader = getClass().getClassLoader();

        // Firstly locate the folder containing the test data for the relevant company..
        File file = new File(classLoader.getResource(TEST_DATA_FOLDER
                + "//" + companyNumber).getFile());

        if (!file.isDirectory()) {
            throw new RuntimeException("Unable to locate the test folder for the company "
                    + companyNumber);
        }

        File[] testFiles = file.listFiles();

        // Ensure that there are test file for the selected company
        if (testFiles == null) {
            throw new RuntimeException("No test files located for company "
                    + companyNumber);
        }

        // Then iterate through each file and save it to MongoDB
        for (File testFile : testFiles) {
            if (StringUtils.contains(testFile.getName(),
                    COMPANY_FILING_HISTORY.replace("_", "-"))) {
                // Then load a company filing history
                companyFilingHistoryMongoTemplate.insert(getJsonFromFile(testFile),
                        COMPANY_FILING_HISTORY);
            } else if (StringUtils.contains(testFile.getName(),
                    COMPANY_PROFILE.replace("_", "-"))) {
                // Otherwise add a company profile
                companyProfileMongoTemplate.insert(getJsonFromFile(testFile),
                        COMPANY_PROFILE);
            }
        }
    }

    /**
     * Method to remove the company records from MongoDB.
     * @param companyNumber The company number to be used to remove from MongoDB
     */
    public void tearDownTestData(String companyNumber) {
        companyProfileMongoTemplate.remove(new Query(Criteria.where("_id")
                .is(companyNumber)), COMPANY_PROFILE);
        companyFilingHistoryMongoTemplate.remove(new Query(Criteria.where("company_number")
                        .is(companyNumber)),
                COMPANY_FILING_HISTORY);
    }

    private static String getJsonFromFile(File jsonFile) {
        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(new FileReader(jsonFile));
        } catch (IOException | ParseException ex) {
            throw new RuntimeException("Unable to read JSON file " + ex.getMessage());
        }

        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.toJSONString();
    }
}
