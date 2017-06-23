@regression @cross_border_merger_reception
Feature: Cross border merger reception

  In order to inform regions of company mergers
  As a customer of BRIS
  I want to notify the regions that two companies have been through a merger

  Scenario: User makes a request for two companies to be merged
    Given a cross border merger request exists
    When I make a cross border merger request
    Then I should get an acknowledgment confirming receipt of the merger