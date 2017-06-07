@regression @document_request
Feature: Document request feature

  In order to receive Companies House documents
  As a customer of BRIS
  I want to request document data from a central location

  Scenario: User is able to retrieve a document
    Given the request contains a valid document id of v09C1_5w7-QDthw9_lKJvYUoXX66nBpd3O4-7jT9K98
    When I make a document details request
    Then the response should contain a document with the id v09C1_5w7-QDthw9_lKJvYUoXX66nBpd3O4-7jT9K98

  Scenario: User makes a request with a document id that is not found in the Business Register
    Given the request contains a document id that does not exist
    When I make a document details request
    Then I should get a message with the error code ERR_BR_3002

  Scenario: User makes a request with an invalid document id
    Given the request contains an invalid document id
    When I make a document details request
    Then no response will be created