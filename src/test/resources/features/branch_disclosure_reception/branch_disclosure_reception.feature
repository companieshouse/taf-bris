@regression @branch_disclosure_reception
Feature: Branch Disclosure reception

  In order to inform regions of a branch disclosure
  As a customer of BRIS
  I want to notify the regions of teh details of a branch disclosure

  Scenario: User makes a request to inform of a branch disclosure
    Given a branch disclosure request exists
    When I make a branch disclosure request
    Then I should get an acknowledgment confirming receipt of the branch disclosure