@regression @xsd_validation @company_request_details @company_details_validation
Feature: Schema validation of BRIS request messages

  In order to ensure valid messages are communicated
  As an ECP
  I want to check messages conform to schema validation

  * ECP - European Central Portal

  Information:
  The below scenarios are for illustration.
  Requests should never be sent from the BRIS ECP due to failing schema validation.

  Scenario: Request contains an invalid correlation id
    Given the request contains an invalid correlation id
    When I make a company details request
    Then no response will be created

  Scenario: Business register country does not exist
    Given the request contains a business country that does not exist
    When I make a company details request
    Then no response will be created