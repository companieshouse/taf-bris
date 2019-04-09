@regression @company_details_request @connection_test
Feature: Company details request feature

  In order to review company details
  As a customer of BRIS
  I want to request company data from a central location

  GLOSSARY:
  * BRIS - Business Registry Integration Service
  * ECP - European Central Portal
  * EUID - European Union Identification Number (Unique Identifier)

  NOTE:
  This test project uses cloned company data and company number(s) that appear here are purely fictional.
  EUID is made up of Country code, Business Register ID and Company Number e.g. UKEW.00006400

  Scenario: Correct company details are returned in response
    Given I am requesting the company details for company 99990000
    When I make a company details request
    Then the response will contain the company details for 99990000
    And the response will contain a valid formed EUID
    And the response should have the following address details
      | SY11 2NZ | 28 SALOP ROAD | OSWESTRY | SHROPSHIRE | UK |

  Scenario: Inform ECP of non-existent company in register
    Given I am requesting details for a company that does not exist
    When I make a company details request
    Then I should get a company details error message with the error code ERR_BR_3001

  Scenario: I am creating an AddBrNotification
    Given I am creating an AddBrNotification