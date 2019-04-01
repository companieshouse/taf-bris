@wip
  Feature: This story tests the BR ability to receive the RemoveBusinessRegister Notification.

    As a BRIS user
    I would like an acknowledgement from the BR when I send a RemoveBusinessRegister Notification
    So that the BR's system complies with BRIS 2.0

    Acceptance Criteria

    Given an RemoveBusinessRegister Notification message is received from the ECP Domibus.
    When the given RemoveBusinessRegister Notification message is validated
    Then A BR-Acknowledgement message is sent back to the ECP