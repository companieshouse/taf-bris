@regression @company_details_request @company_details_validation @generic_validation
Feature: Company details request feature

  In order to request company details
  As a
  I want to ensure that all requests conform to specification

  Note:
  * Errors defined here:
  https://webgate.ec.europa.eu/CITnet/confluence/download/attachments/593789129/BRIS%20Error%20Codes%20-%20AP.xlsx?api=v2

  Scenario: Request contains an invalid correlation id
    Given the request contains an invalid correlation id
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

  Scenario: Correlation id and message id do not match
    Given the request contains a correlation id that does not match the message id
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

  Scenario: Message id is not unique
    Given the request contains a message id that already exists
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

  Scenario: Business register country does not exist
    Given the request contains a business country that does not exist
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

  Scenario: Business register id must correspond business country
    Given the request contains a business register that does not correspond to the business id
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

    # Invalid EUID?





