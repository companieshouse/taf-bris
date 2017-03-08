import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
features = {"C:/Users/rsuller/IdeaProjects/taf-bris/src/test/resources/features/example.feature"},
plugin = {"json:target/cucumber-reports/1.json"},
monochrome = false,
 tags = {"@regression"}, glue = { "io.magentys.cinnamon", "uk.gov.companieshouse.taf" })
public class ExampleIT {
}
