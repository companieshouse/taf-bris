package uk.gov.companieshouse.taf.config;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import uk.gov.companieshouse.taf.config.constants.HttpConstants;
import uk.gov.companieshouse.taf.config.constants.MongoConstants;

import java.net.UnknownHostException;

/**
 */
@Configuration
public class HttpConfig {

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);
        factory.setConnectTimeout(HttpConstants.HTTP_CONNECTION_TIMEOUT);
        return factory;
    }
}
