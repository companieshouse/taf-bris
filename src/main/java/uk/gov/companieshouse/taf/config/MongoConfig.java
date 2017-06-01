package uk.gov.companieshouse.taf.config;

import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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

    private static final String COMPANY_PROFILE_DATABASE = "company_profile";
    private static final String MONGO_URI = "mongodb.instance";
    private static final String COMPANY_FILING_HISTORY_DATABASE = "company_filing_history";
    private static final String BRIS_MESSAGES_DATABASE = "bris_messages_test";
    private static final String URI_SLASH = "/";

    @Autowired
    private Env env;

    // Mongo config for Test Database for incoming and outgoing records
    @Bean
    public MongoTemplate template() throws UnknownHostException {
        return new MongoTemplate(mongoTestDbFactory());
    }

    @Bean
    @Qualifier("TestMongoDbFactory")
    public MongoDbFactory mongoTestDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(env.config.getString(MONGO_URI)
                + URI_SLASH + BRIS_MESSAGES_DATABASE));
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
        return new SimpleMongoDbFactory(new MongoClientURI(env.config.getString(MONGO_URI)
                + URI_SLASH + COMPANY_PROFILE_DATABASE));
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
        return new SimpleMongoDbFactory(new MongoClientURI(env.config.getString(MONGO_URI)
                + URI_SLASH + COMPANY_FILING_HISTORY_DATABASE));
    }
}
