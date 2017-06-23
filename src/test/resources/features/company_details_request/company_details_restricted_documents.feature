@company_details_request @restricted_documents
Feature: Restricting sensitive documents on the register that are not to be returned to the ECP

  As a registry
  I want to ensure that restricted data is not returned to the ECP
  So that we can protect our confidentiality of our customers

  Information.
  * This test is run against a company with only 2 items of filing history.
  * One item is a restricted document.

  Scenario: Do not return the documents for restricted forms
    Given a company has a restricted document present in it's filing history
    When I make a company details request
    Then the response will not include the details of the restricted document