@wip
  Feature: This story is to verify the ability of the BR to receive the AddBusinessRegister Notification.

    As a BRIS user
    I would like an acknowledgement from the BR when I send an AddBusinessRegister Notification
    So that the BR's system complies with BRIS 2.0

    Acceptance Criteria

    Given an AddBusinessRegister Notification message is received from the ECP Domibus
    When the given BR-Notification message is validated
    Then a BR-Acknowledgement message is sent back to the ECP