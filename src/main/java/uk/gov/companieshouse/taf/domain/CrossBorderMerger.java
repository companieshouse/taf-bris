package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CrossBorderMerger implements ParameterMapper {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String MERGER_TYPE = "merger_type";
    private static final String EFFECTIVE_DATE = "effective_date";
    private static final String MERGING_COMPANIES = "merging_companies";

    private static final String MESSAGE_ID = "message_id";

    private static final String RECIPIENT_FOREIGN_REGISTER_ID = "recipient_foreign_register_id";
    private static final String RECIPIENT_FOREIGN_COUNTRY_CODE = "recipient_foreign_country_code";
    private static final String RECIPIENT_FOREIGN_REGISTER_NAME = "recipient_foreign_register_name";

    private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    @JsonProperty(MERGER_TYPE)
    private String mergerType;

    @JsonProperty(EFFECTIVE_DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, timezone = "GMT")
    private Date effectiveDate;

    @JsonProperty(MERGING_COMPANIES)
    private List<MergingCompany> mergingCompanies;

    @JsonProperty(MESSAGE_ID)
    private String messageId;

    @JsonProperty(RECIPIENT_FOREIGN_REGISTER_NAME)
    private String recipientForeignRegisterName;

    @JsonProperty(RECIPIENT_FOREIGN_REGISTER_ID)
    private String recipientForeignRegisterId;

    @JsonProperty(RECIPIENT_FOREIGN_COUNTRY_CODE)
    private String recipientForeignCountryCode;

    public String getMergerType() {
        return mergerType;
    }

    public void setMergerType(String mergerType) {
        this.mergerType = mergerType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public List<MergingCompany> getMergingCompanies() {
        return mergingCompanies;
    }

    public void setMergingCompanies(List<MergingCompany> mergingCompanies) {
        this.mergingCompanies = mergingCompanies;
    }

    public String getRecipientForeignRegisterName() {
        return recipientForeignRegisterName;
    }

    public void setRecipientForeignRegisterName(String recipientForeignRegisterName) {
        this.recipientForeignRegisterName = recipientForeignRegisterName;
    }

    public String getRecipientForeignRegisterId() {
        return recipientForeignRegisterId;
    }

    public void setRecipientForeignRegisterId(String recipientForeignRegisterId) {
        this.recipientForeignRegisterId = recipientForeignRegisterId;
    }

    public String getRecipientForeignCountryCode() {
        return recipientForeignCountryCode;
    }

    public void setRecipientForeignCountryCode(String recipientForeignCountryCode) {
        this.recipientForeignCountryCode = recipientForeignCountryCode;
    }

    @Override
    public String toString() {
        return "CrossBorderMerger [formatter=" + formatter + ", mergerType=" + mergerType
                + ", effectiveDate=" + effectiveDate + ", mergingCompanies="
                + mergingCompanies + ", messageId=" + messageId + ", "
                + "recipientForeignRegisterName=" + recipientForeignRegisterName
                + ", recipientForeignRegisterId="
                + recipientForeignRegisterId + ", recipientForeignCountryCode="
                + recipientForeignCountryCode + "]";
    }

    @Override
    public Map<String, String> getParameters() {
        return null;
    }

}
