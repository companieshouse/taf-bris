package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.util.TestDataHelper;

public class Hooks {

    @Autowired
    private TestDataHelper testDataHelper;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

    /**
     * Inserts the company details data prior to executing the tests to ensure data consistency.
     * Note that this set up step will run for all scenarios.
     */
    @Before
    public void setUpDataForAllScenarios() {
        List<String> companiesToLoad = new ArrayList<>();
        companiesToLoad.add(defaultCompanyNumber);
        testDataHelper.setUpTestData(companiesToLoad);
    }

    /**
     * Cleans up the data that has been injected for testing purposes.
     * Note that this tear down step will run for all scenarios.
     */
    @After
    public void tearDownDataForAllScenarios() {
        List<String> companiesToRemove = new ArrayList<>();
        companiesToRemove.add(defaultCompanyNumber);
        testDataHelper.tearDownTestData(companiesToRemove);
    }
}
