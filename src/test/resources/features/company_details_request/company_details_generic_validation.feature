@regression @company_details_request
Feature: Generic validation of company details request

  In order to request company details
  As a user of the European Central Portal
  I want to ensure that all requests conform to specification

  Scenario: Correlation id and message id do not match
    Given the request contains a correlation id that does not match the message id
    When I make a company details request
    Then I should get a message with the error code ERR_BR_5103

  Scenario: Business register id must correspond business country
    Given the request contains an invalid business register id
    When I make a company details request
    Then I should get a message with the error code ERR_BR_0103

  Scenario: Business register id and country code must match receiving Business Register
    Given the request is not correct for the receiving business register
    When I make a company details request
    Then I should get a message with the error code ERR_BR_0103