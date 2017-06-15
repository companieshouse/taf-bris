@regression @company_details_request
Feature: Company details request feature

  In order to review company details
  As a customer of BRIS
  I want to request company data from a central location

  GLOSSARY:
  * BRIS - Business Registry Integration Service
  * ECP - European Central Portal
  * EUID - European Union Identification Number (Unique Identifier)

  NOTE:
  This test project uses cloned company data and company number that appear here are purely fictional.

  Scenario: Provide data requested from the ECP for company details.
    Given I am requesting details for a valid company
    When I make a company details request
    Then the correct company details will be returned to the ECP

  Scenario: Correct company details are returned in response
    Given I am requesting the company details for company 99990000
    When I make a company details request
    Then the response will contain the company details for 99990000
    And the response will contain a valid formed EUID
    And the response should have the following address details
      | SY11 2NZ | 28 SALOP ROAD | OSWESTRY | SHROPSHIRE | UK |

  Scenario: Inform ECP of non-existent company details request
    Given I am requesting details for a company that does not exist
    When I make a company details request
    Then I should get a message with the error code ERR_BR_3001
