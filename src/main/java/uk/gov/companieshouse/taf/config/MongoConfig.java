package uk.gov.companieshouse.taf.config;

import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.gov.companieshouse.taf.repository.IncomingBrisMessageRepository;
import uk.gov.companieshouse.taf.repository.OutgoingBrisMessageRepository;


@Configuration
@EnableMongoRepositories(basePackageClasses = {IncomingBrisMessageRepository.class,
        OutgoingBrisMessageRepository.class},
        mongoTemplateRef = "template",
        includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Repository")})

@ComponentScan(basePackages = {"uk.gov.companieshouse.taf"})
@PropertySource("classpath:application.properties")
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.company.profile.uri}")
    private String mongoCompanyProfileUri;

    @Value("${spring.data.mongodb.company.filing.history.uri}")
    private String mongoCompanyFilingHistoryUri;

    // Mongo config for Test Database for incoming and outgoing records
    @Bean
    public MongoTemplate template() throws UnknownHostException {
        return new MongoTemplate(mongoTestDbFactory());
    }

    @Bean
    @Qualifier("TestMongoDbFactory")
    public MongoDbFactory mongoTestDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(mongoUri));
    }

    // Mongo config for Company Profile Database
    @Bean
    @Qualifier("CompanyProfileMongoDbTemplate")
    public MongoTemplate templateCompanyProfile() throws UnknownHostException {
        return new MongoTemplate(mongoCompanyProfileDbFactory());
    }

    @Bean
    @Qualifier("CompanyProfileMongoDbFactory")
    public MongoDbFactory mongoCompanyProfileDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(mongoCompanyProfileUri));
    }

    // Mongo config for Company Filing History Database
    @Bean
    @Qualifier("CompanyFilingHistoryMongoDbTemplate")
    public MongoTemplate templateCompanyFilingHistory() throws UnknownHostException {
        return new MongoTemplate(mongoCompanyFilingHistoryDbFactory());
    }

    @Bean
    @Qualifier("CompanyFilingHistoryMongoDbFactory")
    public MongoDbFactory mongoCompanyFilingHistoryDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(mongoCompanyFilingHistoryUri));
    }
}
