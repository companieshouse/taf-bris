package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestData {
 
    protected static final String TEST_CASE_ID = "test_case_id";
    protected static final String TEST_CONDITION_ID = "test_condition_id";
    protected static final String TEST_EXECUTION_ID = "test_execution_id";
    protected static final String TEST_PACKAGE_ID = "test_package_id";
    protected static final String TEST_SESSION_ID = "test_session_id";

    @JsonProperty(TEST_CASE_ID)
    private String testCaseId;
    
    @JsonProperty(TEST_CONDITION_ID)
    private String testConditionId;

    @JsonProperty(TEST_EXECUTION_ID)
    private String testExecutionId;

    @JsonProperty(TEST_PACKAGE_ID)
    private String testPackageId;

    @JsonProperty(TEST_SESSION_ID)
    private String testSessionId;
    
    /**
     * Constructor
     * @param testCaseId
     * @param testConditionId
     * @param testExecutionId
     * @param testPackageId
     * @param testSessionId
     */
    public TestData(String testCaseId, String testConditionId, String testExecutionId, String testPackageId,
                    String testSessionId) {
        super();
        this.testCaseId = testCaseId;
        this.testConditionId = testConditionId;
        this.testExecutionId = testExecutionId;
        this.testPackageId = testPackageId;
        this.testSessionId = testSessionId;
    }

    /**
     * No args constructor
     */
    public TestData() {
        super();
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestConditionId() {
        return testConditionId;
    }

    public void setTestConditionId(String testConditionId) {
        this.testConditionId = testConditionId;
    }

    public String getTestExecutionId() {
        return testExecutionId;
    }

    public void setTestExecutionId(String testExecutionId) {
        this.testExecutionId = testExecutionId;
    }

    public String getTestPackageId() {
        return testPackageId;
    }

    public void setTestPackageId(String testPackageId) {
        this.testPackageId = testPackageId;
    }

    public String getTestSessionId() {
        return testSessionId;
    }

    public void setTestSessionId(String testSessionId) {
        this.testSessionId = testSessionId;
    }

    @Override
    public String toString() {
        return TEST_CASE_ID  + " : " + testCaseId + ", " +
                TEST_CONDITION_ID + " : " + testConditionId + ", " +
                TEST_EXECUTION_ID+ " : " + testExecutionId+ ", " +
                TEST_PACKAGE_ID+ " : " +  testPackageId+ ", " +
                TEST_SESSION_ID+ " : " +  testSessionId;    
    }
}
