package uk.gov.companieshouse.taf.config;

import com.mongodb.Mongo;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.gov.companieshouse.taf.domain.OutgoingBRISMessage;
import uk.gov.companieshouse.taf.repository.IncomingBRISMessageRepository;
import uk.gov.companieshouse.taf.repository.OutgoingBRISMessageRepository;

import java.net.UnknownHostException;

@Configuration
@EnableMongoRepositories(basePackageClasses = {IncomingBRISMessageRepository.class, OutgoingBRISMessageRepository.class},
        mongoTemplateRef = "template",
        includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Repository")}
)
public class MongoConfig {

    @Bean
    public Mongo mongo() throws UnknownHostException {
        return new Mongo("chs-mongo", 27017);
    }

    @Primary
    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(mongo(), "bris_messages_test");
    }

    @Primary
    @Bean
    public MongoTemplate template() throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory());
    }
}
