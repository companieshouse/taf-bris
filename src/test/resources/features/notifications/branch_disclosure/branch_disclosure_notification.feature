@regression @branch_disclosure_notification
Feature: Branch disclosure notification

  In order to inform the ECP of a branch disclosure
  As a Companies House user
  I want to notify the ECP of the changes to a companies status

  Scenario Outline: User makes a valid branch disclosure notification
    Given a valid <proceeding_type> branch disclosure notification exists
    When I submit an branch disclosure notification
    Then I should have sent a branch disclosure notification to the ECP
    Examples:
      | proceeding_type                    |
      | WINDING_UP_OPENING                 |
      | WINDING_UP_TERMINATION             |
      | WINDING_UP_OPENING_AND_TERMINATION |
      | WINDING_UP_REVOCATION              |
      | INSOLVENCY_OPENING                 |
      | INSOLVENCY_TERMINATION             |
      | INSOLVENCY_OPENING_AND_TERMINATION |
      | INSOLVENCY_REVOCATION              |
      | STRIKING_OFF                       |
      | NO_STRIKING_OFF_OF_BRANCH          |