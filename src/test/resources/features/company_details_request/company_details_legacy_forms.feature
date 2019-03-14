@regression @company_details_request @legacy_forms
Feature: This feature is to ensure that we correctly map to legacy forms in the filing history

  In order to view company legacy data
  As a customer of the ECP
  I want to ensure that all legacy forms are included in company detail responses

  Note: Below is a small sample of legacy forms that may make up some transactions on some companies filing history.

  Scenario Outline: The correct explanatory label is mapped to the correct form
    Given a request for a company that has a <form> form
    When I make a company details request
    Then the response will contain the explanatory label <explanatory_label>
    Examples:
      | form     | explanatory_label |
      | 288a     | EL_UK_008         |
      | 288b     | EL_UK_039         |