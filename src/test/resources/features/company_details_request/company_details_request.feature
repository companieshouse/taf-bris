@regression @company_details_request
Feature: Company details request feature

  In order to review company details
  As a customer of BRIS
  I want to request company data from a central location

  GLOSSARY:
  * BRIS - Business Registry Integration Service
  * ECP - European Central Portal

  Scenario: Provide data requested from the ECP for company details.
    Given I am requesting details for a valid company
    When I make a company details request
    Then the correct company details will be returned to the ECP

  Scenario: Inform ECP of non-existent company details request
    Given I am requesting details for a company that does not exist
    When I make a company details request
    Then I should get a message with the error code ERR_BR_3001
