@regression @company_details_request @company_details_validation @ecp_to_ap_errors
Feature: Company details request feature

  In order to request company details
  As a
  I want to ensure that all requests conform to specification

  Note:
  * Errors defined here:
  https://webgate.ec.europa.eu/CITnet/confluence/download/attachments/593789129/BRIS%20Error%20Codes%20-%20AP.xlsx?api=v2

  Scenario: User makes a request that is not unique
    Given the company details request is not unique
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

  Scenario: User makes a request using an invalid country code
    Given the request has an invalid country code
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

  Scenario: User makes a request with a mismatch between business country code and id
    Given the request business id and country do not correspond
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

  # Document retrieval
  Scenario: User makes a request with a document id that is not found in the Business Register
    Given the request contains an invalid document id
    When I make a company details request
    Then I should get a message with the error code "ERROR_CODE"

