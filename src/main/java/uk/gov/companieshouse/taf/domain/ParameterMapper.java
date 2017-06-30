package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface ParameterMapper {
    
    @JsonIgnore
    Map<String, String> getParameters();
}
