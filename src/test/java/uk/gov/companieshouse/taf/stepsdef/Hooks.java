package uk.gov.companieshouse.taf.stepsdef;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.companieshouse.taf.util.TestDataHelper;

public class Hooks {

    @Autowired
    private TestDataHelper testDataHelper;

    @Value("${default.company.number}")
    private String defaultCompanyNumber;

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
        companiesToLoad.add(defaultCompanyNumber);
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
     * Cleans up the data that has been injected for testing purposes.
     * Note that this tear down step will run for all scenarios.
     */
    @After
    public void tearDownDataForAllScenarios() {
        List<String> companiesToRemove = new ArrayList<>();
        companiesToRemove.add(defaultCompanyNumber);
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
}
