package uk.gov.companieshouse.taf.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("MergingCompany")
public class MergingCompanyData extends CrossBorderMergerNotificationData {

    private String foreignRegisterId = "BRA";
    private String foreignCountryCode = "AT";
    private String foreignCompanyNumber = "123456";
    private String foreignCompanyName = "Foreign Company";
    private String foreignLegalForm = "LF_AT_001";
    private String foreignRegisterName = "Foreign Register";
    private String addressLine1 = "Foreign Address line 1.";
    private String addressLine2 = "Foreign Address line 2.";
    private String addressLine3 = "Foreign Address line 3.";
    private String city = "Foreign City";
    private String postalCode = "FA 123";

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

    public String getForeignLegalForm() {
        return foreignLegalForm;
    }

    public void setForeignLegalForm(String foreignLegalForm) {
        this.foreignLegalForm = foreignLegalForm;
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
