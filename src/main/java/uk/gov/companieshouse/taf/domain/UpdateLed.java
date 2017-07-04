package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class UpdateLed implements ParameterMapper {

    private static final String UPDATE_TYPE = "update_type";

    @JsonProperty(UPDATE_TYPE)
    private String updateType;
    
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(UPDATE_TYPE, updateType);
        return paramMap;
    }
    
    @Override
    public String toString() {
        return UPDATE_TYPE + " : " + updateType + ", ";
    }
}
