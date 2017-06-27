package uk.gov.companieshouse.taf.config;

import com.mongodb.MongoClientURI;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import uk.gov.companieshouse.taf.config.constants.MongoConfig;

/**
 * This Mongo configuration is used to load test data into Mongo from JSON files.
 */
@Configuration
public class ChsMongoConfig {

    private static final String COMPANY_PROFILE_DATABASE = "company_profile";
    private static final String COMPANY_FILING_HISTORY_DATABASE = "company_filing_history";

    @Autowired
    private Env env;

    /**
     * Mongo config for Company Profile Mongo Operations.
     */
    @Bean
    @Qualifier("CompanyProfileMongoDbOperations")
    public MongoOperations mongoOperations() throws UnknownHostException {
        return new MongoTemplate(mongoCompanyProfileDbFactory());
    }

    /*
     * Mongo config for Company Profile MongoDb Factory.
     */
    private MongoDbFactory mongoCompanyProfileDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(
                env.config.getString(MongoConfig.MONGO_URI)
                + MongoConfig.URI_SLASH + COMPANY_PROFILE_DATABASE));
    }

    /**
     * Mongo config for Company Filing History Mongo Operations.
     */
    @Bean
    @Qualifier("CompanyFilingHistoryMongoDbOperations")
    public MongoOperations mongoOperationsCompanyFilingHistory() throws UnknownHostException {
        return new MongoTemplate(mongoCompanyFilingHistoryDbFactory());
    }

    /*
     * Mongo config for Company Filing History Factory.
     */
    private MongoDbFactory mongoCompanyFilingHistoryDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(
                env.config.getString(MongoConfig.MONGO_URI)
                + MongoConfig.URI_SLASH + COMPANY_FILING_HISTORY_DATABASE));
    }
}
