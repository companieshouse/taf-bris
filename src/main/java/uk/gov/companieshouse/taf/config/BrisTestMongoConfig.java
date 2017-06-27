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
import uk.gov.companieshouse.taf.config.constants.ConfigConstants;
import uk.gov.companieshouse.taf.config.constants.MongoConfig;

/**
 * This Mongo configuration is used to load data into the BRIS test incoming
 * collection.
 */
@Configuration
public class BrisTestMongoConfig {

    private static final String BRIS_MESSAGES_TEST_DATABASE = "bris_messages_test";

    @Autowired
    private Env env;

    // Mongo config for Bris Test Database for incoming and outgoing records
    @Bean
    @Qualifier("BrisTestMongoDbOperations")
    public MongoOperations mongoOperationsBrisTest() throws UnknownHostException {
        return new MongoTemplate(mongoTestDbFactory());
    }

    private MongoDbFactory mongoTestDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(
                env.config.getString(MongoConfig.MONGO_URI)
                + MongoConfig.URI_SLASH + BRIS_MESSAGES_TEST_DATABASE));
    }
}
