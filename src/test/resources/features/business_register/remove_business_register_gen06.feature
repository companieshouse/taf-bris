@wip
  Feature: This story is to verify the ability of the BR to detect and process invalid BR-RemoveBusinessRegisterNotification

As a BRIS User
I would like A BR-BusinessError with error code ERR_BR_0103 returned if a Remove Business Register Notification fails validation rule GEN06
So that the BR's system complies with BRIS 2.0

Acceptance Criteria

Given a RemoveBusinessRegister Notification is sent to the BR
When the given RemoveBusinessRegister Notification fails validation GEN06
Then a BR-BusinessError is sent to the ECP with error code ERR_BR_0103
