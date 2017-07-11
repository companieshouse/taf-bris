package uk.gov.companieshouse.taf;

import cucumber.api.java8.En;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.companieshouse.taf.config.AppConfig;

@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class StepsConfig implements En{
}