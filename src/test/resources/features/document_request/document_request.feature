@regression @document_request
Feature: Document request feature

  In order to receive Companies House documents
  As a customer of BRIS
  I want to request document data from a central location

  NOTE: This document must be available in the aws environment bucket

  @wip
  Scenario: User is able to retrieve a document
    Given the request contains a valid document id of bbu6A7RoFKI1AmpVMVE2cG6pikNH2lCigB2GVuJ66Zc
    When I make a document details request
    Then the response should contain a document with the id bbu6A7RoFKI1AmpVMVE2cG6pikNH2lCigB2GVuJ66Zc
    And the attached document is the expected document

  Scenario: User makes a request with a document id that is not found in the Business Register
    Given the request contains a document id that does not exist
    When I make a document details request
    Then I should get a document details error message with the error code ERR_BR_3002

  Scenario: User makes a request with an invalid document id
    Given the request contains an invalid document id
    When I make a document details request
    Then I should get a document details error message with the error code ERR_BR_5102