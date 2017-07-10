package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class ManageSubscription implements ParameterMapper {

    private static final String SUBSCRIPTION_TYPE = "subscription_type";
    private static final String FOREIGN_REGISTER_ID = "foreign_register_id";
    private static final String FOREIGN_COUNTRY_CODE = "foreign_country_code";

    @JsonProperty(SUBSCRIPTION_TYPE)
    private String subscriptionType;

    @JsonProperty(FOREIGN_REGISTER_ID)
    private String foreignRegisterId;

    @JsonProperty(FOREIGN_COUNTRY_CODE)
    private String foreignCountryCode;

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(SUBSCRIPTION_TYPE, subscriptionType);
        paramMap.put(FOREIGN_REGISTER_ID, foreignRegisterId);
        paramMap.put(FOREIGN_COUNTRY_CODE, foreignCountryCode);
        return paramMap;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public void setForeignRegisterId(String foreignRegisterId) {
        this.foreignRegisterId = foreignRegisterId;
    }

    public void setForeignCountryCode(String foreignCountryCode) {
        this.foreignCountryCode = foreignCountryCode;
    }

    @Override
    public String toString() {
        return SUBSCRIPTION_TYPE + " : " + subscriptionType + ", "
                + FOREIGN_REGISTER_ID + " : " + foreignRegisterId + ", "
                + FOREIGN_COUNTRY_CODE + " : " + foreignCountryCode;
    }
}
