@regression @update_led
Feature: Update LED

#  In order to inform regions of a branch disclosure
#  As a customer of BRIS
#  I want to notify the regions of the details of a branch disclosure

  Scenario: User makes a request for a valid LED update
    Given a valid LED update request exists
    When I make an LED request
    Then I should have sent an update LED request