package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java8.En;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import uk.gov.companieshouse.taf.config.AppConfig;

@ContextConfiguration(classes = {AppConfig.class})
@TestPropertySource(properties = {"KAFKA_BROKER_ADDR = chs-kafka:9092"})
public class StepsBase implements En {
}