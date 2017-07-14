package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

public class BranchDisclosureNotification implements ParameterMapper {

    private static final String PROCEEDING_TYPE = "proceeding";
    private static final String EFFECTIVE_DATE = "effective_date";

    @JsonProperty(PROCEEDING_TYPE)
    private String proceedingType;

    @JsonProperty(EFFECTIVE_DATE)
    private Date effectiveDate;

    public void setProceedingType(String proceedingType) {
        this.proceedingType = proceedingType;
    }

    public String getProceedingType() {
        return proceedingType;
    }

    @Override
    public Map<String, String> getParameters() {
        return null;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
