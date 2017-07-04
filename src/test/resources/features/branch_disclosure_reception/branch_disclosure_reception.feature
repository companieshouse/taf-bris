@regression @branch_disclosure_reception
Feature: Branch Disclosure reception

  In order to inform regions of a branch disclosure
  As a customer of BRIS
  I want to notify the regions of the details of a branch disclosure

  Scenario: User makes a request to inform of a branch disclosure
    Given a branch disclosure request exists
    When I make a branch disclosure request
    Then I should get an acknowledgment confirming receipt of the branch disclosure

  Scenario: User provides a business register that is not the same as the business register of the disclosure company
    Given the notification business register does not match the disclosure company
    When I make a branch disclosure request
    Then I should get a branch disclosure error message with the error code ERR_BR_0601

  Scenario: User provides a business register in the recipient organisation that does not match the branch business register
    Given the notification has a recipient business register that does not match the branch business register
    When I make a branch disclosure request
    Then I should get a branch disclosure error message with the error code ERR_BR_0603

  Scenario: User provides a business register is the same as the recipient organisation
    Given the notification business register is the same as the recipient business register
    When I make a branch disclosure request
    Then I should get a branch disclosure error message with the error code ERR_BR_0604
