@regression @manage_subscription
Feature: Manage Subscription

  In order to inform the ECP of an interest in a company
  As a companies house user
  I want to notify the ECP of a subscription to the company of interest

  Scenario: User can successfully subscribe to a foreign company
    Given a valid manage subscription notification exists
    When I submit a manage subscription notification
    Then the notification subscription will be successfully sent to the ECP

  Scenario: User can unsubscribe from notifications for a foreign company
    Given an unsubscribe message created
    When I submit a manage subscription notification
    Then the notification will be sent to remove the subscription