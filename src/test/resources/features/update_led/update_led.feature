@regression @update_led
Feature: Update LED

  In order to inform the ECP of a legal entity data change
  As a companies house user
  I want to notify the ECP of the details of a legal entity data change

  Scenario: User makes a request to inform the ECP of a valid LED update
    Given a valid LED update notification exists
    When I submit an LED update notification
    Then I should have sent an update LED notification to the ECP