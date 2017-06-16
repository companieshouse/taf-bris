package uk.gov.companieshouse.taf.config;

import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * This Mongo configuration is used to load test data into Mongo from JSON files.
 * It therefore isn't necessary to load this data via a Spring Mongo repository but
 * can be loaded directly using the Mongo Template API
 */
@Configuration
public class ChsMongoConfig extends MongoConfig {

    private static final String COMPANY_PROFILE_DATABASE = "company_profile";
    private static final String COMPANY_FILING_HISTORY_DATABASE = "company_filing_history";

    @Autowired
    private Env env;

    /**
     * Mongo config for Company Profile Template.
     */
    @Bean
    @Qualifier("CompanyProfileMongoDbTemplate")
    public MongoTemplate templateCompanyProfile() throws UnknownHostException {
        return new MongoTemplate(mongoCompanyProfileDbFactory());
    }

    /**
     * Mongo config for Company Profile MongoDb Factory.
     */
    @Bean
    @Qualifier("CompanyProfileMongoDbFactory")
    private MongoDbFactory mongoCompanyProfileDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(
                env.config.getString(MongoConfig.MONGO_URI)
                + MongoConfig.URI_SLASH + COMPANY_PROFILE_DATABASE));
    }

    /**
     * Mongo config for Company Filing History Template.
     */
    @Bean
    @Qualifier("CompanyFilingHistoryMongoDbTemplate")
    public MongoTemplate templateCompanyFilingHistory() throws UnknownHostException {
        return new MongoTemplate(mongoCompanyFilingHistoryDbFactory());
    }

    /**
     * Mongo config for Company Filing History Factory.
     */
    @Bean
    @Qualifier("CompanyFilingHistoryMongoDbFactory")
    private MongoDbFactory mongoCompanyFilingHistoryDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(
                env.config.getString(MongoConfig.MONGO_URI)
                + MongoConfig.URI_SLASH + COMPANY_FILING_HISTORY_DATABASE));
    }
}
