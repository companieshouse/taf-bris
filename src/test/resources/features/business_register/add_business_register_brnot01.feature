@wip
  Feature: This story is to verify the ability of the BR to detect and process invalid Add Business Register Notifications

As a BRIS User
I would like A BR-BusinessError with error code ERR_BR_0901 returned if a Add Business Register Notification fails validation rule BR-NOT-01
So that the BR's system complies with BRIS 2.0

Acceptance Criteria

Given a AddBusinessRegister Notification is sent to the BR
When the given AddBusinessRegister Notification fails validation BR-NOT-01
Then a BR-BusinessError is sent to the ECP with error code ERR_BR_0901
