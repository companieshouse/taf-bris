@wip
Feature: This feature demonstrates the various company types that can be searched for by the ECP

  In order to review company details regardless of company type
  As a user of the ECP
  I want to search for any approved legal entity

  NOTE:
  ECP - European Central Platform

  Legal form codes;
  LF_UK_001 - Private Limited by Shares/(Section 30 Exemption)
  LF_UK_002 - European Economic Interest Grouping (EEIG)
  LF_UK_003 - European Public Limited-Liability Company (SE)
  LF_UK_004 - Private Limited
  LF_UK_005 - Public Limited Company
  LF_UK_006 - Unregistered Company
  LF_UK_007 - Private Limited by Guarantee/no share capital
  LF_UK_008 - Private Limited by Guarantee/no share capital(use of Limited exemption)
  LF_UK_009 - Overseas Company

  Scenario Outline: User can search for a company with various company types
    Given the user is requesting the details of a <company_type> company
    When I make a company details request
    Then the correct company details will be returned to the ECP

    Examples:
      | company_type |
#      | LF_UK_001    |
#      | LF_UK_002    |
#      | LF_UK_003    |
#      | LF_UK_004    |
      | LF_UK_005    |
#      | LF_UK_006    |
#      | LF_UK_007    |
#      | LF_UK_008    |
#      | LF_UK_009    |