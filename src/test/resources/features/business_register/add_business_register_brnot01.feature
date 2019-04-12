@wip
  Feature: This story is to verify the ability of the BR to detect and process invalid Add Business Register Notifications

As a BRIS User
I would like A BR-BusinessError with error code ERR_BR_0901 returned if a Add Business Register Notification fails validation rule BR-NOT-01
So that the BR's system complies with BRIS 2.0

Acceptance Criteria

Given a AddBusinessRegister Notification is sent to the BR
When the given AddBusinessRegister Notification fails validation BR-NOT-01
Then a BR-BusinessError is sent to the ECP with error code ERR_BR_0901

    #  companyNumber, messageId, correlationId ,businessRegisterId ,countryCode, registerName, notificationDateTime
    #  leave blank to keep default
    #  set to "NULL" to set as null
    Scenario: I am creating an AddBrNotification
      Given I am creating an AddBrNotification with details
        | | | | | | | |
      Then the response will contain an AddBusinessRegisterAcknowledgementTemplateType


    #  companyNumber, messageId, correlationId ,ReceiverBusinessRegisterId , ReceiverCountryCode, registerName, notificationDateTime
    #  leave blank to keep default
    #  set to "NULL" to set as null
    Scenario: I am creating an AddBrNotification with different correlationID to message ID
      Given I am creating an AddBrNotification with details
        | | | 123432432 | | | | |
      Then I should get a add Br Notification error message with the error code ERR_BR_5103


             #  companyNumber, messageId, correlationId ,ReceiverBusinessRegisterId , ReceiverCountryCode, registerName, notificationDateTime
    #  leave blank to keep default
    #  set to "NULL" to set as null
    Scenario: I am creating an AddBrNotification with NULL messageId
      Given I am creating an AddBrNotification with details
        | |NULL |  | | | | |
      Then I should get a add Br Notification error message with the error code ERR_BR_5103


      #  companyNumber, messageId, correlationId ,businessRegisterId ,countryCode, registerName, notificationDateTime
    #  leave blank to keep default
    #  set to "NULL" to set as null
    Scenario: I am creating a RemoveBrNotification
      Given I am creating a RemoveBrNotification with details
        | | | | | | | |
      Then the response will contain a RemoveBusinessRegisterAcknowledgementTemplateType