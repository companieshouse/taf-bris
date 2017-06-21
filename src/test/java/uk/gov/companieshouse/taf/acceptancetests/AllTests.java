package uk.gov.companieshouse.taf.acceptancetests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"uk.gov.companieshouse.taf"},
        features = "src/test/resources/features",
        tags = {"~@regression"},
        format = {"pretty", "json:target/cucumber-reports/cucumber.json",
        "html:target/TestReport"},
        monochrome = true
)
public class AllTests {
}
