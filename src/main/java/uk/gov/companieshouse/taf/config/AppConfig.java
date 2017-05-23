package uk.gov.companieshouse.taf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"uk.gov.companieshouse.taf"})
@PropertySource("classpath:/application.properties")
public class AppConfig {

    @Value("${KAFKA_BROKER_ADDR}")
    private String kafkaBroker;

}