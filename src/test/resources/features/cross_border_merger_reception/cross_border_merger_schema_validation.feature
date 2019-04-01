@regression @cross_border_merger_reception @xsd_validation

Feature: Cross border merger schema validation

  In order to ensure valid messages are handled
  As an Access Point of the ECP
  I want to ensure that the cross border mergers meet the schema validation

  * ECP - European Central Portal

  Scenario: User does not provide a valid merging country code
    Given the notification does not have a valid merging country code
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_5102

  Scenario: User does not provide a valid merging business register id
    Given the notification does not have a valid business register id
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_5102

  Scenario: User provides an invalid issuing country code
    Given the notification has an invalid issuing country code
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_5102