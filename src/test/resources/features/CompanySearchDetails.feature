@regression
Feature: Find details for company
  Customer need to be able to enter company details and search on it.
	
Background:
  Given a request has been made to retrieve company details
  Then the company details should be retrieved

Scenario: The request has valid data
  When a valid request for company data has been made
  Then the company details should be retrieved

