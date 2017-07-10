package uk.gov.companieshouse.taf;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.companieshouse.taf.config.AppConfig;

@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class StepsConfig {
}