Test Automation Project
---
# taf-bris
Automated tests for the BRIS project

## What is BRIS?
Bris is a system of interconnection of business registers being set up at EU level, 
in a joint effort involving all the EU Member States and the European Commission.

## What will BRIS do?
BRIS will:
* Ensure access at EU level to information on companies registered in the Member States
* Enable the electronic communication between all EU business registers allowing for the exchange information in 
relation to foreign branches and cross-border mergers of companies.

A centralised service known as the European Central Platform (ECP) provides a range of functionality which 
all member states interface with.  
Secure communications with the ECP will be achieved through a re-usable component, the BRIS Gateway (domibus). 
Both the ECP and domibus are developed and maintained by the Commission.

## What will Companies House do?
Companies House will develop a service known as BRIS to interface with the ECP, via domibus, to our own suite of APIs 
to provide the following high level functionality:
* Perform the relevant data manipulation to provide relevant data when requested from the ECP
* Upon receipt of either a cross border merger or change of foreign branch act accordingly
* Inform the ECP of either cross border mergers of foreign branch changes

## How is this being tested?
As there is a reliance on a third party service we (Companies House) have mocked the ECP to create a second domibus
instance in order to prove the communication protocol is sufficient.

Using this second service we will mock company details and document requests and ensure we return the correct data.
This project will also demonstrate the features of the notification services e.g. Cross border merger/disclosure.

All the testable features of BRIS will be detailed in Gherkin syntax under the features folder.

_FYI_
Due to the concern over data consistency across environments and the fact that the majority of this project is
based on read only the decision has been made to load the environment with the required test data.

### How do I run the tests?
You must provide the following arguments to run the tests locally

`mvn clean verify -Denv=kermit -Dcucumber.options="--tags @tagname"`

<b>NOTE:</b> @regression is the default tag to run **ALL** tests.
