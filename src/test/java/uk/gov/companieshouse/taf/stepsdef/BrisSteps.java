package uk.gov.companieshouse.taf.stepsdef;

import eu.europa.ec.bris.jaxb.br.components.aggregate.v1_4.MessageObjectType;
import eu.europa.ec.bris.jaxb.components.aggregate.v1_4.TestDataType;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageContainer;
import eu.europa.ec.digit.message.container.jaxb.v1_0.MessageInfo;
import uk.gov.companieshouse.taf.domain.BrisMessageHeaderType;

public class BrisSteps {

    protected BrisMessageHeaderType createBrisMessageHeaderType(MessageObjectType messageObjectType) {
        BrisMessageHeaderType header = new BrisMessageHeaderType();
        header.setMessageId(messageObjectType.getMessageHeader().getMessageID().getValue());
        header.setCorrelationId(messageObjectType.getMessageHeader().getCorrelationID().getValue());
        header.setBusinessRegisterId(messageObjectType.getMessageHeader().getBusinessRegisterReference().getBusinessRegisterID().getValue());
        header.setBusinessRegisterCountry(messageObjectType.getMessageHeader().getBusinessRegisterReference().getBusinessRegisterCountry().getValue());

        TestDataType testData = messageObjectType.getMessageHeader().getTestData();
        if (testData != null) {
            header.getTestData().setCaseId(testData.getTestCaseID().getValue());
            header.getTestData().setConditionId(testData.getTestConditionID().getValue());
            header.getTestData().setExecutionId(testData.getTestExecutionID().getValue());
            header.getTestData().setPackageId(testData.getTestPackageID().getValue());
            header.getTestData().setSessionId(testData.getTestSessionID().getValue());
        }

        return header;
    }

    protected BrisMessageHeaderType createBrisMessageHeaderType(MessageContainer messageContainer) {
        BrisMessageHeaderType header = new BrisMessageHeaderType();

        header.setMessageId(messageContainer.getContainerHeader().getMessageInfo().getMessageID());
        header.setCorrelationId(messageContainer.getContainerHeader().getMessageInfo().getCorrelationID());
        header.setBusinessRegisterId(messageContainer.getContainerHeader().getAddressInfo().getSender().getCode());
        header.setBusinessRegisterCountry(
                messageContainer.getContainerHeader().getAddressInfo().getSender().getCountryCode());

        MessageInfo.TestData testData = messageContainer.getContainerHeader().getMessageInfo().getTestData();
        if (testData != null) {
            header.getTestData().setCaseId(testData.getTestCaseID());
            header.getTestData().setConditionId(testData.getTestStepID());
            header.getTestData().setExecutionId(testData.getTestExecutionID());
            header.getTestData().setPackageId(testData.getTestPackageID());
            header.getTestData().setSessionId(testData.getTestSessionID());
        }

        return header;
    }
}
