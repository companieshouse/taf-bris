package uk.gov.companieshouse.taf.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.gov.companieshouse.taf.data.MergingCompanyData;
import uk.gov.companieshouse.taf.domain.CrossBorderMerger;
import uk.gov.companieshouse.taf.domain.MergingCompany;

public class CrossBorderMergerBuilder {

    /**
     * Create Cross Border Merger Notification.
     */
    public static CrossBorderMerger createDefaultCrossBorderMerger(
            MergingCompanyData mergingCompanyData) {
        CrossBorderMerger crossBorderMerger = new CrossBorderMerger();
        crossBorderMerger.setEffectiveDate(new Date());
        crossBorderMerger.setMergerType(mergingCompanyData.getMergerType());

        // Set merging company data
        MergingCompany mergingCompany = new MergingCompany();
        mergingCompany.setForeignRegisterId(mergingCompanyData.getForeignRegisterId());
        mergingCompany.setForeignCountryCode(mergingCompanyData.getForeignCountryCode());
        mergingCompany.setForeignCompanyNumber(mergingCompanyData.getForeignCompanyNumber());
        mergingCompany.setForeignCompanyName(mergingCompanyData.getForeignCompanyName());
        mergingCompany.setForeignLegalFormCode(mergingCompanyData.getForeignLegalForm());
        mergingCompany.setForeignRegisterName(mergingCompanyData.getForeignRegisterName());

        // Set Address
        mergingCompany.setAddressLine1(mergingCompanyData.getAddressLine1());
        mergingCompany.setAddressLine2(mergingCompanyData.getAddressLine2());
        mergingCompany.setAddressLine3(mergingCompanyData.getAddressLine3());
        mergingCompany.setCity(mergingCompanyData.getCity());
        mergingCompany.setPostalCode(mergingCompanyData.getPostalCode());

        List<MergingCompany> mergingCompanies = new ArrayList<>();
        mergingCompanies.add(mergingCompany);

        // Set merging company
        crossBorderMerger.setMergingCompanies(mergingCompanies);

        crossBorderMerger.setRecipientForeignRegisterId(mergingCompanyData.getForeignRegisterId());
        crossBorderMerger.setRecipientForeignCountryCode(
                mergingCompanyData.getForeignCountryCode());
        crossBorderMerger.setRecipientForeignRegisterName(
                mergingCompanyData.getForeignRegisterName());
        return crossBorderMerger;
    }
}
