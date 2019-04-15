@wip
  Feature: This feature is to verify the ability of the BR to detect and process invalid Add Business Register Notifications
    Acceptance Criteria

    As a BRIS User
    I would like A BR-BusinessError returned if a Add Business Register Notification fails validation
    So that the BR's system complies with BRIS 2.0


    #  companyNumber, messageId, correlationId ,ReceiverBusinessRegisterId , ReceiverCountryCode, registerName
    #  leave blank to keep default
    #  set to "NULL" to set as null

    @wip
    Scenario: I am creating an AddBrNotification with different correlationID to message ID
      Given I am creating an AddBrNotification with details
        | | | 123432432 | | | |
      Then I should get a add Br Notification error message with the error code ERR_BR_5103

    Scenario: I would like a BR-BusinessError with error code ERR_BR_5102 returned if an Add Business Register Notification not matching the valid schema is delivered to the BR.
      Given I am creating an AddBrNotification with details
        | | | | | | |
      Then I should get a add Br Notification error message with the error code ERR_BR_5102

    Scenario: I would like a BR-BusinessError with error code ERR_BR_0901 returned if an Add Business Register Notification when any details are blank  1
      Given I am creating an AddBrNotification with details
        | NULL | | | | | |
      Then I should get a add Br Notification error message with the error code ERR_BR_0901

    Scenario: I would like a BR-BusinessError with error code ERR_BR_0901 returned if an Add Business Register Notification when any details are blank 2
        Given I am creating an AddBrNotification with details
          | | NULL | | | | |
        Then I should get a add Br Notification error message with the error code ERR_BR_0901

    Scenario: I would like a BR-BusinessError with error code ERR_BR_5108 returned if an Add Business Register Notification when correlation id is blank
      Given I am creating an AddBrNotification with details
        | | | NULL | | | |
      Then I should get a add Br Notification error message with the error code ERR_BR_5108

    Scenario: I would like a BR-BusinessError with error code ERR_BR_0901 returned if an Add Business Register Notification when any details are blank 4
      Given I am creating an AddBrNotification with details
        | | | | NULL | | |
      Then I should get a add Br Notification error message with the error code ERR_BR_0901

    Scenario: I would like a BR-BusinessError with error code ERR_BR_0901 returned if an Add Business Register Notification when any details are blank 5
      Given I am creating an AddBrNotification with details
        | | | | | NULL | |
      Then I should get a add Br Notification error message with the error code ERR_BR_0901

    Scenario: I would like a BR-BusinessError with error code ERR_BR_0901 returned if an Add Business Register Notification when any details are blank 6
      Given I am creating an AddBrNotification with details
        | | | | | | NULL |
      Then I should get a add Br Notification error message with the error code ERR_BR_0901

    Scenario: I would like a BR-BusinessError with error code ERR_BR_0901 returned if an Add Business Register Notification when any details are blank 7
      Given I am creating an AddBrNotification with details
        | NULL | NULL | NULL |NULL | NULL | NULL |
      Then I should get a add Br Notification error message with the error code ERR_BR_0901

