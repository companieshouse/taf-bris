package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.util.RequestBuilder;
import uk.gov.companieshouse.taf.util.TestDataHelper;

public class Hooks {

    @Autowired
    private TestDataHelper testDataHelper;

    @Autowired
    private RequestBuilder requestBuilder;

    @Value("${ltd.company.number}")
    private String ltdCompanyNumber;

    @Value("${plc.company.number}")
    private String plcCompanyNumber;

    @Value("${ltd.section30.company.number}")
    private String privateLimitedSharesSection30Exemption;

    @Value("${eeig.company.number}")
    private String eeig;

    @Value("${europeanPlcSe.company.number}")
    private String europeanPublicLimitedLiabilityCompanySe;

    @Value("${unregistered.company.number}")
    private String unregisteredCompany;

    @Value("${ltdGuarantNsc.company.number}")
    private String privateLimitedGuarantNsc;

    @Value("${ltdGuarantNscLtdExemption.company.number}")
    private String privateLimitedGuarantNscLimitedExemption;

    @Value("${overseas.company.number}")
    private String overseaCompany;

    /**
     * Inserts the company details data prior to executing the tests to ensure data consistency.
     * Note that this set up step will run for all scenarios.
     */
    @Before
    public void setUpDataForAllScenarios() {
        List<String> companiesToLoad = new ArrayList<>();
        companiesToLoad.add(ltdCompanyNumber);
        testDataHelper.setUpTestData(companiesToLoad);
    }

    /**
     * Cleans up the data that has been injected for testing purposes.
     * Note that this tear down step will run for all scenarios.
     */
    @After
    public void tearDownDataForAllScenarios() {
        List<String> companiesToRemove = new ArrayList<>();
        companiesToRemove.add(ltdCompanyNumber);
        testDataHelper.tearDownTestData(companiesToRemove);
    }

    /**
     * Inserts all the test data required for testing the legal entity feature.
     */
    @Before("@legal_entity")
    public void setDataForLegalEntityScenario() {
        List<String> companiesToLoad = new ArrayList<>();
        companiesToLoad.add(plcCompanyNumber);
        companiesToLoad.add(privateLimitedSharesSection30Exemption);
        companiesToLoad.add(eeig);
        companiesToLoad.add(europeanPublicLimitedLiabilityCompanySe);
        companiesToLoad.add(unregisteredCompany);
        companiesToLoad.add(privateLimitedGuarantNsc);
        companiesToLoad.add(privateLimitedGuarantNscLimitedExemption);
        companiesToLoad.add(overseaCompany);
        testDataHelper.setUpTestData(companiesToLoad);
    }

    /**
     * Cleans up the data that has been injected for legal entity testing purposes.
     */
    @After("@legal_entity")
    public void tearDownDataForLegalEntityScenario() {
        List<String> companiesToRemove = new ArrayList<>();
        companiesToRemove.add(plcCompanyNumber);
        companiesToRemove.add(privateLimitedSharesSection30Exemption);
        companiesToRemove.add(eeig);
        companiesToRemove.add(europeanPublicLimitedLiabilityCompanySe);
        companiesToRemove.add(unregisteredCompany);
        companiesToRemove.add(privateLimitedGuarantNsc);
        companiesToRemove.add(privateLimitedGuarantNscLimitedExemption);
        companiesToRemove.add(overseaCompany);
        testDataHelper.tearDownTestData(companiesToRemove);
    }

    /**
     * Inserts all the test data required for testing the legacy forms feature.
     */
    @Before("@legacy_forms")
    public void setDataForLegacyFormsScenario() {
        List<String> companiesToLoad = new ArrayList<>();
        companiesToLoad.add(unregisteredCompany);
        companiesToLoad.add(europeanPublicLimitedLiabilityCompanySe);
        companiesToLoad.add(eeig);
        companiesToLoad.add(privateLimitedGuarantNsc);
        testDataHelper.setUpTestData(companiesToLoad);
    }

    /**
     * Cleans up the data that has been injected for legal entity testing purposes.
     */
    @After("@legacy_forms")
    public void tearDownDataForLegacyFormsScenario() {
        List<String> companiesToRemove = new ArrayList<>();
        companiesToRemove.add(unregisteredCompany);
        companiesToRemove.add(europeanPublicLimitedLiabilityCompanySe);
        companiesToRemove.add(eeig);
        companiesToRemove.add(privateLimitedGuarantNsc);
        testDataHelper.tearDownTestData(companiesToRemove);
    }

    /**
     * Inserts all the test data required for testing the legacy forms feature.
     */
    @Before("@restricted_documents")
    public void setDataForRestrictedDocumentScenario() {
        List<String> companiesToLoad = new ArrayList<>();
        companiesToLoad.add(plcCompanyNumber);
        testDataHelper.setUpTestData(companiesToLoad);
    }

    /**
     * Cleans up the data that has been injected for legal entity testing purposes.
     */
    @After("@restricted_documents")
    public void tearDownDataForRestrictedDocumentScenario() {
        List<String> companiesToRemove = new ArrayList<>();
        companiesToRemove.add(plcCompanyNumber);
        testDataHelper.tearDownTestData(companiesToRemove);
    }

    /**
     * Sets up a duplicate message in the BRIS incoming messages collection prior to sending
     * a message in with the same correlation and message id.
     */
    @Before("@loadDuplicateCompanyDetailsRequestData")
    public void setupDataForDuplicateMessageTest() {
        testDataHelper.setupDataForDuplicateMessageTest();
    }
}
