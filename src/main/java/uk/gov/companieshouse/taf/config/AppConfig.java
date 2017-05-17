package uk.gov.companieshouse.taf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages={"uk.gov.companieshouse.taf"})
public class AppConfig {
    @Value("${KAFKA_BROKER_ADDR}")
    private String kafkaBroker;

}