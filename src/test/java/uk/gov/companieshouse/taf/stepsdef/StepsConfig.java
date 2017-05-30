package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java8.En;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.companieshouse.taf.config.AppConfig;

@ContextConfiguration(classes = {AppConfig.class})
public class StepsConfig implements En {
}