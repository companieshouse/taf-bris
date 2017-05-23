package uk.gov.companieshouse.taf.config;

import com.mongodb.Mongo;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.gov.companieshouse.taf.repository.IncomingBRISMessageRepository;
import uk.gov.companieshouse.taf.repository.OutgoingBRISMessageRepository;


@Configuration
@EnableMongoRepositories(basePackageClasses = {IncomingBRISMessageRepository.class,
        OutgoingBRISMessageRepository.class},
        mongoTemplateRef = "template",
        includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Repository")}
)

@ComponentScan(basePackages = {"uk.gov.companieshouse.taf"})
@PropertySource("classpath:application.properties")
public class MongoConfig {

    @Value("${mongo.host}")
    private String mongoHost;

    @Value("${mongo.port}")
    private int mongoPort;

    @Value("${mongo.database}")
    private String mongoDatabase;

    @Primary
    @Bean
    public MongoTemplate template() throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public Mongo mongo() throws UnknownHostException {
        return new Mongo(mongoHost, mongoPort);
    }

    @Primary
    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(mongo(), mongoDatabase);
    }
}
