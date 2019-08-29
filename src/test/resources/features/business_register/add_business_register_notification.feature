@regression @add_business_register
Feature: This story is to verify the ability of the BR to receive the AddBusinessRegister Notification.

  Acceptance Criteria
  As a BRIS user
  I would like an acknowledgement from the BR when I send an AddBusinessRegister Notification
  So that the BR's system complies with BRIS 2.0

  Information:
  * Leave blank to keep default
  * Set to "NULL" to set as null
  * Set EMPTY for empty string

  Scenario: I am creating an AddBrNotification
    Given I am creating an AddBrNotification with details
      | companyNumber | messageId | correlationId | receiverBusinessRegisterId | receiverCountryCode | registerName | notificationDateTime | senderBusinessRegisterId | senderCountryCode |
      |               |           |               |                            |                     |              |                      |                          |                   |
    Then the response will contain an Add Business Register Acknowledgement Template Type