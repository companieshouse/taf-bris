package uk.gov.companieshouse.taf.stepsdef;

import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Allows data to be shared across Steps in different classes as this will be autowired
 * as a singleton and recreated after each scenario is executed.
 */

@Component
public class RequestData {

    private String messageId = UUID.randomUUID().toString();
    private String correlationId = messageId;

    public String getMessageId() {
        return messageId;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
