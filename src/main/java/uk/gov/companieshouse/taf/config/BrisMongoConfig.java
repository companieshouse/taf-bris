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
 * This Mongo configuration is used to load data into the BRIS application incoming
 * collection in order to simulate certain failure scenarios.
 */
@Configuration
public class BrisMongoConfig extends MongoConfig {

    private static final String BRIS_MESSAGES_DATABASE = "bris_messages";

    @Autowired
    private Env env;

    // Mongo config for BRIS Database
    @Bean
    @Qualifier("BrisMongoDbTemplate")
    public MongoTemplate templateBris() throws UnknownHostException {
        return new MongoTemplate(brisMongoDbFactory());
    }

    private MongoDbFactory brisMongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(env.config.getString(MONGO_URI)
                + URI_SLASH + BRIS_MESSAGES_DATABASE));
    }
}
