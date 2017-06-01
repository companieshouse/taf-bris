package uk.gov.companieshouse.taf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"uk.gov.companieshouse.taf"})
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public Env getEnv() {
        return Env.INSTANCE;
    }
}