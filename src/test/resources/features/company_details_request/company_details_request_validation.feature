@wip
Feature: Duplicate request validation of company details request

  In order to request company details
  As a user of the European Central Platform
  I want to ensure that all requests conform to specification

  Note:
  * Errors defined here:
  https://webgate.ec.europa.eu/CITnet/confluence/download/attachments/593789129/BRIS%20Error%20Codes%20-%20AP.xlsx?api=v2

  Scenario: User makes a request that is not unique - duplicate message id
    Given a company details request for c3e69c0d-dcca-4619-8632-8fd9d1828a8d has been received
    And a company details request for c3e69c0d-dcca-4619-8632-8fd9d1828a8d is created
    When I make a company details request
    Then I should get a message with the error code ERR_BR_5103