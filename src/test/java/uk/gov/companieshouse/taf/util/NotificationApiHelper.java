package uk.gov.companieshouse.taf.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.companieshouse.taf.config.Env;
import uk.gov.companieshouse.taf.data.UpdateLedData;

/**
 * Helper class to encapsulate a call to the BRIS Notification REST API .
 */
@Component
public class NotificationApiHelper {

    private static final String BRIS_NOTIFICATION_API_URL = "bris.notification.api.url";
    private static final Logger logger = LoggerFactory.getLogger(NotificationApiHelper.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeaders headers;

    @Autowired
    private Env env;

    @Autowired
    private UpdateLedData data;

    /**
     * Method to call a REST service on the BRIS Notification API.
     *
     * @param requestBody    the request body to be sent to the REST API.
     * @param brisUrlContext the URL context to execute the required function.
     * @return the response from the REST API call.
     */
    public JSONObject callNotificationRestApi(String requestBody,
                                              String brisUrlContext) {
        HttpEntity entity = new HttpEntity<>(requestBody, headers);
        String url = env.config.getString(BRIS_NOTIFICATION_API_URL) + brisUrlContext + "/"
                + data.getCompanyNumber();

        logger.info("Url: {}", url);

        ResponseEntity<String> result = restTemplate.exchange(url,
                HttpMethod.POST, entity, String.class);

        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(result.getBody());
        } catch (ParseException ex) {
            throw new RuntimeException("Error parsing JSON" + ex.getMessage());
        }
    }
}
