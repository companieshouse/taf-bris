package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MergingCompany {

    private static final String FOREIGN_REGISTER_ID = "foreign_register_id";
    private static final String FOREIGN_COUNTRY_CODE = "foreign_country_code";
    private static final String FOREIGN_COMPANY_NUMBER = "foreign_company_number";
    private static final String FOREIGN_COMPANY_NAME = "foreign_company_name";
    private static final String FOREIGN_LEGAL_FORM_CODE = "foreign_legal_form_code";
    private static final String FOREIGN_REGISTER_NAME = "foreign_register_name";
    private static final String ADDRESS_LINE_1 = "address_line_1";
    private static final String ADDRESS_LINE_2 = "address_line_2";
    private static final String ADDRESS_LINE_3 = "address_line_3";
    private static final String CITY = "city";
    private static final String POSTAL_CODE = "postal_code";

    @JsonProperty(FOREIGN_REGISTER_ID)
    private String foreignRegisterId;

    @JsonProperty(FOREIGN_COUNTRY_CODE)
    private String foreignCountryCode;

    @JsonProperty(FOREIGN_COMPANY_NUMBER)
    private String foreignCompanyNumber;

    @JsonProperty(FOREIGN_COMPANY_NAME)
    private String foreignCompanyName;

    @JsonProperty(FOREIGN_LEGAL_FORM_CODE)
    private String foreignLegalFormCode;

    @JsonProperty(FOREIGN_REGISTER_NAME)
    private String foreignRegisterName;

    @JsonProperty(ADDRESS_LINE_1)
    private String addressLine1;

    @JsonProperty(ADDRESS_LINE_2)
    private String addressLine2;

    @JsonProperty(ADDRESS_LINE_3)
    private String addressLine3;

    @JsonProperty(CITY)
    private String city;

    @JsonProperty(POSTAL_CODE)
    private String postalCode;

    public String getForeignRegisterId() {
        return foreignRegisterId;
    }

    public void setForeignRegisterId(String foreignRegisterId) {
        this.foreignRegisterId = foreignRegisterId;
    }

    public String getForeignCountryCode() {
        return foreignCountryCode;
    }

    public void setForeignCountryCode(String foreignCountryCode) {
        this.foreignCountryCode = foreignCountryCode;
    }

    public String getForeignCompanyNumber() {
        return foreignCompanyNumber;
    }

    public void setForeignCompanyNumber(String foreignCompanyNumber) {
        this.foreignCompanyNumber = foreignCompanyNumber;
    }

    public String getForeignCompanyName() {
        return foreignCompanyName;
    }

    public void setForeignCompanyName(String foreignCompanyName) {
        this.foreignCompanyName = foreignCompanyName;
    }

    public String getForeignLegalFormCode() {
        return foreignLegalFormCode;
    }

    public void setForeignLegalFormCode(String foreignLegalFormCode) {
        this.foreignLegalFormCode = foreignLegalFormCode;
    }

    public String getForeignRegisterName() {
        return foreignRegisterName;
    }

    public void setForeignRegisterName(String foreignRegisterName) {
        this.foreignRegisterName = foreignRegisterName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
