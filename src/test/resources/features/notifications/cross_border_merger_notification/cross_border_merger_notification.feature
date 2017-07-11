@regression @cross_border_merger_notification
Feature: Cross Border Merger Notification

  In order to inform the ECP of a merge between companies
  As a companies house user
  I want to send a notification with the details of the merging companies

  Scenario: User makes a valid notification to merge companies
    Given a valid cross border merger notification exists
    When I make a cross border merger notification request
    Then I should have sent a cross border merger notification to the ECP