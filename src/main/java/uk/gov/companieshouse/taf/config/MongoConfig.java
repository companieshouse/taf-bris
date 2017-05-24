package uk.gov.companieshouse.taf.config;

import com.mongodb.Mongo;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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

    @Value("${mongo.host}")
    private String mongoHost;

    @Value("${mongo.port}")
    private int mongoPort;

    @Value("${spring.data.mongodb.uri}")
    private String mongoURI;

    @Primary
    @Bean
    public MongoTemplate template() throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public Mongo mongo() throws UnknownHostException {
        return new MongoClient(mongoHost, mongoPort);
    }

    @Primary
    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(mongoURI));
    }
}
