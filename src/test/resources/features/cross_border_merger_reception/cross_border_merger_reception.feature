@regression @cross_border_merger_reception @cross_border_merger_reception_validation
Feature: Cross border merger reception

  In order to inform regions of company mergers
  As a customer of BRIS
  I want to notify the regions that two companies have been through a merger

  Scenario: User makes a request for two companies to be merged
    Given a valid cross border merger request is created
    When I make a cross border merger request
    Then I should get an acknowledgment confirming receipt of the merger

  Scenario: User supplied an invalid company EUID
    Given the notification does not have a valid company EUID
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_0101

  Scenario: User provides an invalid legal form code
    Given the notification does not have a valid legal form code
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_0103

  Scenario: User supplies an invalid country in their company EUID
    Given the notification does not have a valid country in company EUID
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_0501

  Scenario: User supplies an invalid business register id in their company EUID
    Given the notification does not have a valid business register id in company EUID
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_0501

  Scenario: User supplies an EUID that does not match the recipient organisation
    Given the notification has an EUID that is not for the recipient organisation
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_0502

  Scenario: User supplies a business register that is not the same as the business register  of the issuing country
  in the message
    Given the notification has a business register that does not match business register in the message
    When I make a cross border merger request
    Then I should get a cross border merger error message with the error code ERR_BR_0504
