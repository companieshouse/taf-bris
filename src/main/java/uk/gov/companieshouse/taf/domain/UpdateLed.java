package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class UpdateLed implements ParameterMapper {

    private static final String UPDATE_TYPE = "update_type";
    private static final String MESSAGE_ID = "message_id";
    private static final String TEST_DATA = "test_data";
    
    @JsonProperty(UPDATE_TYPE)
    private String updateType;
    
    //Not passed in JSON object, but set during foreign registry id validation
    private String messageId;
    
    @JsonProperty(TEST_DATA)
    private TestData testData;

    /**
     * Constructor for LED object.
     * @param updateType the type of LED update.
     * @param testCaseId test case id.
     * @param testConditionId test condition id.
     * @param testExecutionId test execution id.
     * @param testPackageId test package id.
     * @param testSessionId test session id.
     */
    public UpdateLed(String updateType, String testCaseId, String testConditionId,
                     String testExecutionId,
                     String testPackageId, String testSessionId) {
        this.updateType = updateType;
        if (testCaseId != null && !testCaseId.isEmpty()) {
            this.testData = new TestData(testCaseId, testConditionId,
                    testExecutionId,  testPackageId,  testSessionId);
        }
    }

    /**
     * No args constructor.
     */
    public UpdateLed() {
        super();
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    @Override
    public Map<String, String> getParameters() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(UPDATE_TYPE, updateType);
        paramMap.put(MESSAGE_ID, messageId);
        if (testData != null) {
            paramMap.put(TestData.TEST_CASE_ID, testData.getTestCaseId());
            paramMap.put(TestData.TEST_CONDITION_ID, testData.getTestConditionId());
            paramMap.put(TestData.TEST_EXECUTION_ID, testData.getTestExecutionId());
            paramMap.put(TestData.TEST_PACKAGE_ID, testData.getTestPackageId());
            paramMap.put(TestData.TEST_SESSION_ID, testData.getTestSessionId());
            
        }
        return paramMap;
    }
    
    @Override
    public String toString() {
        return UPDATE_TYPE + " : " + updateType + ", "
                + MESSAGE_ID + " : " + messageId;
    }
}
