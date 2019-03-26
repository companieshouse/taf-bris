@wip
  Feature: This story is to verify the ability of the BR to detect invalid schema of the notification AddBusinessRegister.

As a BRIS user
I would like a BR-BusinessError message with error code ERR_BR_5102 returned if an AddBusinessRegister Notification fails validation rule GEN02
So that the BR's system complies with BRIS version 2.0

Acceptance Criteria

Given an AddBusinessRegister Notification message is received from the ECP Domibus
When the given AddBusinessRegister Notification fails validation rule GEN02
Then a BR-BusinessError with error code ERR_BR_5102 is delivered to the ECP
