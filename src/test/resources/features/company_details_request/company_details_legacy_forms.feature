@regression @company_details_request @legacy_forms
Feature: This feature is to ensure that we correctly map to legacy forms in the filing history

  In order to view company legacy data
  As a customer of the ECP
  I want to ensure that all legacy forms are included in company detail responses

  Scenario: Company details response contains details of 288a
    Given a request for a company that has a 288a form
    When I make a company details request
    # The below step needs to be amended once Kev has resolved the list
    Then the response will contain the explanatory label EL_UK_

  Scenario: Company details response contains details of 288b
    Given a request for a company that has a 288b form
    When I make a company details request
    # The below step needs to be amended once Kev has resolved the list
    Then the response will contain the explanatory label EL_UK_

  Scenario: Company details response contains details of CERTNM
    Given a request for a company that has a CERTNM form
    When I make a company details request
    # The below step needs to be amended once Kev has resolved the list
    Then the response will contain the explanatory label EL_UK_