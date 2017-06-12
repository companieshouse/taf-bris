@regression @company_details_request
Feature: Company details response content validation

  In order to ensure our responses are valid
  As a business registry of the ECP
  I want to validate our company details response

  Details.
  * EUID - European Union Identification Number (Unique Identifier)

  NOTE:
  This test project uses cloned company data and company number that appear here are purely fictional.

  Scenario: Correct company details are retrieved
    Given I am requesting the company details for company 99990000
    When I make a company details request
    Then the response will contain the company details for 99990000
    And the response will contain a valid formed EUID
    And the response should have the following address details
      | SY11 2NZ | 28 SALOP ROAD | OSWESTRY | SHROPSHIRE | UK |
