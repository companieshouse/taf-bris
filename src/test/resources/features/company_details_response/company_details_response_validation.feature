Feature: Company details response validation

  In order to receive company details
  As an ECP
  I want to ensure that the response meets the appropriate standards

  Scenario: The response message id is not equal to the correlation id
    # Will it be possible to intercept these messages and amend them?
    # Or will they be created and placed on the outgoing Kafka que?
    Given the response message id and correlation id do not match
    When the company details response is sent to the ECP
   # Then the ECP should return an error?


    # Is there any value in writing scenarios that would fail schema validation?

  Scenario: The response contains a message id and correlation id that is not unique
    Given the response contains a message id and correlation id that are not unique
    When the company details response is sent to the ECP
    # Then the ECP should return an error?

  Scenario: The response contains a correlation id that does not correspond to the request
    Given the response contains a correlation id that does not correspond to the request id
    When the company details response is sent to the ECP
    # Then the ECP should return an error

  Scenario: The response contains an invalid country code
    Given the response contains an invalid country code
    When the company details response is sent to the ECP
    # Then the ECP should return an error

  Scenario: The response contains an invalid value in the address
    Given the response contains an invalid value
    When the company details response is sent to the ECP
    # Then the ECP should return an error

  Scenario: The response contains an invalid EUID
    Given the response contains an invalid EUID
    When the company details response is sent to the ECP
    # Then the ECP should return an error