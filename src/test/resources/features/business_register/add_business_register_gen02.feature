@wip
  Feature: This story is to verify the ability of the BR to detect invalid schema of the notification "remove business register".

As a BRIS user
I would like a BR-BusinessError with error code ERR_BR_5102 returned if a Remove Business Register Notification not matching the valid schema is delivered to the BR.
So that the BR's system complies with BRIS 2.0

Acceptance Criteria

Given a RemoveBusinessRegister Notification message is received from the ECP Domibus
When the given RemoveBusinessRegister Notification fails validation rule GEN02
Then a BR-BusinessError with error code ERR_BR_5102 is delivered to the ECP
