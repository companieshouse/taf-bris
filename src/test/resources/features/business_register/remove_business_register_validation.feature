@regression @remove_business_register_validation
Feature: This feature is to verify the ability of the BR to detect and process invalid Remove Business Register Notifications
  Acceptance Criteria

  As a BRIS User
  I would like A BR-BusinessError returned if a Remove Business Register Notification fails validation
  So that the BR's system complies with BRIS 2.0


  #  companyNumber, messageId, correlationId ,ReceiverBusinessRegisterId , ReceiverCountryCode, registerName, NotificationDateTime, SenderBusinessRegisterId, SenderCountryCode
  #  leave blank to keep default
  #  set to "NULL" to set as null
  #  set EMPTY for empty string


  Scenario: I am creating a Remove BrNotification with different correlationID to message ID
    Given I am creating a RemoveBrNotification with details
      |  |  | 123456789 |  |  |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5103

  Scenario: I would like a BR-BusinessError with error code ERR_BR_0103 when the Receiver Country Code is not UK
    Given I am creating a RemoveBrNotification with details
      |  |  |  |  | AT |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_0103

  Scenario: I would like a BR-BusinessError with error code ERR_BR_0103 Receiver business register id is not Valid
    Given I am creating a RemoveBrNotification with details
      |  |  |  | AB |  |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_0103

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5102 when the Country Code is not in the list
    Given I am creating a RemoveBrNotification with details
      |  |  |  |  | AB |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5102

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5102 returned if Receiver Country Code is empty
    Given I am creating a RemoveBrNotification with details
      |  |  |  |  | EMPTY |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5102

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5108 returned when Correlation id is empty
    Given I am creating a RemoveBrNotification with details
      |  |  | EMPTY |  |  |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5108

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5108 returned if correlation id is empty
    Given I am creating a RemoveBrNotification with details
      |  |  | EMPTY |  |  |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5108

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5102 returned if Correlation id is Blank
    Given I am creating a RemoveBrNotification with details
      |  |  |  |  |  |  | NULL |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5102

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5102 returned if an Add Business Register Notification when country code is empty
    Given I am creating a RemoveBrNotification with details
      |  |  |  |  | EMPTY |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5102

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5102 when the Receiver Country Code is empty
    Given I am creating a RemoveBrNotification with details
      |  |  |  |  | EMPTY |  |  |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5102

  Scenario: I would like a BR-BusinessError with error code ERR_BR_5102 returned if an Add Business Register Notification time is NULL
    Given I am creating a RemoveBrNotification with details
      |  |  |  |  |  |  | NULL |  |  |
    Then I should get a remove Br Notification error message with the error code ERR_BR_5102
