package uk.gov.companieshouse.taf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.gov.companieshouse.taf.config.constants.HttpConstants;

/**
 * Configuration for connection to BRIS REST API.
 */
@Configuration
public class HttpConfig {

    /**
     * Connection factory to REST API.
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);
        factory.setConnectTimeout(HttpConstants.HTTP_CONNECTION_TIMEOUT);
        return factory;
    }

    /**
     * Bean for Spring REST Template.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    /**
     * Bean for HTTP headers for Spring REST Template invocation.
     */
    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
