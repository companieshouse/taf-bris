package uk.gov.companieshouse.taf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import uk.gov.companieshouse.taf.config.constants.HttpConstants;

/**
 * Configuration for connection to BRIS REST API.
 */
@Configuration
public class HttpConfig {

    /**
     * Bean for connection factory to REST API.
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);
        factory.setConnectTimeout(HttpConstants.HTTP_CONNECTION_TIMEOUT);
        return factory;
    }
}
