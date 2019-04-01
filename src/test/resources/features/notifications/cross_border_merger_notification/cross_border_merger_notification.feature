@regression @cross_border_merger_notification

Feature: Cross Border Merger Notification

  In order to inform the ECP of a merge between companies
  As a companies house user
  I want to send a notification with the details of the merging companies

  Scenario Outline: User makes a valid notification to merge companies
    Given a valid <merger_type> cross border merger notification exists
    When I make a cross border merger notification request
    Then I should have sent a cross border merger notification to the ECP
    Examples:
      | merger_type              |
      | ACQUISITION              |
      | FORMATION_OF_NEW_COMPANY |
      | OWNED_COMPANY            |

  Scenario: Cross Border Merger Notification for more than 1 merging company
    Given a cross border merger notification contains 2 merging companies
    When I make a cross border merger notification request
    Then I should have sent a cross border merger notification to the ECP