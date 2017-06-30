package uk.gov.companieshouse.taf.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class UpdateLED implements ParameterMapper {

    private static final String UPDATE_TYPE = "update_type";
    private static final String MESSAGE_ID ="message_id";
    
    private static final String TEST_DATA ="test_data"; 
    
    @JsonProperty(UPDATE_TYPE)
    private String updateType;
    
     //Not passed in JSON object, but set during foreign registry id validation
     //@JsonProperty(MESSAGE_ID)
     private String messageId = "JB1111";
    
    @JsonProperty(TEST_DATA)
    private TestData testData;

    /**
     * Constructor
     * @param updateType
     * @param testCaseId
     * @param testConditionId
     * @param testExecutionId
     * @param testPackageId
     * @param testSessionId
     */
    public UpdateLED(String updateType, String testCaseId, String testConditionId, String testExecutionId,
                     String testPackageId, String testSessionId) {
        this.updateType = updateType;
        if (testCaseId != null && !testCaseId.isEmpty()) {
            this.testData = new TestData(testCaseId, testConditionId, testExecutionId,  testPackageId,  testSessionId);
        }
    }

    /**
     * No args constructor
     */
    public UpdateLED() {
        super();
    }

    public String getUpdateType() {
        return updateType;
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
    
    public TestData getTestData() {
        return testData;
    }

    public void setTestData(TestData testData) {
        this.testData = testData;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> paramMap = new HashMap<String, String>();
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
        return UPDATE_TYPE + " : " + updateType + ", " +
                MESSAGE_ID + " : " + messageId;
    }
}
