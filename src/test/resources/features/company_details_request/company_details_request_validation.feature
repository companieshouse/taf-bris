@regression @company_details_request
Feature: Duplicate request validation of company details request

  In order to request company details
  As a user of the European Central Platform
  I want to ensure that all requests conform to specification

  @loadDuplicateCompanyDetailsRequestData
  Scenario: User makes a request that is not unique - duplicate message id
    Given a company details request exists
    And a new company details request is created using the same message id
    When I make a company details request
    Then I should get a message with the error code ERR_BR_5103