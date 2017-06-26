@regression @cross_border_merger_reception
Feature: Cross border merger reception

  In order to inform regions of company mergers
  As a customer of BRIS
  I want to notify the regions that two companies have been through a merger

  Scenario: User makes a request for two companies to be merged
    Given a valid cross border merger request is created
    When I make a cross border merger request
    Then I should get an acknowledgment confirming receipt of the merger

  Scenario: User does not provide a valid merging country code
    Given the notification does not have a valid merging country code
    When I make a cross border merger request
    Then I should get a message with the error code ERR_BR_5102

  Scenario: User does not provide a valid merging business register id
    Given the notification does not have a valid business register id
    When I make a cross border merger request
    Then I should get a message with the error code ERR_BR_5102
